package com.example.myapplication

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import com.example.myapplication.databinding.ActivityImageswitcherBinding
import com.example.myapplication.databinding.ActivitySharedViewBinding
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class imageswitcher : AppCompatActivity() {
    private var matrix: Matrix = Matrix()
    var martixValue = FloatArray(9)

    var imageoriginalheight :Float  = 0f
    var imageoriginalwidth  :Float  = 0f
    var screenwidth         :Float  = 0f
    var screenheight        :Float  = 0f

    var maxscale  = 4.0f
    var initscale = 1.0f
    var scale     = 1.0f
    private var scaleFactor = 1.0f

    var imagelist = arrayListOf<Int>()
    var startX :Float = 0f
    var endX   :Float = 0f
    var currentIndex = 0
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var binding: ActivityImageswitcherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageswitcherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagelist= intent.getIntegerArrayListExtra("imageList")!!
        binding.imageswitcher.setFactory {
            var imageview = ImageView(this@imageswitcher)
            imageview.scaleType = ImageView.ScaleType.MATRIX
            imageview.layoutParams = FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            imageview.setImageResource(imagelist[currentIndex])
            imageview
        }


        binding.imageButton.setOnClickListener {
            binding.imageswitcher.removeAllViews()
            binding.imageswitcher.setInAnimation(this@imageswitcher, R.anim.from_left)
            binding.imageswitcher.setOutAnimation(this@imageswitcher, R.anim.to_right)
            --currentIndex
            if (currentIndex < 0) {
                currentIndex = imagelist.size - 1
            }
            binding.imageswitcher.setFactory {
                var imageview = ImageView(this@imageswitcher)
                imageview.setImageResource(imagelist[currentIndex])
                imageview
            }

        }
        binding.imageButton2.setOnClickListener {
            binding.imageswitcher.removeAllViews()
            binding.imageswitcher.setInAnimation(this@imageswitcher, R.anim.from_right)
            binding.imageswitcher.setOutAnimation(this@imageswitcher, R.anim.to_left)
            currentIndex++
            if (currentIndex == imagelist.size) {
                currentIndex = 0
            }
            binding.imageswitcher.setFactory {
                var imageview = ImageView(this@imageswitcher)
                imageview.setImageResource(imagelist[currentIndex])
                imageview
            }
        }
    }

    fun startfixscale(image:ImageView){
        scale = 1.0f
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenheight = displayMetrics.heightPixels.toFloat()
        screenwidth  = displayMetrics.widthPixels.toFloat()
        val Bitmap:Bitmap = BitmapFactory.decodeResource(resources,imagelist[currentIndex])
        imageoriginalheight = Bitmap.height.toFloat()
        imageoriginalwidth = Bitmap.width.toFloat()

        if (imageoriginalwidth > screenwidth && imageoriginalheight <= screenheight){
            scale = screenwidth / imageoriginalwidth
        }
        if (imageoriginalheight > screenheight && imageoriginalwidth <= screenwidth){
            scale =  screenheight / imageoriginalheight
        }
        if (imageoriginalwidth > screenwidth && imageoriginalheight > screenheight) {
            scale = min(screenwidth / imageoriginalwidth, screenheight / imageoriginalheight)
        }
        if (imageoriginalwidth < screenwidth && imageoriginalheight < screenheight) {
            scale = max(screenwidth / imageoriginalwidth, screenheight / imageoriginalheight)
        }
        matrix.postScale(scale, scale)
        versioncontrol()
        image.imageMatrix = matrix
        initscale = scale
    }
    fun versioncontrol(){
        val rectF = RectF()
        rectF.set(0f, 0f, imageoriginalwidth,imageoriginalheight)
        matrix.mapRect(rectF)

        var deltaX  = 0f
        var deltaY  = 0f
        if (rectF.width() >= screenwidth) {
            if (rectF.left > 0) {
                deltaX = -rectF.left
            }
            if (rectF.right < screenwidth) {
                deltaX = screenwidth - rectF.right
            }
        }
        if (rectF.height() >= screenheight) {
            if (rectF.top > 0) {
                deltaY = -rectF.top
            }
            if (rectF.bottom < screenheight) {
                deltaY = screenheight - rectF.bottom
            }
        }
        if (rectF.width() < screenwidth) {
            deltaX = screenwidth * 0.5f - rectF.right + 0.5f * rectF.width()
        }
        if (rectF.height() < screenheight) {
            deltaY = screenheight * 0.5f - rectF.bottom + 0.5f * rectF.height()
        }
        matrix.postTranslate(deltaX, deltaY)
    }

}
