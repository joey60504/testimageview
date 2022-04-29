package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ImageItemBinding

class CycAdapter(private val Activity:ItemOnTouch): RecyclerView.Adapter<CycAdapter.ViewHolder>() {

     var imageList = ArrayList<Int>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
        field=value
        notifyDataSetChanged()
    }

    class ViewHolder(val view:ImageItemBinding):RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.view.ImageViewShow.apply {
            this.scaleType = ImageView.ScaleType.FIT_CENTER
            this.setImageBitmap(BitmapFactory.decodeResource(resources,imageList[position]))
        }


        holder.view.ImageViewShow.setOnTouchListener { view, motionEvent ->
            holder.view.ImageViewShow.scaleType = ImageView.ScaleType.MATRIX
            Activity.onTouchEvent(view,motionEvent)
            true
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    interface ItemOnTouch{
        fun onTouchEvent(view:View,event:MotionEvent)
    }

}