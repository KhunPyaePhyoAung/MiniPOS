package com.alphasoft.pos.workers;

import com.alphasoft.pos.views.customs.ImagePopupWindow;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.util.function.Function;

public class PieChartPopupImageBuilder extends ChartPopupImageBuilder{

    private final PieChart chart;
    private final Function<String,Image> imageGetter;

    public PieChartPopupImageBuilder(PieChart chart, Function<String, Image> imageGetter){
        this.chart = chart;
        this.imageGetter = imageGetter;
    }


    @Override
    public void build() {
        for (PieChart.Data data:chart.getData()){
            Node node = data.getNode();
            ImagePopupWindow popup = new ImagePopupWindow();
            popup.setPosition(position);
            popup.setMargin(margin);
            popup.setImage(imageGetter.apply(data.getName()));
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
