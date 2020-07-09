package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.*;
import com.alphasoft.pos.models.ProductCategory;
import com.alphasoft.pos.models.Sale;
import com.alphasoft.pos.models.SaleItem;
import com.alphasoft.pos.repos.ProductCategoryRepository;
import com.alphasoft.pos.repos.SaleRepository;
import com.alphasoft.pos.utils.StringUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PosReportController implements Initializable {
    @FXML
    private TextField categoryInput;

    @FXML
    private TextField productInput;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private TableView<SaleItem> reportTable;

    @FXML
    private TableColumn<SaleItem, String> categoryColumn;

    @FXML
    private TableColumn<SaleItem, String> productColumn;

    @FXML
    private TableColumn<SaleItem, LocalDate> dateColumn;

    @FXML
    private TableColumn<SaleItem, Integer> priceColumn;

    @FXML
    private TableColumn<SaleItem, Integer> quantityColumn;

    @FXML
    private TableColumn<SaleItem, Integer> taxRateColumn;

    @FXML
    private TableColumn<SaleItem, Integer> subTotalColumn;

    @FXML
    private TableColumn<SaleItem, Integer> totalColumn;

    @FXML
    private Label totalLabel;

    @FXML
    public void clearInput() {
        categoryInput.clear();
        productInput.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AutoCompleteTextField.attach(categoryInput, ProductCategoryRepository.getRepository()::getAllProductCategoriesLike,this::loadReports);
        categoryInput.textProperty().addListener((l,o,n)-> {
            if(n.isEmpty())
                loadReports();
        });
        productInput.textProperty().addListener((l,o,n)->loadReports());
        fromDatePicker.valueProperty().addListener((l,o,n)->handleDatePickers());
        toDatePicker.valueProperty().addListener((l,o,n)->handleDatePickers());
        setupReportTable();
        loadReports();
    }

    private void loadReports(ProductCategory productCategory){
        categoryInput.setText(productCategory.getName());
        categoryInput.positionCaret(categoryInput.getText().length());
        loadReports();
    }

    private void loadReports(){
        List<Sale> saleList = SaleRepository.getRepository().getAllSales().stream().filter(i->i.getSaleDetail().isPaid()).sorted(Comparator.comparing(i -> i.getSaleDetail().getSaleDate())).collect(Collectors.toList());

        List<SaleItem> saleItemList = new ArrayList<>();
        saleList.forEach(i->saleItemList.addAll(i.getSaleItemList()));

        filterReportItems(saleItemList);
        reportTable.getItems().clear();
        reportTable.getItems().addAll(saleItemList);
        calculateTotal();
    }

    private void filterReportItems(List<SaleItem> saleItemList){


        if(!StringUtils.isEmpty(categoryInput.getText().trim())){
            saleItemList.retainAll(saleItemList.stream().filter(i->
                                                                    i.getCategoryName().equalsIgnoreCase(categoryInput.getText().trim())
                                                                ).collect(Collectors.toList())
                                    );
        }

        if(!StringUtils.isEmpty(productInput.getText().trim())){
            saleItemList.retainAll(saleItemList.stream().filter(i->
                                                                    i.getProductName().toLowerCase().startsWith(productInput.getText().trim().toLowerCase())
                                                                ).collect(Collectors.toList())
                                    );
        }

        if(null != toDatePicker.getValue()){
            saleItemList.retainAll(saleItemList.stream().filter(i->i.getSaleDate().compareTo(toDatePicker.getValue())<=0).collect(Collectors.toList()));
        }

        if(null != fromDatePicker.getValue()){
            saleItemList.retainAll(saleItemList.stream().filter(i->i.getSaleDate().compareTo(fromDatePicker.getValue())>=0).collect(Collectors.toList()));
        }


    }

    private void handleDatePickers(){
        if(null!=fromDatePicker.getValue() && null!=toDatePicker.getValue()){
            if(toDatePicker.getValue().compareTo(fromDatePicker.getValue())<0){
                toDatePicker.setValue(fromDatePicker.getValue().plusDays(1));
            }
        }
        loadReports();

    }

    private void calculateTotal(){
        int total = reportTable.getItems().stream().mapToInt(SaleItem::getTotal).sum();
        totalLabel.setText(StringUtils.formatToMmk(total));
    }
    private void setupReportTable(){
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        productColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        taxRateColumn.setCellValueFactory(new PropertyValueFactory<>("taxRate"));
        subTotalColumn.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        dateColumn.setCellFactory(new DateFormattedCellFactory<>());
        priceColumn.setCellFactory(new DecimalFormattedCellFactory<>());
        quantityColumn.setCellFactory(new DecimalFormattedCellFactory<>());
        taxRateColumn.setCellFactory(new PercentFormattedCellFactory<>());
        subTotalColumn.setCellFactory(new DecimalFormattedCellFactory<>());
        totalColumn.setCellFactory(new DecimalFormattedCellFactory<>());

    }
}
