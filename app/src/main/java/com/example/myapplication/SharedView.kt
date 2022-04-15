package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySharedViewBinding


class SharedView : AppCompatActivity(){

    private var matrix: Matrix = Matrix()
    var imageViewwidth  :Float  = 0f
    var imageViewheight :Float  = 0f
    var drawablewidth   :Float  = 0f
    var drawableheight   :Float  = 0f
    private var scaleFactor = 1.0f
    var maxscale  = 4.0f
    var initscale = 1.0f
    var martixValue = FloatArray(9)
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var binding : ActivitySharedViewBinding
    @SuppressLint("ClickableViewAccessibility")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var image: ImageView = MainActivity.myPicture
        image.buildDrawingCache()
        val bmap: Bitmap = image.drawingCache
        binding.imageViewDetail.setImageBitmap(bmap)
        scaleGestureDetector = ScaleGestureDetector(this,ScaleListener())
    }
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(motionEvent)
        return true
    }
    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScaleBegin(scaleGestureDetector: ScaleGestureDetector): Boolean {
            return super.onScaleBegin(scaleGestureDetector)
        }
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor = scaleGestureDetector.scaleFactor
            val scale: Float = getscale()
            if (scale < maxscale && scaleFactor > 1.0f || scale > initscale && scaleFactor < 1.0f) {
                if (scaleFactor * scale < initscale){
                    scaleFactor = initscale / scale
                }
                if (scaleFactor * scale > maxscale){
                    scaleFactor = maxscale / scale
                }
                matrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.focusX, scaleGestureDetector.focusY)
                versioncontrol()
                binding.imageViewDetail.imageMatrix = matrix
            }
            return true
        }

        override fun onScaleEnd(scaleGestureDetector: ScaleGestureDetector) {
            super.onScaleEnd(scaleGestureDetector)
        }
    }
    fun getscale() :Float{
        matrix.getValues(martixValue)
        return martixValue[Matrix.MSCALE_X]
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        drawablewidth    =  binding.imageViewDetail.drawable.intrinsicWidth.toFloat()
        drawableheight   =  binding.imageViewDetail.drawable.intrinsicHeight.toFloat()
        imageViewwidth   =  binding.imageViewDetail.width.toFloat()
        imageViewheight  =  binding.imageViewDetail.height.toFloat()
        matrix.postTranslate(0f,(imageViewheight-drawableheight)/2)
        binding.imageViewDetail.imageMatrix = matrix
    }
    fun versioncontrol(){
        val rectF = RectF()
        rectF.set(0f, 0f, drawablewidth,drawableheight)
        matrix.mapRect(rectF)

        var deltaX  = 0f
        var deltaY  = 0f
        if (rectF.width() >= imageViewwidth) {
            if (rectF.left > 0) {
                deltaX = -rectF.left
            }
            if (rectF.right < imageViewwidth) {
                deltaX = imageViewwidth - rectF.right
            }
        }
        if (rectF.height() >= imageViewheight) {
            if (rectF.top > 0) {
                deltaY = -rectF.top
            }
            if (rectF.bottom < imageViewheight) {
                deltaY = imageViewheight - rectF.bottom
            }
        }
        if (rectF.width() < imageViewwidth) {
            deltaX = imageViewwidth * 0.5f - rectF.right + 0.5f * rectF.width()
        }
        if (rectF.height() < imageViewheight) {
            deltaY = imageViewheight * 0.5f - rectF.bottom + 0.5f * rectF.height()
        }
        matrix.postTranslate(deltaX, deltaY)

        var scale = 1.0f
        if (rectF.width() > imageViewwidth && rectF.height() <= imageViewheight){
            scale = imageViewwidth/ rectF.width()
        }
        if (rectF.height() > imageViewheight && rectF.width() <= imageViewwidth){
            scale = imageViewheight / rectF.height()
        }
        if (rectF.width() > imageViewwidth && rectF.height() > imageViewheight){
            scale = Math.min(rectF.width() / imageViewwidth, rectF.height() / imageViewheight)
        }
        initscale = scale
    }
}