package com.alphasoft.pos.commons;

import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;

import java.util.function.Function;

public abstract class ChartPopupImageSetter{

    protected Position position = Position.NORTH;
    protected double margin = 10;
    protected double popupWidth = 100;
    protected double popupHeight = 100;
    protected static final double MIN_MARGIN = 10;
    protected static final double MIN_POPUP_WIDTH = 50;
    protected static final double MIN_POPUP_HEIGHT = 50;

    public abstract <X,Y> void pair(XYChart<X,Y> chart, Function<X, Image> imageGetter);
    public abstract void setPosition(Position position);
    public abstract void setMargin(double margin);
    public abstract void setPopupSize(double width,double height);

    public enum Position{
        EAST,WEST,SOUTH,NORTH,SOUTH_EAST,NORTH_EAST,SOUTH_WEST,NORTH_WEST;
    }


}
