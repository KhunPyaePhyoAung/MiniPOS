package com.alphasoft.pos.factories;

import com.alphasoft.pos.contexts.ProductNameAscendingSorter;
import com.alphasoft.pos.contexts.ProductNameDescendingSorter;
import com.alphasoft.pos.contexts.ProductSorter;

public class ProductSorterFactory {
    private static ProductSorterFactory factory;

    public ProductSorter getSorter(ProductSorter.Mode mode){
        ProductSorter productSorter =null;
        switch (mode){
            case NAME_ASCENDING:
                productSorter = new ProductNameAscendingSorter();
                break;
            case NAME_DESCENDING:
                productSorter =  new ProductNameDescendingSorter();
        }
        return productSorter;
    }
    public static ProductSorterFactory getFactory(){
        if(null==factory) factory = new ProductSorterFactory();
        return factory;
    }
}
