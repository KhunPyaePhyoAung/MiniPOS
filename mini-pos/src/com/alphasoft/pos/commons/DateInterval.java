package com.alphasoft.pos.commons;

import java.nio.file.LinkOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public class DateInterval {

    private LocalDate startDate,endDate;

    public DateInterval(TimePeriod timePeriod){
        switch (timePeriod){
            case TODAY:
                startDate = LocalDate.now();
                endDate = startDate;
                break;
            case YESTERDAY:
                startDate = LocalDate.now().minusDays(1);
                endDate = startDate;
                break;
            case THIS_WEEK:
                startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                endDate = startDate.plusDays(6);
                break;
            case LAST_WEEK:
                startDate = LocalDate.now().minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                while (!startDate.getDayOfWeek().equals(DayOfWeek.MONDAY)){
                    startDate = startDate.minusDays(1);
                }
                endDate = startDate.plusDays(6);
                break;
            case THIS_MONTH:
                startDate = LocalDate.now().withDayOfMonth(1);
                endDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
                break;
            case LAST_MONTH:
                startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
                break;
            case THIS_YEAR:
                startDate = LocalDate.now().withDayOfYear(1);
                endDate = startDate.with(TemporalAdjusters.lastDayOfYear());
                break;
            case LAST_YEAR:
                startDate = LocalDate.now().minusYears(1).withDayOfYear(1);
                endDate = startDate.with(TemporalAdjusters.lastDayOfYear());
                break;
        }
    }

    public LocalDate getStartDate(){
        return startDate;
    }

    public LocalDate getEndDate(){
        return endDate;
    }


}
