package com.example.petsapce_week1.accommodation

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.petsapce_week1.accommodation.scroll.reviewFragment
import com.example.petsapce_week1.databinding.ActivityAccHostBinding
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

class AccMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityAccMainBinding
    lateinit var bindingHostBinding: ActivityAccHostBinding
    lateinit var adapter: accImgaeSlideAdapter
    var imgdataList = ArrayList<imageSlideData>()
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
        binding = ActivityAccMainBinding.inflate(layoutInflater)
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
                            if(!heartCheck){
                                btn_heart_before.setImageResource(R.drawable.heartfull)
                                heartCheck = true
                                Toast.makeText(this@AccMainActivity,"찜 리스트에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                btn_heart_before.setImageResource(R.drawable.heart3)
                                heartCheck = false
                                Toast.makeText(this@AccMainActivity,"찜 리스트에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
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
                        val imgUrlAdapter = accImgaeSlideAdapter(imgdataList)
                        binding.viewpager.adapter = imgUrlAdapter
                        imgUrlAdapter.notifyDataSetChanged()

                        // ================= frame host 호스트 ===================
                        binding.frameHost.textName.text = "erlopa"
                        binding.frameHost.tvMaxpet.text = "· 최대 반려동물 4마리"
                        binding.frameHost.tvMaxguest.text = "최대 인원 6명 "
                        val hostDescription = "성수동에 위치한 카페입니다. 1층 카페 공간에 편집샵과 북스토어가 함께 있습니다. " +
                                "모든 메뉴 및 케이터링, 대관 예약은 최소 7일전 미리 연락 주시면 됩니다. " +
                                "50% 예약금을 받고 있으니 이점 참고 부탁 드리며 전화나 인스타그램 DM으로도 예약 가능합니다. " +
                                "합리적인 가격으로 우수한 품질의 토스트와 커피 및 음료 등을 즐길 수 있습니다."
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
                        Glide.with(this@AccMainActivity)
                            .load(R.drawable.facultywifi)
                            .into(binding.frameFacility.imgFac1)
                        binding.frameFacility.tvFac2.text = "테이크 아웃 가능"
                        Glide.with(this@AccMainActivity)
                            .load(R.drawable.facultytakeout)
                            .into(binding.frameFacility.imgFac2)
                        binding.frameFacility.tvFac3.text = "유료 주차"
                        Glide.with(this@AccMainActivity)
                            .load(R.drawable.facultyparking)
                            .into(binding.frameFacility.imgFac3)
                        binding.frameFacility.tvFac4.text = "비건 메뉴"
                        Glide.with(this@AccMainActivity)
                            .load(R.drawable.facultyleaf)
                            .into(binding.frameFacility.imgFac4)
                        binding.frameFacility.tvFac5.text = "남/녀 구분화장실"
                        Glide.with(this@AccMainActivity)
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
            .replace(binding.frameGoogle.id, googleFragment(roomBeforeID.toLong()))
            .commitAllowingStateLoss()

        supportFragmentManager
            .beginTransaction()
            .add(binding.frameReview.id, reviewFragment(roomBeforeID.toLong()))
            .commitAllowingStateLoss()

        binding.frameFacility.tvViewmore.setOnClickListener {
            val intent = Intent(this@AccMainActivity, AccFacilityMoreActivity::class.java)
            startActivity(intent)
        }

        // ============== 예약 하기 버튼 ==============

        binding.btnReserve.setOnClickListener {
            Toast.makeText(this@AccMainActivity,"일정에 추가되었습니다.", Toast.LENGTH_SHORT).show()

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

    private fun initBefore() {

        val intentList = ArrayList<String>()
        val img = intent.getIntExtra("img", -1)
        val name = intent.getStringExtra("name")
        val location = intent.getStringExtra("location")
        val score = intent.getDoubleExtra("score", 0.0)
        val price = intent.getStringExtra("price")
        imgdataList.add(imageSlideData(img))
        binding.apply {

            tvHousename.text = name
            textAddress.text = location
            textStarscore.text = score.toString()
        }
    }

    private fun initData() {
        imgdataList.add(imageSlideData(R.drawable.item10))
        imgdataList.add(imageSlideData(R.drawable.item11))
        imgdataList.add(imageSlideData(R.drawable.item12))
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

