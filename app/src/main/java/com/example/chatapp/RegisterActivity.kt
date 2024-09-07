package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class RegisterActivity : AppCompatActivity() {
    //Widgets
    lateinit var userET : EditText
    lateinit var passET : EditText
    lateinit var emailET : EditText
    lateinit var registerBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Initialize widgets
        userET = findViewById(R.id.userName)
        passET = findViewById(R.id.password)
        emailET = findViewById(R.id.email)
        registerBtn = findViewById(R.id.register_button)
    }
}