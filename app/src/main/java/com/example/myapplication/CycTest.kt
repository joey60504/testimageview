package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
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

    private var mode = MODE_NONE
    private var startDis = 0f
    private val currentMatrix=Matrix()
    private val matrix=Matrix()
    private var scale=0f
    private val midPoint=PointF()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
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

        when(event.action and MotionEvent.ACTION_MASK){

            MotionEvent.ACTION_POINTER_DOWN->{
                mode = if (event.pointerCount <= 2) MODE_ZOOM else MODE_NONE
                if (mode== MODE_ZOOM){
                    getMidPoint(event,midPoint)
                    startDis = distance(event)
                    currentMatrix.set(view.matrix)
                }
            }

            MotionEvent.ACTION_MOVE ->{

                if (mode== MODE_ZOOM){
                    val endDis=distance(event)
                    scale = endDis/startDis
                    if (scale<1f){
                        scale=1f
                    }else if (scale>4f){
                        scale=4f
                    }
                    matrix.set(currentMatrix)
                    matrix.postScale(scale,scale,midPoint.x,midPoint.y)
                    view.animationMatrix=matrix
                }
            }

            MotionEvent.ACTION_POINTER_UP ->{
                mode = MODE_NONE
            }
            MotionEvent.ACTION_UP->{
                mode= MODE_NONE
            }

        }
        binding.viewpager.isUserInputEnabled = mode == MODE_NONE
        this.view=view
    }
    private fun getMidPoint(event: MotionEvent,point: PointF){
        val x=event.getX(1)+event.getX(0)
        val y=event.getY(1)+event.getY(0)
        point.x = x/2 ; point.y = y/2
    }
    private fun distance(event: MotionEvent):Float{
        val dx=event.getX(1)-event.getX(0)
        val dy=event.getY(1)-event.getY(0)
        return sqrt(dx*dx+dy*dy)
    }
}