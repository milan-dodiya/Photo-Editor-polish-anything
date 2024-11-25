package com.example.photoeditorpolishanything.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeditorpolishanything.R

class LayoutAdapter(
    private val layouts: List<Int>,
    private val icons: List<Int>,
    private val onLayoutClick: (Int) -> Unit,
    var imageCount: Int

//    private val listener: OnLayoutSelectedListener // Listener for layout selection
// List of icon resource IDs
) : RecyclerView.Adapter<LayoutAdapter.LayoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_layout_preview, parent, false)
        return LayoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: LayoutViewHolder, position: Int) {
        val layoutResource = layouts[position]
        holder.bind(layoutResource)
    }

    override fun getItemCount(): Int = layouts.size


    inner class LayoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(layoutResource: Int) {
            // Dynamically update ImageViews in preview layout
            val imageView1 = itemView.findViewById<ImageView>(R.id.imageView1)

            // Set sample images or placeholder images for preview
            imageView1.setImageResource(icons[position])

//            // Handle layout selection when clicked
//            itemView.setOnClickListener {
//                Log.e("layoutResourcee", "bind: "+layoutResource)
//                onLayoutClick(layoutResource)
//                onLayoutClick(layouts[position])
//                getLayoutsForImageCount(imageCount)
//                Log.e("onLayoutClick", "bind: "+onLayoutClick(layoutResource))
////               listener.onLayout
//            //               Selected(layoutResource)
//            }

            itemView.setOnClickListener {
                onLayoutClick(layoutResource)


                getLayoutsForImageCount(imageCount)
            }
        }
    }

    private fun getLayoutsForImageCount(imageCount: Int): List<Int> {
        return when (imageCount) {
            2 -> listOf(
                R.layout.collage2_1,
                R.layout.collage2_2,
                R.layout.collage2_3,
                R.layout.collage2_4,
                R.layout.collage2_8,
                R.layout.collage2_9,
                R.layout.collage2_7,
                R.layout.collage2_10,
                R.layout.collage2_5,
                R.layout.collage2_6
            )
            3 -> listOf(
                R.layout.collage3_1,
                R.layout.collage3_2,
                R.layout.collage3_3,
                R.layout.collage3_4,
                R.layout.collage3_5,
                R.layout.collage3_6,
                R.layout.collage3_7,
                R.layout.collage3_8,
                R.layout.collage3_9,
                R.layout.collage3_10
            )
            4 -> listOf(
                R.layout.collage4_1,
                R.layout.collage4_2,
                R.layout.collage4_3,
                R.layout.collage4_4,
                R.layout.collage4_5,
                R.layout.collage4_6,
                R.layout.collage4_7,
                R.layout.collage4_8,
                R.layout.collage4_9,
                R.layout.collage4_10
            )
            5 -> listOf(
                R.layout.collage5_1,
                R.layout.collage5_2,
                R.layout.collage5_3,
                R.layout.collage5_4,
                R.layout.collage5_5,
                R.layout.collage5_6,
                R.layout.collage5_7,
                R.layout.collage5_8,
                R.layout.collage5_9,
                R.layout.collage5_10
            )
            6 -> listOf(
                R.layout.collage6_1,
                R.layout.collage6_2,
                R.layout.collage6_3,
                R.layout.collage6_4,
                R.layout.collage6_5,
                R.layout.collage6_6,
                R.layout.collage6_7,
                R.layout.collage6_8,
                R.layout.collage6_9,
                R.layout.collage6_10
            )
            7 -> listOf(
                R.layout.collage7_1,
                R.layout.collage7_2,
                R.layout.collage7_3,
                R.layout.collage7_4,
                R.layout.collage7_5,
                R.layout.collage7_6,
                R.layout.collage7_7,
                R.layout.collage7_8,
                R.layout.collage7_9,
                R.layout.collage7_10
            )
            8 -> listOf(
                R.layout.collage8_1,
                R.layout.collage8_2,
                R.layout.collage8_3,
                R.layout.collage8_4,
                R.layout.collage8_5,
                R.layout.collage8_6,
                R.layout.collage8_7,
                R.layout.collage8_8,
                R.layout.collage8_9,
                R.layout.collage8_10
            )
            9 -> listOf(
                R.layout.collage9_1,
                R.layout.collage9_2,
                R.layout.collage9_3,
                R.layout.collage9_4,
                R.layout.collage9_5,
                R.layout.collage9_6,
                R.layout.collage9_7,
                R.layout.collage9_8,
                R.layout.collage9_9,
                R.layout.collage9_10
            )
            10 -> listOf(
                R.layout.collage10_1,
                R.layout.collage10_2,
                R.layout.collage10_3,
                R.layout.collage10_4,
                R.layout.collage10_5,
                R.layout.collage10_6,
                R.layout.collage10_7,
                R.layout.collage10_8,
                R.layout.collage10_9,
                R.layout.collage10_10
            )
            else -> emptyList()
        }
    }
}