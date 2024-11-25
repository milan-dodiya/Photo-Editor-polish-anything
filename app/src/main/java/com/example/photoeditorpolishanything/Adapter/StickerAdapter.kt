//package com.example.photoeditorpolishanything.Adapter
//
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.fragment.app.FragmentActivity
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.photoeditorpolishanything.Api.Groupas
//import com.example.photoeditorpolishanything.R
//import com.example.photoeditorpolishanything.StoreFragment.StickerBottomSheetDialogFragment
//
//
//class StickerAdapter(var activity: Activity, var data: MutableList<Groupas>) :
//    RecyclerView.Adapter<StickerAdapter.StickerViewHolder>() {
//
//    private val baseUrl = "https://s3.ap-south-1.amazonaws.com/photoeditorbeautycamera.app/photoeditor/sticker/"
//
//    fun updateData(newData: MutableList<Groupas>) {
//        data = newData
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
//        val itemView =
//            LayoutInflater.from(parent.context).inflate(R.layout.item_sticker, parent, false)
//        Log.e("StoreFragment", "onCreateViewHolder: " + itemView)
//        return StickerViewHolder(itemView)
//    }
//
//    @SuppressLint("SetTextI18n")
//    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
//        val stickerUrl = data[position]
//
//        val item = data.getOrNull(position) ?: return
//
//        val imageUrlList = item.mainImageUrl
//        if (imageUrlList.isNullOrEmpty()) {
//            Log.e("StickerAdapter", "No image URLs available for position $position")
//            holder.imageView.setImageResource(R.drawable.cross) // Placeholder image
//            return
//        }
//
//        val imageUrl = stickerUrl.mainImageUrl!!.firstOrNull() // Take the first URL from the list if available
//
//        if (imageUrl != null)
//        {
//            // Load image using Glide
//            Glide.with(holder.itemView.context)
//                .load(baseUrl + imageUrl)
//                .fitCenter()
//                .centerCrop()
//                .into(holder.imageView)
//
//            holder.itemView.setOnClickListener {
//                val activity = it.context as? FragmentActivity
//                if (activity != null) {
//                    val bottomSheetDialogFragment = StickerBottomSheetDialogFragment.newInstance(
//                        stickerUrl.subImageUrl,
//                        R.color.black,
//                        imageUrl,
//                        stickerUrl.textCategory
//                    )
//                    bottomSheetDialogFragment.show(
//                        activity.supportFragmentManager,
//                        bottomSheetDialogFragment.tag
//                    )
//                }
//            }
//        }
//        else
//        {
//            // Handle case where imageUrl is null
//            Log.e("ImageLoading", "No image URL available for position $position")
//        }
//
////        holder.txtPremiume.text = when (stickerUrl.premium.toString())
////        {
////            "paid" -> context!!.getString(R.string.paid_sticker)
////            "Ad" -> context!!.getString(R.string.ads_sticker)
////            else -> context!!.getString(R.string.free_sticker)
////        }
//
//        holder.txtPremiume.text = item.premium.toString()
//
//        if (stickerUrl.premium.toString() == "[paid]")
//        {
//            holder.txtPremiume.text = "Paid"
//            Log.e("premium", "onBindViewHolder: " + "Paid")
//        }
//        else if (stickerUrl.premium.toString() == "[Ad]")
//        {
//            holder.txtPremiume.text = "Ads"
//            Log.e("premium", "onBindViewHolder: " + "Ads")
//        }
//        else
//        {
//            if (item.premium.toString() == "[free]")
//            {
//                holder.txtPremiume.text = "Free"
//                Log.e("premium", "onBindViewHolder: " + "Free")
//            }
//        }
//
//        holder.textView.text = stickerUrl.textCategory
//        holder.textNumber.text = stickerUrl.subImageUrl!!.size.toString() + " Stickers"
//    }
//
//    override fun getItemCount(): Int {
//        return data.size
//    }
//
//    class StickerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imageView: ImageView = itemView.findViewById(R.id.imgSticker)
//        val textView: TextView = itemView.findViewById(R.id.txtName)
//        val textNumber: TextView = itemView.findViewById(R.id.txtNumber)
//        val txtPremiume: TextView = itemView.findViewById(R.id.txtPremiume)
//    }
//
//}


