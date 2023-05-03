package com.group5.munchbox

import CommentsAdapter
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson


class CommentSectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comment_seciton)
        val gson = Gson()
        val recipe = gson.fromJson(intent.getStringExtra("my_feed_recipe_extra"), RecipeModel::class.java)
        val comments: MutableList<Comment>
        if(recipe.comments != null){
            comments = recipe.comments!!
        }else{
            comments = mutableListOf<Comment>()
        }
        var storageReference = FirebaseStorage.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser
        var commenterEmail: String = ""
        if(user != null){
            if(user.email != null){
                commenterEmail = user.email!!
            }
        }

        val commentEntry : EditText = findViewById(R.id.commentEntry)
        val commentSubmit : Button = findViewById(R.id.CommentSubmit)
        val commentsListRecyclerView : RecyclerView = findViewById(R.id.commentsList)
        commentsListRecyclerView.layoutManager = LinearLayoutManager(this)
        commentsListRecyclerView.adapter = CommentsAdapter(this, comments)
        commentSubmit.setOnClickListener {
            if(commentEntry.text.toString() != ""){
                val database = Firebase.database
                val myRef = database.getReference("Recipes")

                var recipeId = ""
                if (recipe.recipeId != null) {
                    recipeId = recipe.recipeId.toString()
                }
                val ruserID = recipe.userId
                val recipeName = recipe.recipeName
                val recipeDetails = recipe.recipeDetails
                val recipeImage = recipe.recipeImage
                val recipeInstructions = recipe.recipeInstructions
                val recipeIngredients = recipe.recipeIngredients
                val userEmail = recipe.userEmail
                val upvotes = recipe.upvotes
                val downvotes= recipe.downvotes
                val newComment = Comment(commenterEmail,commentEntry.text.toString())
                var comments: MutableList<Comment>? = mutableListOf<Comment>()
                if (recipe.comments != null){
                    comments = recipe.comments?.toMutableList()
                }
                comments?.add(newComment)
                val recipe = RecipeModel(recipeId, ruserID, recipeName, recipeDetails, recipeImage, recipeInstructions, recipeIngredients, userEmail, upvotes, downvotes, comments)
                myRef.child(recipeId).setValue(recipe)
                    .addOnSuccessListener{
                        Toast.makeText(this, "Comment Added", Toast.LENGTH_SHORT).show()
                        commentEntry.setText("")
                    }

            }
        }




    }
}