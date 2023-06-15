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
import com.example.petsapce_week1.home.homefragment.HomeMainData
import com.google.firebase.auth.FirebaseAuth

class PlaceToGoFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

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
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val name = user?.displayName

        binding = FragmentPlaceToGoBinding.inflate(layoutInflater)

        initData(name)
        initRecyclerView()
        return binding.root
    }

    private fun initData(name: String?) {
        //row data add.
        dataList.add(ToGoData(R.drawable.item4, "낙성대공원", "관악, 공원", 3.25))
        binding.textHostname.text = name + "님의 찜 목록"
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