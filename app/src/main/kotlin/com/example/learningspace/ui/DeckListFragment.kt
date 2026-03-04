package com.example.learningspace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learningspace.R
import com.example.learningspace.databinding.FragmentDeckListBinding
import com.example.learningspace.viewmodel.DeckListViewModel
import com.google.android.material.snackbar.Snackbar

class DeckListFragment : Fragment() {
    private var _binding: FragmentDeckListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DeckListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeckListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DeckAdapter(
            onItemClick = { deckWithCount ->
                val action = DeckListFragmentDirections.actionDeckListToCardList(
                    deckWithCount.deck.id,
                    deckWithCount.deck.name
                )
                findNavController().navigate(action)
            },
            onDeleteClick = { deckWithCount ->
                viewModel.deleteDeck(deckWithCount.deck) { cards ->
                    Snackbar.make(binding.root, R.string.deck_deleted, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo) {
                            viewModel.restoreDeck(deckWithCount.deck, cards)
                        }
                        .show()
                }
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.refresh()
        viewModel.allDecks.observe(viewLifecycleOwner) { decks ->
            adapter.submitList(decks)
        }

        binding.fab.setOnClickListener {
            val action = DeckListFragmentDirections.actionDeckListToDeckEdit()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
