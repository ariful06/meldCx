package com.multithread.screencapture

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.graphics.Bitmap




class MyWebViewClient : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        // TODO Auto-generated method stub
        super.onPageStarted(view, url, favicon)
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
        // TODO Auto-generated method stub
        view.loadUrl(url!!)
        return true
    }
}