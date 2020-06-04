package com.alphasoft.pos.commons;


import javafx.scene.control.TextField;

public class NumberInput {
    public static void attach(TextField textField){
        textField.textProperty().addListener((l,o,n)->{
            try{
                if(!StringUtils.isEmpty(n)) Integer.parseInt(n);
            }catch (Exception exception){
                textField.setText(o);
            }
        });
    }
}
