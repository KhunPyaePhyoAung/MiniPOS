package com.alphasoft.pos.commons;

import java.util.Comparator;
import java.util.List;

public class PosSorter {

    public static void sort(List list,Mode mode){
        switch (mode){
            case NAME_ASCENDING:
                list.sort(Comparator.comparing(Object::toString));
                break;
            case NAME_DESCENDING:
                list.sort((o1,o2)->-o1.toString().compareTo(o2.toString()));
                break;
        }
    }

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
