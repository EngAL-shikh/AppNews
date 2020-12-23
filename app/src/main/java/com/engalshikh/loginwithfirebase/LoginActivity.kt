package com.engalshikh.loginwithfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

private lateinit var email: EditText
private lateinit var pass: EditText
private lateinit var login: FloatingActionButton
private lateinit var number: EditText
private lateinit var passphone: EditText
private lateinit var toSignup: TextView
class LoginActivity : AppCompatActivity() {
    var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        email = findViewById(R.id.email)
        pass = findViewById(R.id.pass)
        login = findViewById(R.id.login)
        number = findViewById(R.id.login_phone)
        passphone = findViewById(R.id.pass_phone)
        toSignup = findViewById(R.id.tosignup)


        toSignup.setOnClickListener {
            var i=Intent(this,Signup::class.java)
            startActivity(i)
        }

        login.setOnClickListener {


            if(email.text.toString().trim().length<3){
                Toast.makeText(this,"يجب ادخال الاسم ", Toast.LENGTH_SHORT).show()


            }else if(pass.text.toString().trim().length<6){
                Toast.makeText(this,"كلمة المرور ضعيفة  ", Toast.LENGTH_SHORT).show()

            }else{
                sign()
                //regstertion()

            }


            // custom()


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



                }

            }
    }
}