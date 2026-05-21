# 42 - Trapping Rain Water

**Difficulty**: 🔴 Hard  
**LeetCode**: https://leetcode.com/problems/trapping-rain-water/---
### 1. Clarification & Edge Cases:
*   **Input:** An array of non-negative integers `height` representing an elevation map.
*   **Output:** An integer representing the total amount of trapped water.
*   **Constraints:**
    *   `height.length` (n) is between 1 and 2 * 10^4.
    *   `height[i]` is between 0 and 10^5.
*   **Edge Cases:**
    *   **Empty or small array:** If the array has fewer than 3 elements, it's impossible to trap water.
    *   **No depressions:** An array that is strictly increasing, decreasing, or flat will trap no water.
    *   **Single peak:** An array with one major peak in the middle.
    *   **Boundaries:** The first and last elements cannot trap water as they have no outer wall on one side.
### 2. High-Level Approach Analysis (Trade-offs):
*   **Brute Force Approach:**
    *   Iterate through each bar of the elevation map (excluding the first and last). For each bar `height[i]`, find the maximum height of a bar to its left (`maxLeft`) and the maximum height to its right (`maxRight`).
    *   The amount of water trapped above the current bar is `min(maxLeft, maxRight) - height[i]`. This value is only added if it's positive.
    *   Sum these amounts for all bars.
    *   **Time Complexity:** `O(n^2)` - For each of the `n` bars, we scan the array to find the left and right max heights, which takes `O(n)` time.
    *   **Space Complexity:** `O(1)`.
*   **Optimized Approach (Two Pointers):**
    *   This approach avoids the repeated scans of the brute-force method by using two pointers, `left` and `right`, starting at the opposite ends of the array.
    *   We also maintain two variables, `maxLeft` and `maxRight`, to keep track of the maximum height encountered so far from each side.
    *   The key insight is that the amount of water trapped at any point is limited by the shorter of the `maxLeft` and `maxRight` walls.
    *   By moving the pointer associated with the shorter wall, we can confidently calculate the trapped water for that position.
    *   **Time Complexity:** `O(n)` - We iterate through the array only once with the two pointers.
    *   **Space Complexity:** `O(1)` - We only use a few variables to store state, regardless of the input size.
*   **Comparison:**
    The Two Pointers approach is significantly better because its linear time complexity (`O(n)`) is a major improvement over the quadratic time complexity (`O(n^2)`) of the brute-force method, especially for large inputs. It achieves this optimization without requiring any additional space, making it highly efficient.
### 3. Algorithm Design:
The optimized Two Pointers algorithm works as follows:
1.  **Initialization:**
    *   Initialize a `left` pointer to the start of the array (index 0) and a `right` pointer to the end (index `n-1`).
    *   Initialize `maxLeft` to `height[left]` and `maxRight` to `height[right]`.
    *   Initialize a `totalWater` variable to 0.
2.  **Iteration:**
    *   Loop as long as `left` is less than `right`.
    *   **Compare Walls:** Check if `maxLeft` is smaller than `maxRight`.
        *   **If `maxLeft` is smaller:** This means the trapping wall on the left is the bottleneck.
            *   Move the `left` pointer one step to the right (`left++`).
            *   Calculate the trapped water at this new `left` position: `maxLeft - height[left]`. If this value is positive, add it to `totalWater`.
            *   Update `maxLeft` to be the maximum of its current value and `height[left]`.
        *   **If `maxRight` is smaller or equal:** The right wall is the bottleneck.
            *   Move the `right` pointer one step to the left (`right--`).
            *   Calculate the trapped water at this new `right` position: `maxRight - height[right]`. If this value is positive, add it to `totalWater`.
            *   Update `maxRight` to be the maximum of its current value and `height[right]`.
3.  **Termination:**
    *   The loop terminates when the `left` and `right` pointers meet.
    *   Return `totalWater`.
### 4. Production-Ready Implementation:
```kotlin
class Solution {
    fun trap(height: IntArray): Int {
        // Guard Clause: Cannot trap water with fewer than 3 bars.
        if (height.size < 3) {
            return 0
        }
        var totalWater = 0
        var left = 0
        var right = height.size - 1
        var maxLeft = height[left]
        var maxRight = height[right]
        while (left < right) {
            // The amount of trapped water is determined by the shorter of the two max walls.
            if (maxLeft < maxRight) {
                // Move the left pointer since the left wall is the bottleneck.
                left++
                val currentHeight = height[left]
                // Calculate trapped water at the current position.
                // The water level is determined by the shorter wall (maxLeft).
                val trapped = maxLeft - currentHeight
                if (trapped > 0) {
                    totalWater += trapped
                }
                // Update the max height seen from the left.
                maxLeft = maxOf(maxLeft, currentHeight)
            } else {
                // Move the right pointer since the right wall is the bottleneck.
                right--
                val currentHeight = height[right]
                // Calculate trapped water at the current position.
                // The water level is determined by the shorter wall (maxRight).
                val trapped = maxRight - currentHeight
                if (trapped > 0) {
                    totalWater += trapped
                }
                // Update the max height seen from the right.
                maxRight = maxOf(maxRight, currentHeight)
            }
        }
        return totalWater
    }
}
```
### 5. Verification & Complexity Finalization:
*   **Dry Run Example:** `height = [0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1]`
    1.  `left=0`, `right=11`, `maxLeft=0`, `maxRight=1`, `totalWater=0`.
    2.  `maxLeft < maxRight`. `left` becomes 1. `height[1]=1`. `trapped = 0-1 = -1` (no water). `maxLeft` becomes 1.
    3.  `maxLeft == maxRight` (1==1). `right` becomes 10. `height[10]=2`. `trapped = 1-2 = -1`. `maxRight` becomes 2.
    4.  `maxLeft < maxRight` (1<2). `left` becomes 2. `height[2]=0`. `trapped = 1-0 = 1`. `totalWater=1`. `maxLeft` remains 1.
    5.  `maxLeft < maxRight` (1<2). `left` becomes 3. `height[3]=2`. `trapped = 1-2 = -1`. `maxLeft` becomes 2.
    6.  `maxLeft == maxRight` (2==2). `right` becomes 9. `height[9]=1`. `trapped = 2-1 = 1`. `totalWater=2`. `maxRight` remains 2.
    7.  ...The process continues until `left` meets `right`.
    8.  The final `totalWater` will be 6.
*   **Final Complexity:**
    *   **Time Complexity:** `O(n)` because the `left` and `right` pointers traverse the array exactly once.
    *   **Space Complexity:** `O(1)` because we only use a constant number of extra variables (`totalWater`, `left`, `right`, `maxLeft`, `maxRight`).
