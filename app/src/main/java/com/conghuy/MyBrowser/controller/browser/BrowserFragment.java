package com.conghuy.MyBrowser.controller.browser;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.conghuy.MyBrowser.R;
import com.conghuy.MyBrowser.common.CommonWebViewFragment;
import com.conghuy.MyBrowser.common.PrefManager;
import com.conghuy.MyBrowser.common.Statics;
import com.conghuy.MyBrowser.common.Utils;
import com.conghuy.MyBrowser.common.WebViewCallBack;
import com.conghuy.MyBrowser.controller.homePage.HomePageFragment;
import com.conghuy.MyBrowser.databinding.BrowserFragmentBinding;


/**
 * Created by Android on 4/11/2018.
 */

public class BrowserFragment extends Fragment implements View.OnClickListener {
    private BrowserFragmentBinding binding;
    private CommonWebViewFragment commonWebViewFragment;

    public BrowserFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.browser_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == binding.header.btnHome) {
            home();
        } else if (v == binding.header.btnMenu) {
            menu(v);
        }
    }

    public void bind() {
        commonWebViewFragment = new CommonWebViewFragment(new WebViewCallBack() {
            @Override
            public void onUrl(String url) {
                binding.header.ed.setText(url);
            }
        });
        Utils.addFragment(getChildFragmentManager(),
                "",
                R.id.webView,
                commonWebViewFragment);
        binding.header.btnHome.setOnClickListener(this);
        binding.header.btnMenu.setOnClickListener(this);
        binding.header.ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
//        Utils.clearFocus(binding.header.ed);
//        Utils.hideKeyboard(getActivity());
    }

    private void performSearch() {
        if (commonWebViewFragment != null) {
            Utils.hideKeyboard(getActivity());
            Utils.clearFocus(binding.header.ed);
            commonWebViewFragment.loadUrl(Utils.fullUrl(binding.header.ed.getText().toString().trim()));
        }
    }

    private void home() {
        commonWebViewFragment.loadUrl(Utils.fullUrl(new PrefManager(getActivity()).getHomePage()));
    }

    private void menu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_settings) {
                    settingHomePage();
                }
//                else if (id == R.id.action_delete) {
//                } else if (id == R.id.action_favorite) {
//                } else if (id == R.id.action_un_favorite) {
//                }
                return true;
            }
        });
        popup.show();//showing popup menu
    }

    private void settingHomePage() {
        FragmentManager fm = ((FragmentActivity) getActivity()).getSupportFragmentManager();
        HomePageFragment homePageFragment = new HomePageFragment();
        homePageFragment.show(fm, "HomePageFragment");
    }

    public boolean canGoBack() {
        return commonWebViewFragment.canGoBack();
    }

    public void goBack() {
        commonWebViewFragment.goBack();
    }
}

