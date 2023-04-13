package com.group5.munchbox

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView

class IngredientsAdapter(private val context: Context, private val IngredientItems: List<IngredientsItem>): RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameTextView: EditText
        init {
            nameTextView = itemView.findViewById(R.id.ingredients_item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.ingredients_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredientItems = IngredientItems[position]
        holder.nameTextView.setText(ingredientItems.name)
        holder.nameTextView.hint = "Ingredient"
        holder.nameTextView.doOnTextChanged { text, start, before, count ->
            IngredientItems[position].name = text.toString()
        }
    }

    override fun getItemCount(): Int {
        return IngredientItems.size
    }
}