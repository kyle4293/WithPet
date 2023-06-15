package com.example.petsapce_week1.accommodation.scroll

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petsapce_week1.GifActivity
import com.example.petsapce_week1.R
import com.example.petsapce_week1.databinding.FragmentReviewBinding
import com.example.petsapce_week1.network.AccomoService
import com.example.petsapce_week1.network.RetrofitHelper
import com.example.petsapce_week1.reviewrelated.ReviewReadMoreActivity
import com.example.petsapce_week1.vo.ReviewData
import com.example.petsapce_week1.vo.accomo_datamodel.AccomodationData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class reviewFragment(val roomId : Long) : Fragment() {

    private lateinit var binding:FragmentReviewBinding
    lateinit var adapter: reviewAdapter
    var dataList = ArrayList<reviewData>()
    //백엔드 서버 연동
    private var retrofit: Retrofit = RetrofitHelper.getRetrofitInstance()
    var api : AccomoService = retrofit.create(AccomoService::class.java)

    val accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ5c2xpbTM3QG5hdmVyLmNvbSIsImlhdCI6MTY3NTMyMTY0NywiZXhwIjoxNjc1MzIzNDQ3fQ.4CDgFa2fp_b-9fEuDiiwPkTR3SC23bI23NYOEdBiSB8"
    val accessTokenPost = "Bearer $accessToken"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val reviewList = mutableListOf<ReviewData>()
        binding = FragmentReviewBinding.inflate(layoutInflater)

        binding.btnReviewMore.setOnClickListener {
            val intent = Intent(activity, ReviewReadMoreActivity::class.java)
            startActivity(intent)
        }
        initRecyclerView()

        // ============== 백엔드 연동 =================

        api.getRoomDetail(accessToken = accessTokenPost, roomId = roomId).enqueue(object : Callback<AccomodationData> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<AccomodationData>,
                response: Response<AccomodationData>
            ) {
                Log.d("숙소 세부 정보 review 통신 성공",response.toString())
                Log.d("숙소 세부 정보 review 통신 성공", response.body().toString())
                
                val body = response.body()
                if (body != null) {

                    //frame host data
                    binding.textStarscore.text = body.result.roomAverageScore.toString()
                    binding.textReviewcount.text = body.result.reviewCount.toString()

                    response.takeIf { it.isSuccessful }
                        ?.body()
                        .let {
                            if (it != null) {
                                for (item in it.result.reviewPreviews) {
                                    reviewList.apply {
                                        add(
                                            ReviewData(
                                                createdAt = item.createdAt,
                                                description = item.description,
                                                nickname = item.nickname,
                                                userId = item.userId,
                                                score = item.score,
                                                profileImage = item.profileImage
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    var score = ArrayList<Int>()
                    score.add(5)
                    score.add(4)

                    var description = ArrayList<String>()
                    description.add("성수동에 조용하고 한적한 카페 가고 싶어서 찾아가게 된 성수카페 에롤파.."+
                            "가보니 분위기도 너무 좋고 비건카페다보니 더욱 마음에 들더라구요. 우리 멍멍이도 좋아해요^^.")
                    description.add("애견동반 카페라서 친구 강아지와 함께 갔다. 주인 분도 강아지를 엄청 좋아하시나보다.")


                    for (i in 0 until 2) {
                        /*
                        // 날짜 데이터 현재시간 기준으로 계산 안할 경우
                        val new_date = reviewList[i].createdAt.substring(0, reviewList[i].createdAt.indexOf("T"))
                        val new_date_list = new_date.split("-")
                        Log.d("숙소 Date1", "${new_date_list[0] + "년 " + new_date_list[1] + "월 " + new_date_list[2] + "일"}")
                        val date = "${new_date_list[0]}년 ${new_date_list[1]}월 ${new_date_list[2]}일"
                         */


                        dataList.add (
                            reviewData(
                                reviewList[i].profileImage,
                                score[i],
                                reviewList[i].nickname,
                                reviewList[i].createdAt,
                                description[i])
                        )
                    }

                    //Log.d("숙소 ===", dataList[0].toString())
                    val vreviewAdapter = context?.let { reviewAdapter(dataList, it) }
                    binding.recyclerview.adapter = vreviewAdapter
                }
            }
            override fun onFailure(call: Call<AccomodationData>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initRecyclerView() {

        //기존 adapter(recyclerview adpater)
        binding.recyclerview.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        adapter = context?.let { reviewAdapter(dataList, it) }!!
        binding.recyclerview.adapter = adapter


        adapter.itemClickListener = object : reviewAdapter.OnItemClickListener {
            override fun OnItemClick(data: reviewData) {
                /*  Toast.makeText(getActivity(),"show", Toast.LENGTH_SHORT).show()
                   val intent = packageManager.getLaunchIntentForPackage(data.appackname)
                   startActivity(intent)*/
                val intent = Intent(context, GifActivity::class.java)
                startActivity(intent)

                Log.d("test", "test")
            }
        }
    }

}