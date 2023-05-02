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
import android.content.Context
import android.graphics.Color
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import java.io.InputStream
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
@GlideModule
class AppGlide: AppGlideModule(){
    override fun registerComponents(
        context: Context,
        glide: Glide,
        registry: Registry
    ) {
        super.registerComponents(context, glide, registry)
        registry.append(
            StorageReference::class.java, InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }
}
/**
 * A simple [Fragment] subclass.
 * Use the [MyFeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyFeedFragment : Fragment() {
    // private val userRecipes = mutableListOf<RecipeItem>()
    private lateinit var recipeList: ArrayList<RecipeModel>
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
                    val recipeClass = itemSnapshot.getValue(RecipeModel::class.java)
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
class MyFeedRecipeAdapter(private val recipeList: ArrayList<RecipeModel>, private val context: Context) : RecyclerView.Adapter<MyFeedRecipeAdapter.MyViewHolder>() {
    var storageReference = FirebaseStorage.getInstance().getReference()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = FirebaseAuth.getInstance().currentUser
        var userId: String = ""
        if(user != null){
            userId = user.uid
        }
        val imgPath = recipeList[position].recipeImage
        Glide.with(context).load(storageReference.child("$imgPath.png")).into(holder.recipeImage)
        holder.recipeName.text = recipeList[position].recipeName
        holder.userEmail.text = recipeList[position].userEmail
        holder.recipeDetails.text = recipeList[position].recipeDetails
        holder.commentCount.text = "0"
        if(recipeList[position].comments != null){
            holder.commentCount.text = recipeList[position].comments?.size.toString()
        }
        holder.upvoteCount.text = "0"
        if(recipeList[position].upvotes != null){
            holder.upvoteCount.text = recipeList[position].upvotes?.size.toString()
        }
        holder.downvoteCount.text = "0"
        if(recipeList[position].downvotes != null){
            holder.downvoteCount.text = recipeList[position].downvotes?.size.toString()
        }
        holder.upvote.setColorFilter(Color.rgb(0, 0, 0))
        if(recipeList[position].upvotes != null){
            if(recipeList[position].upvotes?.contains(userId) == true){
                holder.upvote.setColorFilter(Color.rgb(0, 255, 0))
            }
        }
        holder.downvote.setColorFilter(Color.rgb(0, 0, 0))
        if(recipeList[position].downvotes != null){
            if(recipeList[position].downvotes?.contains(userId) == true){
                holder.downvote.setColorFilter(Color.rgb(0, 0, 255))
            }
        }
            holder.upvoteLayout.setOnClickListener {
                val database = Firebase.database
                val myRef = database.getReference("Recipes")

                var recipeId = ""
                if (recipeList[position].recipeId != null) {
                    recipeId = recipeList[position].recipeId.toString()
                }
                val ruserID = recipeList[position].userId
                val recipeName = recipeList[position].recipeName
                val recipeDetails = recipeList[position].recipeDetails
                val recipeImage = recipeList[position].recipeImage
                val recipeInstructions = recipeList[position].recipeInstructions
                val recipeIngredients = recipeList[position].recipeIngredients
                val userEmail = recipeList[position].userEmail
                var upvoters1: MutableList<String>? = mutableListOf<String>()
                if (recipeList[position].upvotes != null){
                    upvoters1 = recipeList[position].upvotes?.toMutableList()
                }
                if(upvoters1?.contains(user?.uid) == false){
                    upvoters1.add(userId)
                }
                val upvotes = upvoters1
                var downvoters1: MutableList<String>? = mutableListOf<String>()
                if (recipeList[position].downvotes != null){
                    downvoters1 = recipeList[position].downvotes?.toMutableList()
                }
                if(downvoters1?.contains(user?.uid) == true){
                    downvoters1.remove(userId)
                }
                val downvotes = downvoters1
                val comments = recipeList[position].comments
                val recipe = RecipeModel(recipeId, ruserID, recipeName, recipeDetails, recipeImage, recipeInstructions, recipeIngredients, userEmail, upvotes, downvotes, comments)
                myRef.child(recipeId).setValue(recipe)
                    .addOnSuccessListener{
                        holder.upvote.setColorFilter(Color.rgb(0, 255, 0))
                        holder.downvote.setColorFilter(Color.rgb(0, 0, 0))
                    }

            }
                holder.downvoteLayout.setOnClickListener {
                    val database = Firebase.database
                    val myRef = database.getReference("Recipes")

                    var recipeId = ""
                    if (recipeList[position].recipeId != null) {
                        recipeId = recipeList[position].recipeId.toString()
                    }
                    val ruserID = recipeList[position].userId
                    val recipeName = recipeList[position].recipeName
                    val recipeDetails = recipeList[position].recipeDetails
                    val recipeImage = recipeList[position].recipeImage
                    val recipeInstructions = recipeList[position].recipeInstructions
                    val recipeIngredients = recipeList[position].recipeIngredients
                    val userEmail = recipeList[position].userEmail
                    var upvoters2: MutableList<String>? = mutableListOf<String>()
                    if (recipeList[position].upvotes != null){
                        upvoters2 = recipeList[position].upvotes?.toMutableList()
                    }
                    if(upvoters2?.contains(user?.uid) == true){
                        upvoters2.remove(userId)
                    }
                    val upvotes = upvoters2
                    var downvoters2: MutableList<String>? = mutableListOf<String>()
                    if (recipeList[position].downvotes != null){
                        downvoters2 = recipeList[position].downvotes?.toMutableList()
                    }
                    if(downvoters2?.contains(user?.uid) == false){
                        downvoters2.add(userId)
                    }
                    val downvotes = downvoters2
                    val comments = recipeList[position].comments
                    val recipe = RecipeModel(recipeId, ruserID, recipeName, recipeDetails, recipeImage, recipeInstructions, recipeIngredients, userEmail, upvotes, downvotes, comments)
                    myRef.child(recipeId).setValue(recipe)
                        .addOnSuccessListener{
                            holder.downvote.setColorFilter(Color.rgb(0, 0, 255))
                            holder.upvote.setColorFilter(Color.rgb(0, 0, 0))
                        }
                }
            }
            override fun getItemCount(): Int {
                return recipeList.size
            }
            inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
                var recipeName: TextView
                var userEmail: TextView
                var recipeDetails: TextView
                var recipeImage: ImageView
                var upvoteCount: TextView
                var downvoteCount: TextView
                var commentCount: TextView
                var upvote: ImageView
                var downvote: ImageView
                var upvoteLayout: LinearLayout
                var downvoteLayout: LinearLayout
                var commentsLayout: LinearLayout

                init {
                    recipeName = itemView.findViewById(R.id.recipe_name)
                    userEmail = itemView.findViewById(R.id.recipe_username)
                    recipeDetails = itemView.findViewById(R.id.recipe_description)
                    recipeImage = itemView.findViewById(R.id.food_image)
                    upvote = itemView.findViewById(R.id.upvote)
                    downvote = itemView.findViewById(R.id.downvote)
                    upvoteCount = itemView.findViewById(R.id.upvoteCount)
                    downvoteCount = itemView.findViewById(R.id.downvoteCount)
                    commentCount = itemView.findViewById(R.id.commentCount)
                    upvoteLayout = itemView.findViewById(R.id.upvoteLayout)
                    downvoteLayout = itemView.findViewById(R.id.downvoteLayout)
                    commentsLayout = itemView.findViewById(R.id.commentsLayout)

                    itemView.setOnClickListener(this)
                }
                override fun onClick(v: View?) {
                    // Navigate to Details screen and pass selected recipe
                    val intent = Intent(context, MyFeedDetailedActivity::class.java)
                    val gson = Gson()
                    val myJson = gson.toJson(recipeList[adapterPosition])
                    intent.putExtra("my_feed_recipe_extra", myJson)
                    context.startActivity(intent)
                }
            }
        }