package com.example.photoeditorpolishanything.frame

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.View.OnDragListener
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.canhub.cropper.CropImageView
import com.example.photoeditorpolishanything.LayoutActivity
import com.example.photoeditorpolishanything.R
import com.example.photoeditorpolishanything.Utils.CollageModelClass.template.PhotoItem
import com.example.photoeditorpolishanything.Utils.ImageDecoder
import com.example.photoeditorpolishanything.Utils.ImageUtils
import com.example.photoeditorpolishanything.databinding.ActivityLayoutBinding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by vanhu_000 on 3/11/2016.
 */
class FramePhotoLayout(context: Context, private val mPhotoItems: List<PhotoItem>) :
    RelativeLayout(context) {

    private var mOnDragListener: OnDragListener = OnDragListener { v, event ->

        when (event.action) {
            DragEvent.ACTION_DRAG_ENTERED -> {
            }

            DragEvent.ACTION_DRAG_EXITED -> {
            }

            DragEvent.ACTION_DROP -> {
                var target: FrameImageView? = v as FrameImageView
                val selectedView = getSelectedFrameImageView(target!!, event)
                if (selectedView != null) {
                    target = selectedView
                    val dragged = event.localState as FrameImageView
                    var targetPath: String? = target.photoItem.imagePath
                    var draggedPath: String? = dragged.photoItem.imagePath
                    if (targetPath == null) targetPath = ""
                    if (draggedPath == null) draggedPath = ""
                    if (targetPath != draggedPath)
                        target.swapImage(dragged)
                }
            }
        }

        true
    }

    private lateinit var binding: ActivityLayoutBinding

    private val mItemImageViews: MutableList<FrameImageView>?
    private val activityContext = context as LayoutActivity
    private var mFrameImageView: FrameImageView? = null
    private var isClicked = false
    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0
    private var mOutputScaleRatio = 1f
    private var mQuickActionClickListener: OnQuickActionClickListener? = null

    private val isNotLargeThan1Gb: Boolean
        get() {
            val memoryInfo = ImageUtils.getMemoryInfo(context)
            return memoryInfo.totalMem > 0 && memoryInfo.totalMem / 1048576.0 <= 1024
        }

    interface OnQuickActionClickListener {
        fun onEditActionClick(v: FrameImageView)

        fun onChangeActionClick(v: FrameImageView)
    }

    //private val contract : ActivityResultLauncher<String>

    init {
        mItemImageViews = ArrayList()
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        /*contract = activityContext.registerForActivityResult(ActivityResultContracts.GetContent()) {
            val image = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
                MediaStore.Images.Media.getBitmap(activityContext.contentResolver, it)
            else {
                val source = android.graphics.ImageDecoder.createSource(activityContext.contentResolver, it!!)
                android.graphics.ImageDecoder.decodeBitmap(source)
            }
            mFrameImageView?.replaceImage(image, mFrameImageView)
        }*/
    }

    fun getFrameImageView(callback: (FrameImageView?) -> Unit) {
        callback.invoke(mFrameImageView)
    }

    private fun getSelectedFrameImageView(
        target: FrameImageView,
        event: DragEvent
    ): FrameImageView? {
        val dragged = event.localState as FrameImageView
        val leftMargin = (mViewWidth * target.photoItem.bound.left).toInt()
        val topMargin = (mViewHeight * target.photoItem.bound.top).toInt()
        val globalX = leftMargin + event.x
        val globalY = topMargin + event.y
        for (idx in mItemImageViews!!.indices.reversed())
        {
            val view = mItemImageViews[idx]
            val x = globalX - mViewWidth * view.photoItem.bound.left
            val y = globalY - mViewHeight * view.photoItem.bound.top
            if (view.isSelected(x, y))
            {
                return if (view === dragged)
                {
                    null
                }
                else
                {
                    view
                }
            }
        }
        return null
    }

    fun saveInstanceState(outState: Bundle) {
        if (mItemImageViews != null)
            for (view in mItemImageViews)
                view.saveInstanceState(outState)
    }

    fun restoreInstanceState(savedInstanceState: Bundle) {
        if (mItemImageViews != null)
            for (view in mItemImageViews)
                view.restoreInstanceState(savedInstanceState)
    }

    fun setQuickActionClickListener(quickActionClickListener: OnQuickActionClickListener) {
        mQuickActionClickListener = quickActionClickListener
    }

    @JvmOverloads
    fun build(
        viewWidth: Int,
        viewHeight: Int,
        outputScaleRatio: Float,
        space: Float = 0f,
        corner: Float = 0f
    ) {
        if (viewWidth < 1 || viewHeight < 1) {
            return
        }
        //add children views
        mViewWidth = viewWidth
        mViewHeight = viewHeight
        mOutputScaleRatio = outputScaleRatio
        mItemImageViews!!.clear()
        //A circle view always is on top
        if (mPhotoItems.size > 4 || isNotLargeThan1Gb) {
            ImageDecoder.SAMPLER_SIZE = 256
        } else {
            ImageDecoder.SAMPLER_SIZE = 512
        }
        for (item in mPhotoItems) {
            val imageView = addPhotoItemView(item, mOutputScaleRatio, space, corner)
            mItemImageViews.add(imageView)
        }
    }

    fun setSpace(space: Float, corner: Float) {
        for (img in mItemImageViews!!)
            img.setSpace(space, corner)
    }

    private fun addPhotoItemView(item: PhotoItem, outputScaleRatio: Float, space: Float, corner: Float): FrameImageView
    {
        val imageView = FrameImageView(context, item)
        val leftMargin = (mViewWidth * item.bound.left).toInt()
        val topMargin = (mViewHeight * item.bound.top).toInt()
        var frameWidth = 0
        var frameHeight = 0

        frameWidth = if (item.bound.right == 1f)
        {
            mViewWidth - leftMargin
        }
        else
        {
            (mViewWidth * item.bound.width() + 0.5f).toInt()
        }

        frameHeight = if (item.bound.bottom == 1f)
        {
            mViewHeight - topMargin
        }
        else
        {
            (mViewHeight * item.bound.height() + 0.5f).toInt()
        }

        imageView.init(frameWidth.toFloat(), frameHeight.toFloat(), outputScaleRatio, space, corner)
//        imageView.setOnImageClickListener(req)

        if (mPhotoItems.size > 1)
            imageView.setOnDragListener(mOnDragListener)

        val params = LayoutParams(frameWidth, frameHeight)
        params.leftMargin = leftMargin
        params.topMargin = topMargin
        imageView.originalLayoutParams = params
        addView(imageView, params)
        return imageView
    }

    @Throws(OutOfMemoryError::class)
    fun createImage(): Bitmap
    {
        try
        {
            val template = Bitmap.createBitmap(
                (mOutputScaleRatio * mViewWidth).toInt(),
                (mOutputScaleRatio * mViewHeight).toInt(),
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(template)
            for (view in mItemImageViews!!)
                if (view.image != null && !view.image!!.isRecycled) {
                    val left = (view.left * mOutputScaleRatio).toInt()
                    val top = (view.top * mOutputScaleRatio).toInt()
                    val width = (view.width * mOutputScaleRatio).toInt()
                    val height = (view.height * mOutputScaleRatio).toInt()
                    //draw image
                    canvas.saveLayer(
                        left.toFloat(),
                        top.toFloat(),
                        (left + width).toFloat(),
                        (top + height).toFloat(),
                        Paint(),
                        Canvas.ALL_SAVE_FLAG
                    )
                    canvas.translate(left.toFloat(), top.toFloat())
                    canvas.clipRect(0, 0, width, height)
                    view.drawOutputImage(canvas)
                    canvas.restore()
                }
            return template
        } catch (error: OutOfMemoryError) {
            throw error
        }
    }

    fun recycleImages() {
        for (view in mItemImageViews!!) {
            view.recycleImage()
        }
        System.gc()
    }


    private fun flip(src: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.preScale(-1.0f, 1.0f)

        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    private fun flip(src: Bitmap, type: Int): Bitmap {
        val matrix = Matrix()

        when (type) {
            LinearLayoutManager.VERTICAL -> {
                matrix.preScale(1.0f, -1.0f)
            }
            LinearLayoutManager.HORIZONTAL -> {
                matrix.preScale(-1.0f, 1.0f)
            }
            else -> {
                return src
            }
        }

        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    private fun rotate(src: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(-90f)
        val scaledBitmap = Bitmap.createScaledBitmap(src, src.width, src.height, true)
        return Bitmap.createBitmap(
            scaledBitmap,
            0,
            0,
            scaledBitmap.width,
            scaledBitmap.height,
            matrix,
            true
        )
    }

    companion object {
        private val TAG = FramePhotoLayout::class.java.simpleName
    }

//    override fun onResizeClick(position: Int) {
//        when (position) {
//            0 -> {
//                binding.cropImageView.setCropMode(CropImageView.CropMode.FIT_IMAGE)
//            }
//
//            1 -> {
//                binding.cropImageView.setCropMode(CropImageView.CropMode.FREE)
//            }
//
//            2 -> {
//
//                binding.cropImageView.setCustomRatio(1, 1)
//            }
//
//            3 -> {
//                binding.cropImageView.setCustomRatio(1, 2)
//            }
//
//            4 -> {
//                binding.cropImageView.setCustomRatio(2, 1)
//            }
//
//            5 -> {
//                binding.cropImageView.setCustomRatio(2, 3)
//            }
//
//            6 -> {
//                binding.cropImageView.setCustomRatio(3, 2)
//            }
//
//            7 -> {
//                binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_3_4)
//            }
//
//            8 -> {
//                binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3)
//            }
//
//            9 -> {
//                binding.cropImageView.setCustomRatio(4, 5)
//            }
//
//            10 -> {
//                binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_9_16)
//            }
//
//            11 -> {
//                binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9)
//            }
//        }
//    }

}