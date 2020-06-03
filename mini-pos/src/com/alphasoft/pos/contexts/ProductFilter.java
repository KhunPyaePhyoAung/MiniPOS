package com.alphasoft.pos.contexts;

import com.alphasoft.pos.models.Product;

import java.util.List;

public interface ProductFilter {
    public void filter(List<Product> productList);

    public enum Mode{
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
