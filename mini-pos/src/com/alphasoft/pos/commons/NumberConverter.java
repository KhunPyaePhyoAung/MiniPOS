package com.alphasoft.pos.commons;

import javafx.util.StringConverter;

public class NumberConverter extends StringConverter<Number> {
    @Override
    public String toString(Number number) {
        return String.valueOf(number.intValue());
    }

    @Override
    public Number fromString(String string) {
        try {
            int value = Integer.parseInt(string);
            return value;
        }catch (Exception e){

        }
        return null;
    }
}
