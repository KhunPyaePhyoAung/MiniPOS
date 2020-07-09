package com.alphasoft.pos.factories;

import com.alphasoft.pos.workers.ProductCategoryNameAscendingSorter;
import com.alphasoft.pos.workers.ProductCategoryNameDescendingSorter;
import com.alphasoft.pos.workers.ProductCategorySorter;

public class ProductCategorySorterFactory {
    private static ProductCategorySorterFactory factory;

    private ProductCategorySorterFactory(){}

    public ProductCategorySorter getSorter(ProductCategorySorter.Mode mode){
        switch (mode){
            case NAME_ASCENDING:return new ProductCategoryNameAscendingSorter();
            case NAME_DESCENDING:return new ProductCategoryNameDescendingSorter();
            default:return null;
        }
    }

    public static ProductCategorySorterFactory getFactory(){
        if(null==factory) factory = new ProductCategorySorterFactory();
        return factory;
    }

}
