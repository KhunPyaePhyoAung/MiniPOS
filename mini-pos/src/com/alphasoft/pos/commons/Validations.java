package com.alphasoft.pos.commons;

import com.alphasoft.pos.contexts.PosException;

public class Validations {
    public static void notEmptyInput(String data,String field){
        if(StringUtils.isEmpty(data))
            throw new PosException(String.format("Please enter %s",field));
    }
}
