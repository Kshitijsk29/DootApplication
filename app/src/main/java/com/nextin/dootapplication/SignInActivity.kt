package com.nextin.dootapplication

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.nextin.dootapplication.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private var binding: ActivitySignInBinding? = null
    private lateinit var auth: FirebaseAuth

    private lateinit var  googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Login")
        progressDialog.setMessage("we are logining")

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this@SignInActivity, gso)

        binding?.btnGoogle?.setOnClickListener{
            val signInClient = googleSignInClient.signInIntent

        }

        binding?.btnSignIn?.setOnClickListener {
            progressDialog.show()
            auth.signInWithEmailAndPassword(
                binding?.etEmail?.text.toString(),
                binding?.etPassword?.text.toString()
            ).addOnCompleteListener { task ->
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

        binding?.tvClickSignup?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                startActivity(intent)
            }

        })

        if (auth.currentUser != null) {

            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            startActivity(intent)

        }
    }
val REQ_ONE_TAP = 2
    private var showOneTapUI = true
    private  fun signIn(){
        val signInClient = googleSignInClient.signInIntent
        startActivityForResult(signInClient,REQ_ONE_TAP)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with Firebase.
                            Log.d(TAG, "Got ID token.")
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    // ...
                }
            }
        }








//    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//        result ->
//        if(result.resultCode== RC_SIGN_IN ){
//
//            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            if (task.isSuccessful){
//                    val account : GoogleSignInAccount? = null
//                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
//                auth.signInWithCredential(credential).addOnCompleteListener{
//                    if (it.isSuccessful){
//                        Toast.makeText(this, " Sign in Done", Toast.LENGTH_LONG)
//
//                        startActivity(Intent(this, MainActivity::class.java))
//                    }
//                    else{
//                        Toast.makeText(this, " Sign in failed", Toast.LENGTH_LONG)
//                    }
//                }
//            }
//        }
//        else{
//            Toast.makeText(this, "failed", Toast.LENGTH_LONG).show()
//        }
//
//    }
}