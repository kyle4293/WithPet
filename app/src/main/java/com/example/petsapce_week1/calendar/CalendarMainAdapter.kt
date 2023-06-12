package com.example.petsapce_week1.calendar

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.petsapce_week1.R
import com.example.petsapce_week1.databinding.CalendarItemBinding
import com.example.petsapce_week1.databinding.FragmentCalendarMainBinding
import com.example.petsapce_week1.home.Home2MainData
import kotlinx.coroutines.NonDisposableHandle.parent

@Suppress("DEPRECATION")
class CalendarMainAdapter(var items: ArrayList<CalendarMainData>, private val fragmentBinding: FragmentCalendarMainBinding
) :
    RecyclerView.Adapter<CalendarMainAdapter.ViewHolder>() {

//    private var selectedItemPosition = RecyclerView.SCROLLBAR_POSITION_LEFT
    private var selectedItemPosition = RecyclerView.SCROLLBAR_POSITION_DEFAULT

//    lateinit var fragmentBinding: FragmentCalendarMainBinding



    interface OnItemClickListener {
        fun OnItemClick(data: CalendarMainData)
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



                if (adapterPosition == selectedItemPosition) {
                    clCalendarItem.isSelected = true
                    tvDateCalendarItem.setTextColor( ContextCompat.getColor(
                        itemView.context,
                        R.color.white
                    ))

                } else {
                    clCalendarItem.isSelected = false
                    tvDateCalendarItem.setTextColor( ContextCompat.getColor(
                        itemView.context,
                        R.color.textForGray
                    ))

                }

                clCalendarItem.setOnClickListener {
                    // 기존에 선택된 아이템의 선택 상태 해제
                    if (selectedItemPosition != RecyclerView.NO_POSITION) {
                        notifyItemChanged(selectedItemPosition)
                    }

                    // 현재 아이템 선택
                    selectedItemPosition = adapterPosition
                    notifyItemChanged(selectedItemPosition)
                }
                when (selectedItemPosition) {
                    0 -> {
                        fragmentBinding.recyclerview2.visibility = View.VISIBLE
                    }
                    1 -> {
//                        Log.d("prrrrr",selectedItemPosition.toString())
                        fragmentBinding.recyclerview2.visibility = View.GONE
                        fragmentBinding.recyclerview3.visibility = View.VISIBLE
                    }
                    else -> {
                        fragmentBinding.recyclerview2.visibility = View.INVISIBLE
                        fragmentBinding.recyclerview3.visibility = View.INVISIBLE
                    }
                }


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


        holder.bind(items[position])

    }

    override fun getItemCount(): Int {
        return items.size
    }


}



