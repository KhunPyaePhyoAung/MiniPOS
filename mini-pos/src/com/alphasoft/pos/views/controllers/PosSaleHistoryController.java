package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.*;
import com.alphasoft.pos.workers.Logger;
import com.alphasoft.pos.models.Payment;
import com.alphasoft.pos.models.Sale;
import com.alphasoft.pos.models.SaleDetail;
import com.alphasoft.pos.models.SaleItem;
import com.alphasoft.pos.repos.SaleRepository;
import com.alphasoft.pos.services.SaleService;
import com.alphasoft.pos.utils.StringUtils;
import com.alphasoft.pos.views.customs.ConfirmBox;
import com.alphasoft.pos.views.customs.PaymentWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.alphasoft.pos.utils.MessageHelper.getMessage;

public class PosSaleHistoryController implements Initializable {

    @FXML
    private TextField saleIdInput;

    @FXML
    private TextField cashierInput;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private TableView<SaleDetail> historyTable;

    @FXML
    private TableColumn<SaleDetail,Integer> historyIdColumn;

    @FXML
    private TableColumn<SaleDetail,String> historyCashierColumn;

    @FXML
    private TableColumn<SaleDetail,LocalDate> historyDateColumn;

    @FXML
    private TableColumn<SaleDetail,LocalTime> historyTimeColumn;

    @FXML
    private TableColumn<SaleDetail,Integer> historyTaxRateColumn;

    @FXML
    private TableColumn<SaleDetail,Integer> historyQtyColumn;

    @FXML
    private TableColumn<SaleDetail,Integer> historySubTotalColumn;

    @FXML
    private TableColumn<SaleDetail,Integer> historyTotalColumn;

    @FXML
    private TableView<SaleItem> cartTable;

    @FXML
    private TableColumn<SaleItem,String> cartProductColumn;

    @FXML
    private TableColumn<SaleItem,Integer> cartPriceColumn;

    @FXML
    private TableColumn<SaleItem,Integer> cartQtyColumn;

    @FXML
    private TableColumn<SaleItem,Integer> cartTotalColumn;

    @FXML
    private Label taxRateLabel;

    @FXML
    private Label subTotalValueLabel;

    @FXML
    private Label taxValueLabel;

    @FXML
    private Label totalValueLabel;

    @FXML
    private Button deleteButton;

    @FXML
    private Button paymentButton;

    @FXML
    private Button saleButton;

    private NumberField saleIdNumberField;


    @FXML
    public void deleteSale() {
        ConfirmBox confirmBox = new ConfirmBox(getStage());
        confirmBox.setTitle(getMessage("confirmation"));
        confirmBox.setContentText(getMessage("alert.deleting.sale"));
        confirmBox.setOnConfirmed(e->{
            SaleService.getService().remove(getSelectedSale());
            loadSales();
            confirmBox.close();
        });
        confirmBox.showAndWait();
    }

    @FXML
    public void goToSale() throws IOException {
        ViewLoader viewLoader = MainWindowController.getViewLoader();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/alphasoft/pos/views/pos_sale.fxml"));
        Parent view = fxmlLoader.load();
        PosSaleController controller = fxmlLoader.getController();
        controller.setSale(getSelectedSale());
        viewLoader.loadView(view);
    }

    @FXML
    public void showPaymentView() {
        PaymentWindow paymentWindow = new PaymentWindow(getSelectedSale(),this::onSavePayment);
        paymentWindow.showAndWait();
    }

    private void onSavePayment(Sale sale){
        sale.getSaleDetail().setPaid(true);
        SaleService.getService().save(sale);
    }

    @FXML
    public void clearInputs(){
        saleIdInput.clear();
        cashierInput.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saleIdNumberField = new NumberField(saleIdInput);
        saleIdNumberField.setMinValue(0);

        saleIdInput.textProperty().addListener((l,o,n)->loadSales());
        cashierInput.textProperty().addListener((l,o,n)->loadSales());
        fromDatePicker.setValue(LocalDate.now().withDayOfMonth(1));
        toDatePicker.setValue(LocalDate.now());
        fromDatePicker.valueProperty().addListener((l,o,n)->{
            handleDatePicker();
            loadSales();
        });
        toDatePicker.valueProperty().addListener((l,o,n)->{
            handleDatePicker();
            loadSales();
        });
        setupHistoryTable();
        setupCartTable();
        loadSales();
        toggleActionButtons();
    }

    private void loadSales(){
        List<SaleDetail> saleDetailList = SaleRepository.getRepository().getAllSales().stream().filter(i->i.getSaleDetail().isPaid()).map(Sale::getSaleDetail).collect(Collectors.toList());
        filterSales(saleDetailList);
        historyTable.getItems().clear();
        historyTable.getItems().addAll(saleDetailList);

    }

