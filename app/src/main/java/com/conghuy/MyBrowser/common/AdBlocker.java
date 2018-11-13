package com.conghuy.MyBrowser.common;

import android.webkit.WebResourceResponse;

import java.io.ByteArrayInputStream;

public class AdBlocker {

    public static WebResourceResponse createEmptyResource() {
        return new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream("".getBytes()));
    }


}
