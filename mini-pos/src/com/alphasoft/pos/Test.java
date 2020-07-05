package com.alphasoft.pos;

import com.alphasoft.pos.commons.DateInterval;
import com.alphasoft.pos.commons.TimePeriod;
import com.alphasoft.pos.models.SoldItem;
import com.alphasoft.pos.services.SoldItemRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<SoldItem> soldItemList = SoldItemRepository.getRepository().getItems(LocalDate.now(),LocalDate.now());
        for(SoldItem soldItem : soldItemList){
            System.out.println(String.format("%s %d %d",soldItem.getProductName(),soldItem.getSoldAmount(),soldItem.getSoldQuantity()));

        }
    }


}
