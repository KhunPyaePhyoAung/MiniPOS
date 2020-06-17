package com.alphasoft.pos.commons;

import java.io.IOException;
import java.util.Properties;

public class MessageRepo {
    private static Properties messageProperties;
    static {

        try {
            messageProperties = new Properties();
            messageProperties.load(MessageRepo.class.getResourceAsStream("/com/alphasoft/pos/properties/message.properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMessage(String key){
        return messageProperties.getProperty(key);
    }
}
