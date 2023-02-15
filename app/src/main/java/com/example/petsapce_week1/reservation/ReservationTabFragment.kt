package com.example.petsapce_week1.reservation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.petsapce_week1.R
import com.example.petsapce_week1.databinding.FragmentReservationTabBinding
import com.example.petsapce_week1.network.AccomoService
import com.example.petsapce_week1.network.ReservationAPI
import com.example.petsapce_week1.network.RetrofitHelper
import com.example.petsapce_week1.vo.ReservationReadResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class ReservationTabFragment : Fragment() {

    companion object {
        fun newInstance(accessToken: String): ReservationTabFragment {
            val fragment = ReservationTabFragment()
            val args = Bundle()
            args.putString("accessToken", accessToken)
            //args.putString("accommoList", accommoList)
            fragment.arguments = args
            return fragment
        }
    }

    //val data = arguments?.getParcelable<ReservationReadResponse.Reservation>("accommoList") // get the data class from the arguments bundle

    @SuppressLint("StaticFieldLeak")
    lateinit var binding : FragmentReservationTabBinding
    var accessToken : String ?= null
    // ========== 백엔드 연동 부분 ===========
    private var retrofit: Retrofit = RetrofitHelper.getRetrofitInstance()
    // 기본 숙소 정보 불러올때 호출
    var api : ReservationAPI = retrofit.create(ReservationAPI::class.java)

    var roomId : Long = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Log.d("예약 완료 data", "$data")
        val binding = FragmentReservationTabBinding.inflate(layoutInflater)
        //binding.recyclerviewReservationTab.adapter = ReservationTabAdapter.ViewHolder

        return binding.root
    }

}