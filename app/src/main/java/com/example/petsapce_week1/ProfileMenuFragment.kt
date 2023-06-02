package com.example.petsapce_week1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.petsapce_week1.databinding.FragmentProfileMenuBinding
import com.example.petsapce_week1.loginrelated.LoginActivity
import com.example.petsapce_week1.review.ReviewPostActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileMenuFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var binding: FragmentProfileMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileMenuBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        val name = user?.displayName
        val email = user?.email
        val photoUrl = user?.photoUrl

        // 프로필 화면에 사용자 정보를 표시합니다.
//        val profileNameTextView = findViewById<TextView>(R.id.profile_name_textview)

        if (name != null)
            binding.profileNickname.text =name.toString()
        if (email != null)
            binding.profileEmail.text =email.toString()

        val profileImageView = binding.imageButton
        if (photoUrl != null)
            Glide.with(this).load(photoUrl).into(profileImageView)


        binding.btnSettings.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
        }
        binding.btnProfileCy.setOnClickListener {
            val intent = Intent(context, ReviewPostActivity::class.java)
            startActivity(intent)
        }

//        binding.imageButton.setOnClickListener {
//            val intent = Intent(context, LoginActivity::class.java)
//            startActivity(intent)
//        }

        binding.linearLayout.setOnClickListener {
            val intent = Intent(context,ProfileActivity::class.java)
            startActivity(intent)
        }


        Log.d("name",name.toString())
        Log.d("email",email.toString())


        return binding.root
    }
}