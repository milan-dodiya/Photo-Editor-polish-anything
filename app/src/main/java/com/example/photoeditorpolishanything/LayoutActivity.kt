package com.example.photoeditorpolishanything

import LocaleHelper
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.photoeditorpolishanything.Adapter.TabAdapter
import com.example.photoeditorpolishanything.Fragment.RatioFragment
import com.example.photoeditorpolishanything.databinding.ActivityLayoutBinding
import com.google.android.material.tabs.TabLayout

class LayoutActivity : BaseActivity(), RatioFragment.OnLayoutSelectedListener {

    lateinit var binding: ActivityLayoutBinding
    private lateinit var selectedImages: ArrayList<String>
    private var seletedLayout: Int = R.layout.collage2_1
    companion object{
        var imageCount : Int? = null
    }
    var imageUriString: String? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.black)

        }
        selectedImages = intent.getStringArrayListExtra("selectedImages") ?: arrayListOf()
        seletedLayout = intent.getIntExtra("selectedLayout", R.layout.default_layout)

        seletedLayout = getDefaultLayoutForImageCount(imageCount ?: 0)

        val selectedImageStrings = intent.getStringArrayListExtra("selectedImages") ?: arrayListOf()
        val selectedImageUris = selectedImageStrings.map { Uri.parse(it) }


        loadCollageLayouts(seletedLayout, selectedImageUris)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)

        initView()
    }

    override fun updateUIForSelectedLanguage() {
        super.updateUIForSelectedLanguage()
        val context = LocaleHelper.onAttach(this)
        val resources = context.resources

        binding.tabLayout.getTabAt(0)?.text = resources.getString(R.string.tab_ratio)
        binding.tabLayout.getTabAt(1)?.text = resources.getString(R.string.tab_layout)
        binding.tabLayout.getTabAt(2)?.text = resources.getString(R.string.tab_margin)
        binding.tabLayout.getTabAt(3)?.text = resources.getString(R.string.tab_border)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initView()
    {
        val ratioFragment = RatioFragment.newInstance(imageUriString)

        val imagesinTemplate = intent.getStringExtra("imagesinTemplate")
        imageCount = intent.getIntExtra("imageCount", 0)
//        imageUris = intent.getStringArrayListExtra("imageUris") ?: arrayListOf()
//        val selectedImages = intent.getStringExtra("selectedImages")
//        val selectedLayout = intent.getIntExtra("selectedLayout", R.layout.default_layout)

        selectedImages = intent.getStringArrayListExtra("selectedImages") ?: arrayListOf()
        seletedLayout = intent.getIntExtra("selectedLayout", R.layout.default_layout)

        Log.e("SelectedImagesAdapter", "initView: " + imageCount)
        // Log the values for debugging
//        Log.e("LayoutActivity", "Selected Layout: $layoutsResource")

//        loadCollageLayouts(seletedLayout)

        ratioFragment.listener = this

        binding.imgback.setOnClickListener {
            onBackPressed()
        }

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_ratio))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_layout))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_margin))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.tab_border))

        binding.viewpager.addOnPageChangeListener(object :
            TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout) {})

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewpager.currentItem = tab.position
                }

                // Show ImageView and hide CropImageView for `Layout`, `Margin`, and `Border` tabs
                if (tab!!.position == 1 || tab.position == 2 || tab.position == 3) {
                    binding.imglayoutSelectImages.visibility = View.VISIBLE
                    binding.imglayoutSelectImage.visibility = View.GONE
                } else {
                    binding.imglayoutSelectImages.visibility = View.GONE
                    binding.imglayoutSelectImage.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })


        imageUriString = intent.getStringExtra("selected_image_uri")
        val imageUris = intent.getStringArrayListExtra("imageUris")?.map { Uri.parse(it) }

    //    val view = layoutInflater.inflate(selectedLayout!!, null)

//         If image URIs are present, arrange them in the layout
//        imageUris?.let {
//            arrangeImagesInLayout(it, view)
//        }

