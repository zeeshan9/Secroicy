package com.example.secroicy;

import android.webkit.WebView;
import android.webkit.WebViewClient;

 public class CustomWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;
    }
}