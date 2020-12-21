package com.engalshikh.loginwithfirebase

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.security.cert.PolicyNode
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

private lateinit var email: EditText
private lateinit var pass: EditText
private lateinit var login: Button
private lateinit var number: EditText
private lateinit var uname: EditText
class Signup : AppCompatActivity() {
    var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        email = findViewById(R.id.email)
        pass = findViewById(R.id.pass)
        login = findViewById(R.id.login)
        number = findViewById(R.id.num)
        uname = findViewById(R.id.uname)

        login.setOnClickListener {


            if(uname.text.toString().trim().length<3){
                Toast.makeText(this,"يجب ادخال الاسم ",Toast.LENGTH_SHORT).show()


            }else if(pass.text.toString().trim().length<6){
                Toast.makeText(this,"كلمة المرور ضعيفة  ",Toast.LENGTH_SHORT).show()

            }else{
                emailAuth()

            }


            // custom()


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
                    var i=Intent(this, phoneN::class.java)
                    startActivity(i)

                } else {

                    Log.w("test", "createUserWithEmail:failure", it.exception)



                }

            }
    }

    fun custom(){
        var email=email.text.toString()
        var pass= pass.text.toString()
        email?.let {
            auth.signInWithCustomToken(email)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCustomToken:success")
                        val user = auth.currentUser

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCustomToken:failure", task.exception)


                    }
                }
        }
    }
}