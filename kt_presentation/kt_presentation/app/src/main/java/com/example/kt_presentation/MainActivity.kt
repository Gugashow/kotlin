package com.example.kt_presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val CAPTURE_IMAGE_CODE = 500;
        private val REQUEST_PERMISSION = 501;
        private val PICKER_IMAGE_GALLERY = 502;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cam.setOnClickListener {
            takePhoto()
        }

        gallery.setOnClickListener {
            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);

                    requestPermissions(permissions, REQUEST_PERMISSION);
                }
                else{
                    selectImageFromGallery();
                }
            } else {
                selectImageFromGallery()
            }
        }
        buttonClear.setOnClickListener{
            imageView.setImageResource(android.R.color.transparent);
            Toast.makeText(this, "Image clear", Toast.LENGTH_SHORT).show()
        }
    }

    private fun takePhoto() {
        startActivityForResult(
            Intent(MediaStore.ACTION_IMAGE_CAPTURE),
            CAPTURE_IMAGE_CODE
        )
    }

    private fun selectImageFromGallery() {
        startActivityForResult(
            Intent().apply {
                action=Intent.ACTION_PICK
                type="image/*"
            },
            PICKER_IMAGE_GALLERY
        )
    }

    private fun checkAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_PERMISSION -> {
                if (checkAllPermissionsGranted(grantResults)) {
                    selectImageFromGallery();
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        when (requestCode) {
            CAPTURE_IMAGE_CODE -> if (data != null) {
                imageView.setImageBitmap(data.extras?.get("data") as Bitmap)
            }
            PICKER_IMAGE_GALLERY -> if (data != null) {
                imageView.setImageURI(data.data)
            }
        }
    }
}
