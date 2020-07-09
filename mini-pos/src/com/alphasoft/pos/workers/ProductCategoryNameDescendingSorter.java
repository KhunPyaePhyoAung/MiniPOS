package com.alphasoft.pos.workers;

import com.alphasoft.pos.models.ProductCategory;

import java.util.List;

public class ProductCategoryNameDescendingSorter implements ProductCategorySorter{
    @Override
    public void sort(List<ProductCategory> categoryList) {
        categoryList.sort((c0,c1)->-c0.getName().compareTo(c1.getName()));
    }
}
