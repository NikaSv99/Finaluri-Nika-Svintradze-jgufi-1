package com.example.finaluri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var editTextName: EditText
    private lateinit var editTextImageUrl: EditText
    private lateinit var buttonSave: Button

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("ProfileInfo")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        init()

        registerListeners()

        database.child(auth.currentUser?.uid!!).addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                val profileInfo: ProfileInfo = snapshot.getValue(ProfileInfo::class.java) ?: return

                textView.text = profileInfo.name

                Glide.with(this@ProfileActivity)
                    .load(profileInfo.url)
                    .into(imageView)

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun init() {
        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
        editTextName = findViewById(R.id.editTextName)
        editTextImageUrl = findViewById(R.id.editTextImageUrl)
        buttonSave = findViewById(R.id.buttonSave)
    }

    private fun registerListeners() {

        buttonSave.setOnClickListener {

            val name = editTextName.text.toString()
            val url = editTextImageUrl.text.toString()

            val profileInfo = ProfileInfo(name, url)

            database.child(auth.currentUser?.uid!!).setValue(profileInfo)

        }

    }

}