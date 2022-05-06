package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.databinding.ActivitySharedViewBinding
import kotlin.math.pow
import kotlin.math.sqrt


class CycTest:AppCompatActivity(),CycAdapter.ItemOnTouch {
    private lateinit var binding: ActivitySharedViewBinding
    private lateinit var view: View

    companion object{
        const val MODE_NONE = 0
        const val MODE_ZOOM = 1
    }
    private var scaleFactor = 1.0f
    private val maxscale:Float = 4.0f
    private val minscale:Float = 1.0f
    private var mode = MODE_NONE
    private val matrix=Matrix()
    private val currentMatrix=Matrix()
    var matrixValue = FloatArray(9)
    private lateinit var scaleGestureDetector: ScaleGestureDetector


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        scaleGestureDetector = ScaleGestureDetector(this,ScaleListener())
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView(){
        val imageList = intent.getIntegerArrayListExtra("imageList")!!
        val cycAdapter=CycAdapter(this)
        binding.viewpager.apply {
            adapter = cycAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onPageScrollStateChanged(state: Int) {
                    view.animationMatrix=currentMatrix
                    super.onPageScrollStateChanged(state)
                }
            })
        }
        cycAdapter.imageList=imageList
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onTouchEvent(view: View, event: MotionEvent) {
        scaleGestureDetector.onTouchEvent(event)
        when(event.action and MotionEvent.ACTION_MASK){
            MotionEvent.ACTION_POINTER_DOWN->{
                mode = if (event.pointerCount <= 2) MODE_ZOOM else MODE_NONE
            }

            MotionEvent.ACTION_MOVE ->{
                binding.viewpager.isUserInputEnabled = mode != MODE_ZOOM
            }
            MotionEvent.ACTION_POINTER_UP ->{
                mode = MODE_NONE
            }
            MotionEvent.ACTION_UP->{
                mode= MODE_NONE
            }
        }
        this.view=view
    }
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(scaleGestureDetector: ScaleGestureDetector): Boolean {
            return super.onScaleBegin(scaleGestureDetector)
        }
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor = scaleGestureDetector.scaleFactor
            val scale: Float = getscale()
            if (scale < maxscale && scaleFactor > 1.0f || scale > minscale && scaleFactor < 1.0f) {
                if (scaleFactor * scale < minscale){
                    scaleFactor = minscale / scale
                }
                if (scaleFactor * scale > maxscale){
                    scaleFactor = maxscale / scale
                }
                matrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.focusX, scaleGestureDetector.focusY)
                versioncontrol(view.height.toFloat(), view.width.toFloat())
                view.animationMatrix = matrix
            }
            return true
        }

        override fun onScaleEnd(scaleGestureDetector: ScaleGestureDetector) {
            super.onScaleEnd(scaleGestureDetector)
        }
    }
    fun getscale() :Float{
        matrix.getValues(matrixValue)
        return matrixValue[Matrix.MSCALE_X]
    }
    fun versioncontrol(height:Float,width:Float){
        val rectF = RectF()
        rectF.set(0f, 0f, width,height)
        matrix.mapRect(rectF)

        var deltaX  = 0f
        var deltaY  = 0f
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                deltaX = -rectF.left
            }
            if (rectF.right < width) {
                deltaX = width - rectF.right
            }
        }
        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                deltaY = -rectF.top
            }
            if (rectF.bottom < height) {
                deltaY = height - rectF.bottom
            }
        }
        if (rectF.width() < width) {
            deltaX = width * 0.5f - rectF.right + 0.5f * rectF.width()
        }
        if (rectF.height() < height) {
            deltaY = height * 0.5f - rectF.bottom + 0.5f * rectF.height()
        }
        matrix.postTranslate(deltaX, deltaY)
    }
}