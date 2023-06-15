package com.example.petsapce_week1.accommodation

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.petsapce_week1.R
import com.example.petsapce_week1.accommodation.scroll.AccFacilityMoreActivity
import com.example.petsapce_week1.accommodation.scroll.googleFragment
import com.example.petsapce_week1.accommodation.scroll.googleFragment2
import com.example.petsapce_week1.accommodation.scroll.reviewFragment
import com.example.petsapce_week1.databinding.ActivityAccHostBinding
import com.example.petsapce_week1.databinding.ActivityAccMain2Binding
import com.example.petsapce_week1.databinding.ActivityAccMainBinding
import com.example.petsapce_week1.home.homefragment.HomeMainData
import com.example.petsapce_week1.network.AccomoService
import com.example.petsapce_week1.network.LoginService
import com.example.petsapce_week1.network.ReservationAPI
import com.example.petsapce_week1.network.RetrofitHelper
import com.example.petsapce_week1.vo.FacilityData
import com.example.petsapce_week1.vo.ReservationCreateResponse
import com.example.petsapce_week1.vo.ReservationUserData
import com.example.petsapce_week1.vo.accomo_datamodel.AccomodationData
import kotlinx.android.synthetic.main.activity_acc_main.*
import kotlinx.android.synthetic.main.host_item_list.location
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.DecimalFormat

class AccMainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityAccMain2Binding
    lateinit var bindingHostBinding: ActivityAccHostBinding
    lateinit var adapter: accImgaeSlideAdapter2
    var imgdataList = ArrayList<imageSlideData2>()
    val reviewList = mutableListOf<FacilityData>()
    var photos = mutableListOf<String>()

    lateinit var roomId: String

    // ========== 백엔드 연동 부분 ===========
    private var retrofit: Retrofit = RetrofitHelper.getRetrofitInstance()

    // 기본 숙소 정보 불러올때 호출
    var api: AccomoService = retrofit.create(AccomoService::class.java)

    // 좋아요 버튼 눌렀을 때 호출
    var apiLike: AccomoService = retrofit.create(AccomoService::class.java)

    //============ 토큰 재발급 ==============
    var apiReissue: LoginService = retrofit.create(LoginService::class.java)

    private val MIN_SCALE = 0.85f // 뷰가 몇퍼센트로 줄어들 것인지
    private val MIN_ALPHA = 0.5f // 어두워지는 정도를 나타낸 듯 하다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val roomBeforeID = intent.getLongExtra("roomBeforeID", 1)

        //홈화면 리사이클러뷰에서 넘어감
        /*     val dataList = initBefore()
             Log.d("intentlist",dataList.toString())*/


        // ============ token ============
        //토큰 저장 객체
        var accessToken: String? = null
        val atpref = getSharedPreferences("accessToken", MODE_PRIVATE)
        if (atpref != null) {
            accessToken = atpref.getString("accessToken", "default")
        }
        val accessTokenPost = "Bearer $accessToken"

        // close btn
        binding.btnBack.setOnClickListener {
//            val intent = Intent(this, AccMainActivity::class.java)
//            startActivity(intent)
            finish()
        }

        // .bind와 .inflate 차이 / layoutinflater , view 객체 차이
        val includeView: View = binding.frameHost.root
        bindingHostBinding = ActivityAccHostBinding.bind(includeView)

        // 이런식으로 간략하게 쳐도됨
        // bindingHostBinding = ActivityAccHostBinding.bind(binding.frameHost.root)

        initBefore()
        initViewPager()
        initData()

        //val data = AccomodationRoomData(roomId = null)
