package com.group5.munchbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


/**
 * A simple [Fragment] subclass.
 * Use the [MyFeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyFeedFragment : Fragment() {

    // private val userRecipes = mutableListOf<RecipeItem>()
    private lateinit var recipeList: ArrayList<RecipeData>
    private lateinit var adapter: MyFeedRecipeAdapter
    private var databaseReference:DatabaseReference? = null
    private var eventListener:ValueEventListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_feed, container, false)

        recipeList = ArrayList()
        adapter = MyFeedRecipeAdapter(recipeList, requireContext())
        var rvRecipes = view.findViewById<RecyclerView>(R.id.home_recipe_list)
        rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        rvRecipes.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipes")

        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                recipeList.clear()
                for(itemSnapshot in snapshot.children){
                    val recipeClass = itemSnapshot.getValue(RecipeData::class.java)
                    if(recipeClass != null){
                        recipeList.add(recipeClass)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        return view
    }

    companion object {
        fun newInstance(page: Int): MyFeedFragment {
            return MyFeedFragment()
        }
    }
}