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
import com.example.photoeditorpolishanything.Api.Groupess
import com.example.photoeditorpolishanything.R
import com.example.photoeditorpolishanything.StoreFragment.FilterBottomSheetDialogFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class Filter_Adapter(var activity: Activity, private var data: MutableList<Groupess>) : RecyclerView.Adapter<Filter_Adapter.FilterViewHolder>()
{
    private val baseUrl = "https://s3.ap-south-1.amazonaws.com/photoeditorbeautycamera.app/photoeditor/filter/"
    private var rewardedAd: RewardedAd? = null

    fun updateData(newData: MutableList<Groupess>)
    {
        data = newData
        Log.d("Adapter", "Updated data size: ${data.size}")
        data.forEachIndexed { index, item -> Log.d("Adapter", "Item $index: ${item.javaClass.simpleName}, value: $item") }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_lightfx, parent, false)
        return FilterViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FilterViewHolder, position: Int)
    {
        val FilterItem = data.getOrNull(position)

        if (FilterItem is Groupess)
        {
            val imageUrl = FilterItem.mainImageUrl?.firstOrNull()
            if (imageUrl != null)
            {
                Glide.with(holder.itemView.context)
                    .load(baseUrl + imageUrl)
                    .fitCenter()
                    .centerCrop()
                    .into(holder.imageView)
            }
            else
            {
                holder.imageView.setImageResource(R.drawable.cross) // Placeholder image
            }

            holder.txtPremiume.text = FilterItem.premium.toString()

            if (FilterItem.premium.toString() == "[paid]")
            {
                holder.txtPremiume.text = "Paid"
                Log.e("premium", "onBindViewHolder: " + "Paid")
            }
            else if (FilterItem.premium.toString() == "[Ad]")
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
                if (FilterItem.premium.toString() == "[free]")
                {
                    holder.txtPremiume.text = "Free"
                    Log.e("premium", "onBindViewHolder: " + "Free")
                }
            }


            holder.txtName.text = FilterItem.textCategory
            holder.txtNumber.text = "${FilterItem.subImageUrl?.size ?: 0} Filters"

            holder.itemView.setOnClickListener {
                val activity = it.context as? FragmentActivity
                activity?.let {
                    val bottomSheetDialogFragment = FilterBottomSheetDialogFragment.newInstance(FilterItem.subImageUrl, R.color.black,
                        FilterItem.textCategory.toString()
                    )
                    bottomSheetDialogFragment.show(it.supportFragmentManager, bottomSheetDialogFragment.tag)
                }
            }
        }
        else
        {
            Log.e("Adapter", "Unexpected data type at position $position: ${FilterItem?.javaClass?.simpleName}")
        }
    }

    override fun getItemCount(): Int
    {
        return data.size
    }

    class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val imageView: ImageView = itemView.findViewById(R.id.imgSticker)
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtNumber: TextView = itemView.findViewById(R.id.txtNumber)
        val txtPremiume : TextView = itemView.findViewById(R.id.txtPremiume)
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