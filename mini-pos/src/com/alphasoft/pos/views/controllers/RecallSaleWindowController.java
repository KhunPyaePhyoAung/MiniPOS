package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.DateFormattedCellFactory;
import com.alphasoft.pos.commons.DecimalFormattedCellFactory;
import com.alphasoft.pos.commons.TimeFormattedCellFactory;
import com.alphasoft.pos.models.Sale;
import com.alphasoft.pos.models.SaleDetail;
import com.alphasoft.pos.services.SaleRepository;
import com.alphasoft.pos.services.SaleService;
import com.alphasoft.pos.views.customs.AlertBox;
import com.alphasoft.pos.views.customs.ConfirmBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.alphasoft.pos.commons.MessageRepo.getMessage;

public class RecallSaleWindowController implements Initializable {

    @FXML
    private TableView<SaleDetail> tableView;


    @FXML
    private TableColumn<SaleDetail, Integer> idColumn;

    @FXML
    private TableColumn<SaleDetail, LocalDate> dateColumn;

    @FXML
    private TableColumn<SaleDetail, LocalTime> timeColumn;

    @FXML
    private TableColumn<SaleDetail, Integer> qtyColumn;

    @FXML
    private TableColumn<SaleDetail, Integer> subTotalColumn;

    @FXML
    private TableColumn<SaleDetail, String> cashierColumn;

    private Consumer<Sale> onRecall;

    public void setOnRecall(Consumer<Sale> onRecall){
        this.onRecall = onRecall;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupTable();
        loadUnpaidSales();
    }

    @FXML
    public void cancel(){
        close();
    }

    @FXML
    public void recall() {
        SaleDetail selectedItem = tableView.getSelectionModel().getSelectedItem();
        if(null!=selectedItem){
            Sale selectedSale = SaleRepository.getRepository().get(selectedItem.getId());
            onRecall.accept(selectedSale);
            close();
        }else{
            showAlert(getMessage("action.cannot.completed"),getMessage("select.item"));
        }
    }

    @FXML
    public void remove() {
        SaleDetail selectedItem = tableView.getSelectionModel().getSelectedItem();
        if(null!=selectedItem){
            ConfirmBox confirmBox = new ConfirmBox(getStage());
            confirmBox.setTitle(getMessage("confirmation"));
            confirmBox.setContentText(getMessage("alert.removing.item"));
            confirmBox.setOnConfirmed(e->{
                Sale selectedSale = SaleRepository.getRepository().get(selectedItem.getId());
                SaleService.getService().remove(selectedSale);
                loadUnpaidSales();
                confirmBox.close();
            });
            confirmBox.showAndWait();


        }else{
            showAlert(getMessage("action.cannot.completed"),getMessage("select.item"));
        }
    }

    private void loadUnpaidSales(){
        List<Sale> saleList = SaleRepository.getRepository().getAllSales();
        saleList.retainAll(saleList.stream().filter(i->!i.getSaleDetail().isPaid()).collect(Collectors.toList()));
        tableView.getItems().clear();
        tableView.getItems().addAll(saleList.stream().map(Sale::getSaleDetail).collect(Collectors.toList()));
    }

    private void setupTable(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("saleTime"));
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        subTotalColumn.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        cashierColumn.setCellValueFactory(new PropertyValueFactory<>("salePersonName"));

        idColumn.setCellFactory(new DecimalFormattedCellFactory<>());
        dateColumn.setCellFactory(new DateFormattedCellFactory<>());
        timeColumn.setCellFactory(new TimeFormattedCellFactory<>());
        qtyColumn.setCellFactory(new DecimalFormattedCellFactory<>());
        subTotalColumn.setCellFactory(new DecimalFormattedCellFactory<>());
    }

    private void close(){tableView.getScene().getWindow().hide();}

    private void showAlert(String title,String message){
        AlertBox alertBox = new AlertBox(getStage());
        alertBox.setTitle(title);
        alertBox.setContentText(message);
        alertBox.showAndWait();
    }

    private Stage getStage(){
        return (Stage)tableView.getScene().getWindow();
    }



}
