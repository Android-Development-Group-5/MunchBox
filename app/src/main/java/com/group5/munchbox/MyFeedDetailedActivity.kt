package com.group5.munchbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson

class MyFeedDetailedActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        val gson = Gson()
        val recipe = gson.fromJson(intent.getStringExtra("my_recipe_extra"), RecipeModel::class.java)
        val recipeIngredientsList = recipe.recipeIngredients
        var storageReference = FirebaseStorage.getInstance().getReference()

        val recipeImageView: ImageView = findViewById(R.id.detailed_recipe_image)
        val recipeName: TextView = findViewById(R.id.detailed_recipe_name)
        val recipeUsername: TextView = findViewById(R.id.detailed_recipe_username)
        val recipeDescription: TextView = findViewById(R.id.detailed_recipe_description)
        val detailedActivityRecyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val recipeInstruction: TextView = findViewById(R.id.detailed_recipe_instructions)
        val postInteraction = findViewById<View>(R.id.postInteraction)

        detailedActivityRecyclerView.layoutManager = LinearLayoutManager(this)

        recipeName.text = recipe.recipeName
        recipeUsername.text = recipe.userEmail
        recipeDescription.text = recipe.recipeDetails
        detailedActivityRecyclerView.adapter = RecyclerViewAdapter(recipeIngredientsList)

        val imgPath = recipe.recipeImage

        Glide.with(this)
            .load(storageReference.child("$imgPath.png"))
            .transform(CenterCrop())
            .into(recipeImageView)
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