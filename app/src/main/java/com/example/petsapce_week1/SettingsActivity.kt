package com.example.petsapce_week1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.petsapce_week1.databinding.ActivitySettingsBinding
import com.example.petsapce_week1.home.HomeActivity
import com.example.petsapce_week1.home.homefragment.ProfileFragment
import com.example.petsapce_week1.loginrelated.LogoutBackendResponse
import com.example.petsapce_week1.network.AccomoService
import com.example.petsapce_week1.network.LoginService
import com.example.petsapce_week1.network.RetrofitHelper
import com.example.petsapce_week1.vo.ReservationReadResponse
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SettingsActivity : AppCompatActivity() {
    lateinit var binding : ActivitySettingsBinding
    private lateinit var firebaseAuth: FirebaseAuth

    var accessToken : String ?= null
    var accommoList = ArrayList<ReservationReadResponse.Reservation>()

    private fun getAccessToken() {
        val atpref = getSharedPreferences("accessToken", Context.MODE_PRIVATE)
        accessToken = atpref.getString("accessToken", "default")
        accessToken = "Bearer $accessToken"
    }


    // ========== 백엔드 연동 부분 ===========
    private var retrofit: Retrofit = RetrofitHelper.getRetrofitInstance()
    // 기본 숙소 정보 불러올때 호출
    var api : LoginService = retrofit.create(LoginService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {

        firebaseAuth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSettingsBack.setOnClickListener {
            val intent = Intent(this, ProfileMenuFragment::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            // Firebase 인증 로그아웃
            FirebaseAuth.getInstance().signOut()

            // 로그아웃 후 원하는 화면으로 전환
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}