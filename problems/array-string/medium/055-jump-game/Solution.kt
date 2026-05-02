package `055-jump-game`

class Solution {
    fun canJump(nums: IntArray): Boolean {
        if (nums.size == 1) return true
        var canJump = false
        var destination = nums.lastIndex
        var backward = nums.lastIndex - 1
        while (backward >= 0) {
            val jump = nums[backward]
            if (backward + jump >= destination) {
                canJump = true
                destination = backward
            } else {
                canJump = false
            }
            backward--
        }
        return canJump
    }
}