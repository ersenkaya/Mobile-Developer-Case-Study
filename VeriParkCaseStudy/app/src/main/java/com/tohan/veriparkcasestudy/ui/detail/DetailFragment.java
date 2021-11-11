package com.tohan.veriparkcasestudy.ui.detail;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tohan.veriparkcasestudy.Helper.HandShakeHelper;
import com.tohan.veriparkcasestudy.databinding.FragmentDetailBinding;

public class DetailFragment extends Fragment {

    private FragmentDetailBinding binding;

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        
        HandShakeHelper handShakeHelper = new HandShakeHelper();
        handShakeHelper.initDetailState(getContext(), root, getArguments().getInt("stockId"), binding);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}