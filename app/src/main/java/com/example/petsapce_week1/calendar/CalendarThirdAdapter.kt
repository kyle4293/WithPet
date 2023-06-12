package com.example.petsapce_week1.calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.petsapce_week1.R
import com.example.petsapce_week1.databinding.CalendarItemBinding
import com.example.petsapce_week1.databinding.DatingRowBinding
import com.example.petsapce_week1.home.Home2MainData

class CalendarThirdAdapter(var items: ArrayList<CalendarThirdData>) :
    RecyclerView.Adapter<CalendarThirdAdapter.ViewHolder>() {


    fun moveItem(oldPos: Int, newPos: Int) {
        val item = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos, item)
        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos: Int) {
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }

    interface OnItemClickListener {
        fun OnItemClick(data: CalendarThirdData)
//        fun onClick(v: View, position: Int)
    }


    var itemClickListener: OnItemClickListener? = null //초기값 null값


    inner class ViewHolder(val binding: DatingRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: CalendarThirdData) {

            binding.apply {
                textNumber.text = data.number.toString()
                textDate.text = data.text
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DatingRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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



