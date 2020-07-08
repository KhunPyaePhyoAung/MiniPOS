package com.alphasoft.pos.commons;

import com.alphasoft.pos.views.customs.ImagePopupWindow;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.util.function.Function;

import static com.alphasoft.pos.commons.ChartPopupImageBuilder.*;


public class XYChartPopupImageBuilder<X,Y> {

    private Position position = ChartPopupImageBuilder.Position.NORTH;
    private double margin = 10;
    private double popupWidth = 100;
    private double popupHeight = 100;

    private XYChart<X,Y> chart;
    private Function<X,Image> imageGetter;

    public void build(XYChart<X,Y> chart, Function<X,Image> imageGetter){

        this.chart = chart;
        this.imageGetter = imageGetter;

        for(XYChart.Series<X,Y> series:chart.getData()){
            for(XYChart.Data<X,Y> data:series.getData()){
                Node node = data.getNode();
                ImagePopupWindow popup = new ImagePopupWindow();
                popup.setPosition(position);
                popup.setMargin(margin);
                popup.setImage(imageGetter.apply(data.getXValue()));
                popup.setSize(popupWidth,popupHeight);
                popup.initOwner(null);
                popup.initStyle(StageStyle.UNDECORATED);
                popup.initModality(Modality.NONE);

                node.setOnMouseEntered(e->{
                    popup.move(e.getScreenX(),e.getScreenY());
                    popup.show();
                });

                node.setOnMouseMoved(e-> popup.move(e.getScreenX(),e.getScreenY()));

                node.setOnMouseExited(e-> popup.hide());
            }
        }
    }


    public void setPosition(ChartPopupImageBuilder.Position position){
        this.position = position;
        rebuild();
    }

    public void setMargin(double margin){
        margin = Math.abs(margin);
        this.margin = Math.max(margin, MIN_MARGIN);
        rebuild();
    }

    public void setPopupSize(double width,double height){
        this.popupWidth = Math.max(width, MIN_POPUP_WIDTH);
        this.popupHeight = Math.max(height, MIN_POPUP_HEIGHT);
        rebuild();
    }

    private void rebuild(){
        build(chart,imageGetter);
    }


}
