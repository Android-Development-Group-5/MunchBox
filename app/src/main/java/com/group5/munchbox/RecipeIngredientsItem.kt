package com.group5.munchbox

import com.google.gson.annotations.SerializedName

class RecipeIngredientsItem {
    @JvmField
    @SerializedName("id")
    var idIngredientsItem: Int? = null

    @JvmField
    @SerializedName("name")
    var name: String? = null

    @JvmField
    @SerializedName("originalString")
    var originalString: String? = null
}