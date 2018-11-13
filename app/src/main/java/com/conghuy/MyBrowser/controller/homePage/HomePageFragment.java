package com.conghuy.MyBrowser.controller.homePage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conghuy.MyBrowser.R;
import com.conghuy.MyBrowser.common.PrefManager;
import com.conghuy.MyBrowser.databinding.HomePageFragmentBinding;


/**
 * Created by Android on 4/11/2018.
 */

public class HomePageFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private HomePageFragmentBinding binding;

    public HomePageFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.home_page_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.btnSave) {
            save();
        }
    }

    public void bind() {
        binding.ed.setText(new PrefManager(getActivity()).getHomePage());
        binding.btnSave.setOnClickListener(this);
    }

    private void save() {
        String msg = binding.ed.getText().toString().trim();
        if (msg.length() == 0) return;
        new PrefManager(getActivity()).setHomePage(msg);
        dismiss();
    }
}

