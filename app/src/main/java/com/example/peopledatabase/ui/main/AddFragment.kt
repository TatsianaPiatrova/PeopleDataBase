package com.example.peopledatabase.ui.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.peopledatabase.databinding.FragmentAddCardBinding
import com.example.peopledatabase.repository.db.Card

class AddFragment : Fragment() {

    private var _binding: FragmentAddCardBinding? = null
    private val binding get() = _binding!!
    private var saveButton: Button? = null
    private var deleteButton: Button? = null
    private var id: Int? = null
    private lateinit var card: Card
    private var openFragments: OpenFragments? = null

    interface OpenFragments {
        fun openListFragment()
    }

    private val addViewModel: AddViewModel by lazy {
        ViewModelProvider(this).get(AddViewModel::class.java)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        openFragments = context as OpenFragments
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        card = Card()
        id = arguments?.getInt("id")
        id?.let { addViewModel.loadCard(it) }
    }

    private fun textWatcher(actionTextChange: (sequence: CharSequence?) -> Unit) =
        object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                actionTextChange(sequence)
            }

            override fun afterTextChanged(s: Editable?) {}
        }


    private val nameWatcher = textWatcher { card.name = it.toString() }
    private val ageWatcher = textWatcher { card.age = it.toString().toIntOrNull() ?: 0 }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)
        saveButton = binding.addCard
        deleteButton = binding.deleteCard
        if (id == null) deleteButton?.visibility = View.INVISIBLE
        else deleteButton?.visibility = View.VISIBLE
        binding.name.addTextChangedListener(nameWatcher)
        binding.age.addTextChangedListener(ageWatcher)
        saveButton?.setOnClickListener {
            if (id == null)
                addViewModel.addCard(card) else
                addViewModel.update(card)
            openFragments?.openListFragment()
        }
        deleteButton?.setOnClickListener {
            addViewModel.delete(card)
            openFragments?.openListFragment()
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addViewModel.cardLiveDate.observe(
            viewLifecycleOwner,
            { card ->
                card?.let {
                    this.card = it
                    updateUI()
                }
            })
    }

    private fun updateUI() {
        binding.name.setText(card.name)
        binding.age.setText(card.age.toString())
    }
}