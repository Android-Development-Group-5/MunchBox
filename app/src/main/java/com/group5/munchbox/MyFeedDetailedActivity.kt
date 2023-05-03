package com.group5.munchbox
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
class MyFeedDetailedActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)
        val gson = Gson()
        val recipe = gson.fromJson(intent.getStringExtra("my_feed_recipe_extra"), RecipeModel::class.java)
        val recipeIngredientsList = recipe.recipeIngredients
        var storageReference = FirebaseStorage.getInstance().getReference()
        val user = FirebaseAuth.getInstance().currentUser
        var userId: String = ""
        if(user != null){
            userId = user.uid
        }
        val recipeImageView: ImageView = findViewById(R.id.detailed_recipe_image)
        val recipeName: TextView = findViewById(R.id.detailed_recipe_name)
        val recipeUsername: TextView = findViewById(R.id.detailed_recipe_username)
        val recipeDescription: TextView = findViewById(R.id.detailed_recipe_description)
        val detailedActivityRecyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val recipeInstruction: TextView = findViewById(R.id.detailed_recipe_instructions)
        val postInteraction = findViewById<View>(R.id.postInteraction)
        val externalLinkButton = findViewById<ImageButton>(R.id.externalLinkBtn)
        var upvoteCount: TextView = findViewById(R.id.upvoteCount)
        var downvoteCount: TextView = findViewById(R.id.downvoteCount)
        var commentCount: TextView = findViewById(R.id.commentCount)
        var upvote: ImageView = findViewById(R.id.upvote)
        var downvote: ImageView = findViewById(R.id.downvote)
        var upvoteLayout: LinearLayout = findViewById(R.id.upvoteLayout)
        var downvoteLayout: LinearLayout = findViewById(R.id.downvoteLayout)
        var commentsLayout: LinearLayout = findViewById(R.id.commentsLayout)

        detailedActivityRecyclerView.layoutManager = LinearLayoutManager(this)

        externalLinkButton.visibility = View.GONE
        recipeName.text = recipe.recipeName
        recipeUsername.text = recipe.userEmail
        recipeDescription.text = recipe.recipeDetails
        recipeInstruction.text = recipe.recipeInstructions
        detailedActivityRecyclerView.adapter = RecyclerViewAdapter(recipeIngredientsList)
        val imgPath = recipe.recipeImage
        Glide.with(this)
            .load(storageReference.child("$imgPath.png"))
            .transform(CenterCrop())
            .into(recipeImageView)
        commentCount.text = "0"
        if(recipe.comments != null){
            commentCount.text = recipe.comments?.size.toString()
        }
        upvoteCount.text = "0"
        if(recipe.upvotes != null){
            upvoteCount.text = recipe.upvotes?.size.toString()
        }
        downvoteCount.text = "0"
        if(recipe.downvotes != null){
            downvoteCount.text = recipe.downvotes?.size.toString()
        }
        upvote.setColorFilter(Color.rgb(0, 0, 0))
        if(recipe.upvotes != null){
            if(recipe.upvotes?.contains(userId) == true){
                upvote.setColorFilter(Color.rgb(0, 255, 0))
            }
        }
        downvote.setColorFilter(Color.rgb(0, 0, 0))
        if(recipe.downvotes != null){
            if(recipe.downvotes?.contains(userId) == true){
                downvote.setColorFilter(Color.rgb(0, 0, 255))
            }
        }
            upvoteLayout.setOnClickListener {
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
                var upvoters1: MutableList<String>? = mutableListOf<String>()
                if (recipe.upvotes != null){
                    upvoters1 = recipe.upvotes?.toMutableList()
                }
                if(upvoters1?.contains(user?.uid) == false){
                    upvoters1.add(userId)
                }
                val upvotes = upvoters1
                var downvoters1: MutableList<String>? = mutableListOf<String>()
                if (recipe.downvotes != null){
                    downvoters1 = recipe.downvotes?.toMutableList()
                }
                if(downvoters1?.contains(user?.uid) == true){
                    downvoters1.remove(userId)
                }
                val downvotes = downvoters1
                val comments = recipe.comments
                val recipe = RecipeModel(recipeId, ruserID, recipeName, recipeDetails, recipeImage, recipeInstructions, recipeIngredients, userEmail, upvotes, downvotes, comments)
                myRef.child(recipeId).setValue(recipe)
                    .addOnSuccessListener{
                        upvote.setColorFilter(Color.rgb(0, 255, 0))
                        downvote.setColorFilter(Color.rgb(0, 0, 0))
                        upvoteCount.text = upvotes?.size.toString()
                        downvoteCount.text = downvotes?.size.toString()
                    }

            }
            downvoteLayout.setOnClickListener {
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
                    var upvoters2: MutableList<String>? = mutableListOf<String>()
                    if (recipe.upvotes != null){
                        upvoters2 = recipe.upvotes?.toMutableList()
                    }
                    if(upvoters2?.contains(user?.uid) == true){
                        upvoters2.remove(userId)
                    }
                    val upvotes = upvoters2
                    var downvoters2: MutableList<String>? = mutableListOf<String>()
                    if (recipe.downvotes != null){
                        downvoters2 = recipe.downvotes?.toMutableList()
                    }
                    if(downvoters2?.contains(user?.uid) == false){
                        downvoters2.add(userId)
                    }
                    val downvotes = downvoters2
                    val comments = recipe.comments
                    val recipe = RecipeModel(recipeId, ruserID, recipeName, recipeDetails, recipeImage, recipeInstructions, recipeIngredients, userEmail, upvotes, downvotes, comments)
                    myRef.child(recipeId).setValue(recipe)
                        .addOnSuccessListener{
                            downvote.setColorFilter(Color.rgb(0, 0, 255))
                            upvote.setColorFilter(Color.rgb(0, 0, 0))
                            upvoteCount.text = upvotes?.size.toString()
                            downvoteCount.text = downvotes?.size.toString()
                        }
                }
            commentsLayout.setOnClickListener {
                // Navigate to Details screen and pass selected recipe
                val intent = Intent(this, CommentSectionActivity::class.java)
                val gson = Gson()
                val myJson = gson.toJson(recipe)
                intent.putExtra("my_feed_recipe_extra", myJson)
                this.startActivity(intent)
            }
            }
            private inner class RecyclerViewAdapter(private val ingredients: MutableList<IngredientsItem>?): RecyclerView.Adapter<RecyclerViewAdapter.IngredientViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.detailed_recipe_ingredients_item, parent, false)
                    return IngredientViewHolder(view)
                }
                override fun getItemCount(): Int = ingredients!!.size
                override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
                    val ingredient = ingredients?.get(position)?.name
                    holder.detailedIngredientsItem.text = ingredient
                }
                inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
                    val detailedIngredientsItem: TextView = itemView.findViewById(R.id.detailed_recipe_ingredient)
                }
            }
        }