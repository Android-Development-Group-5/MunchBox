package com.group5.munchbox

import com.google.gson.annotations.SerializedName

class RecipeItem {
    @JvmField
    @SerializedName("id")
    var idMeal: Int? = null

    @JvmField
    @SerializedName("title")
    var title: String? = null

//    @JvmField
//    @SerializedName("strCategory")
//    var strCategory: String? = null

    @JvmField
    @SerializedName("instructions")
    var instructions: String? = null

    @JvmField
    @SerializedName("image")
    var strMealThumb: String? = null

    @JvmField
    @SerializedName("creditText")
    var creditText: String? = null

    @JvmField
    @SerializedName("extendedIngredients")
    var extendedIngredients: List<RecipeIngredientsItem>? = listOf()
}