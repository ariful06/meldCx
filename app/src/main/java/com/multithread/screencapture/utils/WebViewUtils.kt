package com.multithread.screencapture.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData

fun WebView.loadUrlToWebView(url : String, context : Context,bitmap: MutableLiveData<Bitmap>,isLoading: MutableLiveData<Boolean>){
    val settings: WebSettings = this.settings
    this.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
    settings.useWideViewPort = true
    settings.javaScriptEnabled = true
    settings.loadWithOverviewMode = true
    isLoading.value = true
    this.webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            if (isLoading.value!!) {
                isLoading.value = false
            }
            if (!isLoading.value!!){
                bitmap.value = ImageStoreUtils.getBitmapFromView(view)
            }
        }

        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            if (isLoading.value == true) {
                isLoading.value = false
            }
            Log.e("WebView Error", "description:${description}")
        }
    }
    this.loadUrl(url)
}