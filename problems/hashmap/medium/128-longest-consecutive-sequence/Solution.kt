class Solution {
    fun longestConsecutive(nums: IntArray): Int {
        if (nums.isEmpty() || nums.size == 1) return nums.size
        val consecutiveSet = HashSet<Int>()
        for (num in nums) {
            if (!consecutiveSet.contains(num)) {
                consecutiveSet.add(num)
            }
        }
        var max = 1
        for (key in consecutiveSet) {
            if (consecutiveSet.contains(key - 1)) {
                continue
            }
            var count = 1
            while (consecutiveSet.contains(key + count)) {
                count++
            }
            max = maxOf(max, count)
        }
        return max
    }
}