package com.example.petsapce_week1.coupon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.petsapce_week1.databinding.FragmentReservationMainBinding


class reservationMainFragment : Fragment() {

    private lateinit var binding:FragmentReservationMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReservationMainBinding.inflate(layoutInflater)

       /* val mainVPAdapter = MainVPAdapter(this)
        binding.vpMain.adapter = mainVPAdapter //viewpager연동완료

        binding.vpMain.isUserInputEnabled = false


        val tabTitleArray = arrayOf(
            "방문 완료",
            "예약 완료",
        )

        TabLayoutMediator(binding.tabMain,binding.vpMain){tab,position->
            tab.text = tabTitleArray[position]
        }.attach()*/




        // Inflate the layout for this fragment
        return binding.root
    }

}