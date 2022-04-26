package com.example.myapplication

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ImageItemBinding
import kotlin.math.roundToInt

class ViewPagerAdapter(private val mList: ArrayList<Int>,private val itemListener:OnItemTouchListener) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    private lateinit var binding: ImageItemBinding
    class PagerViewHolder(val view :ImageItemBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        binding= ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PagerViewHolder(binding)
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
//        取得螢幕寬度
        val displayMetrics=Resources.getSystem().displayMetrics
//        取得原始圖檔
        val originBitmap=BitmapFactory.decodeResource(holder.view.ImageViewShow.resources,mList[position])

//        要設定的圖寬
        val imgWidth=displayMetrics.widthPixels
//        要設定的圖高
        val imgHeight=(originBitmap.height*(imgWidth.toFloat()/originBitmap.width.toFloat())).roundToInt()

//        創建新圖用於resize
        val newBitmap=Bitmap.createScaledBitmap(originBitmap,imgWidth,imgHeight,true)

//        設定imageView的顯示大小
        holder.view.ImageViewShow.layoutParams.width=imgWidth
        holder.view.ImageViewShow.layoutParams.height=imgHeight

//        注入bitmap
        holder.view.ImageViewShow.setImageBitmap(newBitmap)
        holder.view.ImageViewShow.setOnTouchListener { v, event ->
            itemListener.OnItemTouch(position,event,holder.view.ImageViewShow)
            true
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
    interface OnItemTouchListener {
        fun OnItemTouch(postition: Int, v: MotionEvent, imageViewShow: ImageView)
    }
}