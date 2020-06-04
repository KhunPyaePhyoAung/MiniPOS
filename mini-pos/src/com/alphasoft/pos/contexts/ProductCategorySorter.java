package com.alphasoft.pos.contexts;

import com.alphasoft.pos.models.ProductCategory;

import java.util.List;

public interface ProductCategorySorter {
    void sort(List<ProductCategory> categoryList);

    enum Mode{
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
