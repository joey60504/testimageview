package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.*
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.databinding.ActivitySharedViewBinding
import kotlin.math.min


class SharedView : AppCompatActivity(),ViewPagerAdapter.OnItemTouchListener{
    //螢幕,圖片寬度
    var imageoriginalheight :Float  = 0f
    var imageoriginalwidth  :Float  = 0f
    var screenwidth         :Float  = 0f
    var screenheight        :Float  = 0f
    //縮放
    var maxscale  = 4.0f
    var initscale = 1.0f
    var scale     = 1.0f
    private var matrix: Matrix = Matrix()
    private var oldmatrix: Matrix = Matrix()
    var martixValue = FloatArray(9)
    private var scaleFactor = 1.0f
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    //Touchevent
    var mode = 0
    private var mPoint = Point()
    private var startDis :Float = 0f
    private var endDis :Float = 0f

    lateinit var imagelist : ArrayList<Int>
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

    override fun OnItemTouch(postition: Int, view: View, move:MotionEvent, imageView: ImageView) {
        getinitscale(postition)
        when(move.actionMasked){
            MotionEvent.ACTION_DOWN->{
                mode = 1
                binding.viewpager.isUserInputEnabled = true
            }
            MotionEvent.ACTION_MOVE -> {
                if(mode == 2){
                    endDis = calculateDist(move)
                    if (endDis > startDis + 1) {
                        changeViewSize(startDis, endDis, mPoint,imageView,postition)
                        startDis = endDis;
                    }
                    if (startDis > endDis + 1){
                        changeViewSize(startDis, endDis, mPoint,imageView,postition)
                        startDis = endDis;
                    }
                }
            }
            MotionEvent.ACTION_POINTER_DOWN->{
                mode = 2
                startDis = calculateDist(move)
                mPoint = getMiPoint(move)
                binding.viewpager.isUserInputEnabled = false
            }
        }
    }
    fun getinitscale(postition:Int){
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
    private fun changeViewSize(enddis:Float,startdis:Float,point:Point,imageView: ImageView,postition: Int){
        scaleFactor = enddis / startdis
        Log.d("kkk",scaleFactor.toString())
        matrix.postScale(scaleFactor,scaleFactor,point.x.toFloat(),point.y.toFloat())
        versioncontrol(imageView,postition)
        imageView.imageMatrix = matrix
    }
    fun versioncontrol(imageView: ImageView,postition: Int){
        val rectF : RectF = getMatrixRectF(postition)
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
        imageView.imageMatrix = matrix
    }
    fun getscale() :Float{
        matrix.getValues(martixValue)
        return martixValue[Matrix.MSCALE_X]
    }
    private fun calculateDist(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }
    private fun getMiPoint(event: MotionEvent): Point {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        mPoint.set(x.toInt() / 2, y.toInt() / 2)
        return mPoint
    }
    private fun getMatrixRectF(postition: Int): RectF {
        val rect = RectF()
        val d = getDrawable(imagelist[postition])
        if (null != d) {
            rect[0f, 0f, d.intrinsicWidth.toFloat()] = d.intrinsicHeight.toFloat()
            matrix.mapRect(rect)
        }
        return rect
    }
}