package com.alphasoft.pos.commons;

import java.util.*;

public class CashSuggester {

    private List<Integer> baseCurrenciesList;

    public CashSuggester(Integer...baseCurrencies){
        baseCurrenciesList = Arrays.asList(baseCurrencies);
    }

    public CashSuggester(List<Integer> baseCurrenciesList){
        this.baseCurrenciesList = baseCurrenciesList;
    }

    public List<Integer> get(int cash){
        Set<Integer> suggestions = new TreeSet<>();
        for(int c:baseCurrenciesList){
            int quotient = cash/c;
            int remainder = cash%c;
            if(remainder!=0){
                int value = (quotient+1)*c;
                if(value!=cash){
                    suggestions.add(value);
                }
            }
        }
        return new ArrayList<>(suggestions);
    }
}