//        // Inflate the selected layout dynamically
//        val layoutInflater = LayoutInflater.from(this)
//        val dynamicLayout = layoutInflater.inflate(layoutsResource!!, binding.imglayoutSelectImages, false)
//
//        // Add the dynamic layout to a container in your XML
//        binding.imglayoutSelectImages.removeAllViews()
//        binding.imglayoutSelectImages.addView(dynamicLayout)
////
//        // Arrange images in the layout
//        imageUris?.let {
//            arrangeImagesInLayout(it, dynamicLayout)
//        }

        loadImageIntoCropView()
        val myAdapter = TabAdapter(supportFragmentManager, imageUriString.toString(), imageCount!!)
        binding.viewpager.adapter = myAdapter

    }



    override fun onLayoutSelected(aspectX: Int, aspectY: Int) {
        if (aspectX == 0 && aspectY == 0) {
            // Free cropping
            binding.imglayoutSelectImage.setFixedAspectRatio(false)
        } else {
            binding.imglayoutSelectImage.setAspectRatio(aspectX, aspectY)
        }
        Log.e("LayoutActivity", "Aspect Ratio Set: $aspectX:$aspectY")
    }

    private fun loadImageIntoCropView()
    {
        imageUriString?.let { uri ->
            Glide.with(this)
                .asBitmap()
                .load(imageUriString)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        // Set the Bitmap into CropImageView
                        binding.imglayoutSelectImage.setImageBitmap(resource)
//                        binding.imglayoutSelect.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Handle any cleanup here
                    }
                })
        }
    }

    private fun loadImage(uri: String?, width: Int, height: Int) {
        uri?.let {
            Glide.with(this)
                .asBitmap()
                .override(width, height)
                .load(it)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        binding.imglayoutSelectImage.setImageBitmap(resource)
//                        binding.imglayoutSelect.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Placeholder cleanup if needed
                    }
                })
        }
    }


//    private fun loadCollageLayouts(seletedLayout: Int) {
//        val stub = layoutInflater.inflate(seletedLayout, null)
//        // Retrieve the image URIs passed through the intent
//        val imageUris = intent.getStringArrayListExtra("imageUris")?.map { Uri.parse(it) }
//
//
//        binding.imglayoutSelectImages.removeAllViews()
//        binding.imglayoutSelectImages.addView(stub)
//
//
//
//        when(seletedLayout){
//            R.layout.default_layout -> {
//                binding.imglayoutSelectImages.visibility = View.GONE
//                binding.imglayoutSelectImage.visibility = View.VISIBLE
//
//                Log.e("loadCollageLayouts", "loadCollageLayouts: "+imageUris )
//                imageUris?.let {
//                    arrangeImagesInLayout(it, stub)
//                }
//            }
//
//        }
//
//    }


    // New method to update layout
    fun updateCollageLayout(newLayout: Int, selectedImageUrisAsStrings: List<String>) {
        seletedLayout = newLayout
        val selectedImageUris = selectedImageUrisAsStrings.map { Uri.parse(it) }
        loadCollageLayouts(seletedLayout, selectedImageUris)
    }



//    private fun loadCollageLayouts(selectedLayout: Int, selectedImageUris: List<Uri>) {
//        // Inflate the selected layout
//        val stub = layoutInflater.inflate(selectedLayout, null)
//
//        // Clear any existing views
//        binding.imglayoutSelectImages.removeAllViews()
//
//        // Add the inflated layout
//        binding.imglayoutSelectImages.addView(stub)
//
//        // Arrange the selected images in the layout
//        arrangeImagesInLayout(selectedImageUris, stub)
//    }


    private fun loadCollageLayouts(seletedLayout: Int, selectedImageUris: List<Uri>) {
        try {
            // Inflate the selected layout
            val stub = layoutInflater.inflate(seletedLayout, null)

            // Clear any existing views
            binding.imglayoutSelectImages.removeAllViews()

            // Add the inflated layout
            binding.imglayoutSelectImages.addView(stub)

            // Arrange the selected images in the layout
            arrangeImagesInLayout(selectedImageUris, stub)

            // Make sure the layout is visible
            binding.imglayoutSelectImages.visibility = View.VISIBLE
            binding.imglayoutSelectImage.visibility = View.GONE

        }
        catch (e: Exception)
        {
            Log.e("ImageCollageActivity", "Error loading collage layout: ${e.message}")
        }
    }



