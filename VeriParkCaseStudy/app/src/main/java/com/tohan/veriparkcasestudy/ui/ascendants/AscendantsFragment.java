package com.tohan.veriparkcasestudy.ui.ascendants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tohan.veriparkcasestudy.Helper.HandShakeHelper;
import com.tohan.veriparkcasestudy.databinding.FragmentAscendantsBinding;

import de.codecrafters.tableview.TableView;

public class AscendantsFragment extends Fragment {


    private TableView<String[]> tableView;

    private FragmentAscendantsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAscendantsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        tableView = binding.tableLayout.tableView;
        SearchView searchView = binding.tableLayout.searchView;

        HandShakeHelper handShakeHelper = new HandShakeHelper();
        handShakeHelper.initListState(getContext(), tableView, searchView, root, "ascendants", "increasing");


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}