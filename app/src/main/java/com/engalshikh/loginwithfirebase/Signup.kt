package com.engalshikh.loginwithfirebase

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.security.cert.PolicyNode
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

private lateinit var email: EditText
private lateinit var pass: EditText
private lateinit var cpass: EditText
private lateinit var signup: FloatingActionButton
private lateinit var verfy: FloatingActionButton
private lateinit var number: EditText
private lateinit var uname: EditText
private lateinit var usephone: TextView
private lateinit var cardemail: CardView
private lateinit var cardphone: CardView
private lateinit var useemail: TextView
private lateinit var Ed_verify: EditText
private lateinit var BackToLogin: TextView
private var storedVerificationId: String? = ""
private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
lateinit var auth : FirebaseAuth
class Signup : AppCompatActivity() {
    var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        email = findViewById(R.id.email)
        pass = findViewById(R.id.pass)
        cpass = findViewById(R.id.cpass)
        signup = findViewById(R.id.signup)
        number = findViewById(R.id.num)
        uname = findViewById(R.id.uname)
        Ed_verify = findViewById(R.id.Ed_verify)
        usephone=findViewById(R.id.userpnenumber)
        useemail=findViewById(R.id.useemail)
        cardemail=findViewById(R.id.card_sign_email)
        cardphone=findViewById(R.id.card_sign_phone)
        verfy=findViewById(R.id.bt_verify)
        BackToLogin=findViewById(R.id.backToLogin)


        BackToLogin.setOnClickListener {
            var i=Intent(this,LoginActivity::class.java)
            startActivity(i)
        }
        auth=FirebaseAuth.getInstance()
        var currentUser = auth.currentUser
        if(currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity2::class.java))
            finish()
        }

        ///////////////verify
        verfy.setOnClickListener{
            var otp=Ed_verify.text.toString().trim()
            if(!otp.isEmpty()){
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId.toString(), otp)
                signInWithPhoneAuthCredential(credential)
            }else{
                Toast.makeText(this,"Enter OTP",Toast.LENGTH_SHORT).show()
                Ed_verify.setBackgroundResource(R.drawable.erorrshape)
            }

            signup.visibility=View.VISIBLE
            verfy.visibility=View.GONE
            Ed_verify.visibility=View.GONE
            Ed_verify.setText("")
        }


        usephone.setOnClickListener {
            siginUpCheak("phone")
        }
        useemail.setOnClickListener {
            siginUpCheak("email")
        }



// Regstertion
        signup.setOnClickListener {

            if (Ed_verify.text.toString()==""){
                if(uname.text.toString().trim().length<3){
                    Toast.makeText(this,"Name is Empty ",Toast.LENGTH_SHORT).show()
                    uname.setBackgroundResource(R.drawable.erorrshape)


                }else if(pass.text.toString().trim().length<6){
                    Toast.makeText(this,"Low password  ",Toast.LENGTH_SHORT).show()
                    pass.setBackgroundResource(R.drawable.erorrshape)
                }else if(!email.text.trim().matches(emailPattern.toRegex())){
                    email.setBackgroundResource(R.drawable.erorrshape)
                    Toast.makeText(this,"Invlide email adriss",Toast.LENGTH_SHORT).show()
                }else if (pass.text.toString()!=cpass.text.toString()){

                    Toast.makeText(this,"The password not the same ",Toast.LENGTH_SHORT).show()
                    pass.setBackgroundResource(R.drawable.erorrshape)
                    cpass.setBackgroundResource(R.drawable.erorrshape)
                }else{

                    emailAuth()
                }

            }else{

                login()
                signup.visibility=View.GONE
                verfy.visibility=View.VISIBLE
                Ed_verify.visibility=View.VISIBLE
            }






        }


        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                Toast.makeText(this@Signup,"done",Toast.LENGTH_LONG).show()
                // var intent = Intent(applicationContext,Verify::class.java)
                //intent.putExtra("storedVerificationId",storedVerificationId)
                // startActivity(intent)
            }
        }



    }


    fun emailAuth(){


        var email=email.text.toString()
        var pass= pass.text.toString()
        auth.createUserWithEmailAndPassword(email, pass)

            .addOnCompleteListener(this) {

                if (it.isSuccessful) {

                    Log.d("tes", "createUserWithEmail:success")
                    val user = auth.currentUser
                    var i=Intent(this, MainActivity2::class.java)
                    startActivity(i)

                } else {

                    Log.w("test", "createUserWithEmail:failure", it.exception)



                }

            }
    }




    //phon auth


    private fun login() {
        val mobileNumber=findViewById<EditText>(R.id.num)
        var number=mobileNumber.text.toString().trim()

        if(!number.isEmpty()){
            number="+"+number
            sendVerificationcode (number)
        }else{
            Toast.makeText(this,"Enter mobile number",Toast.LENGTH_SHORT).show()
        }
    }
    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }




    //Verify
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity2::class.java))
                    finish()
// ...
                } else {
// Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
// The verification code entered was invalid
                        Toast.makeText(this,"Invalid OTP",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }





    fun siginUpCheak(type:String){

        if (type=="email"){
            cardphone.visibility= View.GONE
            cardemail.visibility= View.VISIBLE
            usephone.visibility=View.VISIBLE
            useemail.visibility=View.GONE
            number.setText("")

        }else{

            cardphone.visibility= View.VISIBLE
            cardemail.visibility= View.GONE
            usephone.visibility=View.GONE
            useemail.visibility=View.VISIBLE

        }


    }

}