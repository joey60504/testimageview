package com.example.myapplication

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.databinding.ImageItemBinding
import kotlin.math.min
import kotlin.math.roundToInt

class ViewPagerAdapter(private val mList:ArrayList<Int>) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    private var matrix: Matrix = Matrix()
    private lateinit var binding: ImageItemBinding
    class PagerViewHolder(val view :ImageItemBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        binding= ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PagerViewHolder(binding)
    }
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
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}