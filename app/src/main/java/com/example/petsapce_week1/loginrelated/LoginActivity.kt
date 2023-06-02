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
    lateinit var myApplication:MyApplication


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
                    Toast.makeText(this, "로그인 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
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




    // 회원가입 기능
//        val joinBtn = findViewById<Button>(R.id.joinBtn)
//        joinBtn.setOnClickListener {
//
//            val email = findViewById<EditText>(R.id.email)
//            val password = findViewById<EditText>(R.id.password)
//
//            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(this, "성공", Toast.LENGTH_LONG).show()
//
//                    } else {
//                        Toast.makeText(this, "실패", Toast.LENGTH_LONG).show()
//                    }
//                }
//
//        }
//
//        // 로그인 기능
//        val loginBtn = findViewById<Button>(R.id.loginBtn)
//        loginBtn.setOnClickListener {
//
//            val email = findViewById<EditText>(R.id.email)
//            val password = findViewById<EditText>(R.id.password)
//
//            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_LONG).show()
//                    } else {
//                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_LONG).show()
//                    }
//                }
//        }
//
//        // 비회원 로그인 기능
//        val btn = findViewById<Button>(R.id.noLoginBtn)
//        btn.setOnClickListener {
//
//            auth.signInAnonymously()
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//
//                        val user = auth.currentUser
//
//                        Log.d("MainActivity", user!!.uid)
//
//                    } else {
//
//                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
//
//                    }
//                }
//        }
//
//        // 로그아웃 기능
//        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
//        logoutBtn.setOnClickListener {
//            Firebase.auth.signOut()
//            Toast.makeText(this, "로그아웃", Toast.LENGTH_LONG).show()
//        }

//    }
}
//
//    private var retrofit: Retrofit = RetrofitHelper.getRetrofitInstance() // RetrofitClient의 instance 불러오기
//    private var authToken : String ?= null
//    val bearer = "Bearer "
//    var token: String ?= null
//    var refreshToken_received : String ?= null
//    var api : LoginService = retrofit.create(LoginService::class.java)
//
//    private lateinit var binding: ActivityLoginBinding

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        //id, password check
//        initFlag()
//
//        //회원가입 화면으로 넘어감(채윤 화면)
//        initNext()
//
//        // == kakao login ==
//        retrofit = Retrofit.Builder()
//            .baseUrl("https://99f0-211-106-114-186.jp.ngrok.io/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        /* 지우지 말것!!!!
//        //로그인 정보 확인
//        UserApiClient.instance.accessTokenInfo{ tokenInfo, error ->
//            if(error != null){
//                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
//            }
//            else if(tokenInfo != null) {
//                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
//            }
//        }
//         */
//
//        binding.btnKakao.setOnClickListener {
//            kakaoLogin()
//        }
//
//    }
//    // ================ 카카오 로그인 ==================
//    private fun kakaoLogin() {
//
//
//
//        // 카카오계정으로 로그인 공통 callback 구성
//        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
//        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//            if (error != null) {
//                when {
//                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
//                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
//                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
//                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
//                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
//                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
//                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.ServerError.toString() -> {
//                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
//                    }
//                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
//                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
//                    }
//                    else -> { // Unknown
//                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } else if (token != null) {
//                Log.i(ContentValues.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
//                authToken = token.accessToken
//                Log.d("access_token", "${authToken}authToken")
//                //UserModel(accessToken = authToken.toString())
//                //data = UserModel(accessToken = authToken)
//                //saveData(id, pw)
//                Log.d("access_token2", "$authToken")
//
//                //bearer 붙이나??
//                //authToken = bearer + authToken
//                Log.d("로그인 내가 보낸거", token.toString())
//
//
//
//                api.postAccessToken(UserModelKakao(accessToken = authToken)).enqueue(object : Callback<LoginBackendResponse>{
//                    override fun onResponse(call: Call<LoginBackendResponse>, response: Response<LoginBackendResponse>) {
//                        Log.d("로그인 통신 성공", response.toString())
//                        Log.d("로그인 통신 성공", response.body().toString())
//                        Log.d("로그인 통신 id", response.body()?.result?.email.toString())
//                        Log.d("로그인 통신 at", response.body()?.result?.accessToken.toString())
//
//                        saveIDPW(response.body()?.result?.email.toString(), "")
//                        saveATRT(response.body()?.result?.accessToken.toString(), response.body()?.result?.refreshToken.toString())
//
//                        when (response.code()) {
//                            200 -> {
//                                Log.d("로그인 성공" , "ggg")
//                            }
//                            405 -> Toast.makeText(
//                                this@LoginActivity,
//                                "로그인 실패 : 아이디나 비번이 올바르지 않습니다",
//                                Toast.LENGTH_LONG
//                            ).show()
//                            500 -> Toast.makeText(
//                                this@LoginActivity,
//                                "로그인 실패 : 서버 오류",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
//                        startActivity(intent)
//                    }
//                    override fun onFailure(call: Call<LoginBackendResponse>, t: Throwable) {
//                        Log.d("통신 로그인..", "전송 실패")
//                    }
//                })
//            }
//        }
//
//        UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = callback)

