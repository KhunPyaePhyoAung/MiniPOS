package com.alphasoft.pos.workers;

import com.alphasoft.pos.views.customs.ImagePopupWindow;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image;

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
            setMouseListener(node,popup);
        }
    }
}
