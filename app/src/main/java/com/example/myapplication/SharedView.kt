package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySharedViewBinding
import kotlin.math.max
import kotlin.math.min


class SharedView : AppCompatActivity(){

    private var scaleFactor = 1.0f
    private var matrix: Matrix = Matrix()
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
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(1.0f, min(scaleFactor, 5.0f))
            Log.d("kkk",scaleFactor.toString())
            matrix.postScale(scaleFactor,scaleFactor,scaleGestureDetector.focusX,scaleGestureDetector.focusY)
            binding.imageViewDetail.imageMatrix = matrix
//            binding.imageViewDetail.pivotX = scaleGestureDetector.focusX - binding.imageViewDetail.x
//            binding.imageViewDetail.pivotY = scaleGestureDetector.focusY - binding.imageViewDetail.y
//            binding.imageViewDetail.scaleX = scaleFactor
//            binding.imageViewDetail.scaleY = scaleFactor
            return true
        }

        override fun onScaleEnd(scaleGestureDetector: ScaleGestureDetector) {
            super.onScaleEnd(scaleGestureDetector)
        }
    }
}