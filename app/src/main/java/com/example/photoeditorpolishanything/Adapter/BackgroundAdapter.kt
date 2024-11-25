package com.example.photoeditorpolishanything.Adapter

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
import com.example.photoeditorpolishanything.Api.Groups
import com.example.photoeditorpolishanything.R
import com.example.photoeditorpolishanything.StoreFragment.BackgroundBottomSheetDialogFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class BackgroundAdapter(var activity: Activity, private var data: MutableList<Groups>,/*var groupm : Map<String, Groups?>*/) : RecyclerView.Adapter<BackgroundAdapter.BackgroundViewHolder>()
{
    private val baseUrl = "https://s3.ap-south-1.amazonaws.com/photoeditorbeautycamera.app/photoeditor/background/"
    private var rewardedAd: RewardedAd? = null

    fun updateData(newData: MutableList<Groups>)
    {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundViewHolder
    {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_sticker, parent, false)
        return BackgroundViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BackgroundViewHolder, position: Int)
    {
        val backgroundUrl = data[position]

        val item = data.getOrNull(position) ?: return

        val imageUrlList = item.mainImageUrl
        if (imageUrlList.isNullOrEmpty())
        {
            Log.e("BackgroundAdapter", "No image URLs available for position $position")
            holder.imageView.setImageResource(R.drawable.cross) // Placeholder image
            return
        }

        val imageUrl = backgroundUrl.mainImageUrl!!.firstOrNull() // Take the first URL from the list if available

        if (imageUrl != null)
        {
            // Load image using Glide
            Glide.with(holder.itemView.context)
                .load(baseUrl + imageUrl)
                .fitCenter()
                .centerCrop()
                .into(holder.imageView)

            holder.itemView.setOnClickListener {
                val activity = it.context as? FragmentActivity
                if (activity != null)
                {
                    val bottomSheetDialogFragment = BackgroundBottomSheetDialogFragment.newInstance(backgroundUrl.subImageUrl, R.color.black,backgroundUrl.textCategory.toString())
                    Log.e("PassedData", "Sub Image URLs: ${backgroundUrl.subImageUrl}")
                    bottomSheetDialogFragment.show(activity.supportFragmentManager, bottomSheetDialogFragment.tag)
                }
            }
        }
        else
        {
            // Handle case where imageUrl is null
            Log.e("ImageLoading", "No image URL available for position $position")
        }

        Log.e("backgroundUrl", "onBindViewHolder: "+baseUrl + backgroundUrl.subImageUrl)
        holder.txtPremiume.text = backgroundUrl.premium.toString()
        if (backgroundUrl.premium.toString() == "[paid]")
        {
            holder.txtPremiume.text = "Paid"
            Log.e("premium", "onBindViewHolder: " + "Paid")
        }
        else if (backgroundUrl.premium.toString() == "[Ad]")
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
        holder.txtName.text = backgroundUrl.textCategory
        holder.txtNumber.text = backgroundUrl.subImageUrl!!.size.toString() + " Backgrounds"
    }

    override fun getItemCount(): Int
    {
        return data.size
    }

    class BackgroundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val imageView: ImageView = itemView.findViewById(R.id.imgSticker)
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtNumber: TextView = itemView.findViewById(R.id.txtNumber)
        val txtPremiume: TextView = itemView.findViewById(R.id.txtPremiume)
        val lnrPremiume : LinearLayout = itemView.findViewById(R.id.lnrPremiume)
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
