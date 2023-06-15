package com.example.petsapce_week1.home.homefragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petsapce_week1.ProfileActivity
import com.example.petsapce_week1.ProfileMenuActivity
import com.example.petsapce_week1.R
import com.example.petsapce_week1.databinding.FragmentHomeBinding
import com.example.petsapce_week1.home.HomeResearchActivity
import com.example.petsapce_week1.loginrelated.LoginActivity
import com.example.petsapce_week1.loginrelated.MyApplication
import com.example.petsapce_week1.network.RetrofitHelperHome
import com.example.petsapce_week1.network.homeAPI
import com.example.petsapce_week1.vo.HomeResponse
//import kotlinx.android.synthetic.main.home_main_row.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.Locale.filter


class HomeFragment : Fragment(), View.OnClickListener {

    val btn1House = "커피"
    val btn2Campsite = "숙소"
    val btn3Downtown = "DOWNTOWN"
    val btn4Country = "COUNTRY"
    val btn5Beach = "BEACH"

    val sortDefault = "ID_DESC"
    val sortPriceDesc = "PRICE_ASC"
    val sortPriceAsc = "PRICE_DESC"
    val sortReviewCount = "REVIEW_COUNT_DESC"
    val sortReviewScore = "AVERAGE_REVIEW_SCORE_DESC"


    //스피너 및 버튼 전역변수
    var page = 0
    var spinnerCheck: String = ""
    var buttonCheck: String = ""

    //레트로핏 객체 생성
    var retrofit: Retrofit = RetrofitHelperHome.getRetrofitInstance()

    //서비스 객체 생성
    var api: homeAPI = retrofit.create(homeAPI::class.java)

    lateinit var viewModel: SortViewModel

    //child apdater
    private lateinit var binding: FragmentHomeBinding

    var dataList = ArrayList<HomeMainData>()

    val originalDataList = ArrayList<HomeMainData>()
    lateinit var adapter: HomeMainAdapter
    lateinit var spinner: Spinner
    lateinit var roomId: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(SortViewModel::class.java)


        //데이터
        initData()
        getOriginalDataList()
        //버튼정렬
        initButton()
        //스피너정렬
        initSpinner()
        //리사이클러뷰
        initRecyclerView()
        //다음페이지
        initNext()
        //로그인 (임시)
        initLogin()



