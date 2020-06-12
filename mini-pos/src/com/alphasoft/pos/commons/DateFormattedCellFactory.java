package com.alphasoft.pos.commons;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DateFormattedCellFactory<S> implements Callback<TableColumn<S, LocalDate>, TableCell<S,LocalDate>> {
    @Override
    public TableCell<S, LocalDate> call(TableColumn<S, LocalDate> param) {
        return new TableCell<>(){
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                if(null == item || empty){
                    setText("");
                }else{
                    setText(item.format(DateTimeFormatter.ofPattern("yyyy-MMM-dd (E)")));
                }
            }
        };
    }
}