//        UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity) { token, error ->
//
//            if (error != null) {
//                Log.e(ContentValues.TAG, "카카오톡으로 로그인 실패", error)
//
//                // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
//                // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
//                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
//                    return@loginWithKakaoTalk
//                }
//
//                // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
//                UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = callback)
//            } else if (token != null) {
//                Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
//
//            }
//            else {
//                UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = callback)
//        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
//        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
//            UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity) { token, error ->
//
//                if (error != null) {
//                    Log.e(ContentValues.TAG, "카카오톡으로 로그인 실패", error)
//
//                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
//                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
//                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
//                        return@loginWithKakaoTalk
//                    }
//
//                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
//                    UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = callback)
//                } else if (token != null) {
//                    Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
//
//                }
//            }
//        } else {
//            UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = callback)
//            api.postAccessToken(UserModelKakao(accessToken = authToken)).enqueue(object : Callback<LoginBackendResponse>{
//                override fun onResponse(call: Call<LoginBackendResponse>, response: Response<LoginBackendResponse>) {
//                    Log.d("설치 로그인 통신 성공", response.toString())
//                    Log.d("설치 로그인 통신 성공", response.body().toString())
//                    Log.d("설치 로그인 통신 id", response.body()?.result?.email.toString())
//                    Log.d("설치 로그인 통신 at", response.body()?.result?.accessToken.toString())
//
//                    saveIDPW(response.body()?.result?.email.toString(), "")
//                    saveATRT(response.body()?.result?.accessToken.toString(), response.body()?.result?.refreshToken.toString())
//
//                    when (response.code()) {
//                        200 -> {
//                            Log.d("로그인 성공" , "ggg")
//                        }
//                        405 -> Toast.makeText(
//                            this@LoginActivity,
//                            "로그인 실패 : 아이디나 비번이 올바르지 않습니다",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        500 -> Toast.makeText(
//                            this@LoginActivity,
//                            "로그인 실패 : 서버 오류",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
//                    startActivity(intent)
//                }
//                override fun onFailure(call: Call<LoginBackendResponse>, t: Throwable) {
//                    Log.d("통신 로그인..", "전송 실패")
//                }
//            })
//        }

