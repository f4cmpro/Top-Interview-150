package problem.hashmap.easy.isomorphic

class Solution {
    fun isIsomorphic(s: String, t: String): Boolean {
        val stMap = HashMap<Char, Char>()
        for (i in s.indices) {
            val char1 = s[i]
            val char2 = t[i]
            if ((stMap[char1] != null && stMap[char1]!! != char2)
                || (stMap[char1] == null && stMap.values.contains(char2))
            ) {
                return false
            }
            stMap[char1] = char2
        }
        return true
    }
}