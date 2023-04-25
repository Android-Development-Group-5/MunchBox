package com.group5.munchbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.group5.munchbox.databinding.ActivityMainBinding
import com.group5.munchbox.databinding.FragmentMyFeedBinding
import okhttp3.Headers
import org.json.JSONException

/**
 * A simple [Fragment] subclass.
 * Use the [MyFeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyFeedFragment : Fragment() {

   // private val userRecipes = mutableListOf<RecipeItem>()
    private lateinit var binding: FragmentMyFeedBinding
    private lateinit var recipeList: ArrayList<RecipeData>
    private lateinit var adapter: MyFeedRecipeAdapter
    var databaseReference:DatabaseReference? = null
    var eventListener:ValueEventListener? = null

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