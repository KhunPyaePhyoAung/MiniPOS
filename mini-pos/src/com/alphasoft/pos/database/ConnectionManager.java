package com.alphasoft.pos.database;

import com.alphasoft.pos.contexts.PosConnectionException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static com.alphasoft.pos.utils.MessageHelper.getMessage;

public class ConnectionManager {
    private static String DRIVER;
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {

        try {
            Properties connectionProperties = new Properties();
            connectionProperties.load(ConnectionManager.class.getResourceAsStream("/com/alphasoft/pos/properties/connection.properties"));
            DRIVER = connectionProperties.getProperty("DRIVER");
            URL = connectionProperties.getProperty("URL");
            USER = connectionProperties.getProperty("USER");
            PASSWORD = connectionProperties.getProperty("PASSWORD");
            Class.forName(DRIVER);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        Connection connection;
        try{
            connection = DriverManager.getConnection(URL,USER,PASSWORD);
        }catch (SQLException e){
            throw new PosConnectionException(getMessage("server.cannot.connect"));
        }
        return connection;

    }
}
