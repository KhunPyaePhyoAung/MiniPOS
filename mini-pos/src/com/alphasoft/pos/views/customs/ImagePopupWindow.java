package com.alphasoft.pos.views.customs;

import com.alphasoft.pos.commons.ChartPopupImageBuilder;
import com.alphasoft.pos.models.XYPoint;
import com.alphasoft.pos.views.controllers.ImagePopupController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;

import java.io.IOException;

public class ImagePopupWindow extends PosWindowStage {
    private ImagePopupController controller;
    private ChartPopupImageBuilder.Position position;
    private double margin;

    public ImagePopupWindow(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/alphasoft/pos/views/image_popup.fxml"));
            Parent view = fxmlLoader.load();
            controller = fxmlLoader.getController();
            setScene(new Scene(view));
            initModality(Modality.NONE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImage(Image image){
        controller.setImage(image);
    }

    public void setSize(double width,double height){ controller.setSize(width,height);}

    public void setPosition(ChartPopupImageBuilder.Position position){
        this.position = position;
    }

    public void setMargin(double margin){
        this.margin = margin;
    }

    public void move(double x, double y){
        XYPoint point = getPointForWindow(x,y);
        setX(point.getX());
        setY(point.getY());
        toFront();
    }

    private XYPoint getPointForWindow(double mouseX, double mouseY){
        double popupWidth = getWidth();
        double popupHeight = getHeight();

        XYPoint point = new XYPoint();
        if(null==position) position = ChartPopupImageBuilder.Position.NORTH;
        switch (position){
            case EAST:
                point.setX(mouseX+margin);
                point.setY(mouseY-(popupHeight/2));
                break;
            case WEST:
                point.setX(mouseX-popupWidth-margin);
                point.setY(mouseY-(popupHeight/2));
                break;
            case NORTH:
                point.setX(mouseX-(popupWidth/2));
                point.setY(mouseY-popupHeight-margin);
                break;
            case SOUTH:
                point.setX(mouseX-(popupWidth/2));
                point.setY(mouseY+margin);
                break;
            case NORTH_EAST:
                point.setX(mouseX+margin);
                point.setY(mouseY-popupHeight-margin);
                break;
            case NORTH_WEST:
                point.setX(mouseX-popupWidth-margin);
                point.setY(mouseY-popupHeight-margin);
                break;
            case SOUTH_EAST:
                point.setX(mouseX+margin);
                point.setY(mouseY+margin);
                break;
            case SOUTH_WEST:
                point.setX(mouseX-popupWidth-margin);
                point.setY(mouseY+margin);
                break;
        }
        return point;
    }
}
