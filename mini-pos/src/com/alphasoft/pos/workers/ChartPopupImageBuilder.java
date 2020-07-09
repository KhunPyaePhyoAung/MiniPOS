package com.alphasoft.pos.workers;


import com.alphasoft.pos.views.customs.ImagePopupWindow;
import javafx.scene.Node;

public abstract class ChartPopupImageBuilder {

    protected Position position = Position.NORTH;
    protected double margin;
    protected double popupWidth;
    protected double popupHeight;

    private static final double MIN_MARGIN = 10;
    private static final double MIN_POPUP_WIDTH = 50;
    private static final double MIN_POPUP_HEIGHT = 50;
    private static final Position DEFAULT_POSITION = Position.NORTH;

    public abstract void build();

    public ChartPopupImageBuilder setPosition(Position position){
        this.position = null==position? DEFAULT_POSITION : position;
        return this;
    }


    public ChartPopupImageBuilder setMargin(double margin){
        margin = Math.abs(margin);
        this.margin = Math.max(margin, MIN_MARGIN);
        return this;
    }


    public ChartPopupImageBuilder setPopupSize(double width,double height){
        this.popupWidth = Math.max(width, MIN_POPUP_WIDTH);
        this.popupHeight = Math.max(height, MIN_POPUP_HEIGHT);
        return this;
    }

    protected void setMouseListener(Node dataNode,ImagePopupWindow popup){
        dataNode.setOnMouseEntered(e->{
            popup.show();
            popup.move(e.getScreenX(),e.getScreenY());
        });

        dataNode.setOnMouseMoved(e-> popup.move(e.getScreenX(),e.getScreenY()));

        dataNode.setOnMouseExited(e-> popup.hide());
    }




    public enum Position{
        EAST,WEST,SOUTH,NORTH,SOUTH_EAST,NORTH_EAST,SOUTH_WEST,NORTH_WEST
    }


}
