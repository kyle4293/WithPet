package com.example.petsapce_week1.placetogo

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.petsapce_week1.accommodation.AccMainActivity
import com.example.petsapce_week1.databinding.Home2MainRowBinding
import com.example.petsapce_week1.databinding.HomeMainRowBinding
import com.example.petsapce_week1.databinding.TogoRowBinding
import com.example.petsapce_week1.home.homefragment.HomeMainData

class ToGoAdapter(var items: ArrayList<ToGoData>):
RecyclerView.Adapter<ToGoAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: TogoRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ToGoData) {
            val cut = String.format("%.2f", data.score)
            binding.apply {
                image.setImageResource(data.imgList)
                textName.text = data.name
                textLoc.text = data.location
                textScore.text = cut
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TogoRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

        holder.binding.apply {
            //이미지는 이런식으로 담아야함.
//            imgMain.setImageResource(items[position].img)
            /*  textLoc.text = items[position].location
              textScore.text = items[position].score.toString()
              textDate.text = items[position].date.toString()
              textPrice.text = "₩" + items[position].price.toString() + " / 박"*/
//            textViewDifficulty.text= "난이도 ${position+1}"

        }
        holder.bind(items[position])
    }



}