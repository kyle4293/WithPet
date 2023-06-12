package com.example.petsapce_week1.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
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

    lateinit var adapter: CalendarMainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarMainBinding.inflate(layoutInflater)


        initRecyclerView()
        initData()



        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initData() {
        /*dataList.add(CalendarMainData("1","월"))
        dataList.add(CalendarMainData("1","월"))
        dataList.add(CalendarMainData("1","월"))
        dataList.add(CalendarMainData("1","월"))
        dataList.add(CalendarMainData("1","월"))
        dataList.add(CalendarMainData("1","월"))
        dataList.add(CalendarMainData("1","월"))
        dataList.add(CalendarMainData("1","월"))
        dataList.add(CalendarMainData("1","월"))
        dataList.add(CalendarMainData("1","월"))
        dataList.add(CalendarMainData("1","월"))*/
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
        binding.apply {
            date.text = SimpleDateFormat("yyyy년 MM월 ", Locale.getDefault()).format(calendar.time)
        }

    }

    private fun initRecyclerView() {

        //기존 adapter(recyclerview adpater)
        binding.recyclerview.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        adapter = CalendarMainAdapter(dataList)
        binding.recyclerview.adapter = adapter

    }


}