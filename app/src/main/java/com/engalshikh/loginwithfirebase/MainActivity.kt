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



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         var sign:Button=findViewById(R.id.signup)

        sign.setOnClickListener {
            var i=Intent(this, Signup::class.java)
            startActivity(i)
        }

    }

}




