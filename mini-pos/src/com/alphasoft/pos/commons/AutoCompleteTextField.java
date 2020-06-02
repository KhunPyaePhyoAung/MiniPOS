package com.alphasoft.pos.commons;

import javafx.geometry.Bounds;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AutoCompleteTextField {
    public static <T> void attach(TextField textField, Function<String, List<T>> searcher, Consumer<T> consumer,int suggestStartLength){
        ContextMenu suggestionMenu = new ContextMenu();

        textField.widthProperty().addListener((l,o,n)-> suggestionMenu.setStyle(String.format("-fx-pref-width:%f",n.doubleValue())));
        Consumer<T> executor = s->{
            if(null!=consumer){
                consumer.accept(s);
            }
            suggestionMenu.hide();
        };

        textField.textProperty().addListener((l,o,n)->{
            suggestionMenu.getItems().clear();
            if(textField.getText().length() >= suggestStartLength){
                searcher.apply(textField.getText().trim()).stream().limit(10).forEach(s->{
                    MenuItem menuItem = new MenuItem(s.toString());
                    menuItem.setOnAction(e->executor.accept(s));
                    suggestionMenu.getItems().add(menuItem);
                });

                if(!suggestionMenu.getItems().isEmpty()){
                    if(!suggestionMenu.isShowing()){
                        Bounds bounds = textField.localToScreen(textField.getLayoutBounds());
                        suggestionMenu.show(textField,bounds.getMinX(),bounds.getMaxY());
                    }
                }else{
                    suggestionMenu.hide();
                }
            }else{
                suggestionMenu.hide();
            }
        });


    }

    public static <T> void attach(TextField textField,Function<String,List<T>> mapper,Consumer<T> consumer){
        attach(textField,mapper,consumer,1);
    }
}
