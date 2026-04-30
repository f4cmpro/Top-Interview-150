package `169-majority-element`

class Solution {
    fun majorityElement(nums: IntArray): Int {
        var candidate = nums[0]
        var count = 1
        for (i in 1 until nums.size) {
            if (nums[i] == candidate) {
                count++
                if (count > nums.size / 2) {
                    return candidate
                }
            } else {
                count--
                if (count < 1) {
                    candidate = nums[i]
                    count = 1
                }
            }
        }
        return candidate
    }
}
