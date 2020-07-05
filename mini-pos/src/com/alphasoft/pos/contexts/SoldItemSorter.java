package com.alphasoft.pos.contexts;

import com.alphasoft.pos.models.SoldItem;

import java.util.List;

public interface SoldItemSorter {
    void sort(List<SoldItem> soldItemList);

    enum Mode{
        QUANTITY("Quantity"),AMOUNT("Amount");

        private final String value;

        Mode(String value){
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
