package com.alphasoft.pos.commons;

import com.alphasoft.pos.views.customs.ImagePopupWindow;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.util.function.Function;


public class XYChartPopupImageSetter implements ChartPopupImageSetter {

    private XYChart chart;
    private Function<?,Image> imageGetter;
    private Position position;
    private double margin;


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
                popup.initOwner(null);
                popup.initStyle(StageStyle.UNDECORATED);
                popup.initModality(Modality.NONE);

                node.setOnMouseEntered(e->{
                    popup.move(getMouseX(e),getMouseY(e));
                    popup.show();
                });

                node.setOnMouseMoved(e-> popup.move(getMouseX(e),getMouseY(e)));

                node.setOnMouseExited(e-> popup.hide());
            }
        }
    }



    public void setPosition(Position position){
        this.position = position;
        repair();
    }

    public void setMargin(double margin){
        this.margin = margin;
        repair();
    }

    @SuppressWarnings("unchecked")
    private void repair(){
        pair(chart,imageGetter);
    }

    private double getMouseX(MouseEvent e){
        return e.getScreenX();
    }

    private double getMouseY(MouseEvent e){
        return e.getScreenY();
    }




}
