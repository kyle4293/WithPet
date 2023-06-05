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
import com.google.firebase.auth.FirebaseAuth

class PlaceToGoFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

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
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val name = user?.displayName

        binding = FragmentPlaceToGoBinding.inflate(layoutInflater)


        binding.textHostname.text = name+"님의 찜 목록"





        return binding.root
    }

}