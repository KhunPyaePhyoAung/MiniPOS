package com.alphasoft.pos.commons;

import com.alphasoft.pos.contexts.PosException;


public class Validations {
    public static void notEmptyString(String data, String errorMessage){
        if(StringUtils.isEmpty(data))
            throw new PosException(errorMessage);
    }

    public static void notNull(Object object,String errorMessage){
        if(null==object) throw new PosException(errorMessage);
    }
}
