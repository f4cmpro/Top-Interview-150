# 55 - Jump Game

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/jump-game/

---

### 1. Clarification & Edge Cases:
*   **Input:** An array of non-negative integers `nums`.
*   **Output:** `true` if the last index is reachable, `false` otherwise.
*   **Constraints:**
    *   `1 <= nums.length <= 10^4`
    *   `0 <= nums[i] <= 10^5`
*   **Edge Cases:**
    *   **Single element array:** `[x]` should return `true` as we are already at the last index.
    *   **Zeroes:** An array containing zeroes can create "traps." For example, `[3, 2, 1, 0, 4]`.
    *   **First element is zero:** If `nums.length > 1` and `nums[0] == 0`, we cannot move, so it should be `false`.

### 2. High-Level Approach Analysis (Trade-offs):
*   **Brute Force (Backtracking):**
    *   Start at index 0. Recursively try every possible jump from the current position. If any path reaches the last index, return `true`.
    *   **Time Complexity:** O(2^n). For each element, we might explore multiple jump options, leading to an exponential number of paths.
    *   **Space Complexity:** O(n) for the recursion stack.

*   **Optimized Approach (Greedy):**
    *   The core idea is to iterate through the array, keeping track of the `max_reach`, which is the farthest index we can get to from our current position `i`.
    *   If at any point `i > max_reach`, it means we are stuck in a "trap" and can never reach the current index `i`, let alone the end.
    *   **Comparison:** The Greedy approach is significantly better because it avoids re-computing possibilities. It solves the problem in a single pass.
    *   **Time Complexity:** O(n), as we only iterate through the array once.
    *   **Space Complexity:** O(1), as we only use a few variables to store state.

### 3. Algorithm Design:
1.  Initialize a variable `max_reach` to 0. This will store the farthest index we can jump to.
2.  Iterate through the array `nums` with an index `i` from 0 to `n-1`.
3.  Inside the loop, first check if the current index `i` is beyond our `max_reach`. If `i > max_reach`, it means we could not have possibly reached this index. Therefore, return `false`.
4.  Update `max_reach` by taking the maximum of its current value and `i + nums[i]` (the farthest we can jump from the current position).
5.  If the loop completes, it means we were never stuck and could always advance. This implies the last index is reachable. Return `true`.

### 4. Production-Ready Implementation:
```kotlin
class Solution {
    /**
     * Determines if the end of the array can be reached by jumping.
     *
     * @param nums An array of non-negative integers where each element
     *             represents the maximum jump length from that position.
     * @return True if the last index is reachable, false otherwise.
     */
    fun canJump(nums: IntArray): Boolean {
        // Guard Clause: Handle the edge case of a single-element array.
        if (nums.size <= 1) {
            return true
        }

        var maxReach = 0
        for (i in nums.indices) {
            // If the current index `i` is greater than the maximum reach,
            // it means we are stuck and cannot proceed.
            if (i > maxReach) {
                return false
            }

            // Update the maximum reach with the farthest jump possible
            // from the current index `i`.
            maxReach = maxOf(maxReach, i + nums[i])

            // Optimization: If our maximum reach is already at or beyond the last index,
            // we can stop early and return true.
            if (maxReach >= nums.size - 1) {
                return true
            }
        }

        // This part is technically unreachable due to the check inside the loop,
        // but it's here for logical completeness.
        return false
    }
}
```

### 5. Verification & Complexity Finalization:
*   **Dry Run (Example: `nums = [2, 3, 1, 1, 4]`):**
    1.  `maxReach` = 0.
    2.  `i = 0`: `i` is not > `maxReach`. `maxReach` = max(0, 0 + 2) = 2. `maxReach` (2) is not >= 4.
    3.  `i = 1`: `i` is not > `maxReach`. `maxReach` = max(2, 1 + 3) = 4. `maxReach` (4) is >= 4. Return `true`. The function correctly terminates.

*   **Dry Run (Example: `nums = [3, 2, 1, 0, 4]`):**
    1.  `maxReach` = 0.
    2.  `i = 0`: `i` is not > `maxReach`. `maxReach` = max(0, 0 + 3) = 3. `maxReach` (3) is not >= 4.
    3.  `i = 1`: `i` is not > `maxReach`. `maxReach` = max(3, 1 + 2) = 3. `maxReach` (3) is not >= 4.
    4.  `i = 2`: `i` is not > `maxReach`. `maxReach` = max(3, 2 + 1) = 3. `maxReach` (3) is not >= 4.
    5.  `i = 3`: `i` is not > `maxReach`. `maxReach` = max(3, 3 + 0) = 3. `maxReach` (3) is not >= 4.
    6.  `i = 4`: `i` (4) is > `maxReach` (3). Return `false`. The function correctly identifies the trap.

*   **Final Complexity:**
    *   **Time Complexity:** O(n) - We perform a single pass through the input array.
    *   **Space Complexity:** O(1) - We use a constant amount of extra space (`maxReach` variable).
