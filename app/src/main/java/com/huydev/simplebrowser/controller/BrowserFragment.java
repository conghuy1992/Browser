package com.huydev.simplebrowser.controller;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.huydev.simplebrowser.R;
import com.huydev.simplebrowser.common.AdBlocker;
import com.huydev.simplebrowser.common.Statics;
import com.huydev.simplebrowser.common.Utils;
import com.huydev.simplebrowser.databinding.BrowserFragmentBinding;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Android on 4/11/2018.
 */

public class BrowserFragment extends Fragment {
    private String TAG = this.getClass().getSimpleName();
    private BrowserFragmentBinding binding;
    private List<String> listAdv;
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
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        loadUrl(Statics.ABOUT_BLANK);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind();
    }

    public void bind() {
        if (getActivity() == null) return;
        listAdv = Utils.get_adv(getActivity());
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new MyWebViewClient());
        binding.webView.setWebChromeClient(new MyWebChromeClient());
        loadUrl(Statics.ABOUT_BLANK);
        binding.input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    loadUrl(binding.input.getText().toString().trim());
                    Utils.hideKeyboardFrom(getActivity(),binding.input);
                    return true;
                }
                return false;
            }
        });
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public final WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//            Log.d(TAG, "shouldInterceptRequest:" + url);
            return Utils.isContains(listAdv, url) ? AdBlocker.createEmptyResource() : super.shouldInterceptRequest(view, url);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            if (callBack != null) callBack.onPageStarted(url);
            Log.d(TAG, "onPageStarted");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished url:" + url);
//            if (callBack != null) callBack.onPageFinished(url);
        }
    }
    private class MyWebChromeClient extends WebChromeClient {
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

//        @Override
//        public boolean onConsoleMessage(ConsoleMessage cm) {
//            Log.d(TAG, cm.message() + " #" + cm.lineNumber() + " --" + cm.sourceId() );
//            return super.onConsoleMessage(cm);
//        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }
    private void loadUrl(String url){
        binding.webView.loadUrl(url);
    }
}

