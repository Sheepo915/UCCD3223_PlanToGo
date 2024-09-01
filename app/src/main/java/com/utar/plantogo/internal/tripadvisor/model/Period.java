package com.utar.plantogo.internal.tripadvisor.model;

public class Period {
    private Time open;
    private Time close;

    public Time getOpen() {
        return open;
    }

    public void setOpen(Time open) {
        this.open = open;
    }

    public Time getClose() {
        return close;
    }

    public void setClose(Time close) {
        this.close = close;
    }
}