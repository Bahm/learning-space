package com.example.learningspace.data

import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToInt

object FsrsAlgorithm {

    // FSRS-4.5 default parameters
    private val w = doubleArrayOf(
        0.4072, 1.1829, 3.1262, 15.4722,
        7.2102, 0.5316, 1.0651, 0.0589,
        1.5330, 0.1544, 1.0070, 1.9243,
        0.1100, 0.2900, 2.2700, 0.2700,
        2.9898, 0.5100, 0.8567
    )

    // Forgetting curve decay constant
    private const val DECAY = -0.5
    // FACTOR = 0.9^(1/DECAY) - 1 = 19/81
    private val FACTOR = 0.9.pow(1.0 / DECAY) - 1

    const val RATING_AGAIN = 1
    const val RATING_HARD = 2
    const val RATING_EASY = 4

    private const val DAY_MS = 86_400_000L

    fun previewInterval(card: FlashCard, rating: Int): Int =
        applyRating(card, rating).scheduledDays

    fun applyRating(card: FlashCard, rating: Int): FlashCard {
        val now = System.currentTimeMillis()
        val elapsedDays = if (card.lastReview > 0L) {
            ((now - card.lastReview) / DAY_MS).toInt().coerceAtLeast(0)
        } else {
            0
        }
        return when (card.state) {
            FlashCard.STATE_NEW -> handleNew(card, rating, now)
            FlashCard.STATE_LEARNING -> handleLearning(card, rating, now)
            FlashCard.STATE_REVIEW -> handleReview(card, rating, now, elapsedDays)
            FlashCard.STATE_RELEARNING -> handleRelearning(card, rating, now, elapsedDays)
            else -> card
        }
    }

    private fun handleNew(card: FlashCard, rating: Int, now: Long): FlashCard {
        val s = initStability(rating)
        val d = initDifficulty(rating)
        return when (rating) {
            RATING_AGAIN, RATING_HARD -> card.copy(
                state = FlashCard.STATE_LEARNING,
                stability = s.toFloat(),
                difficulty = d.toFloat(),
                reps = card.reps + 1,
                scheduledDays = 1,
                dueDate = now + DAY_MS,
                lastReview = now
            )
            else -> {
                val interval = nextInterval(s)
                card.copy(
                    state = FlashCard.STATE_REVIEW,
                    stability = s.toFloat(),
                    difficulty = d.toFloat(),
                    reps = card.reps + 1,
                    scheduledDays = interval,
                    dueDate = now + interval * DAY_MS,
                    lastReview = now
                )
            }
        }
    }

    private fun handleLearning(card: FlashCard, rating: Int, now: Long): FlashCard {
        return when (rating) {
            RATING_AGAIN, RATING_HARD -> card.copy(
                state = FlashCard.STATE_LEARNING,
                reps = card.reps + 1,
                scheduledDays = 1,
                dueDate = now + DAY_MS,
                lastReview = now
            )
            else -> {
                val interval = nextInterval(card.stability.toDouble()).coerceAtLeast(1)
                card.copy(
                    state = FlashCard.STATE_REVIEW,
                    reps = card.reps + 1,
                    scheduledDays = interval,
                    dueDate = now + interval * DAY_MS,
                    lastReview = now
                )
            }
        }
    }

    private fun handleReview(card: FlashCard, rating: Int, now: Long, elapsedDays: Int): FlashCard {
        val r = forgettingCurve(elapsedDays, card.stability.toDouble())
        val d = card.difficulty.toDouble()
        val s = card.stability.toDouble()
        return when (rating) {
            RATING_AGAIN -> {
                val newS = forgetStability(d, s, r).toFloat()
                val newD = nextDifficulty(d, rating).toFloat()
                card.copy(
                    state = FlashCard.STATE_RELEARNING,
                    stability = newS,
                    difficulty = newD,
                    reps = card.reps + 1,
                    lapses = card.lapses + 1,
                    scheduledDays = 1,
                    dueDate = now + DAY_MS,
                    lastReview = now
                )
            }
            else -> {
                val newS = recallStability(d, s, r, rating).toFloat()
                val newD = nextDifficulty(d, rating).toFloat()
                val interval = nextInterval(newS.toDouble())
                card.copy(
                    state = FlashCard.STATE_REVIEW,
                    stability = newS,
                    difficulty = newD,
                    reps = card.reps + 1,
                    scheduledDays = interval,
                    dueDate = now + interval * DAY_MS,
                    lastReview = now
                )
            }
        }
    }

    private fun handleRelearning(card: FlashCard, rating: Int, now: Long, elapsedDays: Int): FlashCard {
        val r = forgettingCurve(elapsedDays, card.stability.toDouble())
        val d = card.difficulty.toDouble()
        val s = card.stability.toDouble()
        return when (rating) {
            RATING_AGAIN, RATING_HARD -> card.copy(
                state = FlashCard.STATE_RELEARNING,
                reps = card.reps + 1,
                lapses = if (rating == RATING_AGAIN) card.lapses + 1 else card.lapses,
                scheduledDays = 1,
                dueDate = now + DAY_MS,
                lastReview = now
            )
            else -> {
                val newS = recallStability(d, s, r, rating).toFloat()
                val newD = nextDifficulty(d, rating).toFloat()
                val interval = nextInterval(newS.toDouble()).coerceAtLeast(1)
                card.copy(
                    state = FlashCard.STATE_REVIEW,
                    stability = newS,
                    difficulty = newD,
                    reps = card.reps + 1,
                    scheduledDays = interval,
                    dueDate = now + interval * DAY_MS,
                    lastReview = now
                )
            }
        }
    }

    private fun initStability(rating: Int): Double = when (rating) {
        RATING_AGAIN -> w[0]
        RATING_HARD -> w[1]
        RATING_EASY -> w[3]
        else -> w[2]
    }

    private fun initDifficulty(rating: Int): Double =
        (w[4] - (rating - 3) * w[5]).coerceIn(1.0, 10.0)

    // R = (1 + FACTOR * t/S)^DECAY
    private fun forgettingCurve(elapsedDays: Int, stability: Double): Double {
        if (stability <= 0.0) return 0.0
        return (1.0 + FACTOR * elapsedDays / stability).pow(DECAY)
    }

    // Next review interval = S days (since R=0.9 when t=S for the FSRS model)
    private fun nextInterval(stability: Double): Int =
        stability.roundToInt().coerceAtLeast(1)

    private fun recallStability(d: Double, s: Double, r: Double, rating: Int): Double {
        val hardPenalty = if (rating == RATING_HARD) w[15] else 1.0
        val easyBonus = if (rating == RATING_EASY) w[16] else 1.0
        return s * (exp(w[8]) * (11.0 - d) * s.pow(-w[9]) *
                (exp(w[10] * (1.0 - r)) - 1.0) * hardPenalty * easyBonus + 1.0)
    }

    private fun forgetStability(d: Double, s: Double, r: Double): Double =
        (w[11] * d.pow(-w[12]) * ((s + 1.0).pow(w[13]) - 1.0) *
                exp(w[14] * (1.0 - r))).coerceAtLeast(0.1)

    private fun nextDifficulty(d: Double, rating: Int): Double {
        val deltaD = -w[6] * (rating - 3)
        return (d + deltaD + w[7] * (w[4] - (d + deltaD))).coerceIn(1.0, 10.0)
    }
}
