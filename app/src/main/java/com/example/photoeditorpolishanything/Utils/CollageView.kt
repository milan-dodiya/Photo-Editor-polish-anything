//package com.example.photoeditorpolishanything.Utils
//
//import android.content.Context
//import android.graphics.Color
//import android.graphics.PointF
//import android.view.View
//import android.widget.FrameLayout
//import android.widget.ImageView
//import android.widget.LinearLayout
//import com.example.photoeditorpolishanything.Utils.CollageModelClass.TemplateItem
//
//class CollageView : LinearLayout {
//    constructor(context: Context) : super(context) {
//        orientation = VERTICAL
//    }
//
//    fun addCollageItem(item: TemplateItem) {
//        item.photoItemList.forEach { photoItem ->
//            val photoFrame = FrameLayout(context)
//            val imageView = ImageView(context)
//            imageView.layoutParams = LayoutParams(0, 0).apply {
//                weight = 1f
//            }
//
//            // Adjust image frame size based on photoItem.bound
//            imageView.layoutParams.width = (photoItem.bound.width() * 100).toInt()
//            imageView.layoutParams.height = (photoItem.bound.height() * 100).toInt()
//
//            // Add the image to the collage
//            photoFrame.addView(imageView)
//
//            // Optionally, add clear area if exists
//            photoItem.clearAreaPoints?.let {
//                val maskView = createClearAreaMask(it)
//                photoFrame.addView(maskView)
//            }
//
//            addView(photoFrame)
//        }
//    }
//
//    private fun createClearAreaMask(clearAreaPoints: List<PointF>): View {
//        // Create a custom view that will mask the defined clear area
//        val maskView = View(context)
//        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//        maskView.layoutParams = params
//        maskView.setBackgroundColor(Color.argb(100, 0, 0, 0)) // Semi-transparent mask
//        return maskView
//    }
//}
