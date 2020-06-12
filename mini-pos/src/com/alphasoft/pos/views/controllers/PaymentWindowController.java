package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.CashSuggester;
import com.alphasoft.pos.commons.CurrencyFormatter;
import com.alphasoft.pos.commons.NumberInput;
import com.alphasoft.pos.commons.StringUtils;
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


    public void setSale(Sale sale){
        this.sale = sale;

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
        dueInput.textProperty().addListener((l,o,n)->refreshCashSuggestion());
        changeInput.textProperty().addListener((l,o,n)->{
            int value = sale.getPayment().changeProperty().get();
            String style;
            if(value>=0){
                style = "-fx-text-fill:black";
            }else{
                style = "-fx-text-fill:dark-red";
            }
            changeInput.setStyle(style);
        });

        Platform.runLater(()->{
            totalAmountLabel.setText(StringUtils.formatToMmk(sale.getPayment().totalProperty().get()));

            totalDiscountInput.textProperty().bindBidirectional(sale.getPayment().totalDiscountProperty(),new CurrencyFormatter());
            dueInput.textProperty().bindBidirectional(sale.getPayment().dueProperty(),new CurrencyFormatter());
            changeInput.textProperty().bindBidirectional(sale.getPayment().changeProperty(),new CurrencyFormatter());

        });

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

    private void refreshCashSuggestion(){
        cashSuggestionFlowPane.getChildren().clear();

        Consumer<Integer> executor = i-> tenderedInput.setText(String.valueOf(i));

        Button exactButton = new Button("Exact");
        exactButton.getStyleClass().add("blue-button");
        exactButton.setOnAction(e->executor.accept(sale.getPayment().dueProperty().get()));
        cashSuggestionFlowPane.getChildren().add(exactButton);

        CashSuggester cashSuggester = new CashSuggester(500,1000,5000,10000);
        int MAX_COUNT = 5;
        int count = 0;
        for(int sug:cashSuggester.get(sale.getPayment().dueProperty().get())){
            Button button = new Button(new CurrencyFormatter().toString(sug));
            button.setOnAction(e->executor.accept(sug));
            button.getStyleClass().add("green-button");
            cashSuggestionFlowPane.getChildren().add(button);
            count++;
            if(count>=MAX_COUNT) break;
        }
    }

}
