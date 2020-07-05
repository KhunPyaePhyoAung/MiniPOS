package com.alphasoft.pos.contexts;

import com.alphasoft.pos.models.SoldItem;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SoldQuantitySorter implements SoldItemSorter {
    @Override
    public void sort(List<SoldItem> soldItemList) {
        soldItemList.retainAll(
                soldItemList.stream().sorted(Comparator.comparing(SoldItem::getSoldQuantity)).collect(Collectors.toList())
        );
    }
}
