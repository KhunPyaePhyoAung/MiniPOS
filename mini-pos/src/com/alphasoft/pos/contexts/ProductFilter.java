package com.alphasoft.pos.contexts;

public class ProductFilter {





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
