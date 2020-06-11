package com.alphasoft.pos.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class SaleDetail {
    private int id;
    private int salePersonId;
    private LocalDate saleDate;
    private LocalTime saleTime;
    private int taxRate;

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
}
