package happyNumber

class Solution {
    /**
     * Determines if a number is a "happy number".
     * A happy number is defined by repeatedly replacing it with the sum of squares
     * of its digits until it equals 1, or cycles endlessly.
     *
     * @param n The positive integer to check (1 ≤ n ≤ 2³¹ - 1)
     * @return true if n is a happy number, false otherwise
     */
    fun isHappy(n: Int): Boolean {
        // Guard clause: 1 is already happy
        if (n == 1) return true

        // Track single-digit sums we've seen to detect cycles
        val seenSingleDigits = BooleanArray(10) { false }
        var current = n

        while (true) {
            // Calculate sum of squares of digits
            val sumOfSquares = getSumOfSquares(current)

            // Happy number found
            if (sumOfSquares == 1) {
                return true
            }

            // Cycle detection for single-digit numbers
            if (sumOfSquares < 10) {
                // If we've seen this single digit before, we're in a cycle
                if (seenSingleDigits[sumOfSquares]) {
                    return false
                }
                seenSingleDigits[sumOfSquares] = true
            }

            // Continue with the new sum
            current = sumOfSquares
        }
    }

    /**
     * Calculates the sum of squares of all digits in a number.
     *
     * @param num The number to process
     * @return Sum of squares of all digits
     */
    private fun getSumOfSquares(num: Int): Int {
        var sum = 0
        var remaining = num

        while (remaining > 0) {
            val digit = remaining % 10
            sum += digit * digit
            remaining /= 10
        }

        return sum
    }
}

fun main() {
    val solution = Solution()

    // Test cases
    println("isHappy(19) = ${solution.isHappy(19)}")  // Expected: true
    println("isHappy(2) = ${solution.isHappy(2)}")    // Expected: false
    println("isHappy(1) = ${solution.isHappy(1)}")    // Expected: true
    println("isHappy(7) = ${solution.isHappy(7)}")    // Expected: true
}