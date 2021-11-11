package com.tohan.veriparkcasestudy.ui.falling;

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
import com.tohan.veriparkcasestudy.databinding.FragmentFallingBinding;

import de.codecrafters.tableview.TableView;

public class FallingFragment extends Fragment {

    private TableView<String[]> tableView;
    private FragmentFallingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFallingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        tableView = binding.tableLayout.tableView;
        SearchView searchView = binding.tableLayout.searchView;

        HandShakeHelper handShakeHelper = new HandShakeHelper();
        handShakeHelper.initListState(getContext(), tableView, searchView, root, "falling", "decreasing");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}