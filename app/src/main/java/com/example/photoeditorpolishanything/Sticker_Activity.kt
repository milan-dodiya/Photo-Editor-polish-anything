package com.example.photoeditorpolishanything

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.photoeditorpolishanything.Adapter.Sticker_Activity_Adapter
import com.example.photoeditorpolishanything.Adapter.Sticker_Activity_Adapter.Companion.imageUrlList
import com.example.photoeditorpolishanything.Adapter.Sticker_Group_Images_Adapter
import com.example.photoeditorpolishanything.Adapter.Sticker_Sub_Image_Adapter
import com.example.photoeditorpolishanything.Api.Dataas
import com.example.photoeditorpolishanything.Api.Groupas
import com.example.photoeditorpolishanything.Api.OkHttpHelpers
import com.example.photoeditorpolishanything.StickerView.CustomStickerView
import com.example.photoeditorpolishanything.StoreFragment.StickerBottomSheetDialogFragment
import com.example.photoeditorpolishanything.databinding.ActivityStickerBinding
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class Sticker_Activity : AppCompatActivity(), OnStickerClickListener , StickerClickListener {

    //    private lateinit var viewModel: StickerViewModel
    private lateinit var adapter: Sticker_Activity_Adapter
    private lateinit var adapters: Sticker_Sub_Image_Adapter
    private lateinit var adapteres: Sticker_Group_Images_Adapter
    private val groupsList = mutableListOf<Groupas>()
    private var data: List<String>? = null
    private lateinit var datas: String
    private lateinit var mainImageUrl: String
    var stickerLayout: FrameLayout? = null
    private var currentStickerView: CustomStickerView? = null // Track the currently displayed sticker
    private var stickerCounter = 0 // To track number of stickers
    private var isStickerRemoved = false
    private var dX = 0f
    private var dY = 0f

    companion object {
        lateinit var binding: ActivityStickerBinding

        private const val ARG_DATA = "data"
        private const val ARG_COLOR = "navigation_bar_color"
        private const val ARG_MAIN_IMAGE_URL = "main_image_url"
        private const val ARG_TEXT_CATEGORY = "https://s3.eu-north-1.amazonaws.com/photoeditorbeautycamera.com/photoeditor/sticker/"

        var context: Context? = null

        fun newInstance(data: List<String?>?, navigationBarColor: Int, mainImageUrl: String, textCategory: String?): StickerBottomSheetDialogFragment
        {
            val fragment = StickerBottomSheetDialogFragment()
            val args = Bundle()
            args.putStringArrayList(ARG_DATA, ArrayList(data))
            args.putInt(ARG_COLOR, navigationBarColor)
            args.putString(ARG_MAIN_IMAGE_URL, mainImageUrl)
            args.putString(ARG_TEXT_CATEGORY, textCategory)
            fragment.arguments = args
            return fragment
        }
    }

    fun updateStickersList(subImageUrls: List<String?>)
    {
        // Update the secondary RecyclerView adapter
        adapters.updateData(subImageUrls)
        adapters.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityStickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 21)
        {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.black)
        }

        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)

        val data: ArrayList<String>? = intent.getStringArrayListExtra(ARG_DATA)
        val textCategory: String? = intent.getStringExtra(ARG_TEXT_CATEGORY)
        val mainImageUrl: String? = intent.getStringExtra(ARG_MAIN_IMAGE_URL)


        initializeEmojiCompat()
        initView()
    }

    private fun initializeEmojiCompat()
    {
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView()
    {

//        stickerMainLayout = findViewById(R.id.sticker_main_layout)
//        stickerLayoutView = findViewById(R.id.stickerLayoutview)



//        binding.imgEditSelectImagess.setOnClickListener {
//            if(stickerLayoutView.visibility == View.GONE)
//            {
//                stickerLayoutView.visibility = View.VISIBLE
//            }
//            else
//            {
//                stickerLayoutView.visibility = View.GONE
//            }
//        }


//        binding.imgEditSelectImagess.setOnTouchListener { v, event ->
//            if (isStickerRemoved) return@setOnTouchListener true // Prevent interaction if removed
//
////            stickerImageView.setOnTouchListener {
////            }
//            when (event.action)
//            {
//                MotionEvent.ACTION_DOWN -> {
//                    dX = v.x - event.rawX
//                    dY = v.y - event.rawY
//                }
//
//                MotionEvent.ACTION_MOVE -> {
//                    v.animate()
//                        .x(event.rawX + dX)
//                        .y(event.rawY + dY)
//                        .setDuration(0)
//                        .start()
//                }
//            }
//            true
//        }




        val imageUriString = intent.getStringExtra("selected_image_uri")
        if (imageUriString != null)
        {
            val imageUri = Uri.parse(imageUriString)
            val imageView = findViewById<ImageView>(R.id.imgEditSelectImagess)

            if (imageView != null)
            {
                try
                {
                    Glide.with(this).load(imageUri).into(imageView)
                }
                catch (e: Exception)
                {
                    logErrorAndFinish("Glide error: ${e.message}")
                }
            }
            else
            {
                logErrorAndFinish("ImageView not found in layout")
            }
        }
        else
        {
            logErrorAndFinish("Image URI string is null")
        }

        val url = "https://s3.ap-south-1.amazonaws.com/photoeditorbeautycamera.app/photoeditor/stickers.json"
        var baseurl = "https://s3.ap-south-1.amazonaws.com/photoeditorbeautycamera.app/photoeditor/sticker/"

        OkHttpHelpers.fetchSticker(url) { stickerApi ->
            runOnUiThread {

                if (stickerApi != null)
                {
                    Log.e("StoreFragment", "Fetched data: ${stickerApi.data}")

                    stickerApi.data?.let {
                        populateGroupsList(it)
                        adapter.updateData(groupsList)
                    } ?: Log.e("StoreFragment", "Data is null")
                }
                else
                {
                    Log.e("StoreFragment", "Failed to fetch data")
                }
            }
        }

        adapter = Sticker_Activity_Adapter(this,groupsList, this, this)
        binding.rcvStiker.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        data = intent.getStringArrayListExtra(StickerBottomSheetDialogFragment.ARG_DATA)
        adapters = Sticker_Sub_Image_Adapter(data ?: emptyList())
        binding.rcvStiker.adapter = adapter

        val imageUrls = intent.getStringArrayListExtra("imageUrls") ?: arrayListOf()

        Log.e("imageUrlList", "initView: " + imageUrlList)
    }

    @SuppressLint("MissingInflatedId")
    private fun createNewSticker(bitmap: Bitmap) {
        // Inflate the sticker layout
        val stickerView = layoutInflater.inflate(R.layout.sticker_layout, null)

        // Get the ImageView from the inflated layout
        val stickerImageView = stickerView.findViewById<ImageView>(R.id.stickerImageView)

        // Set the sticker image in the ImageView
        stickerImageView.setImageBitmap(bitmap)

        // Set the layout parameters for the new sticker view
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )

        // Add the new sticker layout to the main layout
        val mainLayout = findViewById<FrameLayout>(R.id.stickerLayout)
        mainLayout.addView(stickerView, layoutParams)

        // Optionally, set initial position or size of the new sticker view
        stickerView.x = 100f // Example position, adjust as needed
        stickerView.y = 200f // Example position, adjust as needed
    }

    private fun populateGroupsList(data: Dataas)
    {
        val items = mutableListOf<Groupas>()

        // Use Kotlin reflection to iterate over the properties of the Dataas class
        data::class.memberProperties.forEach { property ->
            val prop = property as? KProperty1<Dataas, *>
            val value = prop?.get(data)

            // Check if the value is an instance of a class containing a Groupas
            if (value != null) {
                // Use reflection to find the Groupas property within the nested class
                val groupProperty = value::class.memberProperties
                    .firstOrNull { it.returnType.classifier == Groupas::class }
                        as? KProperty1<Any, Groupas>

                val nestedGroup = groupProperty?.get(value)

                if (nestedGroup != null)
                {
                    val categoryName = property.name.replace("_", " ").capitalizeWords()

                    nestedGroup.let {
                        if (it.subImageUrl != null || it.mainImageUrl != null) {
                            items.add(
                                Groupas(
                                    subImageUrl = it.subImageUrl,
                                    mainImageUrl = it.mainImageUrl,
                                    textCategory = categoryName,
                                    premium = it.premium
                                )
                            )
                        }
                    }
                }
            }
        }

        groupsList.clear()
        groupsList.addAll(items)
    }

    private fun logErrorAndFinish(message: String)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish() // Close the activity or handle it appropriately
    }


    private fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }

    // This method will be called when an item is clicked in the first RecyclerView
    override fun onStickerClicked(imageUrls: List<String>) {
        // Create a new adapter for the second RecyclerView to display the selected group of images
        val groupAdapter = Sticker_Group_Images_Adapter(imageUrls, this)

        // Set up the second RecyclerView with a GridLayoutManager (3 columns for images)
        binding.rcvStikers.layoutManager = GridLayoutManager(this, 4)
        binding.rcvStikers.adapter = groupAdapter

        // Make the second RecyclerView visible
        binding.rcvStikers.visibility = View.VISIBLE
    }

    // This method will be called when an item is clicked in the second RecyclerView
    override fun onStickerSelected(imageUrl: String)
    {
//      Remove the existing sticker view if it exists
        currentStickerView?.let {
            val stickerLayout = findViewById<FrameLayout>(R.id.stickerLayout)
            stickerLayout.removeView(it)
        }

        // Create a new instance of your CustomStickerView
        val newStickerView = CustomStickerView(this)

        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)

        newStickerView.layoutParams = layoutParams

        // Make the CustomStickerView visible if needed
        binding.stvStickerView.visibility = View.VISIBLE

        Glide.with(this)
            .load(imageUrl)
            .into(newStickerView.findViewById(R.id.stickerImageView))


        //      Add the new sticker view to your sticker layout
        val stickerLayout = findViewById<FrameLayout>(R.id.stvStickerView)
        stickerLayout.addView(newStickerView)
    }
}
