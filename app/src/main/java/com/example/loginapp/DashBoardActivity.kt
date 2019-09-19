package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_dash_board.*

class DashBoardActivity:AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_dash_board)

        auth = FirebaseAuth.getInstance()

        btn_changePassword.setOnClickListener {
            changePassword()
        }
    }
    private fun changePassword(){
        if (currentPassword.text.isNotEmpty()&&
                newPassword.text.isNotEmpty()&&
                ConfirmPassword.text.isNotEmpty()){
            if (newPassword.text.toString().equals(ConfirmPassword.text.toString())){

                val user: FirebaseUser? = auth.currentUser
                if(user != null && user.email != null){

                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials but there are multiple possible providers,

                    val credential = EmailAuthProvider
                        .getCredential(user!!.email!!,currentPassword.text.toString())
                    // Prompt the user to re-provide their sign-in credentials
                    user?.reauthenticate(credential)
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Re-Authentication successfull",
                                    Toast.LENGTH_LONG
                                ).show()
                                 user?.updatePassword(newPassword.text.toString())
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this,
                                                "Password Change Successful",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            auth.signOut()
                                            startActivity(Intent(this, MainActivity::class.java))

                                        }
                                    }
                                  }else{
                                Toast.makeText(
                                    this,
                                    "Re-Authentication failed, try again",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                              }

                }else{
                    startActivity(Intent(this, MainActivity::class.java))

                }

            }else{
                Toast.makeText(this, "Password mismatch, try again.", Toast.LENGTH_LONG).show()
            }

            }else{
                Toast.makeText(this, "Please enter all fields.", Toast.LENGTH_LONG).show()
        }
    }
}