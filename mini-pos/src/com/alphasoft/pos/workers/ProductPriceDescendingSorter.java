package com.alphasoft.pos.workers;

import com.alphasoft.pos.models.Product;

import java.util.List;

public class ProductPriceDescendingSorter implements ProductSorter {
    @Override
    public void sort(List<Product> list) {
        list.sort((p0,p1)->Integer.compare(p1.getPrice(),p0.getPrice()));
    }
}
