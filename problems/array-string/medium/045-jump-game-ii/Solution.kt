class Solution {
    fun jump(nums: IntArray): Int {
        var count = 0
        var currentReach = 0
        var maxCurrentReach = 0
        for (i in 0 until nums.lastIndex) {
            maxCurrentReach = maxOf(maxCurrentReach, i + nums[i])
            if (i == currentReach) {
                count++
                currentReach = maxCurrentReach
                if (maxCurrentReach >= nums.lastIndex) {
                    break
                }
            }
        }
        return count
    }
}