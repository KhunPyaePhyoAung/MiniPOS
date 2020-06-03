package com.alphasoft.pos.factories;

import com.alphasoft.pos.contexts.AvailableProductFilter;
import com.alphasoft.pos.contexts.ProductFilter;
import com.alphasoft.pos.contexts.UnavailableProductFilter;

public class ProductFilterFactory {
    private static ProductFilterFactory factory;

    public ProductFilter getFilter(ProductFilter.Mode mode) {
        switch (mode){
            case AVAILABLE: return new AvailableProductFilter();
            case UNAVAILABLE: return new UnavailableProductFilter();
            default: return null;
        }
    }
    public static ProductFilterFactory getFactory(){
        if(null==factory) factory = new ProductFilterFactory();
        return factory;
    }
}
