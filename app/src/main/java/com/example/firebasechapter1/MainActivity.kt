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
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var labelView: TextView? = null
    private var nameText: EditText? = null
    private var dataText: EditText? = null

    private var database: FirebaseDatabase? = null

    private var fbase: FirebaseApp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        labelView = findViewById(R.id.textView)
        nameText = findViewById(R.id.nameText)
        dataText = findViewById(R.id.dataText)

        database = FirebaseDatabase.getInstance()
        val people = database!!.getReference("people")

        // Read from the database
        people.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val gt_indicator: GenericTypeIndicator<List<Person?>?> =
                    object : GenericTypeIndicator<List<Person?>?>() {}
                val values = snapshot.getValue(gt_indicator)
                var res = ""
                if (values != null) {
                    for (p in values) {
                        res += p.toString() + "\n"
                    }
                }
                dataText!!.setText(res)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

        setSupportActionBar(binding.toolbar)

        fbase = FirebaseApp.initializeApp(this)

        binding.fab.setOnClickListener { view ->
            val msg: String = "Firebase: " + fbase!!.name
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    fun doAction(view: View?) {
        val myRef = database!!.getReference("message")
        val s = nameText!!.text.toString() + ""
        myRef.setValue(s)
        Toast.makeText(
            this@MainActivity, "write data!",
            Toast.LENGTH_SHORT
        ).show()
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