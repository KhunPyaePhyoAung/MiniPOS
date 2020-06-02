package com.alphasoft.pos.contexts;

import com.alphasoft.pos.models.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductFilter {

    private static ProductFilter filter;

    public void filter(List<Product> list,Mode mode){
        switch (mode){
            case AVAILABLE:
                list.retainAll(list.stream().filter(i->i.isAvailable()).collect(Collectors.toList()));
                break;
            case UNAVAILABLE:
                list.retainAll(list.stream().filter(i->!i.isAvailable()).collect(Collectors.toList()));
                break;
        }
    }

    public static ProductFilter getFilter(){
        if(null==filter) filter = new ProductFilter();
        return filter;
    }


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
