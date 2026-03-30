class Solution {
    fun findMinArrowShots(points: Array<IntArray>): Int {
        points.sortBy { it[1] }
        var intervalEnd = points[0][1]
        var count = 1
        for (i in 1 until points.size) {
            if(intervalEnd < points[i][0]) {
                count++
                intervalEnd = points[i][1]
            }
        }
        return count
    }
}