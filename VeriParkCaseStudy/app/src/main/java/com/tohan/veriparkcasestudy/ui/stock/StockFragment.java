package com.tohan.veriparkcasestudy.ui.stock;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;


import com.tohan.veriparkcasestudy.Helper.HandShakeHelper;

import com.tohan.veriparkcasestudy.databinding.FragmentStockBinding;



import de.codecrafters.tableview.TableView;


public class StockFragment extends Fragment {

    private TableView<String[]> tableView;
    private FragmentStockBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStockBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tableView = binding.tableLayout.tableView;
        SearchView searchView = binding.tableLayout.searchView;

        HandShakeHelper handShakeHelper = new HandShakeHelper();
        handShakeHelper.initListState(getContext(), tableView, searchView, root, "stock", "all");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}