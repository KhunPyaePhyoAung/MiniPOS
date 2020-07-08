package com.alphasoft.pos.commons;

import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;

import java.util.function.Function;

public interface ChartPopupImageSetter{
    <X,Y> void pair(XYChart<X,Y> chart, Function<X, Image> imageGetter);
    void setPosition(Position position);
    void setMargin(double margin);

    enum Position{
        EAST,WEST,SOUTH,NORTH,SOUTH_EAST,NORTH_EAST,SOUTH_WEST,NORTH_WEST;
    }


}
