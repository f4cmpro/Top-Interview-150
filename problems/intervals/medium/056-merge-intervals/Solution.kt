package `056-merge-intervals`

class Solution {
    fun merge(intervals: Array<IntArray>): Array<IntArray> {
        if (intervals.size == 1) return intervals
        intervals.sortBy { it.first() }
        val nonOverlaps: MutableList<IntArray> = mutableListOf()
        var i = 0
        while (i < intervals.size) {
            val start = intervals[i][0]
            var end = intervals[i][1]

            // Extend range while consecutive
            while (i + 1 < intervals.size && end >= intervals[i + 1][0]) {
                i++
                if (end < intervals[i][1]) {
                    end = intervals[i][1]
                }
            }
            nonOverlaps.add(intArrayOf(start, end))
            i++
        }

        return nonOverlaps.toTypedArray()
    }
}