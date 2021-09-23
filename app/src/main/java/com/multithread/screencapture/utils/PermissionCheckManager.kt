package com.multithread.screencapture.utils

import android.Manifest
import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

class PermissionCheckManager {

    companion object {

        fun hasExternalStorageWritePermission(context: Context): Boolean {
            return EasyPermissions.hasPermissions(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

        fun hasExternalStorageReadPermission(context: Context): Boolean {
            return EasyPermissions.hasPermissions(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }
}