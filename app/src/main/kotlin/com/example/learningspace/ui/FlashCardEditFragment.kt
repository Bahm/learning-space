package com.example.learningspace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.learningspace.databinding.FragmentFlashCardEditBinding
import com.example.learningspace.viewmodel.FlashCardEditViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlashCardEditFragment : Fragment() {
    private var _binding: FragmentFlashCardEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FlashCardEditViewModel by viewModels()
    private val args: FlashCardEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlashCardEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardId = args.cardId.takeIf { it != -1 }
        if (cardId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val card = viewModel.getById(cardId)
                withContext(Dispatchers.Main) {
                    card?.let {
                        binding.questionInput.setText(it.question)
                        binding.answerInput.setText(it.answer)
                    }
                }
            }
        }

        binding.saveButton.setOnClickListener {
            val question = binding.questionInput.text?.toString()?.trim() ?: ""
            val answer = binding.answerInput.text?.toString()?.trim() ?: ""
            if (question.isNotEmpty() && answer.isNotEmpty()) {
                viewModel.saveCard(cardId, question, answer) {
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
