package com.alphasoft.pos.workers;

import com.alphasoft.pos.models.SoldItem;

import java.util.List;
import java.util.stream.Collectors;

public class SoldAmountSorter implements SoldItemSorter {
    @Override
    public List<SoldItem> sort(List<SoldItem> soldItemList) {
        return soldItemList.stream().sorted((i1,i2)->-(i1.getSoldAmount()-i2.getSoldAmount())).collect(Collectors.toList());
    }
}
