package com.alphasoft.pos.commons;


import javafx.application.Platform;
import javafx.scene.control.TextField;

public class NumberInput {

    private Integer def=null;

    private NumberInput(){}

    public  void attach(TextField textField,Integer defaultValue){
        def = defaultValue;
        attach(textField);
    }

    public void attach(TextField  textField){
        textField.setText(null==def? "":String.valueOf(def));
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
                    textField.setText(null==def ? "":String.valueOf(def));
                }
            }catch (Exception exception){
                textField.setText(o);
            }
        });
    }

    public static NumberInput getNew(){
        return new NumberInput();
    }


}
