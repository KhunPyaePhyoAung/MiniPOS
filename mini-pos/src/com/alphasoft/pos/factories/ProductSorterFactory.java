package com.alphasoft.pos.factories;

import com.alphasoft.pos.contexts.*;

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
                break;
            case PRICE_ASCENDING:
                productSorter = new ProductPriceAscendingSorter();
                break;
            case PRICE_DESCENDING:
                productSorter = new ProductPriceDescendingSorter();
        }
        return productSorter;
    }
    public static ProductSorterFactory getFactory(){
        if(null==factory) factory = new ProductSorterFactory();
        return factory;
    }
}
