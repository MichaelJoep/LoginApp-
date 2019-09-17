package com.example.loginapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.signup_main.*
import android.view.View as View1

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        signup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
    login.setOnClickListener {
        AppLogin()
    }
    btn_forgot_password.setOnClickListener{
         val builder = AlertDialog.Builder(this)
        builder.setTitle("Forgot Password")
        val view = layoutInflater.inflate(R.layout.forgot_password_dialog, null)
        val username: EditText = view.findViewById(R.id.userName)
        builder.setView(view)
        builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
            forgotPassword(username)
        })
        builder.setNegativeButton("Close", DialogInterface.OnClickListener { _, _ ->  })

       }
    }


    private fun forgotPassword(username: EditText){
        if (username.text.toString().isEmpty()) {
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            return


            auth.sendPasswordResetEmail(username.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email Sent", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }
private fun AppLogin(){
    if (email.text.toString().isEmpty()) {
        email.error = "Please Enter Your Email"
        email.requestFocus()
        return
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
        email.error = "Enter a valid E-mail"
        email.requestFocus()
        return
    }
    if (password.text.toString().isEmpty()) {
        password.error = "Please enter a password"
        password.requestFocus()
        return
    }
    auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                 val user = auth.currentUser
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Login failed.",
                       Toast.LENGTH_LONG).show()
                updateUI(null)
            }


        }

}
    public override fun onStart() {
        super.onStart()
        // check if user is signed in (non-null) and update UI accordingly
        // Initialize Firebase Auth
        val currentUser: FirebaseUser? = auth.currentUser
        updateUI(currentUser)

    }

    fun updateUI(currentUser: FirebaseUser?) {
    if(currentUser != null){
        if(currentUser.isEmailVerified) {
            startActivity(Intent(this, DashBoardActivity::class.java))
            finish()
        }else{
            Toast.makeText(this, "Please verify email address",
                Toast.LENGTH_LONG).show()
        }
    }else{
        Toast.makeText(this, "Fail to login, try again",
            Toast.LENGTH_LONG).show()
    }
    }
}



