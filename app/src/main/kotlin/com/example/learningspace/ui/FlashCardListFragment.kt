package com.example.learningspace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learningspace.databinding.FragmentFlashCardListBinding
import com.example.learningspace.viewmodel.FlashCardListViewModel

class FlashCardListFragment : Fragment() {
    private var _binding: FragmentFlashCardListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FlashCardListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlashCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FlashCardAdapter(
            onItemClick = { card ->
                val action = FlashCardListFragmentDirections.actionListToReview(card.id)
                findNavController().navigate(action)
            },
            onEditClick = { card ->
                val action = FlashCardListFragmentDirections.actionListToEdit(card.id)
                findNavController().navigate(action)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.allCards.observe(viewLifecycleOwner) { cards ->
            adapter.submitList(cards)
        }

        binding.fab.setOnClickListener {
            val action = FlashCardListFragmentDirections.actionListToEdit(-1)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
