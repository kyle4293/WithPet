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
                R.drawable.item7, "구래, 공방",
                "휴먼 앤 펫", "내 반려동물이 쓸 제품을\n직접 만들어보자!", 80000,60000, 3)
        )

        dataList.add(CouponData(
            R.drawable.item5, "용산, 식당",
            "노스트레스버거", "어센틱한 천조국 스타일의 \n유명 치즈버거", 6500, 5000, 5))
    }
}