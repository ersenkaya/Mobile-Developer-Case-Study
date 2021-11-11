package com.tohan.veriparkcasestudy.Listeners;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.navigation.Navigation;

import com.tohan.veriparkcasestudy.Api.Models.StockModel;
import com.tohan.veriparkcasestudy.R;

import java.util.ArrayList;

import de.codecrafters.tableview.listeners.TableDataClickListener;


public class StockClickListener implements TableDataClickListener<String[]> {

    private Context context;
    private View view;
    private String fragmentName;
    private ArrayList<StockModel> stockList = new ArrayList<>();

    public StockClickListener(Context context, View view,  String fragmentName) {
        this.context = context;
        this.view = view;
        this.fragmentName = fragmentName;
    }

    public ArrayList<StockModel> getStockList() {
        return stockList;
    }

    public void setStockList(ArrayList<StockModel> stockList) {
        this.stockList = stockList;
    }

    @Override
    public void onDataClicked(int rowIndex, String[] clickedData) {
        Bundle bundle = new Bundle();
        bundle.putInt("stockId",stockList.get(rowIndex).getId());
        switch (fragmentName) {
            case "stock":
                Navigation.findNavController(view).navigate(R.id.action_nav_stock_to_detailFragment, bundle);
            break;
            case "ascendants":
                Navigation.findNavController(view).navigate(R.id.action_nav_ascendants_to_detailFragment, bundle);
                break;
            case "falling":
                Navigation.findNavController(view).navigate(R.id.action_nav_falling_to_detailFragment, bundle);
                break;
            case "imkb_30":
                Navigation.findNavController(view).navigate(R.id.action_nav_imkb_30_to_detailFragment, bundle);
                break;
            case "imkb_50":
                Navigation.findNavController(view).navigate(R.id.action_nav_imkb_50_to_detailFragment, bundle);
                break;
            case "imkb_100":
                Navigation.findNavController(view).navigate(R.id.action_nav_imkb_100_to_detailFragment, bundle);
                break;
        }

    }
}
