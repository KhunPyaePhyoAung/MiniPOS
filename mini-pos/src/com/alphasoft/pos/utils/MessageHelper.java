package com.alphasoft.pos.utils;

import java.io.IOException;
import java.util.Properties;

public class MessageHelper {
    private static Properties messageProperties;
    static {

        try {
            messageProperties = new Properties();
            messageProperties.load(MessageHelper.class.getResourceAsStream("/com/alphasoft/pos/properties/message.properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMessage(String key){
        return messageProperties.getProperty(key);
    }
}
