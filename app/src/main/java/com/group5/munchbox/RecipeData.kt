package com.group5.munchbox

class RecipeData {
    var recipeImage: String? = null
    var recipeName: String? = null
    var userId: String? = null
    var recipeDetails: String? = null
    var userEmail:String? = null

    constructor(recipeImage: String?, recipeName: String?, userId: String?, recipeDetails: String?, userEmail: String?) {
        this.recipeImage = recipeImage
        this.recipeName = recipeName
        this.userId = userId
        this.recipeDetails = recipeDetails
        this.userEmail = userEmail
    }
    constructor() {

    }
}