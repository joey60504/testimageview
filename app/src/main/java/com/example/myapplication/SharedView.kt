package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.databinding.ActivitySharedViewBinding
import kotlin.math.min


class SharedView : AppCompatActivity(),ViewPagerAdapter.OnItemTouchListener{
    private var matrix: Matrix = Matrix()
    var martixValue = FloatArray(9)

    var imageoriginalheight :Float  = 0f
    var imageoriginalwidth  :Float  = 0f
    var screenwidth         :Float  = 0f
    var screenheight        :Float  = 0f
    var maxscale  = 4.0f
    var initscale = 1.0f
    var scale     = 1.0f

    private var startDis :Float = 0f
    private var endDis :Float = 0f
    var startPointX0:Float=0f
    var startPointY0:Float=0f
    var startPointX1:Float=0f
    var startPointY1:Float=0f
    var midX:Float=0f
    var midY:Float=0f
    var mode = 0
    private var scaleFactor = 1.0f
    lateinit var imagelist : ArrayList<Int>
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var binding : ActivitySharedViewBinding
    @SuppressLint("ClickableViewAccessibility")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagelist= intent.getIntegerArrayListExtra("imageList")!!

        val myAdapter = ViewPagerAdapter(imagelist, this@SharedView)
        binding.viewpager.adapter = myAdapter
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
//        scaleGestureDetector = ScaleGestureDetector(this,ScaleListener())
    }

    override fun OnItemTouch(postition: Int,move:MotionEvent, v: ImageView) {
        getinitscale(postition)
        val scale: Float = getscale()
        when(move.actionMasked){
            MotionEvent.ACTION_DOWN->{
                mode = 0
            }
            MotionEvent.ACTION_POINTER_DOWN->{
                mode = 1
                startPointX0 = move.getX(0)
                startPointY0 = move.getY(0)
                startPointX1 = move.getX(1)
                startPointY1 = move.getY(1)
                startDis = distance()
                if(startDis > 10f){
                    getmid()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if(mode == 1) {
                    startPointX0 = move.getX(0)
                    startPointY0 = move.getY(0)

                    startPointX1 = move.getX(1)
                    startPointY1 = move.getY(1)
                    endDis = distance()
                    scaleFactor = endDis / startDis
                    if (scale < maxscale && scaleFactor > 1.0f || scale > initscale && scaleFactor < 1.0f) {
                        if (scaleFactor * scale < initscale){
                            scaleFactor = initscale / scale
                        }
                        if (scaleFactor * scale > maxscale){
                            scaleFactor = maxscale / scale
                        }
                        matrix.postScale(scaleFactor, scaleFactor, midX, midY)
                        versioncontrol()
                        v.imageMatrix = matrix
                    }
                }
            }
        }
    }
    fun getinitscale(postition:Int){
        scale = 1.0f
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenheight = displayMetrics.heightPixels.toFloat()
        screenwidth  = displayMetrics.widthPixels.toFloat()
        val Bitmap: Bitmap = BitmapFactory.decodeResource(resources,imagelist[postition])
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
        initscale = scale
    }

//    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener(){
//        override fun onScaleBegin(scaleGestureDetector: ScaleGestureDetector): Boolean {
//            return super.onScaleBegin(scaleGestureDetector)
//        }
//        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
//            scaleFactor = scaleGestureDetector.scaleFactor
//            val scale: Float = getscale()
//            if (scale < maxscale && scaleFactor > 1.0f || scale > initscale && scaleFactor < 1.0f) {
//                if (scaleFactor * scale < initscale){
//                    scaleFactor = initscale / scale
//                }
//                if (scaleFactor * scale > maxscale){
//                    scaleFactor = maxscale / scale
//                }
//                matrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.focusX, scaleGestureDetector.focusY)
//                versioncontrol()
//            }
//            return true
//        }
//
//        override fun onScaleEnd(scaleGestureDetector: ScaleGestureDetector) {
//            super.onScaleEnd(scaleGestureDetector)
//        }
//    }
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
    fun distance(): Float {
        var dx :Float = startPointX0-startPointX1
        var dy :Float = startPointY0-startPointY1
        return kotlin.math.sqrt(dx * dx + dy * dy)
    }
    fun getmid() {
        midX= (startPointX0+startPointX1) / 2
        midY= (startPointY0+startPointY1) / 2;
    }
}