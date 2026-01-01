package problems.array_string.candy
class Solution {
    fun isPalindrome(s: String): Boolean {
        var left = 0
        var right = s.length - 1

        val arr = s.toCharArray()

        while (left < right) {
            if (!arr[left].isLetterOrDigit()) {
                left += 1
            } else if (!arr[right].isLetterOrDigit()) {
                right -= 1
            } else {
                val leftChar = arr[left].lowercaseChar()
                val rightChar = arr[right].lowercaseChar()
                if (leftChar == rightChar) {
                    right -= 1
                    left += 1
                }else {
                    return false
                }

            }
        }
        return true
    }
}