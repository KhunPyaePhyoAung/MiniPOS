package com.alphasoft.pos.factories;

import com.alphasoft.pos.contexts.SoldAmountSorter;
import com.alphasoft.pos.contexts.SoldItemSorter;
import com.alphasoft.pos.contexts.SoldQuantitySorter;


public class SoldItemSorterFactory {

    private static SoldItemSorterFactory factory;

    private SoldItemSorterFactory(){}

    public SoldItemSorter getSelector(SoldItemSorter.Mode mode){
        switch (mode){
            case AMOUNT:
                return new SoldAmountSorter();
            case QUANTITY:
                return new SoldQuantitySorter();
            default:
                return null;
        }

    }

    public static SoldItemSorterFactory getFactory(){
        if(null==factory) factory = new SoldItemSorterFactory();
        return factory;
    }
}
