package com.example.petsapce_week1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.petsapce_week1.databinding.ActivitySignin2Binding
import com.example.petsapce_week1.loginrelated.LoginActivity
import com.example.petsapce_week1.network.AccomoService
import com.example.petsapce_week1.network.LoginService
import com.example.petsapce_week1.network.RetrofitHelper
import com.example.petsapce_week1.vo.EmailCheckResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.net.URLEncoder
import java.util.regex.Pattern

class Signin4Activity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signin_2)


        firebaseAuth = FirebaseAuth.getInstance()

        val joinBtn = findViewById<Button>(R.id.btnContinue_after)
        joinBtn.setOnClickListener {
            // 이메일 로그인 버튼 클릭 시 실행되는 코드
            val emailEditText = findViewById<EditText>(R.id.editText_email)
            val passwordEditText = findViewById<EditText>(R.id.editText_password)
            val confirmPasswordEditText = findViewById<EditText>(R.id.editText_password_again)
            val nameEditText = findViewById<EditText>(R.id.editText_name)



            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val name = nameEditText.text.toString()


            signUpWithEmail(email, password, confirmPassword, name)
        }

    }

    // 이메일과 비밀번호로 회원가입하는 함수
    // 회원가입 함수
    // 회원가입 함수
    private fun signUpWithEmail(email: String, password: String, confirmPassword: String, name: String) {
        // 비밀번호 확인
        if (password != confirmPassword) {
            // 비밀번호가 일치하지 않는 경우
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase를 사용하여 회원가입
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 회원가입 성공
                    val user = FirebaseAuth.getInstance().currentUser

                    // 사용자 이름 저장
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                // 사용자 이름 저장 성공
                                // 회원가입 성공 후 수행할 작업 추가
                                navigateToNextScreen()
                            } else {
                                // 사용자 이름 저장 실패
                                Toast.makeText(this, "사용자 이름 저장 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // 회원가입 실패
                    Toast.makeText(this, "회원가입 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToNextScreen() {
        // 다음 화면으로 전환하는 코드를 작성합니다.
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // 현재 액티비티를 종료하여 뒤로가기 버튼을 눌렀을 때 로그인 화면으로 돌아가지 않도록 합니다.
    }

}

//        binding = ActivitySignin2Binding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        viewModel = ViewModelProvider(this).get(EmailViewModel::class.java)
////        viewModel.currentValue.observe(this, this)
//
//        val nameObserver = Observer<Int> { it ->
//            flagButton = it.toInt()
//        }
//        viewModel.currentValue.observe(this,nameObserver)
//
//
//        //순서대로 실행(별 상관없는듯함)
//
//        //1번 이메일 체크
//        inintEmailCheck()
//
//
//        //1-1 이메일 중복 체크
//        //임의의 이메일 선언
//        initButtonCheck()
//
//
//
//        //2번 패스워드 체크
//        initPasswordCheck()
//        //3번 패스워드 동일 체크
//        initPasswordEqual()
//
//
//    }
//
//    //중복 확인(코드 수정 필요..textwather에 넣어야 할듯 이것도...)
//    private fun initButtonCheck(){
//
//        binding.emailDuplicationAfter.setOnClickListener {
//            val email = binding.editTextEmail.text.toString().trim()
//            Log.d("이메일", email)
//            val encodedEmail = URLEncoder.encode(email, "UTF-8")
//            api.EmailCheck(email = encodedEmail).enqueue(object : Callback<EmailCheckResponse>{
//                override fun onResponse(
//                    call: Call<EmailCheckResponse>,
//                    response: Response<EmailCheckResponse>
//                ) {
//                    Log.d("이메일 중복체크 통신 성공",response.toString())
//                    Log.d("이메일 중복체크 통신 성공", response.body().toString())
//                    if(response.body()?.isSuccess == false){
//                        flagButton = 0
//                        viewModel.plusValue(flagButton)
////                    emailDuplicationAfter.isEnabled = false
//                        binding.textEmail.setTextColor(
//                            ContextCompat.getColor(
//                                applicationContext!!,
//                                R.color.red
//                            )
//                        )
//                        binding.editTextEmail.setBackgroundResource(R.drawable.btn_emailbox_error)
//                        binding.textEmail.text = "중복된 이메일입니다. 다른 이메일을 사용해주세요."
//                    }
//                    else{
//                        flagButton = 1
//                        viewModel.plusValue(flagButton)
////                    emailDuplicationAfter.isEnabled = true
//                        binding.textEmail.setTextColor(
//                            ContextCompat.getColor(
//                                applicationContext!!,
//                                R.color.main_green
//                            )
//                        )
//                        binding.textEmail.text = "사용 가능한 이메일 입니다."
//                    }
//                }
//
//                override fun onFailure(call: Call<EmailCheckResponse>, t: Throwable) {
//                    Log.d("이메일 중복 체크 실패", "ㅠㅠ")
//                }
//
//            })
//
//        }

//        binding.apply {
//            binding.emailDuplicationAfter.setOnClickListener {
//                if (email == editTextEmail.text.toString().trim()) {
//                    flagButton = 0
//                    viewModel.plusValue(flagButton)
////                    emailDuplicationAfter.isEnabled = false
//                    textEmail.setTextColor(
//                        ContextCompat.getColor(
//                            applicationContext!!,
//                            R.color.red
//                        )
//                    )
//                    editTextEmail.setBackgroundResource(R.drawable.btn_emailbox_error)
//                    textEmail.text = "중복된 이메일입니다. 다른 이메일을 사용해주세요."
//
//                } else {
//                    flagButton = 1
//                    viewModel.plusValue(flagButton)
////                    emailDuplicationAfter.isEnabled = true
//                    textEmail.setTextColor(
//                        ContextCompat.getColor(
//                            applicationContext!!,
//                            R.color.main_green
//                        )
//                    )
//                    textEmail.text = "사용 가능한 이메일 입니다."
//
//                }
////                emailDuplicationAfter.isEnabled = true
//            }
//        }
//    }
//
//    //1. 이메일
//    fun inintEmailCheck() {
//
//        //textwathcher
//        binding.editTextEmail.addTextChangedListener(object : TextWatcher {
//
//            override fun afterTextChanged(s: Editable?) {
//                // text가 변경된 후 호출
//                // s에는 변경 후의 문자열이 담겨 있다.
//
//            }
//
//            override fun beforeTextChanged(
//                s: CharSequence?,
//                start: Int,
//                count: Int,
//                after: Int
//            ) {
//                // text가 변경되기 전 호출
//                // s에는 변경 전 문자열이 담겨 있다.
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // text가 바뀔 때마다 호출된다.
//                checkEmail()
//
//
//                val flagCheck = viewModel.currentValue.value
//                Log.d("flagbtnn",flagCheck.toString())
//
//
//                //다음화면 넘어가기(사용자가 비밀번호까지 입력하고 변심하여 이메일을 바꿀수도 있기에 체크해야함)
//                if ((flagEmail == 1) && (flagEqual == 1) && (flagPassword == 1) && flagCheck ==1) {
////                if (checkEmail() && checkPassword() && checkPasswordEqual()) {
//
//                    binding.btnContinueAfter.isEnabled = true
//
//                    initNext()
//
//                } else {
//                    binding.btnContinueAfter.isEnabled = false
//
//                }
//            }
//        })
//    }
//
//    //2. 패스워드
//    fun initPasswordCheck() {
//
//        binding.editTextPassword.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                // text가 변경된 후 호출
//                // s에는 변경 후의 문자열이 담겨 있다.
//
//            }
//
//            override fun beforeTextChanged(
//                s: CharSequence?,
//                start: Int,
//                count: Int,
//                after: Int
//            ) {
//                // text가 변경되기 전 호출
//                // s에는 변경 전 문자열이 담겨 있다.
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // text가 바뀔 때마다 호출된다.
//                checkPassword()
//
//                passwordInput = binding.editTextPassword.text.toString()
//
//
//                //다음화면 넘어가기(사용자가 비밀번호까지 입력하고 변심 바꿀수도 있기에 체크해야함)
//
//                val flagCheck = viewModel.currentValue.value
//                Log.d("flagbtnn",flagCheck.toString())                //현재까지 입력된 패스워드 저장
//                //다음화면 넘어가기(사용자가 비밀번호까지 입력하고 변심하여 이메일을 바꿀수도 있기에 체크해야함)
//                if ((flagEmail == 1) && (flagEqual == 1) && (flagPassword == 1) && flagCheck ==1) {
//                    binding.btnContinueAfter.isEnabled = true
//                    Log.d("flagEmail", flagEmail.toString())
//                    Log.d("flagPassword", flagPassword.toString())
//                    Log.d("flagEqual", flagEqual.toString())
//
//                    initNext()
//
//                } else {
//                    binding.btnContinueAfter.isEnabled = false
//
//                }
//            }
//        })
//
//    }
//
//    //3. 패스워드 동일
//    fun initPasswordEqual() {
//
//        binding.apply {
//            val flagCheck = viewModel.currentValue.value
//
//            editTextPasswordAgain.addTextChangedListener(object : TextWatcher {
//                override fun afterTextChanged(s: Editable?) {
//                    // text가 변경된 후 호출
//                    // s에는 변경 후의 문자열이 담겨 있다.
//                    Log.d("flagbtnn3",flagCheck.toString())
//
//                }
//
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                    // text가 변경되기 전 호출
//                    // s에는 변경 전 문자열이 담겨 있다.
//                }
//
//                // text가 바뀔 때마다 호출된다.
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    //비밀번화 일치 true false로 반환
//                    checkPasswordEqual()
//
//                    val flagCheck = viewModel.currentValue.value
//
//
//                    //다음화면 넘어가기(사용자가 비밀번호까지 입력하고 변심하여 이메일을 바꿀수도 있기에 체크해야함)
//                    if ((flagEmail == 1) && (flagEqual == 1) && (flagPassword == 1) && flagCheck ==1) {
//                        binding.btnContinueAfter.isEnabled = true
//                        Log.d("flagEmail", flagEmail.toString())
//                        Log.d("flagPassword", flagPassword.toString())
//                        Log.d("flagEqual", flagEqual.toString())
//
//                        initNext()
//
//                    } else {
//                        binding.btnContinueAfter.isEnabled = false
//
//                    }
//                }
//            })
//        }
//    }
//
//
//    //1번 이메일 형식 체크
//    fun checkEmail(): Boolean {
//        binding.apply {
//
//            var email = editTextEmail.text.toString().trim()
//            val pattern: Pattern = Patterns.EMAIL_ADDRESS
//            if (pattern.matcher(email).matches()) {
//
//                editTextEmail.setBackgroundResource(R.drawable.btn_emailbox)
//                textEmail.text = "사용 가능한 이메일 형식 입니다."
//                textEmail.setTextColor(
//                    ContextCompat.getColor(
//                        applicationContext!!,
//                        R.color.main_green
//                    )
//                )
//                flagEmail = 1
//                saveEmail(email)
//                return true
//            } else {
//                editTextEmail.setBackgroundResource(R.drawable.btn_emailbox_error)
//                textEmail.text = "맞는 이메일 형식을 입력하세요"
//                textEmail.setTextColor(ContextCompat.getColor(applicationContext!!, R.color.red))
//                //또는 questionEmail.setTextColor(R.color.red.toInt())
//
//
//                flagEmail = 0
//                return false
//            }
//        }
//    }
//
//    //2번 비밀번호 체크
//    fun checkPassword(): Boolean {
//        binding.apply {
//
//            var password = editTextPassword.text.toString().trim() //공백제거
//            val pattern =
//                Pattern.compile("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,}$")
//
//            val macher = pattern.matcher(password)
//            if (macher.matches()) {
//
//                editTextPassword.setBackgroundResource(R.drawable.btn_custom)
//                textPassword.text = "사용 가능한 비밀번호 입니다."
//                textPassword.setTextColor(
//                    ContextCompat.getColor(
//                        applicationContext!!,
//                        R.color.main_green
//                    )
//                )
//                flagPassword = 1
//
//                return true
//            } else {
//                editTextPassword.setBackgroundResource(R.drawable.btn_custom_red)
//                textPassword.text = "특수문자 포함 8자 이상 입력해야 합니다."
//                textPassword.setTextColor(ContextCompat.getColor(applicationContext!!, R.color.red))
//                //또는 questionEmail.setTextColor(R.color.red.toInt())
//                flagPassword = 0
//
//                return false
//            }
//        }
//    }
//
//    //3번 패스워드 동일 체크
//    fun checkPasswordEqual(): Boolean {
//        var passwordEqual = passwordInput.trim()
//        binding.apply {
//
//            //비밀번화 워쳐와 현재 eiditext가 일치하는 경우
//            if (passwordEqual == editTextPasswordAgain.text.toString()) {
//
//                editTextPasswordAgain.setBackgroundResource(R.drawable.btn_custom)
//                textPasswordAgain.text = "비밀번호와 일치합니다."
//                textPasswordAgain.setTextColor(
//                    ContextCompat.getColor(
//                        applicationContext!!,
//                        R.color.main_green
//                    )
//                )
//                //val encodedpw = URLEncoder.encode(passwordInput, "UTF-8")
//                savePW(passwordEqual)
//                Log.d("pw",passwordEqual)
//                Log.d("pw",passwordEqual)
//
//                flagEqual = 1
//                return true
//            } else {
//                editTextPasswordAgain.setBackgroundResource(R.drawable.btn_custom_red)
//                textPasswordAgain.text = "비밀번호와 일치하지 않습니다. 다시 확인해주세요."
//                textPasswordAgain.setTextColor(
//                    ContextCompat.getColor(
//                        applicationContext!!,
//                        R.color.red
//                    )
//                )
//                flagEqual = 0
//                return false
//            }
//        }
//    }
//
//    private fun initNext() {
//        binding.apply {
//
//            btnContinueAfter.setOnClickListener {
//
//                val intent = Intent(this@Signin4Activity, SigninDescriptionActivity::class.java)
//                startActivity(intent)
//
//            }
//        }
//    }
//
//    fun saveEmail(email : String){
//        val prefEmail : SharedPreferences = getSharedPreferences("userEmail", MODE_PRIVATE)
//        val editEmail : SharedPreferences.Editor = prefEmail.edit()
//        editEmail.putString("email", email).apply()
//        Log.d("db 회원가입 email", email)
//        Log.d("db 회원가입 email", editEmail.toString())
//    }
//    fun savePW(pw : String){
//        val prefPW : SharedPreferences = getSharedPreferences("userPw", MODE_PRIVATE)
//        val editPW : SharedPreferences.Editor = prefPW.edit()
//        editPW.putString("pw", pw).apply()
//        Log.d("db 회원가입 비번", pw)
//    }
//}