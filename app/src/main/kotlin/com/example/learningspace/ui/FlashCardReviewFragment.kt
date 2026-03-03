package com.example.learningspace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.learningspace.data.FlashCardDatabase
import com.example.learningspace.databinding.FragmentFlashCardReviewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlashCardReviewFragment : Fragment() {
    private var _binding: FragmentFlashCardReviewBinding? = null
    private val binding get() = _binding!!
    private val args: FlashCardReviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlashCardReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = FlashCardDatabase.getInstance(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            val card = db.flashCardDao().getById(args.cardId)
            withContext(Dispatchers.Main) {
                card?.let {
                    binding.questionText.text = it.question
                    binding.showAnswerButton.setOnClickListener {
                        binding.showAnswerButton.visibility = View.GONE
                        binding.answerLabel.visibility = View.VISIBLE
                        binding.answerText.text = card.answer
                        binding.answerText.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
