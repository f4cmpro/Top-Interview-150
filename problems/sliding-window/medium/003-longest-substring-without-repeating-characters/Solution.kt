import kotlin.math.max

class Solution {
    fun lengthOfLongestSubstring(s: String): Int {
        // ASCII array to store last seen index of each character
        // Initialized to -1 to indicate "not seen"
        val ascii = IntArray(128) { -1 }

        var left = 0        // Left boundary of sliding window
        var maxLength = 0   // Maximum length found so far

        // Expand window with right pointer
        for (right in s.indices) {
            val char = s[right]
            val charLatestPos = ascii[char.code]

            // If character was seen in current window, move left pointer
            // to exclude the previous occurrence
            left = max(left, charLatestPos + 1)

            // Update the last seen position of current character
            ascii[char.code] = right

            // Calculate current window size and update max
            maxLength = max(maxLength, right - left + 1)
        }
        return maxLength
    }
}

fun main() {
    val answer = Solution().lengthOfLongestSubstring("bbbbb")
    println("out = $answer")
}