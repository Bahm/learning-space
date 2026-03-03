package com.example.learningspace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.learningspace.R
import com.example.learningspace.data.FlashCard
import com.example.learningspace.data.FsrsAlgorithm
import com.example.learningspace.databinding.FragmentSpacedRepetitionBinding
import com.example.learningspace.viewmodel.SpacedRepetitionViewModel

class SpacedRepetitionFragment : Fragment() {

    private var _binding: FragmentSpacedRepetitionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SpacedRepetitionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpacedRepetitionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupClickListeners()
    }

    private fun observeViewModel() {
        viewModel.currentCard.observe(viewLifecycleOwner) { card ->
            if (card != null) {
                binding.textQuestion.text = card.question
                binding.textAnswer.text = card.answer
                updateButtonIntervals(card)
            }
        }

        viewModel.showAnswer.observe(viewLifecycleOwner) { show ->
            binding.groupAnswer.visibility = if (show) View.VISIBLE else View.GONE
            binding.buttonShowAnswer.visibility = if (show) View.GONE else View.VISIBLE
            binding.groupRating.visibility = if (show) View.VISIBLE else View.GONE
        }

        viewModel.sessionComplete.observe(viewLifecycleOwner) { complete ->
            if (complete) {
                binding.groupCard.visibility = View.GONE
                binding.textProgress.visibility = View.GONE
                binding.buttonShowAnswer.visibility = View.GONE
                binding.groupRating.visibility = View.GONE
                binding.layoutComplete.visibility = View.VISIBLE
                val count = viewModel.totalCards.value ?: 0
                binding.textCompleteMessage.text =
                    getString(R.string.session_complete_message, count)
            }
        }

        viewModel.currentPosition.observe(viewLifecycleOwner) { pos ->
            val total = viewModel.totalCards.value ?: 0
            binding.textProgress.text = getString(R.string.review_progress, pos, total)
        }

        viewModel.totalCards.observe(viewLifecycleOwner) { total ->
            val pos = viewModel.currentPosition.value ?: 0
            binding.textProgress.text = getString(R.string.review_progress, pos, total)
        }
    }

    private fun setupClickListeners() {
        binding.buttonShowAnswer.setOnClickListener {
            viewModel.showAnswer()
        }
        binding.buttonFailed.setOnClickListener {
            viewModel.rateCard(FsrsAlgorithm.RATING_AGAIN)
        }
        binding.buttonHard.setOnClickListener {
            viewModel.rateCard(FsrsAlgorithm.RATING_HARD)
        }
        binding.buttonGood.setOnClickListener {
            viewModel.rateCard(FsrsAlgorithm.RATING_GOOD)
        }
        binding.buttonEasy.setOnClickListener {
            viewModel.rateCard(FsrsAlgorithm.RATING_EASY)
        }
    }

    private fun updateButtonIntervals(card: FlashCard) {
        val failedDays = FsrsAlgorithm.previewInterval(card, FsrsAlgorithm.RATING_AGAIN)
        val hardDays = FsrsAlgorithm.previewInterval(card, FsrsAlgorithm.RATING_HARD)
        val goodDays = FsrsAlgorithm.previewInterval(card, FsrsAlgorithm.RATING_GOOD)
        val easyDays = FsrsAlgorithm.previewInterval(card, FsrsAlgorithm.RATING_EASY)
        binding.buttonFailed.text = getString(
            R.string.rating_failed_interval, getString(R.string.interval_days, failedDays)
        )
        binding.buttonHard.text = getString(
            R.string.rating_hard_interval, getString(R.string.interval_days, hardDays)
        )
        binding.buttonGood.text = getString(
            R.string.rating_good_interval, getString(R.string.interval_days, goodDays)
        )
        binding.buttonEasy.text = getString(
            R.string.rating_easy_interval, getString(R.string.interval_days, easyDays)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
