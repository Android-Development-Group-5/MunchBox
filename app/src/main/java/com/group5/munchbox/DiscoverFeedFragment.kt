package com.group5.munchbox

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONException

private const val TAG = "DiscoverFeedFragment/"
private const val NUM_RECIPES = 10
private const val RECIPE_SEARCH_URL = "https://www.themealdb.com/api/json/v1/1/random.php"
// private const val API_KEY = "1"
const val RECIPE_EXTRA = "API_RECIPE"
const val SOURCE = "DiscoverFeedFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [DiscoverFeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscoverFeedFragment : Fragment() /*, OnListFragmentInteractionListener*/ {
    private val recipes: MutableList<RecipeItem> = mutableListOf()
//    private lateinit var recipeRecyclerView: RecyclerView
//    private lateinit var application: Application

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_discover_feed, container, false)
        val progressBar: ContentLoadingProgressBar = view.findViewById(R.id.discover_feed_progress_bar)
        val discoverFeedRecyclerView: RecyclerView = view.findViewById(R.id.discover_feed_recipe_list)
        val refreshLayout: SwipeRefreshLayout = view.findViewById(R.id.discover_feed_swipe_refresh)

        progressBar.show()
        updateAdapter(progressBar, discoverFeedRecyclerView)
        refreshLayout.setOnRefreshListener {
            updateAdapter(progressBar, discoverFeedRecyclerView)
            refreshLayout.isRefreshing = false
        }

        discoverFeedRecyclerView.adapter =
            RecyclerViewAdapter(view!!.context, recipes)

        return view
    }

    /**
     * Updates the RecyclerView adapter with new data.
     */
    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()
        if (recipes.size > 0) {
            recipes.removeAll(recipes)
            recyclerView.adapter?.notifyDataSetChanged()
        }

        // Create and set up an AsyncHTTPClient() here
        val client = AsyncHttpClient()

        // Using the client, perform the HTTP request
        for (i in 1..NUM_RECIPES) {
            client.get(RECIPE_SEARCH_URL, object: JsonHttpResponseHandler() {
                /**
                 * The onSuccess function gets called when
                 * HTTP response status is "200 OK"
                 */
                override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                    Log.i(TAG, "Successfully fetched recipes: $json")
                    progressBar.hide()

                    try {
                        val resultsJSON: String = json?.jsonObject?.getJSONArray("meals").toString()
//                        val recipeRawJSON: String = resultsJSON.get("meals").toString()
                        val gson = Gson()
                        val arrayRecipeType = object : TypeToken<List<RecipeItem>>() {}.type
                        val models: List<RecipeItem> =
                            gson.fromJson(resultsJSON, arrayRecipeType)
                        recipes.addAll(models)
                        recyclerView.adapter?.notifyItemInserted(recipes.size)
                    } catch (e: JSONException) {
                        Log.e(TAG, "Exception: $e")
                    }
                }

                /**
                 * The onFailure function gets called when
                 * HTTP response status is "4XX" (eg. 401, 403, 404)
                 */
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String?,
                    throwable: Throwable?
                ) {
                    // The wait for a response is over
                    progressBar.hide()

                    // If the error is not null, log it!
                    throwable?.message?.let {
                        Log.e("BestSellerBooksFragment", response.toString())
                    }
                }
            })
        }
    }

    /**
     * [RecyclerView.Adapter] that can display a [RecipeItem]
     */
    private inner class RecyclerViewAdapter(
        private val context: Context,
        private val recipes: List<RecipeItem>,
    ): RecyclerView.Adapter<RecyclerViewAdapter.RecipeViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.api_recipe_item, parent, false)
            return RecipeViewHolder(view)
        }

        override fun getItemCount() = recipes.size

        override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
            val recipe = recipes[position]

            holder.item = recipe
            holder.recipeName.text = recipe.strMeal
            holder.recipeSource.text = "TheMealDB"
            holder.recipeDescription.text = recipe.strInstructions

            Glide.with(holder.itemView)
                .load(recipe.strMealThumb)
                .transform(CenterCrop(), RoundedCorners(32))
                .into(holder.recipeThumbnail)

            holder.recipeSource.setOnClickListener {
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(recipe.strSource.toString()))
                    startActivity(browserIntent, null)
                } catch (e: ActivityNotFoundException) {
                    Log.e("Source Url:", recipe.strSource.toString())
                }
            }
        }

        inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            var item: RecipeItem? = null
            val recipeThumbnail: ImageView = itemView.findViewById(R.id.api_recipe_thumbnail)
            val recipeName: TextView = itemView.findViewById(R.id.api_recipe_name)
            val recipeSource: TextView = itemView.findViewById(R.id.api_recipe_source)
            val recipeDescription: TextView = itemView.findViewById(R.id.api_recipe_description)

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                // Get selected recipe
                val recipe = recipes[adapterPosition]
                val gson = Gson()
                val myJson = gson.toJson(recipe)

                Log.d("discover recipe", myJson)

                // Navigate to Details screen and pass selected recipe
                val intent = Intent(context, DetailedActivity::class.java)
                intent.putExtra(RECIPE_EXTRA, myJson)
                intent.putExtra(SOURCE, SOURCE)
                context.startActivity(intent)
            }
        }
    }

    companion object {
        fun newInstance(): DiscoverFeedFragment {
            return DiscoverFeedFragment()
        }
    }
}