//
//    fun initFlag() {
//
//        binding.apply {
//
//            btnEmail.setOnClickListener {
//
//                val inputEmail = editTextEmail.text.toString()
//                val inputPassword = editTextPassword.text.toString()
//
//                // ==================== 일반 로그인 백엔드 통신 부분 ===================
//                val data = UserModelGeneral(inputEmail, inputPassword)
//                api.userLogin(data).enqueue(object : Callback<LoginBackendResponse> {
//                    override fun onResponse(
//                        call: Call<LoginBackendResponse>,
//                        response: Response<LoginBackendResponse>
//                    ) {
//                        Log.d("로그인 통신 성공",response.toString())
//                        Log.d("로그인 HTTP 코드", response.code().toString())
//                        Log.d("로그인 통신 성공", response.body().toString())
//
//                        when (response.code()) {
//                            200 -> {
//                                // == 기기 db (shared preference가) 로 저장
//                                saveIDPW(inputEmail, inputPassword)
//                                saveATRT(response.body()?.result?.accessToken.toString(), response.body()?.result?.refreshToken.toString())
//                                Log.d("일반 로그인 데이터 저장", "saved")
//                                Log.d("일반 로그인 데이터 저장", "${response.body()?.result?.accessToken}")
//                            }
//                            400 -> Toast.makeText(this@LoginActivity, "로그인 실패 : 아이디나 비번이 올바르지 않습니다", Toast.LENGTH_LONG).show()
//                            500 -> Toast.makeText(this@LoginActivity, "로그인 실패 : 서버 오류", Toast.LENGTH_LONG).show()
//                        }
//
//                        if(response.body()?.isSuccess == true) {
//                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
//                            startActivity(intent)
//
//                        }
//                        //틀리면 빨간글자 뜨게함
//                        else {
//                            editTextEmail.setBackgroundResource(R.drawable.btn_custom_red)
//                            editTextPassword.setBackgroundResource(R.drawable.btn_custom_red)
//                            textViewWarningRed.visibility = View.VISIBLE
//
//                            /*editTextEmail.visibility = View.GONE
//                            editTextPassword.visibility = View.GONE
//                            editTextEmailRed.visibility = View.VISIBLE
//                            editTextPasswordRed2.visibility = View.VISIBLE*/
//
//                            api.userLogin(data).enqueue(object : Callback<LoginBackendResponse> {
//                                override fun onResponse(
//                                    call: Call<LoginBackendResponse>,
//                                    response: Response<LoginBackendResponse>
//                                ) {
//                                    Log.d("틀린 로그인 통신 성공",response.toString())
//                                    Log.d("틀린 로그인 HTTP 코드", response.code().toString())
//                                    Log.d("틀린 로그인 통신 성공", response.body().toString())
//
//                                    when (response.code()) {
//                                        200 -> {
//                                            // == 기기 db (shared preference) 로 저장
//                                            saveIDPW(inputEmail, inputPassword)
//                                            saveATRT(response.body()?.result?.accessToken.toString(), response.body()?.result?.refreshToken.toString())
//                                            Log.d("일반 틀린 로그인 데이터 저장", "${response.body()?.result?.accessToken}")
//                                        }
//                                        400 -> Toast.makeText(this@LoginActivity, "로그인 실패 : 아이디나 비번이 올바르지 않습니다", Toast.LENGTH_LONG).show()
//                                        500 -> Toast.makeText(this@LoginActivity, "로그인 실패 : 서버 오류", Toast.LENGTH_LONG).show()
//                                    }
//                                }
//
//                                override fun onFailure(call: Call<LoginBackendResponse>, t: Throwable) {
//                                    // 실패
//                                    Log.d("틀린 로그인 통신 실패",t.message.toString())
//                                    Log.d("틀린 로그인 통신 실패","fail")
//                                }
//                            })
//                        }
//                    }
//
//                    override fun onFailure(call: Call<LoginBackendResponse>, t: Throwable) {
//                        // 실패
//                        Log.d("로그인 통신 실패",t.message.toString())
//                        Log.d("로그인 통신 실패","fail")
//                    }
//                })
//
//            }
//        }
//    }

//    private fun initNext() {
//        binding.apply {
//            btnNewAccount.setOnClickListener {
//                val intent = Intent(this@LoginActivity, Signin4Activity::class.java)
//                startActivity(intent)
//            }
//        }
//    }

//    fun saveIDPW( id : String, pw : String){
//        val prefID  : SharedPreferences = getSharedPreferences("userID", MODE_PRIVATE)
//        val prefPW  : SharedPreferences= getSharedPreferences("userPW", MODE_PRIVATE)
//        val editID  : SharedPreferences.Editor = prefID.edit()
//        val editPW  : SharedPreferences.Editor = prefPW.edit()
//        editID.putString("id", id).apply()
//        editPW.putString("pw", pw).apply()
//
//        Log.d("로그인 데이터", "saved")
//    }
//    fun saveATRT( at: String, rt : String){
//        //토큰 저장 객체
//        val prefAccessToken : SharedPreferences = getSharedPreferences("accessToken", MODE_PRIVATE)
//        val prefRefreshToken : SharedPreferences = getSharedPreferences("refreshToken", MODE_PRIVATE)
//
//        val editAT : SharedPreferences.Editor  = prefAccessToken.edit()
//        val editRT :SharedPreferences.Editor = prefRefreshToken.edit()
//        editAT.putString("accessToken", at).apply()
//        editRT.putString("refreshToken", rt).apply()
//
//        Log.d("로그인 tokens", "saved")
//        Log.d("로그인 tokens", "$at, $rt")
//    }
//}