package com.tohan.veriparkcasestudy.Helper;

import android.util.Log;
import android.widget.Filter;

import com.tohan.veriparkcasestudy.Api.Models.StockModel;

import java.util.ArrayList;
import java.util.List;

public class SearchHelper {
    private ArrayList<StockModel> resultArray = new ArrayList<>();

    public String[][] searchList(ArrayList<StockModel> stockList, CharSequence charSequence) {
        ArrayList<StockModel> list = stockList;

        ArrayList<StockModel> filteredList = new ArrayList<>();

        if (charSequence.toString().equals("")) {
            Log.i("jsonquery", "test" + "item.getSymbol()");

            filteredList.addAll(list);
        } else {
            filteredList.clear();
            for (StockModel item : list) {

                if (item.getSymbol().toLowerCase().contains(charSequence.toString().toLowerCase())) {

                    filteredList.add(item);
                }
            }
        }
        setResultArray(filteredList);

        String[][] newList = new String[filteredList.size()][7];
        for (int i = 0; i < filteredList.size(); i++) {
            StockModel item = filteredList.get(i);
            String isUpDown = "none";
            if (item.isUp())
                isUpDown = "up";
            else if (item.isDown())
                isUpDown = "down";
            else
                isUpDown = "none";
            String[] str = {item.getSymbol(), String.valueOf(item.getPrice()),
                    String.valueOf(item.getDifference()), String.valueOf(item.getVolume()),
                    String.valueOf(item.getBid()), String.valueOf(item.getOffer()), isUpDown};
            newList[i] = str;
        }


        return newList;
    }

    public ArrayList<StockModel> getResultArray() {
        return resultArray;
    }

    public void setResultArray(ArrayList<StockModel> resultArray) {
        this.resultArray = resultArray;
    }
}
