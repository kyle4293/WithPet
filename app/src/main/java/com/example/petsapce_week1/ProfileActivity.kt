package com.example.petsapce_week1

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseAuth = FirebaseAuth.getInstance()

        // Firebase 인증이 성공한 경우 사용자가 로그인한 것으로 처리합니다.
        val user = firebaseAuth.currentUser

        // 사용자 정보를 가져옵니다.
        val name = user?.displayName
        val email = user?.email
        val photoUrl = user?.photoUrl

        // 프로필 화면에 사용자 정보를 표시합니다.
        val profileNameTextView = findViewById<TextView>(R.id.profile_name_textview)
        profileNameTextView.text = name

        val profileEmailTextView = findViewById<TextView>(R.id.profile_email_textview)
        profileEmailTextView.text = email

        val profileImageView = findViewById<ImageView>(R.id.profile_imageview)
        Glide.with(this).load(photoUrl).into(profileImageView)
    }


}
