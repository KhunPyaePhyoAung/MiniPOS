package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.DateFormattedCellFactory;
import com.alphasoft.pos.commons.NumberInput;
import com.alphasoft.pos.commons.PercentFormattedCellFactory;
import com.alphasoft.pos.commons.Validations;
import com.alphasoft.pos.contexts.PosException;
import com.alphasoft.pos.models.TaxInfo;
import com.alphasoft.pos.repos.TaxRepository;
import com.alphasoft.pos.services.TaxService;
import com.alphasoft.pos.views.customs.AlertBox;
import com.alphasoft.pos.views.customs.ConfirmBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.alphasoft.pos.utils.MessageHelper.getMessage;

public class TaxConfigController implements Initializable {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private TextField taxRateInput;

    @FXML
    private TableView<TaxInfo> taxRepoTable;

    @FXML
    private TableColumn<TaxInfo, LocalDate> startDateColumn;

    @FXML
    private TableColumn<TaxInfo, Integer> taxRateColumn;

    private Runnable taxListener;

    public void setTaxListener(Runnable taxListener){
        this.taxListener = taxListener;
    }

    @FXML
    public void addTax() {
        try{
            Validations.notNull(startDatePicker.getValue(),getMessage("select.date"));
            if(startDatePicker.getValue().isBefore(LocalDate.now())){
                throw new PosException(getMessage("date.invalid"));
            }

            TaxService.getService().saveTaxInfo(startDatePicker.getValue(),Integer.parseInt(taxRateInput.getText()));
            loadTaxRepo();
            taxListener.run();

            startDatePicker.setValue(null);
            taxRateInput.clear();
        }catch (PosException exception){
            showAlert(getMessage("action.cannot.completed"),exception.getMessage());
        }
    }

    @FXML
    public void close() {getStage().close();}

    @FXML
    public void removeTax() {
        TaxInfo selectItem = taxRepoTable.getSelectionModel().getSelectedItem();
        try {
            Validations.notNull(selectItem,getMessage("select.item"));
            if(selectItem.getStartDate().isBefore(LocalDate.now())) throw new PosException(getMessage("item.couldn't.delete"));

            ConfirmBox confirmBox = new ConfirmBox(getStage());
            confirmBox.setTitle(getMessage("confirmation"));
            confirmBox.setContentText(getMessage("alert.deleting.tax"));
            confirmBox.setOnConfirmed(e->{
                TaxService.getService().removeTaxInfo(selectItem.getId());
                confirmBox.close();
            });
            confirmBox.showAndWait();

            loadTaxRepo();
            taxListener.run();
        }catch (PosException exception){
            showAlert(getMessage("action.cannot.completed"),exception.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumberInput.getNew().attach(taxRateInput,0);
        taxRateInput.textProperty().addListener((l,o,n)->{
            final int MAX_TAX_RATE = 100;
            int value = Math.abs(Integer.parseInt(n));
            if(value>MAX_TAX_RATE) value = Math.abs(Integer.parseInt(o));
            int finalValue = value;
            Platform.runLater(()->{
                taxRateInput.setText(String.valueOf(finalValue));
                taxRateInput.positionCaret(taxRateInput.getText().length());
            });
        });
        setupTaxRepoTable();
        loadTaxRepo();
    }

    private void loadTaxRepo(){
        List<TaxInfo> taxInfoList = TaxRepository.getRepository().getAllTaxInfos().stream().sorted((i1,i2)->-i1.getStartDate().compareTo(i2.getStartDate())).collect(Collectors.toList());

        taxRepoTable.getItems().clear();
        taxRepoTable.getItems().addAll(taxInfoList);
    }

    private void setupTaxRepoTable(){
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        taxRateColumn.setCellValueFactory(new PropertyValueFactory<>("taxRate"));

        startDateColumn.setCellFactory(new DateFormattedCellFactory<>());
        taxRateColumn.setCellFactory(new PercentFormattedCellFactory<>());
    }

    private Stage getStage(){
        return (Stage)taxRepoTable.getScene().getWindow();
    }

    private void showAlert(String title,String contentText){
        AlertBox alertBox = new AlertBox(getStage());
        alertBox.setTitle(title);
        alertBox.setContentText(contentText);
        alertBox.showAndWait();
    }

}
