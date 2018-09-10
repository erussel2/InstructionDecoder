package com.decode;

import java.util.HashMap;
import java.util.Map;

public class RTypeDictionary
{
    public Map<String, String> dictionaryLastEleven = new HashMap<>();

    public void setValues()
    {
        dictionaryLastEleven.put("00000100000", "add");
    }

    public String getOp(String firstSixBits)
    {
        return dictionaryLastEleven.get(firstSixBits);
    }
}
