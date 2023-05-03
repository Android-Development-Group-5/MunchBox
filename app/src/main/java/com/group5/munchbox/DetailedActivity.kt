package com.group5.munchbox

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

private const val TAG = "DetailedActivity"

class DetailedActivity : AppCompatActivity() {
    private val ingredients: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)

        val gson = Gson()
        val recipe = gson.fromJson(intent.getStringExtra(RECIPE_EXTRA), RecipeItem::class.java)
        val source = intent.getStringExtra(SOURCE)

        val recipeImageView: ImageView = findViewById(R.id.detailed_recipe_image)
        val recipeName: TextView = findViewById(R.id.detailed_recipe_name)
        val recipeUsername: TextView = findViewById(R.id.detailed_recipe_username)
        val recipeDescription: TextView = findViewById(R.id.detailed_recipe_description)
        val detailedActivityRecyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val recipeInstruction: TextView = findViewById(R.id.detailed_recipe_instructions)
        val postInteraction = findViewById<View>(R.id.postInteraction)
        val externalLinkButton = findViewById<ImageButton>(R.id.externalLinkBtn)
        val saveButton = findViewById<ImageButton>(R.id.saveBtn)

        detailedActivityRecyclerView.layoutManager = LinearLayoutManager(this)

        if (source == "DiscoverFeedFragment" || source == "DiscoveriesActivity") {
            saveButton.visibility = View.VISIBLE; //adds the save button if it's on the api

            val recipeIngredientsList = mutableListOf(recipe.strIngredient1, recipe.strIngredient2,
                recipe.strIngredient3, recipe.strIngredient4, recipe.strIngredient5,
                recipe.strIngredient6, recipe.strIngredient7, recipe.strIngredient8,
                recipe.strIngredient9, recipe.strIngredient10, recipe.strIngredient11,
                recipe.strIngredient12, recipe.strIngredient13, recipe.strIngredient14,
                recipe.strIngredient15, recipe.strIngredient16, recipe.strIngredient17,
                recipe.strIngredient18, recipe.strIngredient19, recipe.strIngredient20)

            val recipeMeasurementsList = mutableListOf(recipe.strMeasure1, recipe.strMeasure2,
                recipe.strMeasure3, recipe.strMeasure4, recipe.strMeasure5, recipe.strMeasure6,
                recipe.strMeasure7, recipe.strMeasure8, recipe.strMeasure9, recipe.strMeasure10,
                recipe.strMeasure11, recipe.strMeasure12, recipe.strMeasure13, recipe.strMeasure14,
                recipe.strMeasure15, recipe.strMeasure16, recipe.strMeasure17, recipe.strMeasure18,
                recipe.strMeasure19, recipe.strMeasure20)

            // removes null values from ingredients and measurements lists
            for (ingredient in recipeIngredientsList) {
                if (!ingredient.isNullOrEmpty()) {
                    val measurement = recipeMeasurementsList[recipeIngredientsList.indexOf(ingredient)]
                    val formatString = "$measurement $ingredient"
                    ingredients.add(formatString)
                }
            }

            detailedActivityRecyclerView.adapter = RecyclerViewAdapter(ingredients)

            recipeDescription.text = null
            recipeDescription.visibility = GONE
            postInteraction.visibility = GONE
            recipeUsername.text = "TheMealDB"
        }

        recipeName.text = recipe.strMeal
        recipeInstruction.text = recipe.strInstructions

        Glide.with(this)
            .load(recipe.strMealThumb)
            .transform(CenterCrop())
            .into(recipeImageView)

        externalLinkButton.setOnClickListener {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe.strSource.toString()))
                startActivity(browserIntent, null)
            } catch (e: ActivityNotFoundException) {
                Log.e("Source Url:", recipe.strSource.toString())
            }
        }

        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child("Discoveries").child(
            recipe.idMeal.toString()
        ).get().addOnSuccessListener{ d ->
            if (d.value == true) {
                saveButton.setImageResource(R.drawable.baseline_star_24)
            } else {
                saveButton.setImageResource(R.drawable.baseline_star_border_24)
            }
        }

        saveButton.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid.toString();
            val ref = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Discoveries").child(
                recipe.idMeal.toString()
            )
            ref.get().addOnSuccessListener { d ->
                if (d.value == null) {
                    saveButton.setImageResource(R.drawable.baseline_star_24)
                    ref.setValue(true)
                } else {
                    saveButton.setImageResource(R.drawable.baseline_star_border_24)
                    ref.setValue(null);
                }


            }


        }
    }

    private inner class RecyclerViewAdapter(
        private val ingredients: List<String?>
    ): RecyclerView.Adapter<RecyclerViewAdapter.IngredientViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.detailed_recipe_ingredients_item, parent, false)
            return IngredientViewHolder(view)
        }

        override fun getItemCount() = ingredients.size

        override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
            val ingredient = ingredients[position]
            holder.detailedIngredientsItem.text = ingredient
        }

        inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val detailedIngredientsItem: TextView = itemView.findViewById(R.id.detailed_recipe_ingredient)
        }
    }

}