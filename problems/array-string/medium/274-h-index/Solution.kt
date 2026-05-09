package `274-h-index`

class Solution {
    fun hIndex(citations: IntArray): Int {
        citations.sort()
        var hIndex = 0
        for (i in 0 until citations.size) {
            if (citations[i] >= citations.size - i) {
                hIndex = citations.size - i
                break
            }
        }
        return hIndex
    }
}