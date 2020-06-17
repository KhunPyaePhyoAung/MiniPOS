package com.alphasoft.pos.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class SaleDetail {
    private int id;
    private int salePersonId;
    private String salePersonName;
    private LocalDate saleDate;
    private LocalTime saleTime;
    private int taxRate;
    private int quantity;
    private int subTotal;
    private boolean paid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalePersonId() {
        return salePersonId;
    }

    public void setSalePersonId(int salePersonId) {
        this.salePersonId = salePersonId;
    }

    public String getSalePersonName() {
        return salePersonName;
    }

    public void setSalePersonName(String salePersonName) {
        this.salePersonName = salePersonName;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public LocalTime getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(LocalTime saleTime) {
        this.saleTime = saleTime;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }

    public int getTotal(){
        return (subTotal*taxRate/100)+subTotal;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
