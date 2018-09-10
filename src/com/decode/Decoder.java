package com.decode;

import com.sun.deploy.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Decoder
{
    /**
     * Body of the application
     */
    public void runDecoder()
    {
        // Obtain instructions from the file
        String machineFilePath = "src/resources/MachineCode.txt";
        List<String> machineInstructionsHex = getInstructions(machineFilePath);

        // Print out machine instructions in hex line by line
        //machineInstructionsHex.forEach(System.out::println);

        // Convert the instructions to binary
        List<String> machineInstructionsBinary = convertInstructionsToBinary(machineInstructionsHex);

        // Print out machine instructions in binary line by line
        //machineInstructionsBinary.forEach(System.out::println);

        // Check for I-Type or R-Type instruction and perform relevant decode operation
        List<String> decodedInstructions = new ArrayList<>();
        for (String binaryCode : machineInstructionsBinary)
        {
            // R-type = 1, I-Type = 0
            Boolean type = checkType(binaryCode);

            // Print out the type
            //System.out.println(type);

            if (type)  // Perform R-type decode
            {
                decodedInstructions.add(decodeRType(binaryCode));
            }
            else // Performs I-Type decode
            {
                decodedInstructions.add(decodeIType(binaryCode));
            }
        }

        // Print out the resultant instructions
        printResults(decodedInstructions);
    }

    /**
     * Returns a list of machine instructions after parsing MachineCode.txt
     * @param machineFilePath the relative path of the MachineCode.txt file
     * @return instructions (a list of machine instructions parsed from the file)
     */
    public List<String> getInstructions(String machineFilePath)
    {
        List<String> instructions = null;

        /**
         * Get the absolute path of the machine file
         */
        File machineFile = new File(machineFilePath);
        String absolutePath = machineFile.getAbsolutePath();

        /**
         * Read in the file line by line
         */
        try (Stream<String> fileStream = Files.lines(Paths.get(absolutePath)))
        {
            instructions = fileStream.collect(Collectors.toList());
            fileStream.close();
        }
        catch (IOException ex)
        {
            System.out.println("IO ERROR: " + ex);
        }

        return instructions;
    }

    /**
     * Returns a list of machine instructions in binary after converting hexCode to binaryCode
     * @param hexInstructions the list of instructions in hex
     * @return binaryInstructions the list of instructions in binary
     */
    public List<String> convertInstructionsToBinary(List<String> hexInstructions)
    {
        List<String> binaryInstructions = new ArrayList<>();

        for (String hexCode : hexInstructions)
        {
            // Convert hexCode to binaryCode
            String binaryCode = new BigInteger(hexCode, 16).toString(2);

            // If the length is less than 32, prepend 0's
            if (binaryCode.length() < 32)
            {
                while(binaryCode.length() < 32)
                {
                    binaryCode = "0" + binaryCode;
                }
            }

            // Add the binaryCode to the list of instructions
            binaryInstructions.add(binaryCode);
        }

        return binaryInstructions;
    }

    /**
     * Returns a hex number from the immediate field bits passed in
     * @param immBits the immediate field bits
     * @return hexNumber (the immediate field bits in hex)
     */
    public String convertImmediateFieldToHex(String immBits)
    {
        // Convert immBits to hex
        String hexNumber = new BigInteger(immBits, 2).toString(16);

        return hexNumber;
    }

    /**
     * Returns a boolean indicating whether the binaryCode is a R-type or an I-type
     * @param binaryCode the machineInstruction in binary
     * @return type the boolean indicating R-type (true = 1) or I-type (false = 0)
     */
    public boolean checkType(String binaryCode)
    {
        // Default to R-type
        boolean type = true;

        // R-type
        if (binaryCode.startsWith("000000"))
        {
            type = true;
        }
        // I-type
        else
        {
            type = false;
        }

        return type;
    }

    /**
     *  Performs a decode on an I-Type instruction
     * @param binaryCode
     * @return the decoded instruction
     */
    public String decodeIType(String binaryCode)
    {
        String decodedInstruction = "";

        // Setup dictionary for IType
        ITypeDictionary iTypeDictionary = new ITypeDictionary();
        iTypeDictionary.setValues();

        // Setup dictionary for registers
        RegisterDictionary registerDictionary = new RegisterDictionary();
        registerDictionary.setValues();

        // Get op from first 6 bits (0 - 6)
        String firstSixBits = binaryCode.substring(0, Math.min(binaryCode.length(), 6));
        String op = iTypeDictionary.getOp(firstSixBits);

        // Get source register from bits (7 - 11)
        String sourceRegisterBits = binaryCode.substring(6, 11);
        String sourceRegister = registerDictionary.getRegister(sourceRegisterBits);

        // Get target register from bits (12 - 16)
        String targetRegisterBits = binaryCode.substring(11, 16);
        String targetRegister = registerDictionary.getRegister(targetRegisterBits);

        // Get immediate field from bits (17 - 32)
        String immediateBits = binaryCode.substring(16, 32);
        String immHexField = convertImmediateFieldToHex(immediateBits);

        // Special case for jump instruction (shift right by two bits)
        if (op.equals("j"))
        {
            immediateBits = binaryCode.substring(6, 32);
            immediateBits = immediateBits.replaceFirst ("^0*", ""); // Remove leading 0's
            int decimalValue = Integer.parseInt(immediateBits, 2); // Convert to decimal
            decimalValue = decimalValue / 4; // Shift right by two bits (2^2 = 4 for decimal shifting)
            immediateBits = Integer.toBinaryString(decimalValue); // Convert back to a binary string

            immHexField = convertImmediateFieldToHex(immediateBits);
        }

        // Construct full instruction
        decodedInstruction = createInstructionIType(op, sourceRegister, targetRegister, immHexField);

        return decodedInstruction;
    }

    /**
     * Performs a decode on an R-Type instruction
     * @param binaryCode
     * @return the decoded instruction
     */
    public String decodeRType(String binaryCode)
    {
        String decodedInstruction = "";

        // Setup dictionary for RType
        RTypeDictionary rTypeDictionary = new RTypeDictionary();
        rTypeDictionary.setValues();

        // Setup dictionary for registers
        RegisterDictionary registerDictionary = new RegisterDictionary();
        registerDictionary.setValues();

        // First 6 bits of R-Type are 0's (0 - 6)

        // Get source register from bits (7 - 11)
        String sourceRegisterBits = binaryCode.substring(6, 11);
        String sourceRegister = registerDictionary.getRegister(sourceRegisterBits);

        // Get target register from bits (12 - 16)
        String targetRegisterBits = binaryCode.substring(11, 16);
        String targetRegister = registerDictionary.getRegister(targetRegisterBits);

        // Get destination register from bits (17 - 21)
        String destinationRegisterBits = binaryCode.substring(16, 21);
        String destinationRegister = registerDictionary.getRegister(destinationRegisterBits);

        // Get op from last 11 bits (21 - 32)
        String lastElevenBits = binaryCode.substring(binaryCode.length() - 11);
        String op = rTypeDictionary.getOp(lastElevenBits);

        // Construct full instruction
        decodedInstruction = createInstructionRType(sourceRegister, targetRegister, destinationRegister, op);

        return decodedInstruction;
    }

    /**
     * Returns a decodedInstruction for IType instructions.
     * @param op
     * @param targetRegister
     * @param sourceRegister
     * @param immHexField
     * @return decodedInstruction which is the full instruction decoded from binary
     */
    public String createInstructionIType(String op, String sourceRegister, String targetRegister, String immHexField)
    {
        String decodedInstruction = "";

        // lui $t, imm
        if (op.equals("lui"))
        {
            decodedInstruction = op + " " + targetRegister + ", 0x" + immHexField.toUpperCase();
        }
        // beq $s, $t, imm
        else if (op.equals("beq"))
        {
            decodedInstruction = op + " " + sourceRegister + ", " + targetRegister + ", 0x" + immHexField.toUpperCase();
        }
        // lw/sw $t, imm($s)
        else if (op.equals("lw") || op.equals("sw"))
        {
            decodedInstruction = op + " " + targetRegister + ", " + immHexField.toUpperCase() + "(" + sourceRegister + ")";
        }
        // j imm
        else if (op.equals("j"))
        {
            decodedInstruction = op + " " +  "0x" + immHexField.toUpperCase();
        }
        // op $t, $s, imm
        else
        {
            decodedInstruction = op + " " + targetRegister + ", " + sourceRegister + ", 0x" + immHexField.toUpperCase();
        }

        return decodedInstruction;
    }

    /**
     * Returns a decodedInstruction for RType instructions.
     * @param sourceRegister
     * @param targetRegister
     * @param destinationRegister
     * @param op
     * @return decodedInstruction which is the full instruction decoded from binary
     */
    public String createInstructionRType(String sourceRegister, String targetRegister, String destinationRegister, String op)
    {
        String decodedInstruction = "";

        // syscall (R-Type)
        if (op == null)
        {
            decodedInstruction = "syscall";
        }
        // op $d, $s, $t
        else
        {
            decodedInstruction = op + " " + destinationRegister + ", " + sourceRegister + ", " + targetRegister;
        }

        return decodedInstruction;
    }

    /**
     * Prints out the set of decoded instructions
     * @param decodedInstructions
     */
    public void printResults(List<String> decodedInstructions)
    {
        for (String instruction : decodedInstructions)
        {
            System.out.println(instruction);
        }
    }
}
