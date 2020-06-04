package com.alphasoft.pos.factories;

import com.alphasoft.pos.contexts.*;

public class ProductSorterFactory {
    private static ProductSorterFactory factory;

    private ProductSorterFactory(){}

    public ProductSorter getSorter(ProductSorter.Mode mode){
        switch (mode){
            case NAME_ASCENDING:return new ProductNameAscendingSorter();
            case NAME_DESCENDING:return new ProductNameDescendingSorter();
            case PRICE_ASCENDING:return new ProductPriceAscendingSorter();
            case PRICE_DESCENDING:return new ProductPriceDescendingSorter();
            default:return null;
        }

    }
    public static ProductSorterFactory getFactory(){
        if(null==factory) factory = new ProductSorterFactory();
        return factory;
    }
}
