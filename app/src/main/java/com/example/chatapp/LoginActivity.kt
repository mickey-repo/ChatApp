package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private val TAG : String = "LoginActivity"
    //Widgets
    lateinit var userET : EditText
    lateinit var passET : EditText
    lateinit var loginBtn : Button
    lateinit var registerBtn : Button

    //Firebase auth
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userET = findViewById(R.id.ET_email)
        passET = findViewById(R.id.ET_password)
        loginBtn = findViewById(R.id.bt_login)
        registerBtn = findViewById(R.id.btn_register)

        auth = Firebase.auth

        registerBtn.setOnClickListener{
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        loginBtn.setOnClickListener(View.OnClickListener {
            val userEmail = userET.text.toString()
            val pass = passET.text.toString()
            if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(pass)) {
                Toast.makeText(this@LoginActivity, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(userEmail, pass)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        })

    }
}