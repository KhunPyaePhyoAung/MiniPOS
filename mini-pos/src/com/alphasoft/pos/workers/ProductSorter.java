package com.alphasoft.pos.workers;

import com.alphasoft.pos.models.Product;

import java.util.List;

public interface ProductSorter {
    void sort(List<Product> list);

    enum Mode{
        NAME_ASCENDING("Name Ascending"),NAME_DESCENDING("Name Descending"),PRICE_ASCENDING("Price Ascending"),PRICE_DESCENDING("Price Descending");
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
