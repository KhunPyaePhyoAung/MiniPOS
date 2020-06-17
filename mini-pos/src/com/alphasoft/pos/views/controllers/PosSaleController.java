package com.alphasoft.pos.views.controllers;

import com.alphasoft.pos.commons.AutoCompleteTextField;
import com.alphasoft.pos.commons.DecimalFormattedCellFactory;
import com.alphasoft.pos.commons.MmkFormatter;
import com.alphasoft.pos.contexts.Logger;
import com.alphasoft.pos.contexts.ProductSorter;
import com.alphasoft.pos.factories.ProductSorterFactory;
import com.alphasoft.pos.models.*;
import com.alphasoft.pos.services.ProductCategoryRepository;
import com.alphasoft.pos.services.ProductRepository;
import com.alphasoft.pos.services.SaleService;
import com.alphasoft.pos.services.TaxRepository;
import com.alphasoft.pos.views.customs.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class PosSaleController implements Initializable {
    @FXML
    private TableView<SaleItem> cart;

    @FXML
    private TableColumn<SaleItem, String> nameColumn;

    @FXML
    private TableColumn<SaleItem, Integer> priceColumn;

    @FXML
    private TableColumn<SaleItem, Integer> qtyColumn;

    @FXML
    private TableColumn<SaleItem, Integer> totalColumn;

    @FXML
    private Label subTotalLabel;

    @FXML
    private Label taxRateLabel;

    @FXML
    private Label taxLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private TextField categoryNameInput;

    @FXML
    private TextField productNameInput;

    @FXML
    private ComboBox<ProductSorter.Mode> sortModeSelector;

    @FXML
    private FlowPane flowPane;

    private Sale sale;

    private Payment payment;

    public void setSale(Sale sale){
        this.sale = sale;
        cart.getItems().clear();
        cart.getItems().addAll(sale.getSaleItemList());
        calculateCartItem();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sortModeSelector.getItems().addAll(ProductSorter.Mode.values());
        sortModeSelector.getSelectionModel().selectFirst();
        sortModeSelector.getSelectionModel().selectedItemProperty().addListener((l,o,n)->loadProducts());

        payment = new Payment();

        int taxRateValue = TaxRepository.getRepository().getTaxRate(LocalDate.now());
        payment.taxRateProperty().set(taxRateValue);
        subTotalLabel.textProperty().bindBidirectional(payment.subTotalProperty(),new MmkFormatter());
        taxRateLabel.setText(String.format("Tax : %d %%",taxRateValue));
        taxLabel.textProperty().bindBidirectional(payment.taxProperty(),new MmkFormatter());
        totalLabel.textProperty().bindBidirectional(payment.totalProperty(),new MmkFormatter());

        AutoCompleteTextField.attach(categoryNameInput, ProductCategoryRepository.getRepository()::getAllProductCategoriesLike,this::loadProductOf);
        categoryNameInput.textProperty().addListener((l,o,n)->{
            if(n.isEmpty()) loadProducts();
        });
        productNameInput.textProperty().addListener((l,o,n)->loadProducts());
        setupTable();
        loadProducts();
    }

    @FXML
    public void createNewSale() {

        if(!cart.getItems().isEmpty()){
            ConfirmBox confirmBox = new ConfirmBox(MainWindowController.mainStage);
            confirmBox.setTitle("Confirm");
            confirmBox.setContentText("Are you sure to create new sale?");
            confirmBox.setOnConfirmed(e->{
                cart.getItems().clear();
                sale = null;
                calculateCartItem();
                confirmBox.close();
            });
            confirmBox.showAndWait();
        }

    }

    @FXML
    public void clearInput() {
        categoryNameInput.clear();
        productNameInput.clear();
    }

    @FXML
    public void hold() {
        if(cart.getItems().isEmpty()){
            showAlertBox("Action cannot be completed","The cart is empty");
            return;
        }
        ConfirmBox confirmBox = new ConfirmBox(MainWindowController.mainStage);
        confirmBox.setTitle("Confirm");
        confirmBox.setContentText("Are you sure to hold this cart?");
        confirmBox.setOnConfirmed(e->{
            Sale saleToSave = getSale();
            saleToSave.getSaleDetail().setPaid(false);
            SaleService.getService().save(saleToSave);
            prepareForNextSale();
            confirmBox.close();
        });
        confirmBox.showAndWait();


    }

    @FXML
    public void pay() {
        if(cart.getItems().isEmpty()){
            showAlertBox("Action cannot be completed","The cart is empty");
            return;
        }
        PaymentWindow paymentWindow = new PaymentWindow(getSale(),this::onSave);
        paymentWindow.showAndWait();
    }

    @FXML
    public void recall() {
        RecallSaleWindow recallSaleWindow = new RecallSaleWindow(this::onRecall);
        AtomicBoolean proceed = new AtomicBoolean(true);
        if(!cart.getItems().isEmpty()){
            ConfirmBox confirmBox = new ConfirmBox(getStage());
            confirmBox.setTitle("Warning");
            confirmBox.setContentText("Sale is in progress.\nProceed anyway?");
            confirmBox.setOnConfirmed(e->{
                proceed.set(true);
                confirmBox.close();
            });
            confirmBox.setOnCanceled(e-> {
                proceed.set(false);
                confirmBox.close();
            });
            confirmBox.showAndWait();
        }

        if(proceed.get()){
            prepareForNextSale();
            recallSaleWindow.showAndWait();
        }
    }

    private void onRecall(Sale sale){
        this.sale = sale;
        cart.getItems().clear();
        cart.getItems().addAll(sale.getSaleItemList());
        resetPayment();
        calculateCartItem();
    }


    private void loadProductOf(ProductCategory productCategory){
        categoryNameInput.setText(productCategory.getName());
        categoryNameInput.positionCaret(categoryNameInput.getText().length());
        loadProducts();
    }

    private void loadProducts(){
        flowPane.getChildren().clear();
        List<Product> productList = ProductRepository.getRepository().getAllProducts().stream().filter(Product::isAvailable).collect(Collectors.toList());
        filterCategories(productList);
        filterProducts(productList);
        ProductSorterFactory.getFactory().getSorter(sortModeSelector.getSelectionModel().getSelectedItem()).sort(productList);

        productList.stream().map(i->new ProductCard(i,this::addToCart)).forEach(i->flowPane.getChildren().add(i));


    }

    private void filterCategories(List<Product> productList){
        if(!categoryNameInput.getText().trim().isEmpty()){
            productList.retainAll(
                    productList.stream().filter(i->i.getCategoryName().equalsIgnoreCase(categoryNameInput.getText().trim()))
                                        .collect(Collectors.toList())
            );
        }
    }

    private void filterProducts(List<Product> productList){
        productList.retainAll(
                productList.stream().filter(i->i.getName().toLowerCase().contains(productNameInput.getText().trim().toLowerCase()))
                                    .collect(Collectors.toList())
        );
    }

    private void addToCart(Product product){
        SaleItem saleItem = cart.getItems().stream().filter(i->i.getProductId()==product.getId()).findAny().orElse(null);
        if(null == saleItem){
            saleItem = new SaleItem();
            saleItem.setProduct(product);
            cart.getItems().add(saleItem);
        }
        saleItem.setQuantity(saleItem.getQuantity()+1);
        calculateCartItem();
    }

    private void prepareForNextSale(){
        sale = null;
        resetPayment();
        cart.getItems().clear();
        calculateCartItem();
    }

    private void calculateCartItem(){
        int subTotalValue = cart.getItems().stream().mapToInt(i->i.getPrice()*i.getQuantity()).sum();
        payment.subTotalProperty().set(subTotalValue);
        List<SaleItem> saleItemList = new ArrayList<>(cart.getItems());
        cart.getItems().clear();
        cart.getItems().addAll(saleItemList);
    }

    private void setupTable(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        priceColumn.setCellFactory(new DecimalFormattedCellFactory<>());
        qtyColumn.setCellFactory(new DecimalFormattedCellFactory<>());
        totalColumn.setCellFactory(new DecimalFormattedCellFactory<>());

        cart.setOnKeyReleased(e->{
            SaleItem selectedItem = cart.getSelectionModel().getSelectedItem();
            if(null!=selectedItem){
                switch (e.getCode()){
                    case ADD:
                        selectedItem.setQuantity(selectedItem.getQuantity()+1);
                        calculateCartItem();
                        cart.getSelectionModel().select(selectedItem);
                        break;
                    case SUBTRACT:
                        if(selectedItem.getQuantity()>1){
                            selectedItem.setQuantity(selectedItem.getQuantity()-1);
                            calculateCartItem();
                            cart.getSelectionModel().select(selectedItem);
                        }else{
                            cart.getItems().remove(selectedItem);
                            calculateCartItem();
                        }
                        break;
                    case DELETE:
                        cart.getItems().removeAll(selectedItem);
                        calculateCartItem();
                        break;
                }

            }
        });
    }

    private void showAlertBox(String title,String message){
        AlertBox alertBox = new AlertBox(MainWindowController.mainStage);
        alertBox.setTitle(title);
        alertBox.setContentText(message);
        alertBox.showAndWait();
    }

    private void onSave(Sale sale){
        sale.getSaleDetail().setPaid(true);
        SaleService.getService().save(sale);
        prepareForNextSale();
    }

    private Sale getSale(){
        if(null==sale){
            sale = new Sale();
        }

        SaleDetail saleDetail = sale.getSaleDetail();
        saleDetail.setSalePersonId(Logger.getLogger().getLoggedAccount().getId());
        saleDetail.setSaleDate(LocalDate.now());
        saleDetail.setSaleTime(LocalTime.now());
        saleDetail.setTaxRate(TaxRepository.getRepository().getTaxRate(LocalDate.now()));
        saleDetail.setPaid(false);
        sale.getSaleItemList().clear();
        sale.getSaleItemList().addAll(cart.getItems());
        sale.getPayment().subTotalProperty().set(payment.subTotalProperty().get());
        sale.getPayment().taxRateProperty().set(payment.taxRateProperty().get());
        return sale;
    }

    private void resetPayment(){
        payment.taxRateProperty().set(TaxRepository.getRepository().getTaxRate(LocalDate.now()));
        payment.subTotalProperty().set(0);
        payment.discountCashProperty().set(0);
        payment.discountPercentProperty().set(0);
        payment.tenderedProperty().set(0);
    }

    private Stage getStage(){
        return (Stage)cart.getScene().getWindow();
    }

}
