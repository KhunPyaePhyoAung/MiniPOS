package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.contexts.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private HBox navigationBar;

    @FXML
    private StackPane contentPane;

    @FXML
    private Label username;

    public static Stage mainStage;


    @FXML
    public void requestView(MouseEvent event){
        var source = event.getSource();
        if(source instanceof VBox){
            VBox vBox = (VBox)source;
            loadView(vBox.getId());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username.setText(Logger.getLogger().getLoggedAccount().getName());
        Platform.runLater(()-> loadView("pos_home"));
    }

    private void loadView(String viewName){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(String.format("/com/alphasoft/pos/views/%s.fxml",viewName)));
            Parent view = fxmlLoader.load();
            loadView(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(Parent view){
        contentPane.getChildren().clear();
        contentPane.getChildren().add(view);
        navigationBar.getChildren().stream().filter(b->b.getId().equals(view.getId())).forEach(Node::requestFocus);
        navigationBar.getChildren().stream().filter(b->b instanceof VBox).forEach(b->{
                                                                                        b.getStyleClass().retainAll("navigation-icon-box");
                                                                                        if(b.getId().equals(view.getId())) b.getStyleClass().add("navigation-icon-box-active");
                                                                                    });
    }

    public void exit(){
        System.out.println("exiting");
        //M10 20c-5.523 0-10-4.477-10-10s4.477-10 10-10v0c5.523 0 10 4.477 10 10s-4.477 10-10 10v0zM7 6v2c0 1.657 1.343 3 3 3s3-1.343 3-3v0-2c0-1.657-1.343-3-3-3s-3 1.343-3 3v0zM3.35 14.44c1.456 2.155 3.89 3.553 6.65 3.553s5.194-1.398 6.632-3.525l0.018-0.028c-1.951-0.918-4.238-1.453-6.65-1.453s-4.699 0.536-6.748 1.495l0.098-0.041z
    }

    public static void show(){
        try {
            Parent view = FXMLLoader.load(PosHomeController.class.getResource("/com/alphasoft/pos/views/main_window.fxml"));
            Stage stage = new Stage();
            mainStage = stage;
            stage.setScene(new Scene(view));
            stage.centerOnScreen();
//            stage.setFullScreen(true);
//            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
//            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
