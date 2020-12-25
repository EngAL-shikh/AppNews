package com.engalshikh.loginwithfirebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

private lateinit var email: EditText
private lateinit var pass: EditText
private lateinit var login: FloatingActionButton
private lateinit var number: EditText
private lateinit var   Ed_verify: EditText
private lateinit var toSignup: TextView
private lateinit var card_login_phone: CardView
private lateinit var card_login_email: CardView
private lateinit var useemail: TextView
private lateinit var usephone: TextView
private lateinit var verfy: FloatingActionButton
private var storedVerificationId: String? = ""
private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
class LoginActivity : AppCompatActivity() {
    var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        var currentUser = auth.currentUser
        if(currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity2::class.java))
            finish()
        }
        email = findViewById(R.id.email)
        pass = findViewById(R.id.pass)
        login = findViewById(R.id.login)
        number = findViewById(R.id.login_phone)
        Ed_verify = findViewById(R.id.Ed_verify)
        toSignup = findViewById(R.id.tosignup)
        card_login_phone = findViewById(R.id.card_login_phon)
        card_login_email = findViewById(R.id.card_login_email)
        verfy = findViewById(R.id.verfy)
        usephone = findViewById(R.id.usephone)
        useemail = findViewById(R.id.useemail)


        //Login with phone number

        usephone.setOnClickListener {
           cheackType("phone")

        }
        useemail.setOnClickListener {

            cheackType("email")

        }

        verfy.setOnClickListener {



            if (number.text.toString().trim().length<9&& Ed_verify.text.toString()==""){
                Toast.makeText(this, "Invlide Number", Toast.LENGTH_SHORT).show()
                number.setBackgroundResource(R.drawable.erorrshape)

            }

            if (number.text.toString()!=""){
                number.setBackgroundResource(R.color.white)
                login()
                Ed_verify.visibility=View.VISIBLE

                number.setText("")

            }else{

                var code=Ed_verify.text.toString().trim()
                if(!code.isEmpty()){
                    val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                            storedVerificationId.toString(), code)
                    signInWithPhoneAuthCredential(credential)
                }else{
                    Toast.makeText(this, "Enter Code", Toast.LENGTH_SHORT).show()

                }
        }
        }


        toSignup.setOnClickListener {
            var i=Intent(this, Signup::class.java)
            startActivity(i)
        }

        login.setOnClickListener {
        var email=email.text.toString().trim()

            if(!email.matches(emailPattern.toRegex())){
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()


            }else if(pass.text.toString().trim().length<6){
                Toast.makeText(this, "worng password ", Toast.LENGTH_SHORT).show()

            }else{
                sign()


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

                Log.d("code", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                Toast.makeText(this@LoginActivity, "done", Toast.LENGTH_LONG).show()

            }
        }
    }



    fun sign(){


        var email=email.text.toString()
        var pass= pass.text.toString()
        auth.signInWithEmailAndPassword(email, pass)

            .addOnCompleteListener(this) {

                if (it.isSuccessful) {

                    Log.d("tes", "success")
                    val user = auth.currentUser
                    var i= Intent(this, MainActivity2::class.java)
                    startActivity(i)

                } else {

                    Log.w("test", "failure", it.exception)

                    Toast.makeText(this, "Email or Password incorrect ", Toast.LENGTH_SHORT).show()

}

            }
    }



    //phon auth


    private fun login() {
        val mobileNumber=findViewById<EditText>(R.id.login_phone)
        var number=mobileNumber.text.toString().trim()

        if(!number.isEmpty()){
            number="+"+number
            sendVerificationcode(number)
        }else{
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show()
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
                            Toast.makeText(this, "The code worng", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
    }




    //cheacktype

    fun cheackType(type: String){

        if (type=="phone"){
            card_login_phone.visibility= View.VISIBLE
            card_login_email.visibility= View.GONE
            login.visibility=View.GONE
            verfy.visibility=View.VISIBLE
            useemail.visibility=View.VISIBLE
            usephone.visibility=View.GONE

        }else{

            card_login_phone.visibility= View.GONE
            card_login_email.visibility= View.VISIBLE
            login.visibility=View.VISIBLE
            verfy.visibility=View.GONE
            useemail.visibility=View.GONE
            usephone.visibility=View.VISIBLE

        }
    }
}