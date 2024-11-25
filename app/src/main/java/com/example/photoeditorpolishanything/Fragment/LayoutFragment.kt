package com.example.photoeditorpolishanything.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photoeditorpolishanything.Adapter.LayoutAdapter
import com.example.photoeditorpolishanything.DashboardActivity.Companion.selectedImages
import com.example.photoeditorpolishanything.LayoutActivity
import com.example.photoeditorpolishanything.LayoutActivity.Companion.imageCount
import com.example.photoeditorpolishanything.R
import com.example.photoeditorpolishanything.databinding.FragmentLayoutBinding


class LayoutFragment : Fragment() {

    private var _binding: FragmentLayoutBinding? = null
    private val binding get() = _binding!!
//    private var listener: OnLayoutSelectedListener? = null
//    interface OnLayoutSelectedListener {
//        fun onLayoutSelected(layoutResource: Int, selectedImages: ArrayList<String>)
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLayoutBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView() {
        binding.rcvCollageLayouts.layoutManager = GridLayoutManager(requireContext(), 5)

//        // Define the onLayoutClick callback
//        val onLayoutClick: (Int) -> Unit = { layoutResource ->
//            openSelectedLayout(layoutResource)
//        }

        // Prepare the list of layouts and icons
        val layouts = getLayoutsForImageCount(selectedImages.size)
        val icons = getIconsForImageCount(selectedImages.size)

        val onLayoutClick: (Int) -> Unit = { layoutResource ->
            handleLayoutSelection(layoutResource)
        }

        // Set up the adapter
        val layoutAdapter = LayoutAdapter(layouts, icons, onLayoutClick,imageCount!!)
        binding.rcvCollageLayouts.adapter = layoutAdapter
    }


//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnLayoutSelectedListener) {
//            listener = context
//        } else {
//            throw RuntimeException("$context must implement OnLayoutSelectedListener")
//        }
//    }

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

    private fun getIconsForImageCount(imageCount: Int): List<Int> {
        return when (imageCount) {
            2 -> listOf(
                R.drawable.icon_2_1,
                R.drawable.icon_2_2,
                R.drawable.icon_2_3,
                R.drawable.icon_2_4,
                R.drawable.icon_2_5,
                R.drawable.icon_2_6,
                R.drawable.icon_2_7,
                R.drawable.icon_2_8,
                R.drawable.icon_2_9,
                R.drawable.icon_2_10
            )
            3 -> listOf(
                R.drawable.icon_3_1,
                R.drawable.icon_3_2,
                R.drawable.icon_3_3,
                R.drawable.icon_3_4,
                R.drawable.icon_3_5,
                R.drawable.icon_3_6,
                R.drawable.icon_3_7,
                R.drawable.icon_3_8,
                R.drawable.icon_3_9,
                R.drawable.icon_3_10
            )
            4 -> listOf(
                R.drawable.icon_4_1,
                R.drawable.icon_4_2,
                R.drawable.icon_4_3,
                R.drawable.icon_4_4,
                R.drawable.icon_4_5,
                R.drawable.icon_4_6,
                R.drawable.icon_4_7,
                R.drawable.icon_4_8,
                R.drawable.icon_4_9,
                R.drawable.icon_4_10
            )
            5 -> listOf(
                R.drawable.icon_5_1,
                R.drawable.icon_5_2,
                R.drawable.icon_5_3,
                R.drawable.icon_5_4,
                R.drawable.icon_5_5,
                R.drawable.icon_5_6,
                R.drawable.icon_5_7,
                R.drawable.icon_5_8,
                R.drawable.icon_5_9,
                R.drawable.icon_5_10
            )
            6 -> listOf(
                R.drawable.icon_6_1,
                R.drawable.icon_6_2,
                R.drawable.icon_6_3,
                R.drawable.icon_6_4,
                R.drawable.icon_6_5,
                R.drawable.icon_6_6,
                R.drawable.icon_6_7,
                R.drawable.icon_6_8,
                R.drawable.icon_6_9,
                R.drawable.icon_6_10
            )
            7 -> listOf(
                R.drawable.icon_7_1,
                R.drawable.icon_7_2,
                R.drawable.icon_7_3,
                R.drawable.icon_7_4,
                R.drawable.icon_7_5,
                R.drawable.icon_7_6,
                R.drawable.icon_7_7,
                R.drawable.icon_7_8,
                R.drawable.icon_7_9,
                R.drawable.icon_7_10
            )
            8 -> listOf(
                R.drawable.icon_8_1,
                R.drawable.icon_8_2,
                R.drawable.icon_8_3,
                R.drawable.icon_8_4,
                R.drawable.icon_8_5,
                R.drawable.icon_8_6,
                R.drawable.icon_8_7,
                R.drawable.icon_8_8,
                R.drawable.icon_8_9,
                R.drawable.icon_8_10
            )
            9 -> listOf(
                R.drawable.icon_9_1,
                R.drawable.icon_9_2,
                R.drawable.icon_9_3,
                R.drawable.icon_9_4,
                R.drawable.icon_9_5,
                R.drawable.icon_9_6,
                R.drawable.icon_9_7,
                R.drawable.icon_9_8,
                R.drawable.icon_9_9,
                R.drawable.icon_9_10
            )
            10 -> listOf(
                R.drawable.icon_10_1,
                R.drawable.icon_10_2,
                R.drawable.icon_10_3,
                R.drawable.icon_10_4,
                R.drawable.icon_10_5,
                R.drawable.icon_10_6,
                R.drawable.icon_10_7,
                R.drawable.icon_10_8,
                R.drawable.icon_10_9,
                R.drawable.icon_10_10
            )
            else -> emptyList()
        }
    }

    private fun handleLayoutSelection(layoutResource: Int) {

        val selectedImageUrisAsStrings = selectedImages.map { it.toString() }

//        // Handle the click event here
//        Log.e("LayoutFragment", "Layout selected: $layoutResource")
//        // Perform your desired action
//        // Notify the activity
////        listener?.onLayoutSelected(layoutResource,ArrayList(selectedImageUrisAsStrings))
//        // Send the selected layout and images to the activity
//        val intent = Intent(requireContext(), LayoutActivity::class.java)
//        intent.putExtra("selectedLayout", layoutResource)
//        intent.putStringArrayListExtra("selectedImages", ArrayList(selectedImageUrisAsStrings))
//        startActivity(intent)


        var activity = requireActivity() as LayoutActivity
        activity.updateCollageLayout(layoutResource,selectedImageUrisAsStrings)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onLayoutSelected(layoutResource: Int) {
//    }
}
