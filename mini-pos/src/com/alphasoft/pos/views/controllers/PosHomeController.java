package com.alphasoft.pos.views.controllers;


import com.alphasoft.pos.commons.StringUtils;
import com.alphasoft.pos.models.Summary;
import com.alphasoft.pos.services.SaleService;
import com.alphasoft.pos.services.SummaryService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PosHomeController implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadSummary();
    }

    private void loadSummary(){
        Summary summary = SummaryService.getService().getSummary();
        salesForTodayLabel.setText(StringUtils.formatToMmk(summary.getSalesForToday()));
        unpaidLabel.setText(String.valueOf(summary.getUnpaid()));
        availableProductsLabel.setText(String.valueOf(summary.getAvailableProducts()));
        taxRateLabel.setText(String.format("%d%%",summary.getTaxRate()));
    }
}
