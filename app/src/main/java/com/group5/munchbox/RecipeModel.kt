package com.group5.munchbox

data class RecipeModel(
    var recipeId: String? = null,
    var userId: String? = null,
    var recipeName: String? = null,
    var recipeDetails: String? = null,
    var recipeImage: String? = "",
    var recipeIngredients: MutableList<IngredientsItem>? = null,

)