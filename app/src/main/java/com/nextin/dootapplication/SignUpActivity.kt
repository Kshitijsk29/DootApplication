package com.nextin.dootapplication

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.nextin.dootapplication.Models.User
import com.nextin.dootapplication.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private var binding :ActivitySignUpBinding? = null

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = Firebase.auth

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Creating Account")
        progressDialog.setMessage("we are creating account")

        val database = Firebase.database

        binding?.btnSignup?.setOnClickListener {
            progressDialog.show()
            auth.createUserWithEmailAndPassword(
                binding?.etEmail?.text.toString(),
                binding?.etPassword?.text.toString()
            ).addOnCompleteListener(OnCompleteListener
            { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    val user = User(binding?.etUsername?.text.toString(),
                        binding?.etEmail?.text.toString(),binding?.etPassword?.text.toString())
                   val id = task.result.user?.uid
                    if (id != null) {
                        database.reference.child("Users").child(id).setValue(user)
                    }
                    Toast.makeText(
                        this@SignUpActivity,
                        "Account Sign Up is Successful", Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@SignUpActivity,
                        task.exception?.message, Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
    }
