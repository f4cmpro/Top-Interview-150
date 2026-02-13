package validAnagram

class Solution {
    fun isAnagram(s: String, t: String): Boolean {
        if (s.length != t.length) return false
        val letters = Array(26) { 0 }
        for (char in s) {
            letters[char.code - 'a'.code]++
        }
        for (char in t) {
            val index = char.code - 'a'.code
            letters[index]--
            if (letters[index] < 0) {
                return false
            }
        }
        return letters.sum() == 0
    }
}