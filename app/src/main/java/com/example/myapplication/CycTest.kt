package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.databinding.ActivitySharedViewBinding


class CycTest:AppCompatActivity(),CycAdapter.ItemOnTouch {
    private lateinit var binding: ActivitySharedViewBinding
    private lateinit var view: View

    private var mode = MODE_NONE
    companion object{
        const val MODE_NONE = 0
        const val MODE_ZOOM = 1
        const val MODE_MOVE = 2
    }


    private var moveX = 1.0f
    private var moveY = 1.0f
    private var XDiff = 0.0f
    private var YDiff = 0.0f

    private var scaleFactor = 1.0f
    private val maxscale:Float = 4.0f
    private val minscale:Float = 1.0f

    private var matrix=Matrix()
    private val currentMatrix=Matrix()
    private var scaledMatrix =Matrix()
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
                    this@CycTest.matrix.setScale(1.0f,1.0f)
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
            MotionEvent.ACTION_DOWN->{
                if(event.pointerCount == 1 && getscale() > 1.0f){
                    mode = MODE_MOVE
                    moveX = event.x
                    moveY = event.y
                    view.animationMatrix = scaledMatrix
                }
            }
            MotionEvent.ACTION_POINTER_DOWN->{
                if (event.pointerCount == 2){
                    mode = MODE_ZOOM
                }
                else{
                    mode = MODE_NONE
                }
            }
            MotionEvent.ACTION_MOVE ->{
                binding.viewpager.isUserInputEnabled  = mode != MODE_ZOOM
                binding.viewpager.isUserInputEnabled  = getscale() == 1.0f
                if(mode == MODE_MOVE) {
                    var dx = event.x - moveX;
                    var dy = event.y - moveY;
                    onDrag(dx,dy,view.width.toFloat(),view.height.toFloat(),view)
                }
            }
            MotionEvent.ACTION_POINTER_UP ->{
                mode = MODE_NONE
            }
            MotionEvent.ACTION_UP->{
                if(mode == MODE_MOVE) {
                scaledMatrix.postTranslate(XDiff, YDiff)
                view.animationMatrix = scaledMatrix
                }
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
                versionControlScale(view.height.toFloat(), view.width.toFloat())
                view.animationMatrix = matrix
            }
            return true
        }

        override fun onScaleEnd(scaleGestureDetector: ScaleGestureDetector) {
            scaledMatrix = matrix
            super.onScaleEnd(scaleGestureDetector)
        }
    }
    fun getscale() :Float{
        matrix.getValues(matrixValue)
        return matrixValue[Matrix.MSCALE_X]
    }
    fun versionControlScale(width:Float,height:Float){
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
    @RequiresApi(Build.VERSION_CODES.Q)
    fun onDrag(xDiff:Float, yDiff:Float, width:Float, height:Float,view: View) {
        XDiff = xDiff
        YDiff = yDiff

        val rectF = RectF()
        rectF.set(0f, 0f, width,height)
        matrix.mapRect(rectF)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels.toFloat()
        val displayHeight = displayMetrics.heightPixels.toFloat()

        when {
            rectF.right - rectF.left < displayWidth -> {
                XDiff = 0f
            }
            rectF.left + XDiff > 0 -> {
                XDiff = if(rectF.left < 0){
                    -rectF.left
                }else{
                    0f
                }
            }
            rectF.right + XDiff < displayWidth -> {
                XDiff = if(rectF.right > displayWidth){
                    displayWidth - rectF.right
                } else{
                    0f
                }
            }
        }
        when {
            rectF.bottom - rectF.top < displayHeight -> {
                YDiff = 0f
            }
            rectF.top + YDiff > 0 -> {
                YDiff = if(rectF.top < 0){
                    -rectF.top
                } else{
                    0f
                }
            }
            rectF.bottom + YDiff < displayHeight -> {
                YDiff = if(rectF.bottom > displayHeight){
                    displayHeight - rectF.bottom
                } else{
                    0f
                }
            }
        }
        matrix.postTranslate(XDiff,YDiff)
        view.animationMatrix = matrix
        matrix.postTranslate(-XDiff,-YDiff)

    }
}