package com.alphasoft.pos.commons;


public abstract class ChartPopupImageBuilder {

    protected static final double MIN_MARGIN = 10;
    protected static final double MIN_POPUP_WIDTH = 50;
    protected static final double MIN_POPUP_HEIGHT = 50;

    protected abstract void setPosition(Position position);
    protected abstract void setMargin(double margin);
    protected abstract void setPopupSize(double width,double height);
    protected abstract void build();

    public enum Position{
        EAST,WEST,SOUTH,NORTH,SOUTH_EAST,NORTH_EAST,SOUTH_WEST,NORTH_WEST
    }


}
