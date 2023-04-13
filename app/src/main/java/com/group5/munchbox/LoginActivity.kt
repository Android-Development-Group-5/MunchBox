package com.group5.munchbox

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    fun loggedIn() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            loggedIn()
        }
        var mainButton: Button = findViewById(R.id.mainButton)
        var switchButton: Button = findViewById(R.id.switchButton)
        var helpText: TextView = findViewById(R.id.helpText)
        var usernameText: EditText = findViewById(R.id.editTextTextPersonName)
        var passwordText: EditText = findViewById(R.id.editTextTextPassword)
        var layout: View = findViewById(R.id.constraintLayout)
        mainButton.setOnClickListener {
            if (usernameText.text.length == 0 || passwordText.text.length == 0) {
                Snackbar.make(layout, "Make sure you've written a valid username/password combination.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            when (mainButton.text) {
                "login" -> {
                    auth.signInWithEmailAndPassword(usernameText.text.toString(),
                        passwordText.text.toString()
                    )
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")
                                val user = auth.currentUser
                                loggedIn()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Snackbar.make(layout, "Authentication failed.", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                }
                "sign up" -> {
                    auth.createUserWithEmailAndPassword(usernameText.text.toString(),
                        passwordText.text.toString()
                    )
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = auth.currentUser
                                loggedIn()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Snackbar.make(layout, "Authentication failed.", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }

        switchButton.setOnClickListener {
            when (mainButton.text) {
                "login" -> {
                    mainButton.text = "sign up"
                    switchButton.text = "login"
                    helpText.text = "Already have an account?"
                }
                "sign up" -> {
                    mainButton.text = "login"
                    switchButton.text = "sign up"
                    helpText.text = "Need an account?"
                }
            }
        }


    }
}