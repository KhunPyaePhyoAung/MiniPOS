package com.alphasoft.pos.commons;


import javafx.application.Platform;
import javafx.scene.control.TextField;

public class NumberInput {

    public static void attach(TextField  textField){
        textField.setText("0");
        textField.textProperty().addListener((l,o,n)->{
            try{
                if(!StringUtils.isEmpty(n)){
                    int value = Integer.parseInt(n);
                    StringBuilder sb = new StringBuilder(String.valueOf(value));
                    while (sb.length()>1 && sb.charAt(0)=='0'){
                        sb.deleteCharAt(0);
                    }
                    Platform.runLater(()->{
                        textField.setText(sb.toString());
                        textField.positionCaret(textField.getText().length());
                    });
                }else{
                    textField.setText("0");
                }
            }catch (Exception exception){
                textField.setText(o);
            }
        });
    }


}
