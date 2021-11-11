package com.tohan.veriparkcasestudy.Api.Models;

import java.util.ArrayList;

public class StockListModel {
    private ArrayList<StockModel> stocks;
    private Status status;



    public ArrayList<StockModel> getStocks() {
        return stocks;
    }

    public void setStocks(ArrayList<StockModel> stocks) {
        this.stocks = stocks;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
