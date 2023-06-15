package com.example.petsapce_week1.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petsapce_week1.databinding.ActivityHome2Binding
import com.example.petsapce_week1.home.homefragment.HomeFragment
import com.example.petsapce_week1.home.homefragment.HomeMainData
import com.example.petsapce_week1.home.homefragment.SortViewModel
import com.example.petsapce_week1.network.RetrofitHelperHome
import com.example.petsapce_week1.network.homeAPI
import com.example.petsapce_week1.vo.Home2Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class Home2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityHome2Binding

    //레트로핏 객체 생성
    var retrofit: Retrofit = RetrofitHelperHome.getRetrofitInstance()

    //서비스 객체 생성
    var api: homeAPI = retrofit.create(homeAPI::class.java)

    lateinit var viewModel: SortViewModel


    var dataList = ArrayList<HomeMainData>()
    var originList = ArrayList<HomeMainData>()
    lateinit var spinner: Spinner
    lateinit var roomId: String

    lateinit var adapter: Home2MainAdapter

    //초기 검색값 세팅
    lateinit var searchText: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHome2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(SortViewModel::class.java)

        //검색결과 : 최근등록순으로 정렬
        initRecyclerView()
        initFirst()
        //스피너 초기화
        initSpinner()
        //버튼 초기화
        initButtonSort()
        //이전으로
        initBefore()

    }


    private fun initFirst() {
        val home = HomeFragment()
        home.initData()

        val homDatalist = home.getOriginalDataList()
        dataList.addAll(homDatalist)
        originList.addAll(homDatalist)

        searchText = intent.getStringExtra("searchText").toString().trim()
        binding.textChange.text = searchText
        filterText(searchText)


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filterText(query: String) {

        val text = originList.filter { it.location.contains(query) }
        dataList.clear()
        dataList.addAll(text)
        adapter.notifyDataSetChanged()
    }

    private fun initSpinner() {
        spinner = binding.spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d(
                    "MainActivity",
                    "onItemSelected : $position, ${spinner.getItemAtPosition(position)}"
                )
                when (spinner.getItemAtPosition(position)) {
                    "최근등록순" -> {

                    }

                    "높은가격순" -> {
                        adapter.sortDescendingPrice()

                    }

                    "낮은가격순" -> {
                        adapter.sortAscendingPrice()

                    }

                    "평점높은순" -> {
                        adapter.sortDescending()

                    }

                    "평점낮은순" -> {
                        adapter.sortAscending()

                    }

                    else -> {

                    }
                }
            }
        }

    }


    private fun initButtonSort() {
        binding.apply {
            b1.setOnClickListener {
                filterText("전체")
            }
            b2.setOnClickListener {
                filterText("카페")
            }
            b3.setOnClickListener {
                filterText("식당")
            }
            b4.setOnClickListener {
                filterText("공원")
            }
            b5.setOnClickListener {
                filterText("호텔")
            }
        }
    }


    private fun initRecyclerView() {

        //기존 adapter(recyclerview adpater)
        binding.recyclerviewMain.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        adapter = Home2MainAdapter(dataList)
        binding.recyclerviewMain.adapter = adapter
        binding.recyclerviewMain.isNestedScrollingEnabled = false

    }

    private fun initBefore() {
        binding.imgBack.setOnClickListener {
            val intent = Intent(this, HomeResearchActivity::class.java)
            startActivity(intent)
        }

    }


}