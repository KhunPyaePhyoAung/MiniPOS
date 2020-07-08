package com.alphasoft.pos.views.controllers;


import com.alphasoft.pos.commons.*;
import com.alphasoft.pos.contexts.SoldItemSorter;
import com.alphasoft.pos.models.SoldItem;
import com.alphasoft.pos.models.Summary;
import com.alphasoft.pos.services.*;
import com.alphasoft.pos.views.customs.TaxConfigWindow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PosHomeController implements Initializable {
    @FXML
    private ComboBox<TimePeriod> periodSelector;

    @FXML
    private ComboBox<SoldItemSorter.Mode> soldItemSortModeSelector;

    @FXML
    private PieChart bestSellerPieChart;

    @FXML
    private BarChart<String, Integer> bestSellerBarChart;

    @FXML
    private Label salesForTodayLabel;

    @FXML
    private Label unpaidLabel;

    @FXML
    private Label availableProductsLabel;

    @FXML
    private Label taxRateLabel;

    @FXML
    public void editTaxConfig(){
        Thread onEditTax = new Thread(()->{
            int taxRate = TaxRepository.getRepository().getTaxRate(LocalDate.now());
            taxRateLabel.setText(String.format("%d%%",taxRate));
        });
        TaxConfigWindow taxConfigWindow = new TaxConfigWindow(onEditTax);
        taxConfigWindow.initOwner(getStage());
        taxConfigWindow.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        soldItemSortModeSelector.getItems().addAll(SoldItemSorter.Mode.values());
        soldItemSortModeSelector.getSelectionModel().selectFirst();
        soldItemSortModeSelector.getSelectionModel().selectedItemProperty().addListener((l, o, n)->loadChartData());

        periodSelector.getItems().addAll(TimePeriod.values());
        periodSelector.getSelectionModel().select(TimePeriod.TODAY);
        periodSelector.getSelectionModel().selectedItemProperty().addListener((l,o,n)->loadChartData());
        loadSummary();
        loadChartData();
    }

    private void loadSummary(){
        Summary summary = SummaryService.getService().getSummary();
        salesForTodayLabel.setText(StringUtils.formatToMmk(summary.getSalesForToday()));
        unpaidLabel.setText(String.valueOf(summary.getUnpaid()));
        availableProductsLabel.setText(String.valueOf(summary.getAvailableProducts()));
        taxRateLabel.setText(String.format("%d%%",summary.getTaxRate()));
    }

    @SuppressWarnings("unchecked")
    private void loadChartData(){

        bestSellerPieChart.getData().clear();
        bestSellerBarChart.getData().clear();

        DateInterval dateInterval = new DateInterval(periodSelector.getValue());
        List<SoldItem> soldItemList = BestSellerService.getService().getItemList(dateInterval.getStartDate(),dateInterval.getEndDate(),soldItemSortModeSelector.getValue());

         List<PieChart.Data> pieChartDataList = new ArrayList<>();
         List<XYChart.Data<String,Integer>> barChartDataList = new ArrayList<>();

         soldItemList.stream().limit(BestSellerService.MAX_ITEM).forEach(i->{
             switch (soldItemSortModeSelector.getValue()){
                 case AMOUNT:
                     pieChartDataList.add(new PieChart.Data(i.getProductName(),i.getSoldAmount()));
                     barChartDataList.add(new XYChart.Data<>(i.getProductName(),i.getSoldAmount()));
                     break;
                 case QUANTITY:
                     pieChartDataList.add(new PieChart.Data(i.getProductName(),i.getSoldQuantity()));
                     barChartDataList.add(new XYChart.Data<>(i.getProductName(),i.getSoldQuantity()));
                     break;
             }

         });
        bestSellerPieChart.getData().addAll(pieChartDataList);
        XYChart.Series<String,Integer> barChartSeries = new XYChart.Series<>();
        barChartSeries.setName(periodSelector.getValue().toString());
        barChartSeries.getData().addAll(barChartDataList);
        bestSellerBarChart.getData().addAll(barChartSeries);

        XYChartPopupImageBuilder<String,Integer> barChartImageSetter = new XYChartPopupImageBuilder<>();

        barChartImageSetter.build(bestSellerBarChart, ProductImageHelper.getInstance()::getImage);
        barChartImageSetter.setPosition(ChartPopupImageBuilder.Position.NORTH);
        barChartImageSetter.setMargin(10);
        barChartImageSetter.setPopupSize(100,100);


    }

    private Stage getStage(){
        return (Stage)bestSellerBarChart.getScene().getWindow();
    }
}
