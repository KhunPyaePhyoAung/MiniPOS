package com.alphasoft.pos.workers;

import com.alphasoft.pos.models.Product;

import java.util.List;
import java.util.stream.Collectors;

public class AvailableProductFilter implements ProductFilter{
    @Override
    public void filter(List<Product> productList) {
        productList.retainAll(productList.stream().filter(Product::isAvailable).collect(Collectors.toList()));
    }
}
