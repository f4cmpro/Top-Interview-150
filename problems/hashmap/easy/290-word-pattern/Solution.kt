package wordPattern

class Solution {
    fun wordPattern(pattern: String, s: String): Boolean {
        val sSplit = s.split(" ")
        if (pattern.length != sSplit.size) return false
        // Two maps to ensure bijection
        val pToS = HashMap<Char, String>()  // pattern -> s mapping
        val sToP = HashMap<String, Char>()  // s -> pattern mapping (reverse)
        for (i in pattern.indices) {
            val char = pattern[i]
            val str = sSplit[i]

            // Check forward mapping consistency
            if (pToS.containsKey(char)) {
                if (sToP[str] != char) {
                    return false  // char already maps to different str
                }
            }

            // Check reverse mapping consistency
            if (sToP.containsKey(str)) {
                if (pToS[char] != str) {
                    return false  // str already maps from different character
                }
            }

            pToS[char] = str
            sToP[str] = char

        }

        return true
    }
}