package com.alphasoft.pos.commons;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeFormattedCellFactory<S> implements Callback<TableColumn<S, LocalTime>, TableCell<S,LocalTime>> {
    @Override
    public TableCell<S, LocalTime> call(TableColumn<S, LocalTime> param) {
        return new TableCell<>(){
            @Override
            protected void updateItem(LocalTime item, boolean empty) {
                if(null == item || empty){
                    setText("");
                }else{
                    setText(item.format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
                }
            }
        };
    }
}
