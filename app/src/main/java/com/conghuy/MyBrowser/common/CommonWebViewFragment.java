package com.conghuy.MyBrowser.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.conghuy.MyBrowser.R;
import com.conghuy.MyBrowser.databinding.CommonWebviewFragmentBinding;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Android on 4/11/2018.
 */

public class CommonWebViewFragment extends Fragment {
    private String TAG = "WebviewFragment";
    private CommonWebviewFragmentBinding binding;
    //    private String url = "https://www.google.com";
    private String url = "https://www.24h.com.vn/";
    private ArrayList<String> listAdv;
    private WebViewCallBack webViewCallBack;

    public CommonWebViewFragment() {

    }

    @SuppressLint("ValidFragment")
    public CommonWebViewFragment(WebViewCallBack webViewCallBack) {
        this.webViewCallBack = webViewCallBack;
    }

    @SuppressLint("ValidFragment")
    public CommonWebViewFragment(String url) {
        if (url != null) this.url = url;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.common_webview_fragment, container, false);
        View view = binding.getRoot();
        bind();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadUrl(Statics.ABOUT_BLANK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        activityResult(requestCode, resultCode, data);
    }

    public void bind() {
        listAdv = Utils.get_adv(getActivity());
        Log.d(TAG, "URL:" + url);
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        // scale web
        binding.webView.setInitialScale(1);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        // setAllowFileAccess
        webSettings.setAllowFileAccess(true);
        // enable load video
        webSettings.setDomStorageEnabled(true);
        binding.webView.setWebViewClient(new MyWebViewClient());
        binding.webView.setWebChromeClient(new MyWebChromeClient());
        loadUrl(url);
//        binding.webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.requestFocusFromTouch();
//                return false;
//            }
//        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.webView.loadUrl(binding.webView.getUrl());
            }
        });
    }

    public void loadUrl(String url) {
        binding.webView.loadUrl(url);
    }

    private void dismissSwipeRefreshLayout() {
        if (getActivity() == null) return;
        binding.swipeRefreshLayout.setRefreshing(false);
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
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissSwipeRefreshLayout();
            if(webViewCallBack!=null)webViewCallBack.onUrl(url);
        }
    }


    private class MyWebChromeClient extends WebChromeClient {
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.d(TAG, "newProgress:" + newProgress);
            binding.progressBar.setProgress(newProgress);
            binding.progressBar.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
        }
    }

    private void activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_FILE_LEGACY) {
            if (mUploadMessage == null) return;
            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else if (requestCode == REQUEST_SELECT_FILE) {
            if (mUploadMessageArr == null) return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mUploadMessageArr.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            }
            mUploadMessageArr = null;
        }
    }


    private ValueCallback<Uri> mUploadMessage;
    private int REQUEST_SELECT_FILE_LEGACY = 98;
    private int REQUEST_SELECT_FILE = 99;
    private ValueCallback<Uri[]> mUploadMessageArr;

    public boolean canGoBack() {
        return binding.webView.canGoBack();
    }

    public void goBack() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        }
    }
}

