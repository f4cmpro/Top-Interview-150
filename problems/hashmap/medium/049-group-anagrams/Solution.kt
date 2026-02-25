package groupanagram

class Solution {
    fun groupAnagrams(strs: Array<String>): List<List<String>> {
        val strMap = HashMap<String, ArrayList<String>>()
        for (str in strs) {
            val key = generateMapKey(str)
            strMap.getOrPut(key) { arrayListOf() }.add(str)
        }
        val listGroupAnagrams = arrayListOf<List<String>>()
        for (item in strMap) {
            listGroupAnagrams.add(item.value)
        }
        return listGroupAnagrams
    }

    private fun generateMapKey(s: String): String {
        val letters = Array(26) { 0 }
        for (char in s) {
            letters[char.code - 'a'.code]++
        }
        val builder = StringBuilder()
        for (i in 0..25) {
            val count = letters[i]
            if (count > 0) {
                val char = Char('a'.code + i)
                builder.append(char).append(count)
            }
        }
        return builder.toString()
    }
}