//        val roomId : Long = intent.getLongExtra("content", -1)
//        Log.d("숙소 roomId", roomId.toString())

        // =================== 백엔드 연동 부분 =====================
        //홈화면 연결 후 roomId 받아오면 반영!
        api.getRoomDetail(accessTokenPost, roomBeforeID.toLong())
            .enqueue(object : Callback<AccomodationData> {
                @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<AccomodationData>,
                    response: Response<AccomodationData>
                ) {

                    Log.d("숙소 세부 정보 통신 성공", response.toString())
                    Log.d("숙소 세부 정보 통신 성공", response.body().toString())

                    val body = response.body()
                    if (body != null) {

                        //============ 좋아요 버튼 ==========
                        // like btn
                        var heartCheck = false
//                        if (response.body()!!.result.favorite) {
//                            if1Checked = 1
//                            binding.btnHeartAfter.visibility = View.VISIBLE
//                        } else {
//                            if1Checked = 0
//                            binding.btnHeartAfter.visibility = View.INVISIBLE
//                        }

                        binding.btnHeartBefore.setOnClickListener {
                            //if 로그인 정보 없으면, 로그인 화면으로 화면 이동
                            //있으면 밑에 if 문 돌리면 될듯

//                            if (if1Checked == 0) {
//                                binding.btnHeartAfter.visibility = View.VISIBLE
//                                if1Checked = 1
//                                // 상진쓰랑 할것
//                                api.postLikes(accessTokenPost, roomBeforeID.toLong())
//                                    .enqueue(object : Callback<AccomodationData> {
//                                        override fun onResponse(
//                                            call: Call<AccomodationData>,
//                                            response: Response<AccomodationData>
//                                        ) {
//                                            Log.d("숙소 좋아요 표시", "했음")
//                                        }
//
//                                        override fun onFailure(
//                                            call: Call<AccomodationData>,
//                                            t: Throwable
//                                        ) {
//                                            Log.d("숙소 좋아요 표시", "x했음")
//                                        }
//
//                                    })
//                            }
//                        }
                            binding.btnHeartBefore.setOnClickListener {
                                if (!heartCheck) {
                                    btn_heart_before.setImageResource(R.drawable.heartfull)
                                    heartCheck = true
                                    Toast.makeText(
                                        this@AccMainActivity2,
                                        "찜 리스트에 추가되었습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    btn_heart_before.setImageResource(R.drawable.heart3)
                                    heartCheck = false
                                    Toast.makeText(
                                        this@AccMainActivity2,
                                        "찜 리스트에서 삭제되었습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
//                            if (if1Checked == 1) {
//                                if1Checked = 0
//                                // 상진쓰랑 할것
//                                api.postLikes(accessTokenPost, roomBeforeID.toLong())
//                                    .enqueue(object : Callback<AccomodationData> {
//                                        override fun onResponse(
//                                            call: Call<AccomodationData>,
//                                            response: Response<AccomodationData>
//                                        ) {
//                                            Log.d("숙소 좋아요 표시", "했음")
//                                        }
//
//                                        override fun onFailure(
//                                            call: Call<AccomodationData>,
//                                            t: Throwable
//                                        ) {
//                                            Log.d("숙소 좋아요 표시", "x했음")
//                                        }
//
//                                    })
                            }
                        }
                        // ================ 맨 위 프레임 ==================


                        Log.d("숙소", "${body.result.roomImageUrls}")
                        Log.d("숙소", "$photos")
                        val imgUrlAdapter = accImgaeSlideAdapter2(imgdataList)
                        binding.viewpager.adapter = imgUrlAdapter
                        imgUrlAdapter.notifyDataSetChanged()

                        // ================= frame host 호스트 ===================
                        binding.frameHost.textName.text = "노스트레스 버거"
                        binding.frameHost.tvMaxguest.text = "최대 인원 4명 "
                        binding.frameHost.tvMaxpet.text = "· 최대 반려동물 3마리"
                        val hostDescription =
                            "해방촌 경사길에 위치한 ‘노스트레스버거’는 오리지널 미국식 햄버거 가게를 표방합니다. 노란 배경에 검은 글씨로 써진 간판과 메뉴판은 무심한듯하지만 강렬한 존재감을 뽐냅니다. 대표 메뉴는 버터를 발라 구운 번 안에 소고기 패티, 치즈, 잘게 다진 양파, 소스, 피클을 차례대로 올린 ‘CHEESE BURGER’. 일반적인 수제버거와 달리 패티를 얇게 펼쳐 튀기듯이 구워 바삭한 식감과 고소한 맛이 조화롭게 어울립니다. 치즈와 패티가 들어가는 개수에 따라 클래식, 더블, 트리플로 메뉴가 구성되어 있으며 별도로 치즈를 추가할 수 있습니다. 핫윙, 감자튀김, 해쉬브라운 등 곁들이기 좋은 사이드 메뉴도 준비되어 있습니다.\n"
                        binding.frameHost.textAbout.text = hostDescription

                    }
                    // ================ facility 편의시설 프레임 =====================
                    response.takeIf { it.isSuccessful }
                        ?.body()
                        .let {
                            if (it != null) {
                                for (item in it.result.facilities) {
                                    reviewList.apply {
                                        add(
                                            FacilityData(
                                                imgUrl = item.facilityImageUrl,
                                                facname = item.facilityName
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    for (i in 0 until reviewList.size) {
                        reviewList.add(
                            FacilityData(
                                reviewList[i].imgUrl,
                                reviewList[i].facname
                            )
                        )
                    }


                    if (reviewList.isNotEmpty()) {
                        Log.d("숙소 facility 리스트", "${reviewList}")
                        //binding.frameFacility.tvFac0.text = reviewList[0].facname
                        binding.frameFacility.tvFac1.text = "무선 인터넷"
                        Glide.with(this@AccMainActivity2)
                            .load(R.drawable.facultywifi)
                            .into(binding.frameFacility.imgFac1)
                        binding.frameFacility.tvFac2.text = "테이크 아웃 가능"
                        Glide.with(this@AccMainActivity2)
                            .load(R.drawable.facultytakeout)
                            .into(binding.frameFacility.imgFac2)
                        binding.frameFacility.tvFac3.text = "유료 주차"
                        Glide.with(this@AccMainActivity2)
                            .load(R.drawable.facultyparking)
                            .into(binding.frameFacility.imgFac3)
                        binding.frameFacility.tvFac4.text = "비건 메뉴"
                        Glide.with(this@AccMainActivity2)
                            .load(R.drawable.facultyleaf)
                            .into(binding.frameFacility.imgFac4)
                        binding.frameFacility.tvFac5.text = "남/녀 구분화장실"
                        Glide.with(this@AccMainActivity2)
                            .load(R.drawable.facultytoliet)
                            .into(binding.frameFacility.imgFac5)

                        binding.frameFacility.imgFac6.visibility = View.INVISIBLE
                        binding.frameFacility.tvFac6.visibility = View.INVISIBLE
                    }

                }

                override fun onFailure(call: Call<AccomodationData>, t: Throwable) {
                    Log.d("숙소 시설 facility 세부 정보", "failed")
                }
            })

        supportFragmentManager
            .beginTransaction()
            .replace(binding.frameGoogle.id, googleFragment2(roomBeforeID.toLong()))
            .commitAllowingStateLoss()

        supportFragmentManager
            .beginTransaction()
            .add(binding.frameReview.id, reviewFragment(roomBeforeID.toLong()))
            .commitAllowingStateLoss()

        binding.frameFacility.tvViewmore.setOnClickListener {
            val intent = Intent(this@AccMainActivity2, AccFacilityMoreActivity::class.java)
            startActivity(intent)
        }

        // ============== 예약 하기 버튼 ==============

        binding.btnReserve.setOnClickListener {
            Toast.makeText(this@AccMainActivity2, "일정에 추가되었습니다.", Toast.LENGTH_SHORT).show()

//            var apiReservation: ReservationAPI = retrofit.create(ReservationAPI::class.java)
//            apiReservation.postReservation(
//                accessTokenPost,
//                jsonParams = ReservationUserData(2, 1, "2023-02-20", "2023-02-21"),
//                1
//            ).enqueue(object : Callback<ReservationCreateResponse> {
//                override fun onResponse(
//                    call: Call<ReservationCreateResponse>,
//                    response: Response<ReservationCreateResponse>
//                ) {
//                    Log.d("예약 생성 통신 성공", response.toString())
//                    Log.d("예약 생성 통신 성공", response.body().toString())
//                    Toast.makeText(this@AccMainActivity, "예약 완료되었습니다.", Toast.LENGTH_LONG).show()
//                }
//
//                override fun onFailure(call: Call<ReservationCreateResponse>, t: Throwable) {
//                    Log.d("예약 생성 통신 실패", t.toString())
//                }
//
//            })
        }
    }


    @SuppressLint("SetTextI18n")
    private fun initBefore() {

        val intentList = ArrayList<String>()
        val img = intent.getIntExtra("img", -1)
        val name = intent.getStringExtra("name")
        val location = intent.getStringExtra("location")
        val score = intent.getDoubleExtra("score", 4.7)
        val price = intent.getStringExtra("price")
        val originPrice = intent.getStringExtra("origin")
        imgdataList.add(imageSlideData2(img))
        binding.apply {

            tvHousename.text = name
            textAddress.text = location
            textStarscore.text = score.toString()
            textPrice.text = "할인가 " + price.toString() + "원"
            textPriceOrigin.text = "정가 " + originPrice.toString() + "원"
            textPriceOrigin.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun initData() {
//        imgdataList.add(imageSlideData2(R.drawable.item5))
        imgdataList.add(imageSlideData2(R.drawable.nostress2))
        imgdataList.add(imageSlideData2(R.drawable.nostress3))
    }

    private fun initViewPager() {

        //binding.viewpager.adapter = accImgaeSlideAdapter(imgdataList)

        val pageMarginPx =
            resources.getDimensionPixelOffset(R.dimen.pageMargin) // dimen 파일 안에 크기를 정의해두었다.
        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pageWidth) // dimen 파일이 없으면 생성해야함
        val screenWidth = resources.displayMetrics.widthPixels // 스마트폰의 너비 길이를 가져옴
        val offsetPx = screenWidth - pageMarginPx - pagerWidth

        binding.viewpager.apply {
            setPageTransformer { page, position ->
                page.translationX = position * -offsetPx
            }
            setPageTransformer(ZoomOutPageTransformer())
            offscreenPageLimit = 1 // 몇 개의 페이지를 미리 로드 해둘것인지
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

        //기존 adapter(recyclerview adpater)
        /* binding.recyclerviewSlide.layoutManager = LinearLayoutManager(
             this, LinearLayoutManager.HORIZONTAL, false
         )
         adapter = accImgaeSlideAdapter(imgdataList)
         binding.recyclerviewSlide.adapter = adapter*/


        /* adapter.itemClickListener = object : accImgaeSlideAdapter.OnItemClickListener {
             override fun OnItemClick(data: imageSlideData) {
                 *//*  Toast.makeText(getActivity(),"show", Toast.LENGTH_SHORT).show()
                   val intent = packageManager.getLaunchIntentForPackage(data.appackname)
                   startActivity(intent)*//*
                val intent = Intent(this@AccMainActivity, GifActivity::class.java)
                startActivity(intent)

                Log.d("test", "test")
            }


        }*/
    }

    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }

                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }

                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
}

