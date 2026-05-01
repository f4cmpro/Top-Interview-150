package `189-rotate-array`

class Solution {
    fun rotate(nums: IntArray, k: Int): Unit {
        val rotateCount = k % nums.size
        if (rotateCount == 0) return
        //reversed array
        for (i in 0 until nums.size / 2) {
            val temp = nums[i]
            nums[i] = nums[nums.size - i - 1]
            nums[nums.size - i - 1] = temp
        }
        println("reversed array: ${nums.contentToString()}")
        //reverse k group
        for (i in 0 until rotateCount / 2) {
            val temp = nums[i]
            nums[i] = nums[rotateCount - i - 1]
            nums[rotateCount - i - 1] = temp
        }
        println("reverse k group: ${nums.contentToString()}")
        //reverse n-k group
        for (i in 0 until (nums.size - rotateCount) / 2) {
            val temp = nums[rotateCount + i]
            nums[rotateCount + i] = nums[nums.size - i - 1]
            nums[nums.size - i - 1] = temp
        }
        println("reverse n-k group: ${nums.contentToString()}")

    }
}