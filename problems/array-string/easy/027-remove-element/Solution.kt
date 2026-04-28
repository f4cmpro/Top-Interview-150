class Solution {
    fun removeElement(nums: IntArray, `val`: Int): Int {
        if (`val` > 50) return nums.size
        var curr = 0
        var next = 1
        var count = 0
        while (curr < nums.size) {
            if (nums[curr] == `val`) {
                while (next < nums.size) {
                    if (nums[next] != nums[curr]) {
                        val temp = nums[curr]
                        nums[curr] = nums[next]
                        nums[next] = temp
                        next++
                        count++
                        break
                    }
                    next++
                }
            } else {
                count++
            }
            if (next >= nums.size) break
            curr++
            if (next <= curr) {
                next = curr + 1
            }
        }
        return count
    }
}
