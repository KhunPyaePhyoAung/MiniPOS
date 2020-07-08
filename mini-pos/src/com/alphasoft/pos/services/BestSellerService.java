package com.alphasoft.pos.services;


import com.alphasoft.pos.contexts.SoldItemSorter;
import com.alphasoft.pos.factories.SoldItemSorterFactory;
import com.alphasoft.pos.models.SoldItem;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BestSellerService {
    private static BestSellerService service;
    public static final int MAX_ITEM = 5;

    private BestSellerService(){}

    public List<SoldItem> getItemList(LocalDate startDate,LocalDate endDate,SoldItemSorter.Mode sortMode){
        List<SoldItem> soldItemList =  SoldItemRepository.getRepository().getItems(startDate,endDate);
        return SoldItemSorterFactory.getFactory().getSorter(sortMode).sort(soldItemList).stream().limit(MAX_ITEM).collect(Collectors.toList());

    }

    public static BestSellerService getService(){
        if(null == service) service = new BestSellerService();
        return service;
    }
}
