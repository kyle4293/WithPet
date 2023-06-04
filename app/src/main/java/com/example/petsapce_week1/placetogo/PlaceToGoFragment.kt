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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petsapce_week1.R
import com.example.petsapce_week1.databinding.FragmentPlaceToGoBinding
import com.example.petsapce_week1.home.Home2MainAdapter
import com.example.petsapce_week1.home.Home2MainData
import com.example.petsapce_week1.home.homefragment.HomeMainAdapter
import com.example.petsapce_week1.home.homefragment.HomeMainData

class PlaceToGoFragment : Fragment() {

    lateinit var binding : FragmentPlaceToGoBinding
    lateinit var adapter: ToGoAdapter

    var dataList = ArrayList<ToGoData>()

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

        initData()
        initRecyclerView()
        return binding.root
    }

    private fun initData() {
        dataList.add(ToGoData(R.drawable.imgcoffee2, "로우커피스탠드", "카페, 성수동", 4.50))
        dataList.add(ToGoData(R.drawable.imgcaat4x, "로우커피스탠드2", "카페, 성수동", 4.50))
        dataList.add(ToGoData(R.drawable.imgforest4x, "로우커피스탠드3", "카페, 성수동", 4.50))
        dataList.add(ToGoData(R.drawable.home2, "경주 숙소", "숙소, 경주", 3.25))

    }
    private fun initRecyclerView(){

            //기존 adapter(recyclerview adpater)
            binding.recyclerviewTogoList.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.VERTICAL, false
            )
            adapter = ToGoAdapter(dataList)
            binding.recyclerviewTogoList.adapter = adapter
            binding.recyclerviewTogoList.isNestedScrollingEnabled = true
    }

}