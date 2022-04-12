package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.scaleMatrix
import java.lang.Math.sqrt


class SharedView : AppCompatActivity(){


    var mode = 0 // 初始狀態
    private var matrix: Matrix = Matrix()
    private var startDis :Float = 0f
    private var endDis :Float = 0f
    var startPointX0:Float=0f
    var startPointY0:Float=0f
    var startPointX1:Float=0f
    var startPointY1:Float=0f
    var midX:Float=0f
    var midY:Float=0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_view)
        var image:ImageView= MainActivity.myPicture
        val imageView: ImageView = findViewById(R.id.imageView_Detail)
        image.buildDrawingCache()
        val bmap:Bitmap = image.drawingCache
        imageView.setImageBitmap(bmap)

        imageView.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN->{
                    mode = 1
                    startPointX0=event.getX()
                    startPointY0=event.getY()
                }
                MotionEvent.ACTION_POINTER_DOWN->{
                    mode = 2
                    startPointX0 = event.getX(0)
                    startPointY0 = event.getY(0)

                    startPointX1 = event.getX(1)
                    startPointY1 = event.getY(1)
                    startDis = distance()
                    if(startDis > 10f){
                        getmid()
                    }
//                    Log.d("kkk","${startPointX0},${startPointY0},${startPointX1},${startPointY1}")
//                    Log.d("kkk",startDis.toString())
//                    Log.d("kkk1",midPoint.toString())

                }
                MotionEvent.ACTION_MOVE -> {
                    if(mode == 1){
                        val dx:Float = event.getX() - startPointX0
                        val dy:Float = event.getY() - startPointY0
                        imageView.x = imageView.x + dx
                        imageView.y = imageView.y + dy
                    }
                    else if (mode == 2){
                        startPointX0 = event.getX(0)
                        startPointY0 = event.getY(0)

                        startPointX1 = event.getX(1)
                        startPointY1 = event.getY(1)
                        endDis=distance()
                        var scale :Float = endDis/startDis
                        matrix.postScale(scale,scale,midX,midY)
                    }
                }
                MotionEvent.ACTION_UP->{

                }
                MotionEvent.ACTION_POINTER_UP->{
                    mode = 0
                }
            }
            true;
        }
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