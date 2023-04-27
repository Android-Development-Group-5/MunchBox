package com.group5.munchbox

data class Comment(
    var user:String? = null,
    var message:String? = null,

    );
data class RecipeModel(
    var recipeId: String? = null,
    var userId: String? = null,
    var recipeName: String? = null,
    var recipeDetails: String? = null,
    var recipeImage: String? = "",
    var recipeIngredients: MutableList<IngredientsItem>? = null,
    var userEmail:String? = null,
    var upvotes:Int = 0,
    var dowmvotes:Int = 0,
    var comments:MutableList<Comment>? = null

)