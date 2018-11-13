package com.conghuy.MyBrowser.controller;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conghuy.MyBrowser.R;
import com.conghuy.MyBrowser.databinding.ATempLayoutBinding;


/**
 * Created by Android on 4/11/2018.
 */

public class TempBinding extends Fragment {
    private ATempLayoutBinding binding;

    public TempBinding() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.a_temp_layout, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    public void bind() {
    }
}

