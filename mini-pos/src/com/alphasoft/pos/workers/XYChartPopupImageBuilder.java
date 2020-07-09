package com.alphasoft.pos.workers;

import com.alphasoft.pos.views.customs.ImagePopupWindow;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;

import java.util.function.Function;



public class XYChartPopupImageBuilder<X,Y> extends ChartPopupImageBuilder{

    private final XYChart<X,Y> chart;
    private final Function<X,Image> imageGetter;

    public XYChartPopupImageBuilder(XYChart<X,Y> chart, Function<X,Image> imageGetter){
        this.chart = chart;
        this.imageGetter = imageGetter;
    }



    @Override
    public void build() {
        for(XYChart.Series<X,Y> series:chart.getData()){
            for(XYChart.Data<X,Y> data:series.getData()){
                Node node = data.getNode();
                ImagePopupWindow popup = new ImagePopupWindow();
                popup.setPosition(position);
                popup.setMargin(margin);
                popup.setImage(imageGetter.apply(data.getXValue()));
                popup.setSize(popupWidth,popupHeight);
                setMouseListener(node,popup);
            }
        }
    }
}
