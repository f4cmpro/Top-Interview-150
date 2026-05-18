package `array-string`.hard.`135-candy`

import kotlin.math.max

class Solution {
    fun candy(ratings: IntArray): Int {
        // Guard clause for empty input, though constraints say length >= 1
        if (ratings.isEmpty()) {
            return 0
        }

        val n = ratings.size
        val candies = IntArray(n) { 1 }

        // First pass: left to right
        // Ensure children with higher ratings than their left neighbor get more candies.
        for (i in 1 until n) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1
            }
        }

        // Second pass: right to left
        // Ensure children with higher ratings than their right neighbor get more candies.
        // This also handles cases where a child is a peak between two lower-rated neighbors.
        for (i in n - 2 downTo 0) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = max(candies[i], candies[i + 1] + 1)
            }
        }

        return candies.sum()
    }
}

