package com.alphasoft.pos.contexts;

import com.alphasoft.pos.models.Product;

import java.util.Collection;
import java.util.List;

public interface ProductSorter {
    public void sort(List<Product> list);

    public enum Mode{
        NAME_ASCENDING("Name Ascending"),NAME_DESCENDING("Name Descending");
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
