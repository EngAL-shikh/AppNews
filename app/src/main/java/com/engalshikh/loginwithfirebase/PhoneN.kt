package com.engalshikh.loginwithfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit





class phoneN : AppCompatActivity() {
    lateinit var btnGenerateOTP : Button
    lateinit var btn_sign_in : Button
    lateinit var etPhoneNumber : EditText
    lateinit var etOTP : EditText
    lateinit var keynum : TextView

    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var auth : FirebaseAuth
    lateinit var verificationCode:String
    lateinit var phoneNumber:String
    lateinit var otp:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_n)

        btnGenerateOTP=findViewById(R.id.btn_generate_otp)
        keynum=findViewById(R.id.keynum)
        btn_sign_in=findViewById(R.id.btn_sign_in)
        etPhoneNumber=findViewById(R.id.et_phone_number)

        findViews()
        StartFirebaseLogin()


        btnGenerateOTP.setOnClickListener {
            var key= keynum.text.toString()
            phoneNumber = key+etPhoneNumber.text.toString()

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
            )

        }

        btn_sign_in.setOnClickListener {

            otp =etOTP.text.toString()

            val credential = PhoneAuthProvider.getCredential(verificationCode, otp)
            SigninWithPhone(credential)

        }

    }

    private fun findViews() {
        btnGenerateOTP = findViewById(R.id.btn_generate_otp)
        btn_sign_in = findViewById(R.id.btn_sign_in)

        etPhoneNumber = findViewById(R.id.et_phone_number)
        etOTP = findViewById(R.id.et_otp)
    }


    private fun StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance()
        mCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Toast.makeText(this@phoneN, "verification completed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@phoneN, "verification fialed", Toast.LENGTH_SHORT).show()
            }



            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG", "onCodeSent:$verificationId")

                 verificationCode = verificationId
                 Toast.makeText(this@phoneN, "Code sent", Toast.LENGTH_SHORT).show()

                // ...
            }
        }
    }

    private fun SigninWithPhone(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var i=Intent(this, MainActivity::class.java)
                    i.putExtra("number",etPhoneNumber.toString())


                    finish()
                } else {
                    Toast.makeText(this, "Incorrect OTP", Toast.LENGTH_SHORT).show()
                }
            }
    }


}