    private void filterSales(List<SaleDetail> saleDetailList){
        if(!saleIdNumberField.isEmpty()){
            saleDetailList.retainAll(saleDetailList.stream().filter(i->String.valueOf(i.getId()).contains(saleIdInput.getText().trim())).collect(Collectors.toList()));
        }
        if(!StringUtils.isEmpty(cashierInput.getText().trim())){
            saleDetailList.retainAll(saleDetailList.stream().filter(i->i.getSalePersonName().toLowerCase().contains(cashierInput.getText().trim().toLowerCase())).collect(Collectors.toList()));
        }
        if(null!=fromDatePicker.getValue()){
            saleDetailList.retainAll(saleDetailList.stream().filter(i->i.getSaleDate().compareTo(fromDatePicker.getValue())>=0).collect(Collectors.toList()));
        }
        if(null!=toDatePicker.getValue()){
            saleDetailList.retainAll(saleDetailList.stream().filter(i->i.getSaleDate().compareTo(toDatePicker.getValue()) <=0).collect(Collectors.toList()));
        }
    }

    private void handleDatePicker(){
        if(null!=fromDatePicker.getValue() && null!=toDatePicker.getValue()){
            if(fromDatePicker.getValue().compareTo(toDatePicker.getValue()) >0){
                toDatePicker.setValue(fromDatePicker.getValue().plusDays(1));
            }
        }
    }

    private void setupHistoryTable(){
        historyIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        historyCashierColumn.setCellValueFactory(new PropertyValueFactory<>("salePersonName"));
        historyDateColumn.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        historyTimeColumn.setCellValueFactory(new PropertyValueFactory<>("saleTime"));
        historyTaxRateColumn.setCellValueFactory(new PropertyValueFactory<>("taxRate"));
        historyQtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        historySubTotalColumn.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        historyTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        historyIdColumn.setCellFactory(new DecimalFormattedCellFactory<>());
        historyDateColumn.setCellFactory(new DateFormattedCellFactory<>());
        historyTimeColumn.setCellFactory(new TimeFormattedCellFactory<>());
        historyTaxRateColumn.setCellFactory(new PercentFormattedCellFactory<>());
        historySubTotalColumn.setCellFactory(new DecimalFormattedCellFactory<>());
        historyTotalColumn.setCellFactory(new DecimalFormattedCellFactory<>());

        historyTable.getSelectionModel().selectedItemProperty().addListener((l,o,n)->{
            if(null!=n){
                Sale selectedSale = SaleRepository.getRepository().get(n.getId());
                List<SaleItem> saleItemList = selectedSale.getSaleItemList();
                cartTable.getItems().clear();
                cartTable.getItems().addAll(saleItemList);
                setupBill(selectedSale.getPayment());

            }else{
                cartTable.getItems().clear();
            }
            toggleActionButtons();
        });
    }

    private void setupBill(Payment payment){
        subTotalValueLabel.setText(StringUtils.formatToMmk(payment.subTotalProperty().get()));
        taxRateLabel.setText(String.format("Tax %d %%",payment.taxRateProperty().get()));
        taxValueLabel.setText(StringUtils.formatToMmk(payment.taxProperty().get()));
        totalValueLabel.setText(StringUtils.formatToMmk(payment.totalProperty().get()));
    }

    private void toggleActionButtons(){
        if(null!=getSelectedSale()){
            if(getSelectedSale().getSaleDetail().getSalePersonId()== Logger.getLogger().getLoggedAccount().getId()){
                enableActionButtons();
            }else {
                disableActionButtons();
            }
        }else {
            disableActionButtons();
        }
    }

    private void enableActionButtons(){
        deleteButton.setDisable(false);
        paymentButton.setDisable(false);
        saleButton.setDisable(false);
    }

    private void disableActionButtons(){
        deleteButton.setDisable(true);
        paymentButton.setDisable(true);
        saleButton.setDisable(true);
    }

    private void setupCartTable(){
        cartProductColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        cartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        cartQtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        cartTotalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        cartPriceColumn.setCellFactory(new DecimalFormattedCellFactory<>());
        cartQtyColumn.setCellFactory(new DecimalFormattedCellFactory<>());
        cartTotalColumn.setCellFactory(new DecimalFormattedCellFactory<>());
    }

    private Sale getSelectedSale(){
        if(null!=historyTable.getSelectionModel().getSelectedItem())
            return SaleRepository.getRepository().get(historyTable.getSelectionModel().getSelectedItem().getId());
        else
            return null;
    }

    private Stage getStage(){
        return (Stage)historyTable.getScene().getWindow();
    }
}
