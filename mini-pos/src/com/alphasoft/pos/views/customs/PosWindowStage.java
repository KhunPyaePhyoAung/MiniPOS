package com.alphasoft.pos.views.customs;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PosWindowStage extends Stage {
    public PosWindowStage(){
        setup();
    }

    public PosWindowStage(Stage owner){
        setup();
        initOwner(owner);
    }

    private void setup(){
        setResizable(false);
        centerOnScreen();
        initStyle(StageStyle.UNDECORATED);
        initModality(Modality.WINDOW_MODAL);
    }
}
