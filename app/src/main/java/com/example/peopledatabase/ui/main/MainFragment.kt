package com.example.peopledatabase.ui.main

import android.content.ContentValues.TAG
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.peopledatabase.ItemClickListener
import com.example.peopledatabase.R
import com.example.peopledatabase.adapter.CardAdapter
import com.example.peopledatabase.databinding.MainFragmentBinding
import com.example.peopledatabase.repository.db.Card
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var cardRecyclerView: RecyclerView
    private var adapter: CardAdapter? = null
    private var button: FloatingActionButton? = null

    interface OpenFragments {
        fun openAddFragment()
        fun openSettingsFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.sort_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_setting -> {
                openFragments?.openSettingsFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    private var openFragments: OpenFragments? = null
    private var itemClickListener: ItemClickListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val order = sharedPreferences.getString("sort_by", "name") ?: "name"

        viewModel.sortBy(order)
        viewModel.cardListLiveData.observe(viewLifecycleOwner,
            { cards ->
                cards?.let {
                    updateUI(it)
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        button = binding.addButton
        cardRecyclerView = binding.cardRecyclerView
        cardRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CardAdapter(itemClickListener!!)
        cardRecyclerView.adapter = adapter
        button?.setOnClickListener {
            openFragments?.openAddFragment()
        }
        return view
    }

    private fun updateUI(cards: List<Card>) {
        Log.d(TAG, "${cards.size}")
        adapter?.submitList(cards)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        openFragments = context as OpenFragments
        itemClickListener = context as ItemClickListener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}