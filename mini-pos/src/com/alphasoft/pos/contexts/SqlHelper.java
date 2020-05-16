package com.alphasoft.pos.contexts;

import java.io.IOException;
import java.util.Properties;

public class SqlHelper {
    private static Properties properties;
    static {
        try{
            properties = new Properties();
            properties.load(SqlHelper.class.getResourceAsStream("sql.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getQuery(String key){
        return properties.getProperty(key);
    }
}
