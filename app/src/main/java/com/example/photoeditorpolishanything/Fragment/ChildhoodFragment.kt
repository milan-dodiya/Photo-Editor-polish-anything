package com.example.photoeditorpolishanything.Fragment

import ImageAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeditorpolishanything.Adapter.CollageTemplateAdapter
import com.example.photoeditorpolishanything.Api.Group
import com.example.photoeditorpolishanything.Api.OkHttpHelper
import com.example.photoeditorpolishanything.EditActivity
import com.example.photoeditorpolishanything.R
import com.example.photoeditorpolishanything.TemplatesActivity
import com.example.photoeditorpolishanything.databinding.FragmentChildhoodBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ChildhoodFragment : Fragment() {

    private lateinit var binding: FragmentChildhoodBinding
    lateinit var adapter: CollageTemplateAdapter
//    lateinit var recyclerView: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChildhoodBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.recyclerView.layoutManager = GridLayoutManager(TemplatesActivity.contexts,2)

//        val url = "https://s3.eu-north-1.amazonaws.com/photoeditorbeautycamera.com/collagemaker/templates.json"

        //        testing
        val url = "https://s3.ap-south-1.amazonaws.com/photoeditorbeautycamera.app/photoeditor/testing.json"

        requireActivity().runOnUiThread {
            binding.progressBar.visibility = View.VISIBLE
        }

        OkHttpHelper.fetchTemplates(url) { categoryApi ->

            requireActivity().runOnUiThread {
                binding.progressBar.visibility = View.GONE
                categoryApi?.let {
                    val groupsList = mutableListOf<Group>()

                    it.templates.forEach { template ->

//                    template?.childhood?.let { childhood ->
//                        childhood.group1?.let { groupsList.add(it) }
//                        childhood.group2?.let { groupsList.add(it) }
//                        childhood.group3?.let { groupsList.add(it) }
//                        childhood.group4?.let { groupsList.add(it) }
//                        childhood.group5?.let { groupsList.add(it) }
//                        childhood.group6?.let { groupsList.add(it) }
//                        childhood.group7?.let { groupsList.add(it) }
//                        childhood.group8?.let { groupsList.add(it) }
//                        childhood.group9?.let { groupsList.add(it) }
//                        childhood.group10?.let { groupsList.add(it) }
//                        childhood.group11?.let { groupsList.add(it) }
//                        childhood.group12?.let { groupsList.add(it) }
//                        childhood.group13?.let { groupsList.add(it) }
//                        childhood.group14?.let { groupsList.add(it) }
//                        childhood.group15?.let { groupsList.add(it) }
//                        childhood.group16?.let { groupsList.add(it) }
//                        childhood.group17?.let { groupsList.add(it) }
//                        childhood.group18?.let { groupsList.add(it) }
//                        childhood.group19?.let { groupsList.add(it) }
//                        childhood.group20?.let { groupsList.add(it) }
                        // Repeat for all groups in the childhood category
//                    }
                    }

//                requireActivity().runOnUiThread {
//                    adapter = CollageTemplateAdapter(
//                        requireContext(),
//                        groupsList
//                    ) /*{ selectedGroup ->
//                        showBottomSheetForImageEditurl()
//                    }*/
//                    binding.recyclerView.adapter = adapter
//                }
                }
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    fun showBottomSheetForImageEditurl( ) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetStyle)
        val view = layoutInflater.inflate(R.layout.camera_dialog, null)
        view.findViewById<ImageView>(R.id.imgCross)?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        // Set layout manager with 3 columns
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        val lnrPhotos = view.findViewById<LinearLayout>(R.id.lnrPhotos)
        val lnrAlbums = view.findViewById<LinearLayout>(R.id.lnrAlbums)

        // Ensure Photos is selected by default
        lnrPhotos.setBackgroundResource(R.drawable.toggle_selected)
        lnrAlbums.setBackgroundResource(R.drawable.toggle_unselected)

        lnrPhotos.setOnClickListener {
            lnrPhotos.setBackgroundResource(R.drawable.toggle_selected)
            lnrAlbums.setBackgroundResource(R.drawable.toggle_unselected)

            view.findViewById<LinearLayout>(R.id.lnrselectedCountTextView).visibility = View.VISIBLE
            view.findViewById<LinearLayout>(R.id.lnrimgDelete).visibility = View.VISIBLE
            view.findViewById<RecyclerView>(R.id.rcvSelected_Image).visibility = View.VISIBLE
            view.findViewById<RecyclerView>(R.id.recyclerView).visibility = View.VISIBLE
            view.findViewById<RecyclerView>(R.id.recyclerViewAlbum).visibility = View.GONE
        }

        lnrAlbums.setOnClickListener {
            lnrAlbums.setBackgroundResource(R.drawable.toggle_selected)
            lnrPhotos.setBackgroundResource(R.drawable.toggle_unselected)

            view.findViewById<RecyclerView>(R.id.recyclerViewAlbum).visibility = View.VISIBLE
            view.findViewById<LinearLayout>(R.id.lnrselectedCountTextView).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.lnrimgDelete).visibility = View.GONE
            view.findViewById<RecyclerView>(R.id.rcvSelected_Image).visibility = View.GONE
            view.findViewById<RecyclerView>(R.id.recyclerView).visibility = View.GONE
        }

        // Fetch images using AsyncTask and populate the RecyclerView
        TemplatesActivity.FetchImagesTasks(requireContext()) { imageList ->
            recyclerView.adapter = ImageAdapter(imageList) { selectedImageUri ->
                bottomSheetDialog.dismiss()
                val intent = Intent(requireContext(), EditActivity::class.java)
                intent.putExtra("selected_image_uri", selectedImageUri.toString())
                startActivity(intent)
            }

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.setOnShowListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    bottomSheetDialog.window?.navigationBarColor =
                        ContextCompat.getColor(requireContext(), R.color.black)
                }
            }

            bottomSheetDialog.show()
        }.execute()
    }
}