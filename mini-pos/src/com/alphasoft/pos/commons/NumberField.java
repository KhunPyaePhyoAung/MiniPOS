package com.alphasoft.pos.commons;

import com.alphasoft.pos.utils.StringUtils;
import javafx.application.Platform;
import javafx.scene.control.TextField;


public class NumberField {

    private final TextField numberField;
    private Integer defaultValue;
    private Integer minValue;
    private Integer maxValue;

    public NumberField(TextField numberField){
        this.numberField = numberField;
        minValue = Integer.MIN_VALUE;
        maxValue = Integer.MAX_VALUE;
        setup();
    }

    public void setDefaultValue(Integer defaultValue){
        if(defaultValue>maxValue) throw new IllegalArgumentException("Default value is greater than maximum value");
        if(defaultValue<minValue) throw new IllegalArgumentException("Default value is less than minimum value");
        this.defaultValue = defaultValue;
        if(null==getValue()){
            setValue(defaultValue);
        }
    }

    public void setMinMaxValues(Integer minValue,Integer maxValue){
        setMinValue(minValue);
        setMaxValue(maxValue);
    }

    public void setMinValue(Integer minValue){
        this.minValue = null==minValue? Integer.MIN_VALUE:minValue;
        if(null==getValue() || null==minValue) return;
        if(getValue()<minValue){
            setValue(minValue);
        }
    }

    public void setMaxValue(Integer maxValue){
        this.maxValue = null==maxValue ? Integer.MAX_VALUE:maxValue;
        if(null==getValue() || null == maxValue) return;
        if(getValue()>maxValue){
            setValue(maxValue);
        }
    }

    public void setValue(Integer value){
        numberField.setText(null==value? null:String.valueOf(value));
        numberField.positionCaret(numberField.getText().length());
    }

    public Integer getValue(){
        String textValue = numberField.getText();
        return StringUtils.isEmpty(textValue)? null: Integer.parseInt(textValue);
    }

    private void setup(){
        numberField.setText(null==defaultValue? null:String.valueOf(defaultValue));
        numberField.textProperty().addListener((l,o,n)->{
            try{
                if(!StringUtils.isEmpty(n)){
                    int value = Integer.parseInt(n);
                    if(value<minValue) throw new Exception("Value is less than minimum value");
                    if(value>maxValue) throw new Exception("Value is greater than maximum value");
                    Platform.runLater(()-> setValue(value));
                }else {
                    numberField.setText(null==defaultValue? null:String.valueOf(defaultValue));
                }
            }catch (Exception e){
                numberField.setText(o);
            }

        });
    }

    public boolean isEmpty(){
        return null==getValue();
    }


    public boolean valueEquals(Integer value){
        if(null==getValue() && null==value){
            return true;
        }else if(null==getValue() || null==value){
            return false;
        }else{
            return getValue().equals(value);
        }
    }


}
