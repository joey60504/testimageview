package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ImageItemBinding
import java.io.InputStream
import java.net.URL

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
        //using url
//        holder.view.ImageViewShow.scaleType = ImageView.ScaleType.FIT_CENTER
//        DownloadImageTask(holder.view.ImageViewShow).execute(imageList[position])

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
    //using url
//    private class DownloadImageTask(bmImage: ImageView) :
//        AsyncTask<String?, Void?, Bitmap?>() {
//        var bmImage: ImageView = bmImage
//        override fun doInBackground(vararg urls: String?): Bitmap? {
//            val urldisplay = urls[0]
//            var mIcon11: Bitmap? = null
//            try {
//                val `in`: InputStream = URL(urldisplay).openStream()
//                mIcon11 = BitmapFactory.decodeStream(`in`)
//            } catch (e: Exception) {
//                Log.e("Error", e.message!!)
//                e.printStackTrace()
//            }
//            return mIcon11
//        }
//
//        override fun onPostExecute(result: Bitmap?) {
//            bmImage.setImageBitmap(result)
//        }
//
//    }
}