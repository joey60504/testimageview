package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.databinding.ImageItemBinding

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    private var mList: List<Int> = ArrayList()
    var imageoriginalheight :Float  = 0f
    set(value) {
        field=value
        notifyDataSetChanged()
    }
    var imageoriginalwidth  :Float  = 0f
    set(value) {
        field=value
        notifyDataSetChanged()
    }
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
        Log.d("kk1",position.toString())

//        val Bitmap: Bitmap = BitmapFactory.decodeResource(resources,mList[position])
//        imageoriginalheight = Bitmap.height.toFloat()
//        imageoriginalwidth = Bitmap.width.toFloat()
//        Log.d("kkk",imageoriginalheight.toString())
//        Log.d("kkk",screenwidth.toString())
//        Log.d("kkk",screenheight.toString())
    }

    fun setList(list: List<Int>) {
        mList = list
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}