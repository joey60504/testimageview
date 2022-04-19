package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySharedViewBinding

class SharedView : AppCompatActivity(){
    //TODO 調整每一張圖片大小
    private var matrix: Matrix = Matrix()
    var martixValue = FloatArray(9)

    var imageheight   :Float  = 0f
    var imagewidth  :Float  = 0f
    var screenwidth   :Float  = 0f
    var screenheight  :Float  = 0f

    var maxscale  = 4.0f
    var initscale = 1.0f
    var scale     = 1.0f
    private var scaleFactor = 1.0f

    var imagelist = arrayListOf<Int>()
    var startX :Float = 0f
    var endX   :Float = 0f
    var imageindex:Int= 0
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var binding : ActivitySharedViewBinding
    @SuppressLint("ClickableViewAccessibility")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imagelist = intent.getBundleExtra("image")?.getIntegerArrayList("imageList") as ArrayList<Int>
        binding.imageViewDetail.setImageResource(imagelist[imageindex])

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenheight = displayMetrics.heightPixels.toFloat()
        screenwidth  = displayMetrics.widthPixels.toFloat()

        whenimagechange()
        fixXY()

        scaleGestureDetector = ScaleGestureDetector(this,ScaleListener())
    }
    fun whenimagechange(){
        val Bitmap:Bitmap = BitmapFactory.decodeResource(resources,imagelist[imageindex])
        imageheight = Bitmap.height.toFloat()
        imagewidth = Bitmap.width.toFloat()
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
                    binding.imageViewDetail.setImageResource(imagelist[imageindex])
//                    whenimagechange()
//                    fixXY()
                }
                else if (endX - startX < -100f){
                    if(imageindex == (imagelist.size -1)){
                        imageindex = 0
                    }
                    else{
                        imageindex += 1
                    }
                    binding.imageViewDetail.setImageResource(imagelist[imageindex])
//                    whenimagechange()
//                    fixXY()
                }
            }
        }
        return true
    }
    fun fixXY(){
        if (imagewidth > screenwidth && imageheight <= screenheight){
            scale = screenwidth / imagewidth
        }
        if (imageheight > screenheight && imagewidth <= screenwidth){
            scale = screenheight / imageheight
        }
        if (imagewidth > screenwidth && imageheight > screenheight) {
            scale = Math.min(imagewidth / screenwidth, imageheight / screenheight)
        }
        matrix.postTranslate((screenwidth - imagewidth) / 2, (screenheight - imageheight) / 2)
        matrix.postScale(scale, scale, screenwidth / 2, screenheight / 2)
        binding.imageViewDetail.imageMatrix = matrix
        initscale = scale
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
    fun versioncontrol(){
        val rectF = RectF()
        rectF.set(0f, 0f, imagewidth,imageheight)
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