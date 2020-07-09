package com.alphasoft.pos.utils;

import java.io.IOException;
import java.util.Properties;

public class MySqlHelper {
    private static Properties properties;
    static {
        try{
            properties = new Properties();
            properties.load(MySqlHelper.class.getResourceAsStream("/com/alphasoft/pos/properties/sql.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getQuery(String key){
        return properties.getProperty(key);
    }
}
