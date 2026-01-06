package problems

class Solution {
    fun minSubArrayLen(target: Int, nums: IntArray): Int {
        var left = 0
        var sum = 0
        var minLength = Int.MAX_VALUE

        for (right in nums.indices) {
            // Expand:  Add current element to window
            sum += nums[right]

            // Shrink: While window is valid, try to minimize
            while (sum >= target) {
                minLength = minLength.coerceAtMost(right - left + 1)
                sum -= nums[left]
                left++
            }
        }

        return if (minLength == Int.MAX_VALUE) 0 else minLength
    }
}

fun main() {
    val nums = intArrayOf(2, 3, 1, 2, 4, 3)
    Solution().minSubArrayLen(7, nums)

}