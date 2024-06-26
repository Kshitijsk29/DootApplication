package com.nextin.dootapplication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

import com.nextin.dootapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarExcerise)

        auth = Firebase.auth
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menus, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menuSetting -> Toast.makeText(this, "Welcome to setting", Toast.LENGTH_LONG).show()
            R.id.menuLogout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Logout")
                builder.setMessage("Do you want to Logout ?")
                builder.setIcon(R.drawable.baseline_logout)
                builder.setPositiveButton("Yes",DialogInterface.OnClickListener
                { dialog, which ->
                    auth.signOut()
                    val intent = Intent(this@MainActivity , SignInActivity::class.java)
                    startActivity(intent)
                })
                builder.setNegativeButton("No",DialogInterface.OnClickListener
                { dialog, which ->  })

                builder.show()
            }
        }
        return true
    }
}