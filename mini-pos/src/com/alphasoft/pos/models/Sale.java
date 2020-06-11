package com.alphasoft.pos.models;

import java.util.ArrayList;
import java.util.List;

public class Sale {
    private List<SaleItem> saleItemList = new ArrayList<>();
    private SaleDetail saleDetail = new SaleDetail();
    private Payment payment = new Payment();

    public List<SaleItem> getSaleItemList() {
        return saleItemList;
    }

    public void setSaleItemList(List<SaleItem> saleItemList) {
        this.saleItemList = saleItemList;
    }

    public SaleDetail getSaleDetail() {
        return saleDetail;
    }

    public void setSaleDetail(SaleDetail saleDetail) {
        this.saleDetail = saleDetail;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
