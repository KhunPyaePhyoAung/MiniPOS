package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.CurrencyFormatter;
import com.alphasoft.pos.commons.NumberInput;
import com.alphasoft.pos.commons.StringUtils;
import com.alphasoft.pos.models.Sale;
import com.alphasoft.pos.views.customs.ConfirmBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;


public class PaymentWindowController implements Initializable {

    @FXML
    private Label totalAmountLabel;

    @FXML
    private TextField discountCashInput;

    @FXML
    private TextField discountPercentInput;

    @FXML
    private TextField dueInput;

    @FXML
    private TextField tenderedInput;

    @FXML
    private TextField changeInput;

    @FXML
    private TextField totalDiscountInput;

    private Sale sale;
    private Consumer<Sale> onSave;


    public void setSale(Sale sale){
        this.sale = sale;
        totalAmountLabel.setText(StringUtils.formatToMmk(sale.getPayment().totalProperty().get()));

        totalDiscountInput.textProperty().bindBidirectional(sale.getPayment().totalDiscountProperty(),new CurrencyFormatter());
        dueInput.textProperty().bindBidirectional(sale.getPayment().dueProperty(),new CurrencyFormatter());
        changeInput.textProperty().bindBidirectional(sale.getPayment().changeProperty(),new CurrencyFormatter());
    }

    public void setOnSave(Consumer<Sale> onSave){
        this.onSave = onSave;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        NumberInput.attach(discountCashInput);
        NumberInput.attach(discountPercentInput);
        NumberInput.attach(tenderedInput);

        discountCashInput.textProperty().addListener((l,o,n)-> {
            if(isValidDiscountAmount()){
                Platform.runLater(()->{
                    discountCashInput.setText(stringAbsOf(n));
                    setPositionCaret(discountCashInput);
                    sale.getPayment().discountCashProperty().set(Integer.parseInt(discountCashInput.getText()));
                });
            }else {
                Platform.runLater(()->{
                    discountCashInput.setText(stringAbsOf(o));
                    setPositionCaret(discountCashInput);
                });
            }

        });
        discountPercentInput.textProperty().addListener((l,o,n)-> {
            if(isValidDiscountAmount()){
                Platform.runLater(()->{
                    discountPercentInput.setText(stringAbsOf(n));
                    setPositionCaret(discountPercentInput);
                    sale.getPayment().discountPercentProperty().set(Integer.parseInt(discountPercentInput.getText()));
                });

            }else {
                Platform.runLater(()->{
                    discountPercentInput.setText(stringAbsOf(o));
                    setPositionCaret(discountPercentInput);
                });
            }

        });

        tenderedInput.textProperty().addListener((l,o,n)-> Platform.runLater(()->{
            tenderedInput.setText(String.valueOf(Math.abs(Integer.parseInt(n))));
            setPositionCaret(tenderedInput);
            sale.getPayment().tenderedProperty().set(Integer.parseInt(tenderedInput.getText()));
        }));

    }

    @FXML
    public void cancel() {
        sale.getPayment().discountCashProperty().set(0);
        sale.getPayment().discountPercentProperty().set(0);
        sale.getPayment().tenderedProperty().set(0);
        close();
    }

    @FXML
    public void save() {

        if(sale.getPayment().dueProperty().get()>0 && sale.getPayment().tenderedProperty().get()==0){
            ConfirmBox confirmBox = new ConfirmBox(MainWindowController.mainStage);
            confirmBox.setTitle("Confirm");
            confirmBox.setContentText("Have not entered tendered amount.\nProceed anyway?");
            confirmBox.setOnConfirmed(e->{
                onSave.accept(sale);
                confirmBox.close();
                close();
            });
            confirmBox.showAndWait();
        }else{
            onSave.accept(sale);
            close();
        }

    }

    private void close(){
        totalAmountLabel.getScene().getWindow().hide();
    }

    private boolean isValidDiscountAmount(){
        int percentDiscountAmount = (sale.getPayment().totalProperty().get()/100)*Integer.parseInt(discountPercentInput.getText());
        int cashDiscountAmount = Integer.parseInt(discountCashInput.getText());
        int totalDiscount = percentDiscountAmount+cashDiscountAmount;
        return totalDiscount<=sale.getPayment().totalProperty().get() && percentDiscountAmount>=0 && cashDiscountAmount>=0;
    }

    private String stringAbsOf(String numString){
        try {
            return String.valueOf(Math.abs(Integer.parseInt(numString)));
        }catch (Exception e){
            return null;
        }

    }

    private void setPositionCaret(TextField textField){
        textField.positionCaret(textField.getText().length());
    }


}
