package com.example.HW3

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var imageView: ImageView
    private lateinit var blurButton: Button
    private var selectedImageUri: Uri? = null

    companion object{
        val IMAGE_REQUEST_CODE  = 100
    }

    //temp 123
    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.pick_img)
        blurButton = findViewById(R.id.blur)
        imageView = findViewById(R.id.img_save)

        button.setOnClickListener{
            pickImageGallery()
        }

        blurButton.setOnClickListener{
            blurImage()
        }
    }

    private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    private fun blurImage(){
        selectedImageUri?.let{ uri ->
//           blurImageInBackground(uri)
           Glide.with(this).load(uri).apply(RequestOptions.bitmapTransform(BlurTransformation(25,3))).into(imageView)
        }

    }

    private fun blurImageInBackground(imageUri: Uri) {
        val inputData = Data.Builder()
            .putString("imageUri", imageUri.toString())
            .build()

        val blurWorkRequest = OneTimeWorkRequest.Builder(BlurWorker::class.java)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(blurWorkRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            selectedImageUri = data?.data
            imageView.setImageURI(selectedImageUri)
            //imageView.setImageURI(data?.data)
            //imageView.setImageURI(selectedImageUri)
        }
    }
}