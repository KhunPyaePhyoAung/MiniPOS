package com.alphasoft.pos.commons;

import com.alphasoft.pos.utils.StringUtils;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.ParseException;

public class MmkFormatter extends StringConverter<Number> {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#,### MMK");

    @Override
    public String toString(Number number) {
        try{
            if(null!=number){
                return decimalFormat.format(number);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Number fromString(String string) {
        try {
            if(!StringUtils.isEmpty(string)){
                return decimalFormat.parse(string);
            }
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
