package com.alphasoft.pos.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class FileHelper {

    public static InputStream fileToInputStream(File file){
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
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
