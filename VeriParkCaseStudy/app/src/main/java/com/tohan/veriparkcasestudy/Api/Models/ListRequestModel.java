package com.tohan.veriparkcasestudy.Api.Models;

public class ListRequestModel {
    private String period;

    public ListRequestModel(String period) {
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
