package com.alphasoft.pos.factories;

import com.alphasoft.pos.workers.SoldAmountSorter;
import com.alphasoft.pos.workers.SoldItemSorter;
import com.alphasoft.pos.workers.SoldQuantitySorter;


public class SoldItemSorterFactory {

    private static SoldItemSorterFactory factory;

    private SoldItemSorterFactory(){}

    public SoldItemSorter getSorter(SoldItemSorter.Mode mode){
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
