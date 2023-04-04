package com.group5.munchbox

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 * Use the [AddRecipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "AddRecipeFragment/"

class AddRecipeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private val ingredients = mutableListOf<IngredientsItem>(IngredientsItem(""))

    private lateinit var ingredientsList : RecyclerView
    private lateinit var ingredientsAdapter: IngredientsAdapter

    private lateinit var recipeName : EditText
    private lateinit var recipeDetails : EditText

    private lateinit var addRecipeButton : Button
    private lateinit var addIngredientButton: Button
    private lateinit var removeIngredientButton: Button

    private lateinit var recipeImage: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)

        val layoutManager = LinearLayoutManager(context)
        ingredientsList = view.findViewById(R.id.IngredientsList)
        ingredientsList.layoutManager = layoutManager
        ingredientsAdapter = IngredientsAdapter(view.context, ingredients)
        ingredientsList.adapter = ingredientsAdapter

        addRecipeButton = view.findViewById(R.id.AddRecipeButton)
        addIngredientButton = view.findViewById(R.id.AddIngredientButton)
        removeIngredientButton = view.findViewById(R.id.RemoveIngredientButton)

        recipeName = view.findViewById(R.id.RecipeName)
        recipeDetails = view.findViewById(R.id.RecipeDetails)

        recipeImage = view.findViewById(R.id.RecipeImage)

        addRecipeButton.setOnClickListener {
            Toast.makeText(context, ingredients.toString(), Toast.LENGTH_SHORT).show()
            ingredients.forEach{println(it.name) }

        }
        recipeImage.setOnClickListener {
            pickImageFromGallery()
        }
        addIngredientButton.setOnClickListener {
            ingredients.add(IngredientsItem(""))
            ingredientsAdapter.notifyItemInserted(ingredientsAdapter.itemCount)
            removeIngredientButton.visibility = View.VISIBLE
        }
        removeIngredientButton.setOnClickListener {
            if(ingredients.size > 1) {
                ingredients.removeAt(ingredientsAdapter.itemCount - 1)
                ingredientsAdapter.notifyItemRemoved(ingredientsAdapter.itemCount)
            }
            if(ingredients.size <= 1){
                removeIngredientButton.visibility = View.INVISIBLE
            }
        }
        return view
    }

    companion object {
        const val IMAGE_REQUEST_CODE = 100
        fun newInstance(): AddRecipeFragment {
            return AddRecipeFragment()
            }
    }
    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            recipeImage.setImageURI(data?.data)
        }
    }
}