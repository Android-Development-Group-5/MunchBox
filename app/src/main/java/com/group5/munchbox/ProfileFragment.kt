package com.group5.munchbox

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.options
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.group5.munchbox.AddRecipeFragment.Companion.IMAGE_REQUEST_CODE
import kotlin.math.log

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var picView: ImageView
    private lateinit var uid: String

    var image_path = ""
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
        uid = auth.currentUser?.uid.toString()
        picView = view.findViewById<ImageView>(R.id.imageView)
        picView.setOnLongClickListener {

            pickImageFromGallery()
            true
        }

        Glide.with(view)
            .load(FirebaseStorage.getInstance().getReference("images").child("users").child("$uid.png"))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            //.placeholder(R.drawable.baseline_downloading_24) //loading
            .error(R.drawable.outline_person_24) //in case of user not having an image
            .centerCrop() // scale image to fill the entire ImageView
            .transform(RoundedCorners(100))
            .into(picView)
        val nameView = view.findViewById<TextView>(R.id.NameView)
        auth.currentUser?.email?.let { Log.i("name", it) }
        nameView.text = auth.currentUser?.email
        val logoutButton : Button = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
        val discoveriesButton : Button = view.findViewById(R.id.discoveriesButton)
        discoveriesButton.setOnClickListener {
            startActivity(Intent(activity, DiscoveriesActivity::class.java))
        }
        return view

    }

    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(intent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("vals", (requestCode.toString()) + " " + resultCode.toString())
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null){
            picView.setImageURI(data?.data)
            image_path = data?.data.toString()
            val storageRef = FirebaseStorage.getInstance().getReference("images/users/$uid.png")
            storageRef.delete().addOnSuccessListener{d->

            }.addOnFailureListener{

            }
            storageRef.putFile(image_path.toUri())

            Firebase.database.getReference("Users").child(uid).child("picture").setValue("images/users/$uid.png")

        }
    }
}