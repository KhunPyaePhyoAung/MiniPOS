package com.alphasoft.pos.contexts;

import com.alphasoft.pos.models.Product;

import java.util.List;

public interface ProductFilter {
    void filter(List<Product> productList);

    enum Mode{
        ALL("All"),AVAILABLE("Available"),UNAVAILABLE("Unavailable");
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
