package com.alphasoft.pos.commons;

import java.text.DecimalFormat;

public class StringUtils {
    private static DecimalFormat decimalFormat = new DecimalFormat("#,##0");
    public static boolean isEmpty(String data){
        return   null == data || data.isEmpty();
    }

    public static String formatToMmk(int value){
        return String.format("%s MMK",decimalFormat.format(value));
    }
}
