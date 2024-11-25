package com.example.photoeditorpolishanything.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeditorpolishanything.R
import com.example.photoeditorpolishanything.Utils.CollageModelClass.TemplateItem
import com.example.photoeditorpolishanything.Utils.PhotoUtils

class FrameAdapter(context: Context , imageList : List<TemplateItem>) : RecyclerView.Adapter<FrameAdapter.FrameHolder>() {

    var mContext = context
    var mImages = imageList

//    var frameListener: OnFrameClickListener = frameClickListener

    interface OnFrameClickListener
    {
        fun onFrameClick(templateItem: TemplateItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameHolder
    {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_frame, parent, false)

        return FrameHolder(view)
    }

    override fun getItemCount(): Int
    {
        return mImages.size
    }

    override fun onBindViewHolder(holder: FrameHolder, @SuppressLint("RecyclerView") position: Int)
    {
//        holder.img_frame.setImageResource(mImages[position])
        PhotoUtils.loadImageWithGlide(mContext, holder.img_frame, mImages[position].preview)

        if (mImages[position].isSelected)
        {
            holder.ll_itemframe.setBackgroundResource(R.drawable.toggle_selected)
        }
//        else
//        {
////            holder.ll_itemframe.setBackgroundColor(mContext.resources.getColor(R.color.white))
//        }

//        holder.img_frame.setOnClickListener(object : View.OnClickListener {
////          override fun onClick(v: View?) {
////              frameListener.onFrameClick(mImages[position])
////          }
//        })
    }

    class FrameHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var img_frame: ImageView = itemView.findViewById(R.id.img_frame)

        var ll_itemframe: LinearLayout = itemView.findViewById(R.id.ll_itemframe)
    }
}