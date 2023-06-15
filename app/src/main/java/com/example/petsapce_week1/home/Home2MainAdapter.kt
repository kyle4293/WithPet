package com.example.petsapce_week1.home

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.petsapce_week1.accommodation.AccMainActivity
import com.example.petsapce_week1.databinding.HomeMainRowBinding
import com.example.petsapce_week1.home.homefragment.HomeMainData


class Home2MainAdapter(var items: ArrayList<HomeMainData>) :
    RecyclerView.Adapter<Home2MainAdapter.ViewHolder>() {


    interface OnItemClickListener {
        fun OnItemClick(data: HomeMainData)
//        fun onClick(v: View, position: Int)
    }


    //평점 오름 차순 정렬
    @SuppressLint("NotifyDataSetChanged")
    fun sortAscending() {
        items.sortBy { it.score }
        notifyDataSetChanged()
    }


    // 평점 내림 차순 정렬
    @SuppressLint("NotifyDataSetChanged")
    fun sortDescending() {
        items.sortByDescending { it.score }
        notifyDataSetChanged()
    }

    fun sortAscendingPrice() {
        items.sortBy { it.price }
        notifyDataSetChanged()
    }

    fun sortDescendingPrice() {
        items.sortByDescending { it.price }
        notifyDataSetChanged()
    }

    //텍스트 검색
    @SuppressLint("NotifyDataSetChanged")
    fun filterByText(query: String) {
        val filteredList = items.filter { it.name.contains(query) }
        items.clear()
        items.addAll(filteredList)
        notifyDataSetChanged()
    }



    var itemClickListener: OnItemClickListener? = null //초기값 null값


    inner class ViewHolder(val binding: HomeMainRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: HomeMainData) {
            val cut = String.format("%.2f", data.score)
            val formattedAmount = String.format("%,d", data.price)


            binding.apply {
                image.setImageResource(data.imgList)
                textName.text = data.name
                textLoc.text = data.location
                textScore.text = cut
                Price.text = formattedAmount+"원"

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HomeMainRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val img = items[position].imgList
        val name = items[position].name
        val location = items[position].location
        val score = items[position].score
        val price = String.format("%,d", items[position].price)


        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AccMainActivity::class.java)

            intent.putExtra("img", img)
            intent.putExtra("name", name)
            intent.putExtra("location", location)
            intent.putExtra("score", score)
            intent.putExtra("price", price)
//            intent.putParcelableArrayListExtra("score", homeMainData)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
//            Log.d("content", roomIDNext.toString())
        }

        holder.bind(items[position])


    }


    override fun getItemCount(): Int {
        return items.size
    }

}



