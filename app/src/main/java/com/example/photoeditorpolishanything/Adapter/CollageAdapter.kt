package com.example.photoeditorpolishanything.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeditorpolishanything.R
import com.example.photoeditorpolishanything.Utils.CollageModelClass.TemplateItem
import com.example.photoeditorpolishanything.databinding.ItemCollageTemplateBinding

class CollageAdapter(private val templates: List<TemplateItem>) : RecyclerView.Adapter<CollageAdapter.CollageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_collage, parent, false)
        return CollageViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollageViewHolder, position: Int) {
        val template = templates[position]
        holder.bind(template)
    }

    override fun getItemCount(): Int {
        return templates.size
    }

    class CollageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(template: TemplateItem) {
            // Assuming template contains the image path or a drawable resource
            // Load the image associated with this template
            val collageImage = getCollageImage(template) // Implement this based on how the collage is stored
            imageView.setImageBitmap(collageImage)
        }

        private fun getCollageImage(template: TemplateItem): Bitmap {
            // Use your logic to load the collage image, possibly by accessing
            // template's image path or resources.
            return BitmapFactory.decodeResource(itemView.context.resources,R.drawable.cancel) // Example
        }
    }
}
