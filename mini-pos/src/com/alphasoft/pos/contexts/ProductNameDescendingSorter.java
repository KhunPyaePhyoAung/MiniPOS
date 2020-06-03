package com.alphasoft.pos.contexts;

import com.alphasoft.pos.models.Product;
import java.util.List;

public class ProductNameDescendingSorter implements ProductSorter {
    @Override
    public void sort(List<Product> list) {
        list.sort((p0,p1)->-(p0.getName().compareTo(p1.getName())));
    }
}
