package com.alphasoft.pos.workers;

import com.alphasoft.pos.models.Product;

import java.util.List;
import java.util.stream.Collectors;

public class UnavailableProductFilter implements ProductFilter{
    @Override
    public void filter(List<Product> productList) {
        productList.retainAll(productList.stream().filter(i->!(i.isAvailable())).collect(Collectors.toList()));
    }
}