//    private fun arrangeImagesInLayout(imageUris: List<Uri>, view: View) {
//        // List of possible ImageViews based on the current layout
//        val imageViews = mutableListOf<ImageView?>()
//
//        // Add ImageViews based on the current layout (check your layout for the correct IDs)
//        val imageView1 = view.findViewById<ImageView>(R.id.imageView1)
//        val imageView2 = view.findViewById<ImageView>(R.id.imageView2)
//        val imageView3 = view.findViewById<ImageView>(R.id.imageView3)
//        val imageView4 = view.findViewById<ImageView>(R.id.imageView4)
//        val imageView5 = view.findViewById<ImageView>(R.id.imageView5)
//        val imageView6 = view.findViewById<ImageView>(R.id.imageView6)
//        val imageView7 = view.findViewById<ImageView>(R.id.imageView7)
//        val imageView8 = view.findViewById<ImageView>(R.id.imageView8)
//        val imageView9 = view.findViewById<ImageView>(R.id.imageView9)
//        val imageView10 = view.findViewById<ImageView>(R.id.imageView10)
//
//        if (imageView1 != null) imageViews.add(imageView1)
//        if (imageView2 != null) imageViews.add(imageView2)
//        if (imageView3 != null) imageViews.add(imageView3)
//        if (imageView4 != null) imageViews.add(imageView4)
//        if (imageView5 != null) imageViews.add(imageView5)
//        if (imageView6 != null) imageViews.add(imageView6)
//        if (imageView7 != null) imageViews.add(imageView7)
//        if (imageView8 != null) imageViews.add(imageView8)
//        if (imageView9 != null) imageViews.add(imageView9)
//        if (imageView10 != null) imageViews.add(imageView10)
//
//        // Log the found ImageViews for debugging
//        Log.e("TAG", "Found ImageViews: $imageViews")
//
//        // Load the images into the ImageViews
//        imageViews.forEachIndexed { index, imageView ->
//            if (imageView != null && index < imageUris.size) {
//                Glide.with(this).load(imageUris[index]).into(imageView)
//                Log.e("ImageViewindex", "ImageView at index  is null or no image to load!" + imageUris[index])
//            }
//            else
//            {
//                Log.e("ImageViewindex", "ImageView at index $index is null or no image to load!")
//            }
//        }
//    }

    private fun arrangeImagesInLayout(imageUris: List<Uri>, view: View) {
        val imageViews = listOf(
            view.findViewById<ImageView>(R.id.imageView1),
            view.findViewById<ImageView>(R.id.imageView2),
            view.findViewById<ImageView>(R.id.imageView3),
            view.findViewById<ImageView>(R.id.imageView4),
            view.findViewById<ImageView>(R.id.imageView5),
            view.findViewById<ImageView>(R.id.imageView6),
            view.findViewById<ImageView>(R.id.imageView7),
            view.findViewById<ImageView>(R.id.imageView8),
            view.findViewById<ImageView>(R.id.imageView9),
            view.findViewById<ImageView>(R.id.imageView10)
        )

        imageViews.forEachIndexed { index, imageView ->
            if (imageView != null && index < imageUris.size) {
                Glide.with(this).load(imageUris[index])
                    .centerCrop()
                    .into(imageView)
            }
        }
    }

    private fun getDefaultLayoutForImageCount(count: Int): Int {
        return when (count) {
            2 -> R.layout.collage2_1
            3 -> R.layout.collage3_1
            4 -> R.layout.collage4_1
            5 -> R.layout.collage5_1
            6 -> R.layout.collage6_1
            7 -> R.layout.collage7_1
            8 -> R.layout.collage8_1
            9 -> R.layout.collage9_1
            10 -> R.layout.collage10_1
            else -> R.layout.collage2_1 // Default fallback layout
        }
    }
}
