package com.example.petsapce_week1.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petsapce_week1.R
import com.example.petsapce_week1.databinding.FragmentCalendarMainBinding
import com.example.petsapce_week1.home.Home2MainAdapter
import com.example.petsapce_week1.home.Home2MainData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalendarMainFragment : Fragment() {

    private lateinit var binding: FragmentCalendarMainBinding
    var dataList = ArrayList<CalendarMainData>()
    var dataList2 = ArrayList<CalendarSecondData>()

    lateinit var adapter: CalendarMainAdapter
    lateinit var adapter2: CalendarSecondAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarMainBinding.inflate(layoutInflater)


        initRecyclerView()
        initData()
        initRecyclerView2()
        initData2()


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initData2() {
        for (i in 0 until 30)
            dataList2.add(CalendarSecondData(i + 1, "13:00 리요리요 커피"))

    }

    private fun initRecyclerView2() {
        binding.recyclerview2.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        adapter2 = CalendarSecondAdapter(dataList2)
        binding.recyclerview2.adapter = adapter2

        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                adapter2.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter2.removeItem(viewHolder.adapterPosition)
            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerview2)
    }


    private fun initData() {

        val calendar = Calendar.getInstance()
        //이번 달의 말일
        val totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
        val dayFormat = SimpleDateFormat("E", Locale.getDefault())

        for (i in 0 until totalDays) {
            val date = dateFormat.format(calendar.time)
            val dayOfWeek = dayFormat.format(calendar.time)

            dataList.add(CalendarMainData(date, dayOfWeek))


            if (date == totalDays.toString())
                break
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        binding.date.text =
            SimpleDateFormat("yyyy년 MM월 ", Locale.getDefault()).format(calendar.time)


    }

    private fun initRecyclerView() {

        binding.recyclerview.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        adapter = CalendarMainAdapter(dataList)
        binding.recyclerview.adapter = adapter


    }


}