package com.example.photoeditorpolishanything.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photoeditorpolishanything.Api.Groupas
import com.example.photoeditorpolishanything.R
import com.example.photoeditorpolishanything.StoreFragment.StickerBottomSheetDialogFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class StickerAdapter(var activity: Activity, var data: MutableList<Groupas>) :
    RecyclerView.Adapter<StickerAdapter.StickerViewHolder>() {

    private val baseUrl =
        "https://s3.ap-south-1.amazonaws.com/photoeditorbeautycamera.app/photoeditor/sticker/"
    private var rewardedAd: RewardedAd? = null

    init {
        loadRewardedAd()
    }

    fun updateData(newData: MutableList<Groupas>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_sticker, parent, false)
        return StickerViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        val stickerUrl = data[position]
        val item = data.getOrNull(position) ?: return

        val imageUrlList = item.mainImageUrl
        if (imageUrlList.isNullOrEmpty()) {
            holder.imageView.setImageResource(R.drawable.cross) // Placeholder image
            return
        }

        val imageUrl = stickerUrl.mainImageUrl!!.firstOrNull() // Take the first URL from the list
        if (imageUrl != null) {
            Glide.with(holder.itemView.context)
                .load(baseUrl + imageUrl)
                .fitCenter()
                .centerCrop()
                .into(holder.imageView)

            holder.itemView.setOnClickListener {
                val activity = it.context as? FragmentActivity
                if (activity != null) {
                    val bottomSheetDialogFragment = StickerBottomSheetDialogFragment.newInstance(
                        stickerUrl.subImageUrl,
                        R.color.black,
                        imageUrl,
                        stickerUrl.textCategory
                    )
                    bottomSheetDialogFragment.show(
                        activity.supportFragmentManager,
                        bottomSheetDialogFragment.tag
                    )
                }
            }
        } else {
            Log.e("ImageLoading", "No image URL available for position $position")
        }

        // Handle premium text
//        holder.txtPremiume.text = item.premium.toString()
//        when (stickerUrl.premium.toString()) {
//            "[paid]" -> holder.txtPremiume.text = "Paid"
//            "[Ad]" -> holder.txtPremiume.text = "Ads"
//            "[free]" -> holder.txtPremiume.text = "Free"
//        }

        if (stickerUrl.premium.toString() == "[paid]")
        {
            holder.txtPremiume.text = "Paid"
            Log.e("premium", "onBindViewHolder: " + "Paid")
        }
        else if (stickerUrl.premium.toString() == "[Ad]")
        {
            loadRewardedAd()

            holder.txtPremiume.text = "Ads"
            holder.lnrPremiume.setOnClickListener {
                if (rewardedAd != null)
                {
                    rewardedAd?.show(activity) { rewardItem: RewardItem ->
                        val rewardAmount = rewardItem.amount
                        val rewardType = rewardItem.type
                        Toast.makeText(activity, "Reward Earned: $rewardAmount $rewardType", Toast.LENGTH_SHORT).show()
                        showRewardedAd()
                    }
                }
                else
                {
                    Toast.makeText(activity, "Ad not ready yet!", Toast.LENGTH_SHORT).show()
                    loadRewardedAd() // Load again
                }
            }
            Log.e("premium", "onBindViewHolder: " + "Ads")
        }
        else
        {
            if (item.premium.toString() == "[free]")
            {
                holder.txtPremiume.text = "Free"
                Log.e("premium", "onBindViewHolder: " + "Free")
            }
        }

        holder.textView.text = stickerUrl.textCategory
        holder.textNumber.text = "${stickerUrl.subImageUrl!!.size} Stickers"

        // Handle txtPremiume click for Rewarded Ad

//        loadRewardedAd()
//        holder.itemView.setOnClickListener {
//
//            if (rewardedAd != null) {
//                rewardedAd?.show(activity) { rewardItem: RewardItem ->
//                    val rewardAmount = rewardItem.amount
//                    val rewardType = rewardItem.type
//                    Toast.makeText(activity, "Reward Earned: $rewardAmount $rewardType", Toast.LENGTH_SHORT).show()
//                    showRewardedAd()
//                }
//            } else {
//                Toast.makeText(activity, "Ad not ready yet!", Toast.LENGTH_SHORT).show()
//                loadRewardedAd() // Load again
//            }
//        }

//            if (stickerUrl.premium.toString() == "[Ad]") {
//                showRewardedAd(holder.itemView.context as Activity)
//            } else {
//                Toast.makeText(activity, "This item is not ad-supported", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    // ViewHolder
    class StickerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgSticker)
        val textView: TextView = itemView.findViewById(R.id.txtName)
        val textNumber: TextView = itemView.findViewById(R.id.txtNumber)
        val txtPremiume: TextView = itemView.findViewById(R.id.txtPremiume)
        val lnrPremiume: LinearLayout = itemView.findViewById(R.id.lnrPremiume)
    }

    // Load rewarded ad
    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            activity, // Context or activity
            "ca-app-pub-3940256099942544/5224354917", // Test Rewarded Ad Unit ID
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    Log.d("RewardedAd", "Ad loaded successfully")
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                    Log.e("RewardedAd", "Ad failed to load: ${adError.message}")
                }
            }
        )
    }

    // Show rewarded ad
    private fun showRewardedAd() {
        if (rewardedAd != null) {
            rewardedAd?.show(activity) { rewardItem: RewardItem ->
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
                Toast.makeText(
                    activity,
                    "You earned $rewardAmount $rewardType!",
                    Toast.LENGTH_SHORT
                ).show()

                // Add logic to unlock premium content or other rewards here
            }
        } else {
            Toast.makeText(activity, "Ad is not ready yet!", Toast.LENGTH_SHORT).show()
            loadRewardedAd() // Load ad again if needed
        }
    }

}