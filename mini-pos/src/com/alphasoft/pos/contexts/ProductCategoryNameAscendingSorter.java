package com.alphasoft.pos.contexts;

import com.alphasoft.pos.models.ProductCategory;
import com.alphasoft.pos.services.ProductCategoryService;

import java.util.Comparator;
import java.util.List;

public class ProductCategoryNameAscendingSorter implements ProductCategorySorter {
    @Override
    public void sort(List<ProductCategory> categoryList) {
        categoryList.sort(Comparator.comparing(ProductCategory::getName));
    }
}
