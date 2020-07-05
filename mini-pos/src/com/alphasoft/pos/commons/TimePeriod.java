package com.alphasoft.pos.commons;

public enum TimePeriod {
    LAST_YEAR("Last Year"),THIS_YEAR("This Year"),LAST_MONTH("Last Month"),THIS_MONTH("This Month"),LAST_WEEK("Last Week"),THIS_WEEK("This Week"),YESTERDAY("Yesterday"),TODAY("Today");
    private final String value;

    TimePeriod(String value){
        this.value  = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
