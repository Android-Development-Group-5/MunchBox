package com.group5.munchbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyFeedRecipeAdapter(private val recipeList: ArrayList<RecipeData>, private val context: Context) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(recipeList[position].dataImage).into(holder.recipeImage)
        holder.recipeName.text = recipeList[position].recipeName
        holder.username.text = recipeList[position].username
        holder.recipeDetails.text = recipeList[position].recipeDetails
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var recipeName: TextView
    var username: TextView
    var recipeDetails: TextView
    var recipeImage: ImageView

    init {
        recipeName = itemView.findViewById(R.id.recipe_name)
        username = itemView.findViewById(R.id.recipe_username)
        recipeDetails = itemView.findViewById(R.id.recipe_description)
        recipeImage = itemView.findViewById(R.id.food_image)
    }
}
