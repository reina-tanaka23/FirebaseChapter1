package com.example.firebasechapter1

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.firebasechapter1.databinding.ActivityMainBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
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
    private var buttonFacebookLogin: Button? = null
    private var mAuth: FirebaseAuth? = null

    private var fbase: FirebaseApp? = null

    var callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        labelView = findViewById(com.example.firebasechapter1.R.id.labelView)
        emailText = findViewById(com.example.firebasechapter1.R.id.emailText)
        passText = findViewById(com.example.firebasechapter1.R.id.passText)


        // ここからfacebookログイン処理です。
        buttonFacebookLogin = findViewById<View>(com.example.firebasechapter1.R.id.login_button) as LoginButton
        (buttonFacebookLogin as LoginButton).setReadPermissions("email")

        // Callback registration
        (buttonFacebookLogin as LoginButton).registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                // App code
                val msg: String = "facebook login success token is : " + loginResult?.accessToken
                Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG)
                    .show()
            }

            override fun onCancel() {
                // App code
                Toast.makeText(this@MainActivity, "facebook login cancel", Toast.LENGTH_LONG)
                    .show()
            }

            override fun onError(exception: FacebookException) {
                // App code
                Toast.makeText(this@MainActivity, "facebook login error", Toast.LENGTH_LONG)
                    .show()
            }
        })

        // ここまでfacebookログイン処理です。

        setSupportActionBar(binding.toolbar)


        fbase = FirebaseApp.initializeApp(this)

        mAuth = FirebaseAuth.getInstance()

        binding.fab.setOnClickListener { view ->
            val msg: String = "Firebase: " + fbase!!.name
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
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
}