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
import com.example.photoeditorpolishanything.Api.Groupes
import com.example.photoeditorpolishanything.R
import com.example.photoeditorpolishanything.StoreFragment.LightFxBottomSheetDialogFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class LightFx_Adapter(var activity: Activity,private var data: MutableList<Groupes>) : RecyclerView.Adapter<LightFx_Adapter.LightFxViewHolder>() {
    private val baseUrl = "https://s3.ap-south-1.amazonaws.com/photoeditorbeautycamera.app/photoeditor/lightfx/"
    private var rewardedAd: RewardedAd? = null

    fun updateData(newData: MutableList<Groupes>) {
        data = newData
        Log.d("Adapter", "Updated data size: ${data.size}")
        data.forEachIndexed { index, item ->
            Log.d("Adapter", "Item $index: ${item.javaClass.simpleName}, value: $item")
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightFxViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_lightfx, parent, false)
        return LightFxViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LightFxViewHolder, position: Int) {
        val lightFxItem = data.getOrNull(position)

        if (lightFxItem is Groupes) {
            val imageUrl = lightFxItem.mainImageUrl?.firstOrNull()
            if (imageUrl != null) {
                Glide.with(holder.itemView.context)
                    .load(baseUrl + imageUrl)
                    .fitCenter()
                    .centerCrop()
                    .into(holder.imageView)
            } else {
                holder.imageView.setImageResource(R.drawable.cross) // Placeholder image
            }

            holder.txtPremiume.text = lightFxItem.premium.toString()

            if (lightFxItem.premium.toString() == "[paid]")
            {
                holder.txtPremiume.text = "Paid"
                Log.e("premium", "onBindViewHolder: " + "Paid")
            }
            else if (lightFxItem.premium.toString() == "[Ad]")
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
                if (lightFxItem.premium.toString() == "[free]")
                {
                    holder.txtPremiume.text = "Free"
                    Log.e("premium", "onBindViewHolder: " + "Free")
                }
            }

            holder.txtName.text = lightFxItem.textCategory
            holder.txtNumber.text = "${lightFxItem.subImageUrl?.size ?: 0} Light Fx"

            holder.itemView.setOnClickListener {
                val activity = it.context as? FragmentActivity
                activity?.let {
                    val bottomSheetDialogFragment = LightFxBottomSheetDialogFragment.newInstance(lightFxItem.subImageUrl, R.color.black,
                        lightFxItem.textCategory.toString()
                    )
                    bottomSheetDialogFragment.show(it.supportFragmentManager, bottomSheetDialogFragment.tag)
                }
            }
        } else {
            Log.e("Adapter", "Unexpected data type at position $position: ${lightFxItem?.javaClass?.simpleName}")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class LightFxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
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