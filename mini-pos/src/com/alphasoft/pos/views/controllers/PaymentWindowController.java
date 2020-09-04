package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.CashSuggester;
import com.alphasoft.pos.commons.DecimalFormatter;
import com.alphasoft.pos.commons.NumberField;
import com.alphasoft.pos.utils.StringUtils;
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
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.alphasoft.pos.utils.MessageHelper.getMessage;


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
    private NumberField discountCashNumberField;
    private NumberField discountPercentNumberField;
    private NumberField tenderedNumberField;


    public void setSale(Sale sale){
        this.sale = sale;
    }

    public void setOnSave(Consumer<Sale> onSave){
        this.onSave = onSave;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        payment = new Payment();

        discountCashNumberField = new NumberField(discountCashInput);
        discountCashNumberField.setDefaultValue(0);
        discountCashNumberField.setMinValue(0);

        discountPercentNumberField = new NumberField(discountPercentInput);
        discountPercentNumberField.setDefaultValue(0);
        discountPercentNumberField.setMinMaxValues(0,100);

        tenderedNumberField = new NumberField(tenderedInput);
        tenderedNumberField.setDefaultValue(0);
        tenderedNumberField.setMinValue(0);

        discountCashInput.textProperty().addListener((l,o,n)-> {
            if(isValidDiscountAmount()){
                Platform.runLater(()->{
                    discountCashNumberField.setValue(Integer.parseInt(n));
                    payment.discountCashProperty().set(discountCashNumberField.getValue());
                    setPositionCaret(discountCashInput);
                });
            }else {
                Platform.runLater(()-> discountCashNumberField.setValue(Integer.parseInt(o)));
            }
        });

        discountPercentInput.textProperty().addListener((l,o,n)-> {
            if(isValidDiscountAmount()){
                Platform.runLater(()->{
                    discountPercentNumberField.setValue(Integer.parseInt(n));
                    payment.discountPercentProperty().set(discountPercentNumberField.getValue());
                    setPositionCaret(discountPercentInput);
                });
            }else {
                Platform.runLater(()-> discountPercentNumberField.setValue(Integer.parseInt(o)));
            }
        });

        tenderedInput.textProperty().addListener((l,o,n)->{
            payment.tenderedProperty().set(tenderedNumberField.getValue());
            setPositionCaret(tenderedInput);
        });

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
            discountCashNumberField.setValue(payment.discountCashProperty().get());
            discountPercentNumberField.setValue(payment.discountPercentProperty().get());
            tenderedNumberField.setValue(payment.tenderedProperty().get());

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

        if(!isPaid()){
            ConfirmBox confirmBox = new ConfirmBox(getStage());
            confirmBox.setTitle(getMessage("confirmation"));
            confirmBox.setContentText(getMessage("tenderedAmount.haven't.entered").concat("\n").concat(getMessage("alert.proceeding")));
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


    private void setPositionCaret(TextField textField){
        textField.positionCaret(textField.getText().length());
    }

    private void refreshCashSuggestion(){
        cashSuggestionFlowPane.getChildren().clear();

        Consumer<Integer> executor = i-> tenderedNumberField.setValue(i);

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

    private boolean isPaid(){
        System.out.println(payment.dueProperty().get()+"\t"+payment.tenderedProperty().get());
        return payment.dueProperty().get()>0 && payment.tenderedProperty().get()>0;
    }

    private Stage getStage(){
        return (Stage)tenderedInput.getScene().getWindow();
    }


}
