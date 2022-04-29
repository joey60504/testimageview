package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.example.myapplication.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val imageList = arrayListOf(
        R.drawable.cat,
        R.drawable.n2,
        R.drawable.n3,
        R.drawable.n4,
        R.drawable.building)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageViewImage.setImageResource(imageList[0])
        binding.imageViewImage.setOnClickListener{
            val intent = Intent(this@MainActivity, SharedView::class.java)
//            val bundle = Bundle()
//            bundle.putIntegerArrayList("imageList",imageList)
//            intent.putExtra("image",bundle)
            intent.putExtra("imageList",imageList)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity, binding.imageViewImage, ViewCompat.getTransitionName(binding.imageViewImage)!!
            )
            startActivity(intent, options.toBundle())
        }
        binding.textView4.setOnClickListener{
            val intent = Intent(this@MainActivity, imageswitcher::class.java)
//            val bundle = Bundle()
//            bundle.putIntegerArrayList("imageList",imageList)
//            intent.putExtra("image",bundle)
            intent.putExtra("imageList",imageList)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity, binding.imageViewImage, ViewCompat.getTransitionName(binding.imageViewImage)!!
            )
            startActivity(intent, options.toBundle())
        }
        binding.textView5.setOnClickListener{
            val intent = Intent(this@MainActivity, sharedView2::class.java)
//            val bundle = Bundle()
//            bundle.putIntegerArrayList("imageList",imageList)
//            intent.putExtra("image",bundle)
            intent.putExtra("imageList",imageList)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity, binding.imageViewImage, ViewCompat.getTransitionName(binding.imageViewImage)!!
            )
            startActivity(intent, options.toBundle())
        }
        binding.textView6.setOnClickListener {
            Intent(this,CycTest::class.java).apply {
                putExtra("imageList",imageList)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@MainActivity, binding.imageViewImage, ViewCompat.getTransitionName(binding.imageViewImage)!!
                )
                startActivity(this,options.toBundle())
            }
        }
    }
}
