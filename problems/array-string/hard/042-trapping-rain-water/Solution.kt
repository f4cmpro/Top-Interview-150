package `042-trapping-rain-water`

class Solution {
    fun trap(height: IntArray): Int {
        var totalWater = 0
        var leftSide = 0
        var rightSide = height.lastIndex
        var occupancy = 0
        for (i in 1 until height.size) {
            if (height[i] >= height[leftSide]) {
                val square = (i - leftSide - 1) * height[leftSide]
                totalWater += square - occupancy
                leftSide = i
                occupancy = 0
            } else {
                occupancy += height[i]
            }
        }
        occupancy = 0
        for (i in height.lastIndex - 1 downTo 0) {
            if (height[i] > height[rightSide]) {
                val square = (rightSide - i - 1) * height[rightSide]
                totalWater += square - occupancy
                rightSide = i
                occupancy = 0
            } else {
                occupancy += height[i]
            }
        }
        return totalWater
    }
}