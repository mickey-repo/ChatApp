package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import javax.annotation.Nonnull

class RegisterActivity : AppCompatActivity() {
    private val TAG : String = "RegisterActivity"
    //Widgets
    lateinit var userET : EditText
    lateinit var passET : EditText
    lateinit var emailET : EditText
    lateinit var registerBtn : Button

    //Firebase auth
    lateinit var auth : FirebaseAuth
    lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Initialize widgets
        userET = findViewById(R.id.userName)
        passET = findViewById(R.id.password)
        emailET = findViewById(R.id.email)
        registerBtn = findViewById(R.id.register_button)

        //Firebase Auth
        auth = Firebase.auth

        //Add event listener to button
        registerBtn.setOnClickListener {
            val username: String = userET.text.toString()
            val password: String = passET.text.toString()
            val email: String = emailET.text.toString().trim()
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(
                    email
                )
            ) {
                Toast.makeText(
                    baseContext,
                    "username/password/email can not be empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                registerNow(username, password, email)
            }
        }
    }

    private fun registerNow(username : String, password : String, email : String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val userId = user?.uid
                    if (userId != null) {
                        dbRef = Firebase.database.reference.child("MyUsers").child(userId)
                    }
                    //HashMaps
                    val userMaps : MutableMap<String, String> = HashMap<String, String>()
                    userMaps["username"] = username
                    userMaps["password"] = password
                    userMaps["email"] = email

                    //Open MainActivity when registration successful
                    dbRef.setValue(userMaps).addOnCompleteListener(this) { dbTask ->
                        if (dbTask.isSuccessful) {
                            intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.w(TAG, "serValueToFireBaseDB:failure", dbTask.exception)
                            Toast.makeText(
                                baseContext,
                                "serValueToFireBaseDB failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}