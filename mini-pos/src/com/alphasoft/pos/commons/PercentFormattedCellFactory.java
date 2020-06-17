package com.alphasoft.pos.commons;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class PercentFormattedCellFactory<S> implements Callback<TableColumn<S,Integer>, TableCell<S,Integer>> {

    @Override
    public TableCell<S, Integer> call(TableColumn<S, Integer> param) {
        return new TableCell<>(){
            @Override
            protected void updateItem(Integer item, boolean empty) {
                if(null==item & empty){
                    setText("");
                }else{
                    setText(String.format("%d %%",item));
                }
            }
        };
    }
}