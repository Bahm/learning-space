package com.example.learningspace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learningspace.R
import com.example.learningspace.databinding.FragmentFlashCardListBinding
import com.example.learningspace.viewmodel.FlashCardListViewModel
import com.google.android.material.snackbar.Snackbar

class FlashCardListFragment : Fragment() {
    private var _binding: FragmentFlashCardListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FlashCardListViewModel by viewModels()
    private val args: FlashCardListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlashCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setDeckId(args.deckId)
        binding.toolbar.title = args.deckName

        val adapter = FlashCardAdapter(
            onItemClick = { card ->
                val action = FlashCardListFragmentDirections.actionListToReview(card.id)
                findNavController().navigate(action)
            },
            onEditClick = { card ->
                val action = FlashCardListFragmentDirections.actionListToEdit(card.id, args.deckId)
                findNavController().navigate(action)
            },
            onDeleteClick = { card ->
                viewModel.deleteCard(card)
                Snackbar.make(binding.root, R.string.card_deleted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo) { viewModel.insertCard(card) }
                    .show()
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.cards.observe(viewLifecycleOwner) { cards ->
            adapter.submitList(cards)
        }

        binding.fab.setOnClickListener {
            val action = FlashCardListFragmentDirections.actionListToEdit(-1, args.deckId)
            findNavController().navigate(action)
        }

        binding.toolbar.inflateMenu(R.menu.menu_flash_card_list)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_study -> {
                    findNavController().navigate(
                        FlashCardListFragmentDirections.actionListToStudy(args.deckId)
                    )
                    true
                }
                else -> false
            }
        }

        viewModel.dueCardCount.observe(viewLifecycleOwner) { count ->
            val title = if (count > 0) {
                getString(R.string.study_cards_count, count)
            } else {
                getString(R.string.study_cards)
            }
            binding.toolbar.menu.findItem(R.id.action_study)?.title = title
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
