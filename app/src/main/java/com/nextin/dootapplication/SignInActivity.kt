package com.nextin.dootapplication


import android.app.ProgressDialog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

import com.nextin.dootapplication.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private var binding: ActivitySignInBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = Firebase.auth

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Login")
        progressDialog.setMessage("we are logining")


        binding?.btnSignIn?.setOnClickListener {
                progressDialog.show()
                auth.signInWithEmailAndPassword(binding?.etEmail?.text.toString(),
                    binding?.etPassword?.text.toString()).addOnCompleteListener { task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful) {
                        val intent = Intent(this@SignInActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@SignInActivity,
                            task.exception?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

        binding?.tvClickSignup?.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        if (auth.currentUser != null) {

            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            startActivity(intent)

        }
    }

}