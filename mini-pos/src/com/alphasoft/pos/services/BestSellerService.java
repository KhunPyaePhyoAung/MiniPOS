package com.alphasoft.pos.services;


import com.alphasoft.pos.workers.SoldItemSorter;
import com.alphasoft.pos.factories.SoldItemSorterFactory;
import com.alphasoft.pos.models.SoldItem;
import com.alphasoft.pos.repos.SoldItemRepository;

import java.time.LocalDate;
import java.util.List;

public class BestSellerService {
    private static BestSellerService service;

    private BestSellerService(){}

    public List<SoldItem> getItemList(LocalDate startDate,LocalDate endDate,SoldItemSorter.Mode sortMode){
        List<SoldItem> soldItemList =  SoldItemRepository.getRepository().getItems(startDate,endDate);
        return SoldItemSorterFactory.getFactory().getSorter(sortMode).sort(soldItemList);

    }

    public static BestSellerService getService(){
        if(null == service) service = new BestSellerService();
        return service;
    }
}
