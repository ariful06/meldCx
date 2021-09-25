package com.multithread.screencapture.home.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Picture
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebSettings.PluginState
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.multithread.screencapture.MyWebViewClient
import com.multithread.screencapture.databinding.ActivityMainBinding
import com.multithread.screencapture.history.HistoryActivity
import com.multithread.screencapture.history.model.ImageDataModel
import com.multithread.screencapture.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import android.R
import android.R.attr
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.multithread.screencapture.utils.Constants.URL_PREFIX
import com.multithread.screencapture.utils.Constants.URL_PREFIX_SECURE
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.util.*
import android.media.MediaScannerConnection

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Canvas

import androidx.annotation.NonNull
import android.graphics.BitmapFactory

import android.os.ParcelFileDescriptor
import java.io.FileDescriptor
import android.R.attr.data
import com.multithread.screencapture.utils.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    companion object {
        const val REQUEST_EXTERNAL_STORAGE = 420
    }

    private lateinit var binding: ActivityMainBinding
    private var url: String = ""


    private lateinit var mViewModel: HomeViewModel
    private lateinit var imageObj: ImageDataModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.viewModel = mViewModel
        getIntentData()
        setOnClickListeners()
        initObserver()
    }

    private fun getIntentData() {
        if (intent.hasExtra(Constants.INTENT_DATA)) {
            imageObj = intent.extras?.get(Constants.INTENT_DATA) as ImageDataModel
            binding.etUrl.setText(imageObj.title)
            mViewModel.imageTitle.value = imageObj.title
            binding.webView.loadUrlToWebView(mViewModel.imageTitle.value!!,this,mViewModel.imageBitmap,mViewModel.isLoading)
        }
    }

    private fun initObserver() {
        mViewModel.isImageSaved.observe(this, androidx.lifecycle.Observer {
            if (mViewModel.isImageSaved.value != null && mViewModel.isImageSaved.value!!) {
                Toast.makeText(this, "Screen shot saved successfully", Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.isLoading.observe(this, {
            if (mViewModel.isLoading.value == true)
                binding.progressBar.visibility = View.VISIBLE
            else binding.progressBar.visibility = View.GONE
        })
    }

    private fun setOnClickListeners() {
        binding.btnGo.setOnClickListener {
            if (!binding.etUrl.text.toString().isNullOrEmpty()) {
                mViewModel.isLoading.value = true
                url = binding.etUrl.text.toString().trim()
                if (!url.startsWith(URL_PREFIX_SECURE) || !url.startsWith(URL_PREFIX)){
                    url = "$URL_PREFIX_SECURE$url"
                    binding.etUrl.setText(url)
                }
                mViewModel.imageTitle.value = url
                binding.webView.loadUrlToWebView(mViewModel.imageTitle.value!!,this,mViewModel.imageBitmap,mViewModel.isLoading)
            } else
                Toast.makeText(this, "Please enter an url first", Toast.LENGTH_LONG).show()
        }
        binding.btnCapture.setOnClickListener {
            if (!binding.etUrl.text.toString().isNullOrEmpty()) {
                if (PermissionCheckManager.hasExternalStorageWritePermission(this)) {
                    mViewModel.actualImagePath.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        ImageStoreUtils.getRealPath(this,mViewModel.imageBitmap.value,mViewModel.getImageFileName())
                    }else{
                        ImageStoreUtils.getRealPath(mViewModel.imageBitmap.value,mViewModel.getImageFileName())
                    }
                    mViewModel.saveImage()
                } else
                    EasyPermissions.requestPermissions(
                        this,
                        "This application needs Storage permission to save screen shot to your device.",
                        REQUEST_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
            } else
                Toast.makeText(this, "Please enter an url first", Toast.LENGTH_LONG).show()
        }
        binding.btnHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        Log.d("Snapshot", "onPermissionsGranted:" + requestCode + ":" + perms.size)
        mViewModel.actualImagePath.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
             ImageStoreUtils.getRealPath(this,mViewModel.imageBitmap.value,mViewModel.getImageFileName())
        }else{
           ImageStoreUtils.getRealPath(mViewModel.imageBitmap.value,mViewModel.getImageFileName())
        }
        mViewModel.saveImage()

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.d("Snapshot", "onPermissionsDenied:" + requestCode + ":" + perms.size);
        if (EasyPermissions.somePermissionDenied(this, perms.first())) {
            AppSettingsDialog.Builder(this).build().show()
        } else
            EasyPermissions.requestPermissions(
                this,
                "This application needs Storage permission to save screen shot to your device.",
                REQUEST_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
    }

    override fun onRationaleAccepted(requestCode: Int) {
        Log.d("SnapShot", "onRationaleAccepted:$requestCode");
    }

    override fun onRationaleDenied(requestCode: Int) {
        Log.d("SnapShot", "onRationaleDenied:$requestCode");
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Toast.makeText(this, "Write permission from settings", Toast.LENGTH_LONG).show()
        }
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            Toast.makeText(this, "Write permission", Toast.LENGTH_LONG).show()
        }
    }

}