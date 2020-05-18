package com.alphasoft.pos.commons;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class ImageHelper {
    public static InputStream fileToInputStream(File file){
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream blobToInputStream(Blob blob){
        try {
            return blob.getBinaryStream();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
