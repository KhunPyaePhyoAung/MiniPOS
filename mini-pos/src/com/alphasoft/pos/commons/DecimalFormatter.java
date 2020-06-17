package com.alphasoft.pos.commons;

import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.ParseException;

public class DecimalFormatter extends StringConverter<Number> {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    @Override
    public String toString(Number num) {
        try{
            if(null!=num)
                return decimalFormat.format(num);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Number fromString(String string) {
        try{
            if(!StringUtils.isEmpty(string)){
                return decimalFormat.parse(string);
            }
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
