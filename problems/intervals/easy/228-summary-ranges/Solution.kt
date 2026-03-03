class Solution {
    fun summaryRanges(nums: IntArray): List<String> {
        val rtn: MutableList<String> = mutableListOf()
        var i = 0

        while (i < nums.size) {
            val start = nums[i]

            // Extend range while consecutive
            while (i + 1 < nums.size && nums[i + 1] == nums[i] + 1) {
                i++
            }

            // Single number or range
            if (nums[i] == start) {
                rtn.add("$start")
            } else {
                rtn.add("$start->${nums[i]}")
            }

            i++
        }

        return rtn
    }
}