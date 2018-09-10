package com.decode;

import java.util.HashMap;
import java.util.Map;

public class RegisterDictionary
{
    public Map<String, String> dictionaryRegisters = new HashMap<>();

    public void setValues()
    {
        dictionaryRegisters.put("00000", "$zero");
        dictionaryRegisters.put("00001", "$at");
        dictionaryRegisters.put("00010", "$v0");
        dictionaryRegisters.put("00011", "$v1");
        dictionaryRegisters.put("00100", "$a0");
        dictionaryRegisters.put("00101", "$a1");
        dictionaryRegisters.put("00110", "$a2");
        dictionaryRegisters.put("00111", "$a3");
        dictionaryRegisters.put("01000", "$t0");
        dictionaryRegisters.put("01001", "$t1");
        dictionaryRegisters.put("01010", "$t2");
        dictionaryRegisters.put("01011", "$t3");
        dictionaryRegisters.put("01100", "$t4");
        dictionaryRegisters.put("01101", "$t5");
        dictionaryRegisters.put("01110", "$t6");
        dictionaryRegisters.put("01111", "$t7");
        dictionaryRegisters.put("10000", "$s0");
        dictionaryRegisters.put("10001", "$s1");
        dictionaryRegisters.put("10010", "$s2");
        dictionaryRegisters.put("10011", "$s3");
        dictionaryRegisters.put("10100", "$s4");
        dictionaryRegisters.put("10101", "$s5");
        dictionaryRegisters.put("10110", "$s6");
        dictionaryRegisters.put("10111", "$s7");
        dictionaryRegisters.put("11000", "$t8");
        dictionaryRegisters.put("11001", "$t9");
        dictionaryRegisters.put("11100", "$gp");
        dictionaryRegisters.put("11101", "$sp");
        dictionaryRegisters.put("11110", "$fp");
        dictionaryRegisters.put("11111", "$ra");
    }

    public String getRegister(String registerBits)
    {
        return dictionaryRegisters.get(registerBits);
    }
}
