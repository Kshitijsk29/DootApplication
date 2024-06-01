package com.nextin.dootapplication


import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.core.utilities.Utilities
import com.nextin.dootapplication.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private var binding: ActivitySignInBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var googleSignInClient: GoogleSignInClient


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

        binding?.btnGoogle?.setOnClickListener {
            val signInClient = googleSignInClient.signInIntent
            signIn()
        }
        val email = binding?.etEmail?.text.toString()
       val pass = binding?.etPassword?.text.toString()

        binding?.btnSignIn?.setOnClickListener {
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(this,
                    "Please Fill the credentials",
                    Toast.LENGTH_LONG).show()
            }
            else if(pass.length<8){
                Toast.makeText(this,
                    "Password is too short ",
                    Toast.LENGTH_LONG).show()
            }
            else{
                progressDialog.show()
                auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener { task ->
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

    val REQ_ONE_TAP = 2
    private var showOneTapUI = true
    private fun signIn() {
        val signInClient = googleSignInClient.signInIntent
        startActivityForResult(signInClient, REQ_ONE_TAP)
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

        val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
        val idToken = googleCredential.googleIdToken
        when {
            idToken != null -> {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success")

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.exception)

                        }
                    }
            }
            else -> {
                // Shouldn't happen.
                Log.d(TAG, "No ID token!")
            }
        }
    }
}