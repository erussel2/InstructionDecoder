package com.decode;

import java.util.HashMap;
import java.util.Map;

public class ITypeDictionary
{
    public Map<String, String> dictionaryFirstSix = new HashMap<>();

    public void setValues()
    {
        dictionaryFirstSix.put("001111", "lui");
        dictionaryFirstSix.put("001101", "ori");
        dictionaryFirstSix.put("100011", "lw");
        dictionaryFirstSix.put("001000", "addi");
        dictionaryFirstSix.put("000100", "beq");
        dictionaryFirstSix.put("000000", "add");
        dictionaryFirstSix.put("101011", "sw");
        dictionaryFirstSix.put("000010", "j");
    }

    public String getOp(String firstSixBits)
    {
        return dictionaryFirstSix.get(firstSixBits);
    }
}
