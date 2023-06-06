package com.example.petsapce_week1.coupon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petsapce_week1.CouponAdapter
import com.example.petsapce_week1.CouponData
import com.example.petsapce_week1.R
import com.example.petsapce_week1.databinding.FragmentCouponBinding

class CouponFragment : Fragment() {

    lateinit var binding : FragmentCouponBinding
    lateinit var adapter: CouponAdapter

    var dataList = ArrayList<CouponData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCouponBinding.inflate(layoutInflater)

        initData()
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerviewCoupon.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        adapter = CouponAdapter(dataList)
        binding.recyclerviewCoupon.adapter = adapter
        binding.recyclerviewCoupon.isNestedScrollingEnabled = true

    }

    private fun initData() {
        //dummy data 입력
        dataList.add(
            CouponData(
                R.drawable.imgcoffee2, "카페, 성수동",
                "로우커피스탠드", "매력적이고 예쁜 take-out \ncoffee bar", 20000, 3)
        )

        dataList.add(CouponData(
            R.drawable.imgcaat4x, "카페, 합정동",
            "카페 버킨", "붉은색 벽돌 속 아늑한 공간", 30000, 5))
    }
}