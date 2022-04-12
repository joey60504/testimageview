package com.example.myapplication

import android.R
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.example.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageViewImage.setOnClickListener{
            myPicture = binding.imageViewImage
            val intent = Intent(this@MainActivity, SharedView::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity, binding.imageViewImage, ViewCompat.getTransitionName(binding.imageViewImage)!!
            )
            startActivity(intent, options.toBundle())
        }
    }
    companion object {
        lateinit var myPicture: ImageView
    }
}

