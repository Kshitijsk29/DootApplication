package com.nextin.dootapplication

import android.app.ProgressDialog
import android.content.Intent
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
                progressDialog.dismiss()
                if (task.isSuccessful) {

                    val user = User(binding?.etUsername?.text.toString(),
                        binding?.etEmail?.text.toString(),binding?.etPassword?.text.toString())

                    val intent = Intent(this@SignUpActivity ,
                        SignInActivity::class.java)
                    startActivity(intent)

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

        binding?.tvAlreadyHaveAccount!!.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(this@SignUpActivity,
                    SignInActivity::class.java)

                startActivity(intent)
            }

        })

    }
}
