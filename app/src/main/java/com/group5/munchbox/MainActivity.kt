package com.group5.munchbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(AddRecipeFragment())
        // define your fragments here
        //val fragment1: Fragment
        val fragment2: Fragment = AddRecipeFragment()
        //val fragment3: Fragment

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                //R.id.home -> fragment = fragment1
                R.id.add_recipe -> fragment = fragment2
                //R.id.profile -> fragment = fragment3
            }
            replaceFragment(fragment)
            true
        }

        // Set default selection
        //bottomNavigationView.selectedItemId = R.id.home
        //TODO("replace line below with line above")
        bottomNavigationView.selectedItemId = R.id.add_recipe
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.app_frame_layout, fragment)
        fragmentTransaction.commit()
    }
    val fragmentManager: FragmentManager = supportFragmentManager
}
