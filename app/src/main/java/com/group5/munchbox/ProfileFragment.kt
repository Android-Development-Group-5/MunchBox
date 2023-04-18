package com.group5.munchbox

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val picView = view.findViewById<ImageView>(R.id.imageView)
        Glide.with(view)
                    .load(auth.currentUser?.photoUrl)
                    .placeholder(R.drawable.outline_person_24)
                    .centerCrop() // scale image to fill the entire ImageView
                    .transform(RoundedCorners(30))
                    .into(picView)
        val nameView = view.findViewById<TextView>(R.id.NameView)
        auth.currentUser?.email?.let { Log.i("name", it) }
        nameView.text = auth.currentUser?.email
        val logoutButton : Button = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        return view

    }
}