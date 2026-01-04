package problems.two_pointers.three_sum

class Solution {
    fun threeSum(nums: IntArray): List<List<Int>> {
        nums.sort()
        var fixedIndex = 0
        val listThreeSum = arrayListOf<List<Int>>()
        print("nums = [")
        for (i in nums) {
            print("$i,")
        }
        println("]")
        while (fixedIndex < nums.size - 2 && nums[fixedIndex] <= 0) {
            var left = fixedIndex + 1
            var right = nums.lastIndex
            while (left < right) {
                val sum = nums[fixedIndex] + nums[left] + nums[right]
                when {
                    sum > 0 -> {
                        right--
                        while (left < right && nums[right] == nums[right + 1]) {
                            right--
                        }
                    }

                    sum < 0 -> {
                        left++
                        while (left < right && nums[left] == nums[left - 1]) {
                            left++
                        }
                    }

                    else -> {
                        val listInt = listOf(nums[fixedIndex], nums[left], nums[right])
                        listThreeSum.add(listInt)
                        left++
                        while (left < right && nums[left] == nums[left - 1]) {
                            left++
                        }
                        right--
                        while (left < right && nums[right] == nums[right + 1]) {
                            right--
                        }
                    }
                }
            }
            fixedIndex++
            while (fixedIndex < nums.size && nums[fixedIndex] == nums[fixedIndex - 1]) {
                fixedIndex++
            }
        }
        print("threesums = [")
        for (i in listThreeSum) {
            print("$i,")
        }
        println("]")
        return listThreeSum
    }
}

fun main() {
    val nums = intArrayOf(0, 0, 0, 0)
    val sol = Solution()
    sol.threeSum(nums)
}