package com.example.myapplication

import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ImageItemBinding

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    private var mList: List<Int> = ArrayList()
    var imageoriginalheight :Float  = 0f
    var imageoriginalwidth  :Float  = 0f
    var screenwidth         :Float  = 0f
    var screenheight        :Float  = 0f
    private var matrix: Matrix = Matrix()
    private lateinit var binding: ImageItemBinding
    class PagerViewHolder(val view :ImageItemBinding) : RecyclerView.ViewHolder(view.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        binding= ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PagerViewHolder(binding)
    }
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.view.ImageViewShow.setImageResource(mList[position])
//        Log.d("kkk",imageoriginalwidth.toString())
//        Log.d("kkk",imageoriginalheight.toString())
//        Log.d("kkk",screenwidth.toString())
//        Log.d("kkk",screenheight.toString())
    }
    fun setList(list: List<Int>) {
        mList = list
    }
    fun setImagewidthheight(imagewidth:Float,imageheight:Float,width:Float,height:Float){
        imageoriginalwidth = imagewidth
        imageoriginalheight = imageheight
        screenwidth = width
        screenheight = height
    }
    override fun getItemCount(): Int {
        return mList.size
    }
}