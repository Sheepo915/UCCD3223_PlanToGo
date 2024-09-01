package com.utar.plantogo.internal.tripadvisor.model;

import java.util.List;

public class Hours {
    private List<Period> periods;
    private List<String> weekdayText;

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public List<String> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<String> weekdayText) {
        this.weekdayText = weekdayText;
    }
}