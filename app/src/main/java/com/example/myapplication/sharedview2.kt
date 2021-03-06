package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySharedview2Binding
import kotlin.math.min

class sharedView2 : AppCompatActivity(){
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
    var imageindex:Int= 0
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var binding : ActivitySharedview2Binding
    @SuppressLint("ClickableViewAccessibility")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedview2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        imagelist= intent.getIntegerArrayListExtra("imageList")!!
        binding.imageViewDetail2.setImageResource(imagelist[imageindex])
        startfixscale()
        scaleGestureDetector = ScaleGestureDetector(this,ScaleListener())
    }
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(motionEvent)
        when(motionEvent.actionMasked){
            MotionEvent.ACTION_DOWN->{
                startX = motionEvent.getX()
            }
            MotionEvent.ACTION_UP->{
                endX = motionEvent.getX()
                if(endX - startX > 100f){
                    if(imageindex == 0){
                        imageindex = imagelist.size - 1
                    }
                    else{
                        imageindex -= 1
                    }
                    scaleback()
                    val anim :Animation = AnimationUtils.loadAnimation(this,R.anim.from_left)
                    binding.imageViewDetail2.startAnimation(anim)
                    binding.imageViewDetail2.setImageResource(imagelist[imageindex])
                    startfixscale()
                }
                else if (endX - startX < -100f){
                    if(imageindex == (imagelist.size -1)){
                        imageindex = 0
                    }
                    else{
                        imageindex += 1
                    }
                    scaleback()
                    val anim :Animation = AnimationUtils.loadAnimation(this,R.anim.from_right)
                    binding.imageViewDetail2.startAnimation(anim)
                    binding.imageViewDetail2.setImageResource(imagelist[imageindex])
                    startfixscale()
                }
            }
        }
        return true
    }
    fun scaleback(){
        matrix.postScale(1/scale, 1/scale, screenwidth / 2, screenheight / 2)
        binding.imageViewDetail2.imageMatrix = matrix
    }
    fun startfixscale(){
        scale = 1.0f
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenheight = displayMetrics.heightPixels.toFloat()
        screenwidth  = displayMetrics.widthPixels.toFloat()
        val Bitmap:Bitmap = BitmapFactory.decodeResource(resources,imagelist[imageindex])
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
            scale = min(screenwidth / imageoriginalwidth, screenheight / imageoriginalheight)
        }
        matrix.postScale(scale, scale, screenwidth / 2, screenheight / 2)
        versioncontrol()
        binding.imageViewDetail2.imageMatrix = matrix

        initscale = scale
    }
    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScaleBegin(scaleGestureDetector: ScaleGestureDetector): Boolean {
            return super.onScaleBegin(scaleGestureDetector)
        }
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor = scaleGestureDetector.scaleFactor
            val scale: Float = getscale()
            Log.d("kkk1",scaleFactor.toString())
            Log.d("kkk2",scale.toString())
            if (scale < maxscale && scaleFactor > 1.0f || scale > initscale && scaleFactor < 1.0f) {
                if (scaleFactor * scale < initscale){
                    scaleFactor = initscale / scale
                }
                if (scaleFactor * scale > maxscale){
                    scaleFactor = maxscale / scale
                }
                matrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.focusX, scaleGestureDetector.focusY)
                versioncontrol()
                binding.imageViewDetail2.imageMatrix = matrix
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