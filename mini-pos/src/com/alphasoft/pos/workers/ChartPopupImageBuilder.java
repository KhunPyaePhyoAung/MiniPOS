package com.alphasoft.pos.workers;


public abstract class ChartPopupImageBuilder {

    protected Position position = Position.NORTH;
    protected double margin;
    protected double popupWidth;
    protected double popupHeight;

    private static final double MIN_MARGIN = 10;
    private static final double MIN_POPUP_WIDTH = 50;
    private static final double MIN_POPUP_HEIGHT = 50;
    private static final Position DEFAULT_POSITION = Position.NORTH;

    protected abstract void build();

    public void setPosition(Position position){
        this.position = null==position? DEFAULT_POSITION : position;
    }


    public void setMargin(double margin){
        margin = Math.abs(margin);
        this.margin = Math.max(margin, MIN_MARGIN);
    }


    public void setPopupSize(double width,double height){
        this.popupWidth = Math.max(width, MIN_POPUP_WIDTH);
        this.popupHeight = Math.max(height, MIN_POPUP_HEIGHT);
    }




    public enum Position{
        EAST,WEST,SOUTH,NORTH,SOUTH_EAST,NORTH_EAST,SOUTH_WEST,NORTH_WEST
    }


}
