package problem.hashmap.easy.ransomnote

class Solution {
    fun canConstruct(ransomNote: String, magazine: String): Boolean {
        val magazineHashMap = HashMap<Char, Int>()
        for (char in magazine) {
            magazineHashMap[char] = magazineHashMap.getOrDefault(char, 0) + 1
        }

        for (char in ransomNote) {
            if (magazineHashMap.getOrDefault(char, 0) <= 0) {
                return false
            }
            magazineHashMap[char] = magazineHashMap.getOrDefault(char, 0) - 1
        }
        return true
    }
}