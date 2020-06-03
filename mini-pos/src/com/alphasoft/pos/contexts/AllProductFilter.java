package com.alphasoft.pos.contexts;

import com.alphasoft.pos.models.Product;

import java.util.List;

public class AllProductFilter implements ProductFilter{
    @Override
    public void filter(List<Product> productList) {
        productList.retainAll(productList);
    }
}
