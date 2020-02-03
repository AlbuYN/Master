package ru.shrott.shrottmaster.other.Utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import ru.shrott.shrottmaster.R

class Permissions {

    private var isDialogCamera = false
    private var isDialogRequestPermissions = false

    companion object {
        const val PERMISSION_CAMERA = 11
        const val PERMISSIONS_MEDIA_CODE = 109
    }


    fun checkPermissionCamera(activity: Activity): Boolean {
        val permission = Manifest.permission.CAMERA
        val grant = ContextCompat.checkSelfPermission(activity, permission)
        return grant == PackageManager.PERMISSION_GRANTED
    }

    fun camera(fragment: Fragment) {

        if (!isDialogCamera && !isDialogRequestPermissions) {
            val alertBuilder = AlertDialog.Builder(fragment.context)
            alertBuilder.setCancelable(true)
            alertBuilder.setTitle(R.string.title_message_comment_need_permission_camera)
            alertBuilder.setMessage(R.string.comment_need_permission_camera)
            alertBuilder.setPositiveButton(android.R.string.yes) { _, _ -> requestPermissionCamera(fragment) }
            val alert = alertBuilder.create()

            alert.show()
            isDialogCamera = true
        }
    }

    fun storageMediaFiles(activity: FragmentActivity): Boolean {
        val permWST = ActivityCompat.checkSelfPermission(
            activity, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permEST = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            TODO("VERSION.SDK_INT < JELLY_BEAN")
        }
        //int permRAU = ActivityCompat.checkSelfPermission(activity,
        //      Manifest.permission.RECORD_AUDIO);

        if (permEST != PackageManager.PERMISSION_GRANTED ||
            //permRAU != PackageManager.PERMISSION_GRANTED ||
            permWST != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(activity,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                ), PERMISSIONS_MEDIA_CODE
            )
            return false
        } else {
            return true
        }
    }


    private fun requestPermissionCamera(fragment: Fragment) {
        isDialogCamera = false
        isDialogRequestPermissions = true

        val permission = Manifest.permission.CAMERA
        val grant = ContextCompat.checkSelfPermission(fragment.context!!, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            fragment.requestPermissions(arrayOf(permission), PERMISSION_CAMERA)
        }
    }

    fun setDialogRequestPermissions(dialogRequestPermissions: Boolean) {
        isDialogRequestPermissions = dialogRequestPermissions
    }
}