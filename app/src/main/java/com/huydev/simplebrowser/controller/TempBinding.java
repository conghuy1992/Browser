package com.huydev.simplebrowser.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.huydev.simplebrowser.R;
import com.huydev.simplebrowser.databinding.ATempLayoutBinding;


/**
 * Created by Android on 4/11/2018.
 */

public class TempBinding extends Fragment {
    private String TAG = this.getClass().getSimpleName();
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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind();
    }

    public void bind() {
        if (getActivity() == null) return;
    }
}

