package com.example.learningspace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.learningspace.databinding.FragmentDeckEditBinding
import com.example.learningspace.viewmodel.DeckEditViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeckEditFragment : Fragment() {
    private var _binding: FragmentDeckEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DeckEditViewModel by viewModels()
    private val args: DeckEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeckEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deckId = args.deckId.takeIf { it != -1 }
        if (deckId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val deck = viewModel.getById(deckId)
                withContext(Dispatchers.Main) {
                    deck?.let {
                        binding.deckNameInput.setText(it.name)
                    }
                }
            }
        }

        binding.saveButton.setOnClickListener {
            val name = binding.deckNameInput.text?.toString()?.trim() ?: ""
            if (name.isNotEmpty()) {
                viewModel.saveDeck(deckId, name) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
