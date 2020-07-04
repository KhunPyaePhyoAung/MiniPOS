package com.alphasoft.pos.models;

public class Summary {
    private int salesForToday;
    private int unpaid;
    private int availableProducts;
    private int taxRate;

    public int getSalesForToday() {
        return salesForToday;
    }

    public void setSalesForToday(int salesForToday) {
        this.salesForToday = salesForToday;
    }

    public int getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(int unpaid) {
        this.unpaid = unpaid;
    }

    public int getAvailableProducts() {
        return availableProducts;
    }

    public void setAvailableProducts(int availableProducts) {
        this.availableProducts = availableProducts;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }
}
