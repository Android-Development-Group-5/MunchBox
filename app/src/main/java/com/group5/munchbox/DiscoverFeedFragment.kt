package com.group5.munchbox

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass.
 * Use the [DiscoverFeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscoverFeedFragment : Fragment() {
    private val recipes = mutableListOf<String>()
    private lateinit var sleepRecyclerView: RecyclerView
    private lateinit var application: Application

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover_feed, container, false)
    }

    companion object {
        fun newInstance(): DiscoverFeedFragment {
            return DiscoverFeedFragment()
        }
    }
}