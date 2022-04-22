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
    val imageList = arrayListOf<Int>(
        com.example.myapplication.R.drawable.cat,
        com.example.myapplication.R.drawable.n2,
        com.example.myapplication.R.drawable.n3,
        com.example.myapplication.R.drawable.n4,
        com.example.myapplication.R.drawable.building)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageViewImage.setImageResource(imageList[0])
        binding.imageViewImage.setOnClickListener{
            val intent = Intent(this@MainActivity, SharedView::class.java)
            var bundle = Bundle()
            bundle.putIntegerArrayList("imageList",imageList)
            intent.putExtra("image",bundle)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity, binding.imageViewImage, ViewCompat.getTransitionName(binding.imageViewImage)!!
            )
            startActivity(intent, options.toBundle())
        }
    }
}
