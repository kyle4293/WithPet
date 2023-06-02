package com.example.petsapce_week1.placetogo


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.petsapce_week1.R
import com.example.petsapce_week1.databinding.FragmentPlaceToGoBinding

class PlaceToGoFragment : Fragment() {

    lateinit var binding : FragmentPlaceToGoBinding

    private var tcontext: Context ?= null

    var accessToken : String ?= null

    override fun onResume() {
        super.onResume()
        getAccessToken()
    }
    private fun getAccessToken() {
        val atpref = requireContext().getSharedPreferences("accessToken", MODE_PRIVATE)
        accessToken = atpref.getString("accessToken", "default")
        Log.d("함께 갈 곳 액토","$accessToken")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.tcontext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPlaceToGoBinding.inflate(layoutInflater)


        binding.textHostname.text =





       /* getAccessToken()
        if(accessToken != null){

        }
        else{
            Log.d("함께 갈 곳", "비었음")

//            val fragmentManager = (activity as AppCompatActivity).supportFragmentManager
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            val newFragment = NoLoginPlacetogoFragment()
//            fragmentTransaction.replace(R.id.placetogoLayout, newFragment)
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
        }

        //adapter = PlaceToGoRegionAdapter(dataList)
        //        binding.recyclerviewMain.adapter = adapter
        //        binding.recyclerviewMain.isNestedScrollingEnabled = true
//        adapter = context?.let { accessToken?.let { it1 -> PlaceGridAdapter(it, img, it1) } }!!*/

        return binding.root
    }

}