package com.example.peopledatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.peopledatabase.ui.main.AddFragment
import com.example.peopledatabase.ui.main.MainFragment

class MainActivity : AppCompatActivity(R.layout.main_activity), AddFragment.OpenFragments,
MainFragment.OpenFragments,SettingsFragment.OpenFragment,ItemClickListener {

    private var navController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = this.findNavController(R.id.fragment_container)
    }

    override fun openListFragment() {
        navController?.popBackStack()
    }

    override fun openSettingsFragment(){
        navController?.navigate(R.id.action_mainFragment_to_settingsFragment)
    }

    override fun openAddFragment() {
        navController?.navigate(R.id.action_mainFragment_to_addFragment)
    }

    override fun onItemClick(id: Int) {
        navController?.navigate(R.id.action_mainFragment_to_addFragment, bundleOf("id" to id))
    }
}