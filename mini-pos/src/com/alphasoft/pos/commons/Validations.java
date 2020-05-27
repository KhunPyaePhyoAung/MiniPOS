package com.alphasoft.pos.commons;

import com.alphasoft.pos.contexts.PosException;

import java.util.Objects;

public class Validations {
    public static void notEmptyInput(String data,String field){
        if(StringUtils.isEmpty(data))
            throw new PosException(String.format("Please enter %s",field));
    }

    public static void notNull(Object object,String errorMessage){
        if(null==object) throw new PosException(errorMessage);
    }
}
