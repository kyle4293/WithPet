package com.example.petsapce_week1.accommodation

import android.annotation.SuppressLint
import android.nfc.tech.IsoDep.get
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petsapce_week1.databinding.AccMainImageslide2Binding
import com.example.petsapce_week1.databinding.AccMainImageslideBinding
import com.example.petsapce_week1.databinding.AccReviewRowBinding

class accImgaeSlideAdapter2(val items: ArrayList<imageSlideData2>) : RecyclerView.Adapter<accImgaeSlideAdapter2.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(data: imageSlideData2)
    }

    var itemClickListener: OnItemClickListener? = null //초기값 null값

    inner class ViewHolder(val binding: AccMainImageslide2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        init {


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AccMainImageslide2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply {
            //이미지는 이런식으로 담아야함.
            imgMain.setImageResource(items[position].imgSlide)
          /*  textName.text = items[position].name
            textDate.text = items[position].date.toString()+"주 전"
            textDetail.text = items[position].text*/
//            textViewDifficulty.text= "난이도 ${position+1}"
        }


    }


    override fun getItemCount(): Int {
        return items.size
    }


}