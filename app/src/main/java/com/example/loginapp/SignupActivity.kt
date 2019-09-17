package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.signup_main.*
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_main)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        btn_signup.setOnClickListener {
            signUpUser()
        }
    }

    fun signUpUser() {
        if (name.text.toString().isEmpty()) {
            name.error = "Please Enter your Name"
            name.requestFocus()
            return
        }
        if (emailText.text.toString().isEmpty()) {
            emailText.error = "Please Enter Your Email"
            emailText.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText.text.toString()).matches()) {
            emailText.error = "Enter a valid E-mail"
            emailText.requestFocus()
            return
        }
        if (passwordText.text.toString().isEmpty()) {
            passwordText.error = "Please enter a password"
            passwordText.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(emailText.text.toString(), passwordText.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                                }
                        }

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(
                        baseContext, "Sign up fail, please try again later.",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
    }
}








