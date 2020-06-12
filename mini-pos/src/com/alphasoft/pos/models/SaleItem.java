package com.alphasoft.pos.models;

public class SaleItem {


    private int saleId;
    private int productId;
    private String productName;
    private int categoryId;
    private String categoryName;
    private int price;
    private int quantity;

    public void setProduct(Product product){
        productId = product.getId();
        productName = product.getName();
        categoryId = product.getCategoryId();
        categoryName = product.getCategoryName();
        price = product.getPrice();
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal() {
        return price*quantity;
    }

}
