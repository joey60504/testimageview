package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    private var mList: List<Int> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
        return PagerViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bindData(mList[position])
        Log.d("kkk","123")
    }
    fun setList(list: List<Int>) {
        mList = list
    }
    override fun getItemCount(): Int {
        return mList.size
    }
    //	ViewHolder需要繼承RecycleView.ViewHolder
    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(i: Int) {
            itemView.findViewById<ImageView>(R.id.ImageViewShow).setImageResource(i)
        }
    }
}