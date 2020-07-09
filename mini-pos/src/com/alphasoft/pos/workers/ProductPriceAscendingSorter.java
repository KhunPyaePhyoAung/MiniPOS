package com.alphasoft.pos.workers;

import com.alphasoft.pos.models.Product;

import java.util.Comparator;
import java.util.List;

public class ProductPriceAscendingSorter implements ProductSorter{
    @Override
    public void sort(List<Product> list) {
        list.sort(Comparator.comparing(Product::getPrice));
    }
}
