package com.example.petsapce_week1.loginrelated


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petsapce_week1.GifActivity
import com.example.petsapce_week1.R.*
import com.example.petsapce_week1.Signin4Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 123
    }

    // FirebaseAuth 의 인스턴스를 선언
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var myApplication: MyApplication


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_login)

        // onCreate() 메서드에서 FirebaseAuth 인스턴스를 초기화시키기
        firebaseAuth = FirebaseAuth.getInstance()

        //myAppliacation 초기화(로그인 정보 저장)
        myApplication = MyApplication()


        // 로그인 기능
        val loginBtn = findViewById<Button>(id.btn_email)
        loginBtn.setOnClickListener {
            // 이메일 로그인 버튼 클릭 시 실행되는 코드
            val emailEditText = findViewById<EditText>(id.editText_email)
            val passwordEditText = findViewById<EditText>(id.editTextPassword)

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            signInWithEmail(email, password)
        }

        //  회원가입 기능
        val joinBtn = findViewById<TextView>(id.btn_newAccount)
        joinBtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, Signin4Activity::class.java)
            startActivity(intent)
        }


        // Google 로그인 구성
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 로그인 버튼 클릭 시 이벤트 처리
        val signInButton = findViewById<Button>(id.btn_google)
        signInButton.setOnClickListener {
            signIn()

        }

    }

    // 이메일과 비밀번호로 로그인하는 함수
    private fun signInWithEmail(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공
                    val user = FirebaseAuth.getInstance().currentUser
                    val name = user?.displayName.toString()
                    // 기기에 로그인 정보 저장
                    MyApplication.prefs.setString("name", name)
                    // 로그인 성공 후 수행할 작업 추가
                    navigateToNextScreen(name)
                } else {
                    // 로그인 실패
                    Toast.makeText(this, "로그인 실패: ${task.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google 로그인이 성공한 경우 Firebase에 인증 정보를 전달합니다.
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account?.idToken)
            } catch (e: ApiException) {
                // Google 로그인이 실패한 경우 에러 처리를 수행합니다.
                Log.e(TAG, "Google sign-in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Firebase 인증이 성공한 경우 사용자가 로그인한 것으로 처리합니다.
                    val user = firebaseAuth.currentUser
                    val name = user?.displayName.toString()
                    //기기에 로그인 정보 저장
                    MyApplication.prefs.setString("name", name)

                    // 사용자 정보를 처리하거나 다음 화면으로 이동합니다.
                    navigateToNextScreen(name)
                } else {
                    // Firebase 인증이 실패한 경우 에러 처리를 수행합니다.
                    Log.e(TAG, "Firebase authentication failed", task.exception)

                }
            }
    }

    private fun navigateToNextScreen(user: String) {
        // 다음 화면으로 전환하는 코드를 작성합니다.
        val intent = Intent(this, GifActivity::class.java)
        intent.putExtra("userName", user)

        startActivity(intent)

        finish() // 현재 액티비티를 종료하여 뒤로가기 버튼을 눌렀을 때 로그인 화면으로 돌아가지 않도록 합니다.
    }


}