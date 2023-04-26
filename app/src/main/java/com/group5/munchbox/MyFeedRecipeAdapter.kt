package com.group5.munchbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import java.io.InputStream
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth


@GlideModule
class AppGlide: AppGlideModule(){
    override fun registerComponents(
        context: android.content.Context,
        glide: Glide,
        registry: Registry
    ) {
        super.registerComponents(context, glide, registry)
        registry.append(
            StorageReference::class.java, InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }
}

class MyFeedRecipeAdapter(private val recipeList: ArrayList<RecipeData>, private val context: Context) : RecyclerView.Adapter<MyViewHolder>() {
    var storageReference = FirebaseStorage.getInstance().getReference()
    var auth = FirebaseAuth.getInstance().uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imgPath = recipeList[position].recipeImage
        Glide.with(context).load(storageReference.child("$imgPath.png")).into(holder.recipeImage)

        holder.recipeName.text = recipeList[position].recipeName
        holder.userEmail.text = recipeList[position].userEmail
        holder.recipeDetails.text = recipeList[position].recipeDetails
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var recipeName: TextView
    var userEmail: TextView
    var recipeDetails: TextView
    var recipeImage: ImageView

    init {
        recipeName = itemView.findViewById(R.id.recipe_name)
        userEmail = itemView.findViewById(R.id.recipe_username)
        recipeDetails = itemView.findViewById(R.id.recipe_description)
        recipeImage = itemView.findViewById(R.id.food_image)
    }
}
