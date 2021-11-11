package com.example.firebasechapter1

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.firebasechapter1.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var labelView: TextView? = null
    private var emailText: EditText? = null
    private var passText: EditText? = null
    private var mAuth: FirebaseAuth? = null

    private var fbase: FirebaseApp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        labelView = findViewById(R.id.labelView)
        emailText = findViewById(R.id.emailText)
        passText = findViewById(R.id.passText)

        fbase = FirebaseApp.initializeApp(this)

        mAuth = FirebaseAuth.getInstance()

        binding.fab.setOnClickListener { view ->
            val msg: String = "Firebase: " + fbase!!.name
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }


    fun doLogin(view: View?) {
        val email = emailText!!.text.toString() + ""
        val password = passText!!.text.toString() + ""
        Toast.makeText(
            this@MainActivity, "Login start.",
            Toast.LENGTH_SHORT
        ).show()
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity, "Logined!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = mAuth!!.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(
                        this@MainActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    fun doLogout(view: View?) {
        mAuth!!.signOut()
        Toast.makeText(
            this@MainActivity, "logouted...",
            Toast.LENGTH_SHORT
        ).show()
        updateUI(null)
    }


    fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            labelView!!.text = "no login..."
        } else {
            labelView!!.text = "login: " + user.email
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}