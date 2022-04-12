package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.max
import kotlin.math.min


class SharedView : AppCompatActivity(){
    private var startDis :Float = 0f
    var startPointX0:Float=0f
    var startPointY0:Float=0f
    var startPointX1:Float=0f
    var startPointY1:Float=0f
    var midX:Float=0f
    var midY:Float=0f
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f
    private lateinit var imageView: ImageView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_view)
        var image: ImageView = MainActivity.myPicture
        imageView = findViewById(R.id.imageView_Detail)
        image.buildDrawingCache()
        val bmap: Bitmap = image.drawingCache
        imageView.setImageBitmap(bmap)
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
    }
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(motionEvent)
        when (motionEvent.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN->{
                startPointX0 = motionEvent.getX(0)
                startPointY0 = motionEvent.getY(0)

                startPointX1 = motionEvent.getX(1)
                startPointY1 = motionEvent.getY(1)
                startDis = distance()
                if(startDis > 10f){
                    getmid()
                }
            }
        }
        return true
    }
    fun distance(): Float {
        var dx :Float = startPointX0-startPointX1
        var dy :Float = startPointY0-startPointY1
        return kotlin.math.sqrt(dx * dx + dy * dy)
    }
    fun getmid() {
        midX= (startPointX0+startPointX1) / 2
        midY= (startPointY0+startPointY1) / 2
    }
    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            return super.onScaleBegin(detector)
        }
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 5.0f))
            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor
            return true
        }
    }
}