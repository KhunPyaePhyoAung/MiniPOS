package com.alphasoft.pos.commons;

import com.alphasoft.pos.views.customs.ImagePopupWindow;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.util.function.Function;


public class XYChartPopupImageSetter extends ChartPopupImageSetter {

    private XYChart chart;
    private Function<?,Image> imageGetter;

    @Override
    public <X,Y> void pair(XYChart<X,Y> chart,Function<X,Image> imageGetter){

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


    @Override
    public void setPosition(Position position){
        this.position = position;
        repair();
    }

    @Override
    public void setMargin(double margin){
        margin = Math.abs(margin);
        this.margin = margin<MIN_MARGIN? MIN_MARGIN:margin;
        repair();
    }

    @Override
    public void setPopupSize(double width,double height){
        this.popupWidth = width<MIN_POPUP_WIDTH? MIN_POPUP_WIDTH:width;
        this.popupHeight = height<MIN_POPUP_HEIGHT ? MIN_POPUP_HEIGHT:height;
        repair();
    }

    @SuppressWarnings("unchecked")
    private void repair(){
        pair(chart,imageGetter);
    }





}
