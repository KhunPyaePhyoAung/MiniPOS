package com.alphasoft.pos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class PosApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException{
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/com/alphasoft/pos/views/login_form.fxml"))));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
