package `057-insert-interval`

class Solution {
    fun insert(intervals: Array<IntArray>, newInterval: IntArray): Array<IntArray> {
        val ans = mutableListOf<IntArray>()
        var i = 0
        var start = newInterval[0]
        var end = newInterval[1]
        val n = intervals.size

        // Case 1: No overlapping before merging intervals
        while(i < n && intervals[i][1] < start){
            ans.add(intervals[i])
            i++
        }

        // Case 2: Overlapping and merging intervals
        while(i < n && intervals[i][0] <= end){
            start = minOf(intervals[i][0],start)
            end = maxOf(intervals[i][1],end)
            i++
        }
        ans.add(intArrayOf(start,end))

        // Case 3: No overlapping after merging newInterval
        while(i<n){
            ans.add(intervals[i])
            i++
        }

        return ans.toTypedArray()
    }
}