        // Inflate the layout for this fragment
        return binding.root
    }



    //버튼 정렬
    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(v: View?) {
        when (v?.id) {
            binding.b1.id -> {
              /*  adapter = HomeMainAdapter(dataList)
                adapter.items = dataList
                adapter.notifyDataSetChanged()*/

                filterText("")
//                adapter.sortDescendingPrice()

//                buttonCheck = btn1House
//                adapter.filterByText("숙소")

                /* buttonCheck = btn1House
                 updateTripple(page,spinnerCheck,buttonCheck)*/
            }

            binding.b2.id -> {
                buttonCheck = btn2Campsite
                filterText("카페")


                /*   buttonCheck = btn2Campsite
                   updateTripple(page,spinnerCheck,buttonCheck)*/
            }

            binding.b3.id -> {
                filterText("식당")


                /*   buttonCheck = btn3Downtown
                   updateTripple(page, spinnerCheck, buttonCheck)*/
            }

            binding.b4.id -> {
                filterText("공원")

           /*     buttonCheck = btn4Country
                updateTripple(page, spinnerCheck, buttonCheck)*/
            }

            binding.b5.id -> {
                filterText("호텔")

              /*  buttonCheck = btn5Beach
                updateTripple(page, spinnerCheck, buttonCheck)*/
            }
        }
    }

    fun priceSortDescending(){
        dataList.sortByDescending { it.price }
        adapter.items = dataList
        adapter.notifyDataSetChanged()
    }
    fun filterText(query: String) {

        val text = originalDataList.filter { it.location.contains(query) }
        dataList.clear()
        dataList.addAll(text)
//        adapter.items = dataList
        adapter.notifyDataSetChanged()
    }



    //스피너 정렬
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
//                        priceSortDescending()
                        adapter.sortDescendingPrice()
                       /* spinnerCheck = sortPriceDesc
                        filter(
                            buttonCheck,spinnerCheck
                        )*/

                        /*  spinnerCheck = sortPriceAsc
                          updateTripple(page,spinnerCheck,buttonCheck)*/
                    }

                    "낮은가격순" -> {
                        adapter.sortAscendingPrice()

                       /* spinnerCheck = sortPriceDesc
                        updateTripple(page, spinnerCheck, buttonCheck)*/
                    }

                    "평점높은순" -> {
                        adapter.sortDescending()

                        /*  spinnerCheck = sortReviewScore
                          updateTripple(page, spinnerCheck, buttonCheck)*/
                    }

                    "평점낮은순" -> {
                        adapter.sortAscending()

                    }

                    else -> {
//                        adapter.sortAscending()

                        /*  spinnerCheck = sortDefault
                          updateTripple(page,spinnerCheck,buttonCheck)*/
                    }
                }
            }
        }

    }



    //삼중 정렬
    fun updateTripple(page: Int, sort: String, category: String) {
        ArrayList<HomeMainData>()

        api.getTriple(page, sort, category).enqueue(object : Callback<HomeResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<HomeResponse>,
                response: Response<HomeResponse>
            ) {
                val usersSort = response.body()
                if (usersSort != null && usersSort.result != null) {
                    val resultSize = usersSort.result.size
                    val dataList = ArrayList<HomeMainData>()
//                    var dateList = arrayListOf(1,2,3)
                    var statdate = ""
                    var endDate = ""


                    for (i in 0 until resultSize) {
                        roomId = usersSort.result[i].roomId.toString()
//                        val availDaysList = usersSort.result[i].availableDays.size
                        val availImageSize = usersSort.result[i].roomImages.size

                        var childataList = ArrayList<HomeChildData>()
                        for (j in 0 until availImageSize) {
//                            childataList.add(HomeChildData(R.drawable.map))
                            childataList.add(HomeChildData(usersSort.result[i].roomImages[j]))
//                            Log.d("childataList",usersSort.result[i].roomImages[j])

                        }

                        /*       if (availDaysList != 0) {
                                   statdate = usersSort.result[i].availableDays[0]
                                   endDate = usersSort.result[i].availableDays[availDaysList - 1]
                               }*/


                        /*  dataList.add(
                              HomeMainData(
                                  childataList,
                                  usersSort.result[i].averageReviewScore,
                                  usersSort.result[i].city + ", " + usersSort.result[i].district,
                                  "$statdate~$endDate",
                                  usersSort.result[i].price,
                                  usersSort.result[i].numberOfReview,
                                  usersSort.result[i].roomId

                              )

                          )*/
                    }

                    adapter.items = dataList
                    adapter.notifyDataSetChanged()


                } else {
                    Log.d("PRICE_DESC", response.code().toString())

                }
            }

            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                Log.d("PRICE_DESC", t.message.toString())
            }
        })

    }


    fun initData() {

        dataList.add(HomeMainData(R.drawable.item11, "에롤파", "카페, 성수동", 4.50, 7000))
        dataList.add(HomeMainData(R.drawable.home2, "경주신라호텔", "호텔, 경주", 3.25,10000))
        dataList.add(HomeMainData(R.drawable.item1, "호텔 카푸치노", "강남, 호텔", 4.50,110000))
        dataList.add(HomeMainData(R.drawable.item2, "스타필드 하남", "하남, 쇼핑몰", 4.50,0))
        dataList.add(HomeMainData(R.drawable.item3, "자매의부엌 어스", "신사, 식당", 4.50,25000))
        dataList.add(HomeMainData(R.drawable.item4, "낙성대공원", "관악, 공원", 3.25,0))
        dataList.add(HomeMainData(R.drawable.item5, "노스트레스버거", "용산, 식당", 3.25,6500))
        dataList.add(HomeMainData(R.drawable.item6, "아이딜", "행운동, 카페", 3.25,4000))
        dataList.add(HomeMainData(R.drawable.item7, "휴먼 앤 펫", "구래, 공방", 3.25,80000))

        originalDataList.addAll(dataList)
    }

    fun getOriginalDataList(): List<HomeMainData> {
        return originalDataList
    }


    private fun initRecyclerView() {

        //기존 adapter(recyclerview adpater)
        binding.recyclerviewMain.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )

        adapter = HomeMainAdapter(dataList)
        binding.recyclerviewMain.adapter = adapter
        binding.recyclerviewMain.isNestedScrollingEnabled = true


    }



    fun initButton() {
        binding.b1.setOnClickListener(this)
        binding.b2.setOnClickListener(this)
        binding.b3.setOnClickListener(this)
        binding.b4.setOnClickListener(this)
        binding.b5.setOnClickListener(this)

    }

    private fun initNext() {
        binding.btnSearch.setOnClickListener {
            val intent = Intent(context, HomeResearchActivity::class.java)
            startActivity(intent)

        }
    }

    private fun initLogin() {
        MyApplication()
        val loginCheck = MyApplication.prefs.getString("name", "null")
        binding.ticket.setOnClickListener {
            Log.d("loginCheck", loginCheck.toString())
            if (loginCheck == "null"){
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(context, ProfileMenuActivity::class.java)
                startActivity(intent)
            }
        }
    }



}