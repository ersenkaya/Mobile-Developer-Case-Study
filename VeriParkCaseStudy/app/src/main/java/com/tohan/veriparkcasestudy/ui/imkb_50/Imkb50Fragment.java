package com.tohan.veriparkcasestudy.ui.imkb_50;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.tohan.veriparkcasestudy.Helper.HandShakeHelper;
import com.tohan.veriparkcasestudy.databinding.FragmentImkb50Binding;

import de.codecrafters.tableview.TableView;

public class Imkb50Fragment extends Fragment {

    private TableView<String[]> tableView;
    private FragmentImkb50Binding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentImkb50Binding.inflate(inflater, container, false);
        View root = binding.getRoot();


        tableView = binding.tableLayout.tableView;
        SearchView searchView = binding.tableLayout.searchView;

        HandShakeHelper handShakeHelper = new HandShakeHelper();
        handShakeHelper.initListState(getContext(), tableView, searchView, root, "imkb_50", "volume50");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}