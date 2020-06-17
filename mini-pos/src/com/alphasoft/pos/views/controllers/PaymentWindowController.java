package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.CashSuggester;
import com.alphasoft.pos.commons.DecimalFormatter;
import com.alphasoft.pos.commons.NumberInput;
import com.alphasoft.pos.commons.StringUtils;
import com.alphasoft.pos.models.Payment;
import com.alphasoft.pos.models.Sale;
import com.alphasoft.pos.views.customs.ConfirmBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

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

    @FXML
    private FlowPane cashSuggestionFlowPane;

    private Sale sale;
    private Consumer<Sale> onSave;
    private Payment payment;


    public void setSale(Sale sale){
        this.sale = sale;
    }

    public void setOnSave(Consumer<Sale> onSave){
        this.onSave = onSave;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        payment = new Payment();

        NumberInput.getNew().attach(discountCashInput,0);
        NumberInput.getNew().attach(discountPercentInput,0);
        NumberInput.getNew().attach(tenderedInput,0);

        discountCashInput.textProperty().addListener((l,o,n)-> {
            if(isValidDiscountAmount()){
                Platform.runLater(()->{
                    discountCashInput.setText(stringAbsOf(n));
                    setPositionCaret(discountCashInput);
                    payment.discountCashProperty().set(Integer.parseInt(discountCashInput.getText()));
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
                    payment.discountPercentProperty().set(Integer.parseInt(discountPercentInput.getText()));
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
            payment.tenderedProperty().set(Integer.parseInt(tenderedInput.getText()));
        }));
        dueInput.textProperty().addListener((l,o,n)->refreshCashSuggestion());
        changeInput.textProperty().addListener((l,o,n)->{
            int value = payment.changeProperty().get();
            String style;
            if(value>=0){
                style = "-fx-text-fill:black";
            }else{
                style = "-fx-text-fill:dark-red";
            }
            changeInput.setStyle(style);
        });

        Platform.runLater(()->{
            setPayment();

            totalAmountLabel.setText(StringUtils.formatToMmk(payment.totalProperty().get()));
            discountCashInput.setText(String.valueOf(payment.discountCashProperty().get()));
            discountPercentInput.setText(String.valueOf(payment.discountPercentProperty().get()));
            tenderedInput.setText(String.valueOf(payment.tenderedProperty().get()));

            totalDiscountInput.textProperty().bindBidirectional(payment.totalDiscountProperty(),new DecimalFormatter());
            dueInput.textProperty().bindBidirectional(payment.dueProperty(),new DecimalFormatter());
            changeInput.textProperty().bindBidirectional(payment.changeProperty(),new DecimalFormatter());

        });

    }

    @FXML
    public void cancel() {
        close();
    }

    @FXML
    public void save() {

        if(payment.dueProperty().get()>0 && payment.tenderedProperty().get()==0){
            ConfirmBox confirmBox = new ConfirmBox(MainWindowController.mainStage);
            confirmBox.setTitle("Confirm");
            confirmBox.setContentText("Have not entered tendered amount.\nProceed anyway?");
            confirmBox.setOnConfirmed(e-> onSave());
            confirmBox.showAndWait();
        }else{
            onSave();
        }
    }

    private void onSave(){
        sale.setPayment(payment);
        onSave.accept(sale);
        close();
    }

    private void close(){
        totalAmountLabel.getScene().getWindow().hide();
    }

    private void setPayment(){
        Payment originPayment = sale.getPayment();
        payment.subTotalProperty().set(originPayment.subTotalProperty().get());
        payment.taxRateProperty().set(originPayment.taxRateProperty().get());
        payment.discountCashProperty().set(originPayment.discountCashProperty().get());
        payment.discountPercentProperty().set(originPayment.discountPercentProperty().get());
        payment.tenderedProperty().set(originPayment.tenderedProperty().get());
    }

    private boolean isValidDiscountAmount(){
        int percentDiscountAmount = (payment.totalProperty().get()/100)*Integer.parseInt(discountPercentInput.getText());
        int cashDiscountAmount = Integer.parseInt(discountCashInput.getText());
        int totalDiscount = percentDiscountAmount+cashDiscountAmount;
        return totalDiscount<=payment.totalProperty().get() && percentDiscountAmount>=0 && cashDiscountAmount>=0;
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

    private void refreshCashSuggestion(){
        cashSuggestionFlowPane.getChildren().clear();

        Consumer<Integer> executor = i-> tenderedInput.setText(String.valueOf(i));

        Button exactButton = new Button("Exact");
        exactButton.getStyleClass().add("blue-button");
        exactButton.setOnAction(e->executor.accept(payment.dueProperty().get()));
        cashSuggestionFlowPane.getChildren().add(exactButton);

        CashSuggester cashSuggester = new CashSuggester(500,1000,5000,10000);
        int MAX_COUNT = 5;
        int count = 0;
        for(int sug:cashSuggester.get(payment.dueProperty().get())){
            Button button = new Button(new DecimalFormatter().toString(sug));
            button.setOnAction(e->executor.accept(sug));
            button.getStyleClass().add("green-button");
            cashSuggestionFlowPane.getChildren().add(button);
            count++;
            if(count>=MAX_COUNT) break;
        }
    }

}
