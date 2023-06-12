package com.example.petsapce_week1.calendar

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.petsapce_week1.R
import com.example.petsapce_week1.accommodation.AccMainActivity
import com.example.petsapce_week1.databinding.CalendarItemBinding
import com.example.petsapce_week1.databinding.Home2MainRowBinding
import com.example.petsapce_week1.databinding.HomeMainRowBinding
import com.example.petsapce_week1.databinding.HomeMainRowChildBinding
import com.example.petsapce_week1.home.Home2ChildAdapter
import com.example.petsapce_week1.home.Home2MainData
import com.example.petsapce_week1.home.homefragment.HomeChildAdapter
import com.example.petsapce_week1.home.homefragment.HomeMainData
import com.example.petsapce_week1.vo.HomeResponse
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
//import kotlinx.android.synthetic.main.activity_acc_main.view.*
//import kotlinx.android.synthetic.main.home_main_row.view.*
import java.text.DecimalFormat

class CalendarMainAdapter(var items: ArrayList<CalendarMainData>) :
    RecyclerView.Adapter<CalendarMainAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(data: Home2MainData)
//        fun onClick(v: View, position: Int)
    }


    var itemClickListener: OnItemClickListener? = null //초기값 null값


    inner class ViewHolder(val binding: CalendarItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: CalendarMainData) {

            binding.apply {
                tvDateCalendarItem.text = data.date
                tvDayCalendarItem.text = data.day

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CalendarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        var isClicked = true
        holder.itemView.setOnClickListener {

            if (isClicked) {
                holder.binding.clCalendarItem.setBackgroundColor(Color.parseColor("#B0DAFF"))
                isClicked = false
            } else {
                holder.binding.clCalendarItem.setBackgroundResource(R.drawable.calendar_item_background)
                isClicked = true
            }
        }



        holder.bind(items[position])

    }

    override fun getItemCount(): Int {
        return items.size
    }


}



