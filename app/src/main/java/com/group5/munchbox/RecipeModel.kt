package com.group5.munchbox

data class Comment(
    var userEmail:String? = null,
    var message:String? = null,

    )
data class RecipeModel(
    var recipeId: String? = null,
    var userId: String? = null,
    var recipeName: String? = null,
    var recipeDetails: String? = null,
    var recipeImage: String? = "",
    var recipeInstructions: String? = "",
    var recipeIngredients: MutableList<IngredientsItem>? = null,
    var userEmail:String? = null,
    var upvotes:MutableList<String>? = null,
    var downvotes:MutableList<String>? = null,
    var comments:MutableList<Comment>? = null

)