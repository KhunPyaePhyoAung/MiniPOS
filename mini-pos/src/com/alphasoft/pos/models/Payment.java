package com.alphasoft.pos.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Payment {
    private final IntegerProperty subTotal = new SimpleIntegerProperty();
    private final IntegerProperty taxRate= new SimpleIntegerProperty();
    private final IntegerProperty tax= new SimpleIntegerProperty();
    private final IntegerProperty total = new SimpleIntegerProperty();
    private final IntegerProperty discountPercent = new SimpleIntegerProperty();
    private final IntegerProperty discountCash = new SimpleIntegerProperty();
    private final IntegerProperty totalDiscount = new SimpleIntegerProperty();
    private final IntegerProperty due = new SimpleIntegerProperty();
    private final IntegerProperty tendered = new SimpleIntegerProperty();
    private final IntegerProperty change = new SimpleIntegerProperty();

    public Payment(){
        tax.bind(subTotal.multiply(taxRate).divide(100));
        total.bind(subTotal.add(tax));
        totalDiscount.bind(total.multiply(discountPercent).divide(100).add(discountCash));
        due.bind(total.subtract(totalDiscount));
        change.bind(tendered.subtract(due));
    }

    public IntegerProperty subTotalProperty() {
        return subTotal;
    }

    public IntegerProperty taxRateProperty() {
        return taxRate;
    }

    public IntegerProperty taxProperty() {
        return tax;
    }

    public IntegerProperty totalProperty() {
        return total;
    }

    public IntegerProperty discountPercentProperty() {
        return discountPercent;
    }

    public IntegerProperty discountCashProperty(){ return discountCash; }

    public IntegerProperty totalDiscountProperty(){ return totalDiscount; }

    public IntegerProperty dueProperty() {
        return due;
    }

    public IntegerProperty tenderedProperty() {
        return tendered;
    }

    public IntegerProperty changeProperty() {
        return change;
    }
}
