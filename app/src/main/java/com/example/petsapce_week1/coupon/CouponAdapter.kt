package com.example.petsapce_week1

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.petsapce_week1.accommodation.AccMainActivity
import com.example.petsapce_week1.databinding.CouponItemsBinding
import com.example.petsapce_week1.databinding.TogoRowBinding
import com.example.petsapce_week1.placetogo.ToGoAdapter
import com.example.petsapce_week1.placetogo.ToGoData

class CouponAdapter(var items: ArrayList<CouponData>):
    RecyclerView.Adapter<CouponAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: CouponItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CouponData) {
            binding.apply {
                couponImg.setImageResource(data.imgList)
                couponName.text = data.name
                couponPlace.text = data.location
                couponDetail.text = data.detail
                couponPrice.text = "할인가 " + data.price.toString()+"원"
                couponDayleft.text = "D-" + data.dayLeft

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CouponItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView?.context, AccMainActivity::class.java)
//            intent.putExtra("content", roomIDNext)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
//            Log.d("content", roomIDNext.toString())
        }
        holder.bind(items[position])
    }
}