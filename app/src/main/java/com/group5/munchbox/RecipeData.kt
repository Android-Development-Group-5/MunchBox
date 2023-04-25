package com.group5.munchbox

class RecipeData {
    var dataImage: String? = null
    var recipeName: String? = null
    var username: String? = null
    var recipeDetails: String? = null

    constructor(dataImage: String?, recipeName: String?, username: String?, recipeDetails: String?) {
        this.dataImage = dataImage
        this.recipeName = recipeName
        this.username = username
        this.recipeDetails = recipeDetails
    }
    constructor() {

    }
}