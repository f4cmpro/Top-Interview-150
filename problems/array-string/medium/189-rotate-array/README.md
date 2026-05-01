# 189 - Rotate Array

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/rotate-array/

---

## Solution Design

### 1. Clarification & Edge Cases:
*   **Input:** An integer array `nums` and an integer `k` (number of steps to rotate).
*   **Output:** The same array `nums` rotated in-place.
*   **Constraints:**
    *   `1 <= nums.length <= 10^5`
    *   `-2^31 <= nums[i] <= 2^31 - 1`
    *   `0 <= k <= 10^5`
    *   The problem asks for an in-place solution with O(1) extra space.
*   **Edge Cases:**
    *   **Empty Array:** The constraints state `nums.length >= 1`, so we don't need to handle an empty array.
    *   **Single Element Array:** Rotating it has no effect.
    *   **`k = 0`:** No rotation is needed.
    *   **`k` is a multiple of `nums.length`:** The array returns to its original state after `nums.length` rotations. We should handle this by taking `k % nums.length`.
    *   **`k > nums.length`:** Similar to the above, the effective rotation is `k % nums.length`.

### 2. High-Level Approach Analysis (Trade-offs):

*   **Brute Force Approach:**
    *   For each of the `k` steps, physically shift every element one position to the right. The last element becomes the first.
    *   **Time Complexity:** O(n * k), where `n` is the number of elements. For each of the `k` rotations, we iterate through all `n` elements.
    *   **Space Complexity:** O(1), as no extra space is used besides a temporary variable for the shift.

*   **Optimized Approach (Using Reversal):**
    *   This clever approach achieves the rotation in three steps using array reversals.
    1.  Reverse the entire array.
    2.  Reverse the first `k` elements (where `k` is the effective rotation count).
    3.  Reverse the remaining `n - k` elements.
    *   **Time Complexity:** O(n). Each element is touched a constant number of times (three reversals in total, each pass is O(n)).
    *   **Space Complexity:** O(1). The reversals are done in-place.

*   **Comparison:**
    *   The **Optimized Approach** is significantly better because its time complexity O(n) is linear, whereas the brute force approach O(n * k) can be quadratic in the worst case (if `k` is close to `n`). Given the constraints (`n` and `k` up to 10^5), the brute force method would be too slow and time out. The optimized solution is efficient and meets the O(1) extra space requirement.

### 3. Algorithm Design:

The logic relies on the three-reversal technique:

1.  **Handle Rotations:** The number of rotations `k` can be larger than the array's length. A full rotation of `n` steps brings the array back to its original state. Therefore, the actual number of rotations needed is `k % n`. Let's call this `effectiveK`.
2.  **First Reversal:** Reverse all elements in the entire array. For `[1,2,3,4,5,6,7]` and `k=3`, this results in `[7,6,5,4,3,2,1]`.
3.  **Second Reversal:** Reverse the first `effectiveK` elements. In our example, `effectiveK` is 3. Reversing the first 3 elements `[7,6,5]` gives `[5,6,7]`. The array becomes `[5,6,7,4,3,2,1]`.
4.  **Third Reversal:** Reverse the rest of the elements from index `effectiveK` to the end. Reversing `[4,3,2,1]` gives `[1,2,3,4]`. The array becomes `[5,6,7,1,2,3,4]`.
5.  **Result:** The array is now correctly rotated.

A helper function `reverse(array, start, end)` will be used to perform the in-place reversal of a sub-array. This function will use **Two Pointers**, one starting at `start` and one at `end`, swapping elements and moving towards the center until they meet.

### 4. Production-Ready Implementation:

```kotlin
class Solution {
    fun rotate(nums: IntArray, k: Int) {
        // Guard Clause: If array has 1 or fewer elements, or k is 0, no rotation is needed.
        if (nums.size <= 1) {
            return
        }

        // Calculate the effective number of rotations
        val effectiveK = k % nums.size

        if (effectiveK == 0) {
            return
        }

        // Step 1: Reverse the entire array
        reverse(nums, 0, nums.size - 1)

        // Step 2: Reverse the first k elements
        reverse(nums, 0, effectiveK - 1)

        // Step 3: Reverse the remaining n-k elements
        reverse(nums, effectiveK, nums.size - 1)
    }

    /**
     * Helper function to reverse a portion of an array in-place.
     * @param nums The array to modify.
     * @param start The starting index (inclusive).
     * @param end The ending index (inclusive).
     */
    private fun reverse(nums: IntArray, start: Int, end: Int) {
        var i = start
        var j = end
        while (i < j) {
            // Swap elements using a temporary variable
            val temp = nums[i]
            nums[i] = nums[j]
            nums[j] = temp
            i++
            j--
        }
    }
}
```

### 5. Verification & Complexity Finalization:

*   **Dry Run:**
    *   **Input:** `nums = [1, 2, 3, 4, 5]`, `k = 2`
    *   **`effectiveK`:** `2 % 5 = 2`.
    *   **Step 1 (Reverse all):** `nums` becomes `[5, 4, 3, 2, 1]`.
    *   **Step 2 (Reverse first `k=2`):** `reverse(nums, 0, 1)`. `[5, 4]` becomes `[4, 5]`. `nums` is now `[4, 5, 3, 2, 1]`.
    *   **Step 3 (Reverse remaining):** `reverse(nums, 2, 4)`. `[3, 2, 1]` becomes `[1, 2, 3]`. `nums` is now `[4, 5, 1, 2, 3]`.
    *   **Output:** `[4, 5, 1, 2, 3]`. The result is correct.

*   **Final Complexity:**
    *   **Time Complexity:** **O(n)**. The array is traversed a total of three times (once for each reversal), which simplifies to O(n).
    *   **Space Complexity:** **O(1)**. The rotation is performed in-place, using only a few variables for indices and one for the swap. No extra data structures proportional to the input size are used.
