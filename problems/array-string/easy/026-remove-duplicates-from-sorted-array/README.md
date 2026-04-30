# 26 - Remove Duplicates from Sorted Array

**Difficulty**: 🟢 Easy  
**LeetCode**: https://leetcode.com/problems/remove-duplicates-from-sorted-array/

```
This is the description of what the code block changes:
<changeDescription>
Appending the detailed solution design to the README.md file, following the structure requested in the prompt.
</changeDescription>

This is the code block that represents the suggested code change:
````markdown

---

### 1. Clarification & Edge Cases:
*   **Input:** A sorted integer array `nums`.
*   **Output:** An integer `k` representing the number of unique elements. The first `k` elements of `nums` should be the unique elements in their original order.
*   **Constraints:**
    *   The input array is sorted in non-decreasing order.
    *   The duplicates must be removed **in-place**, meaning we modify the input array directly without using extra space for another array.
    *   The relative order of the unique elements must be preserved.
*   **Edge Cases:**
    *   **Empty array:** `nums = []`. The result should be `k = 0`.
    *   **Array with one element:** `nums = [1]`. The result should be `k = 1`.
    *   **Array with all identical elements:** `nums = [2, 2, 2, 2]`. The result should be `k = 1`, and the array should start with `[2]`.
    *   **Array with no duplicates:** `nums = [1, 2, 3, 4]`. The result should be `k = 4`.
    *   **Array containing negative numbers:** `nums = [-3, -1, -1, 0, 2, 2]`. The result should be `k = 4`, and the array should start with `[-3, -1, 0, 2]`.

### 2. High-Level Approach Analysis (Trade-offs):

*   **Brute Force (Using Extra Space):**
    *   **Description:** Iterate through the input array and store unique elements in a separate data structure, like a `Set` or a new `List`. Then, copy the unique elements from the new structure back into the beginning of the original `nums` array.
    *   **Time Complexity:** `O(n)` to iterate through the array and `O(k)` to copy elements back, where `n` is the number of elements and `k` is the number of unique elements. This simplifies to `O(n)`.
    *   **Space Complexity:** `O(k)` or `O(n)` to store the unique elements in the auxiliary data structure. This violates the in-place requirement.

*   **Optimized Approach (Two Pointers):**
    *   **Description:** Use two pointers. A `write` pointer (`k`) keeps track of the next position to place a unique element, and a `read` pointer (`i`) iterates through the entire array. If `nums[i]` is different from `nums[k-1]`, it's a new unique element, so we place it at `nums[k]` and increment `k`.
    *   **Time Complexity:** `O(n)`. We iterate through the array only once.
    *   **Space Complexity:** `O(1)`. The modification is done in-place, using only a constant amount of extra space for the pointers.
    *   **Comparison:** The Two Pointers approach is superior because it meets the `O(1)` space complexity constraint of the problem by modifying the array in-place, whereas the brute-force method requires extra memory.

### 3. Algorithm Design:

The optimized algorithm uses a **Two Pointers** technique.

1.  Initialize a `write` pointer, let's call it `k`, to `1`. This pointer marks the position where the next unique element should be placed. We start at `1` because the first element of the array is always unique by definition (it has no preceding element).
2.  Initialize a `read` pointer, `i`, to `1` as well. This pointer will scan the array from the second element to the end.
3.  Iterate with the `read` pointer `i` from `1` to `n-1` (where `n` is the size of the array).
4.  In each iteration, compare the element at the current `read` position, `nums[i]`, with the last recorded unique element, `nums[k-1]`.
5.  **If `nums[i]` is different from `nums[k-1]`:**
    *   It means we have found a new unique element.
    *   Copy the value from `nums[i]` to `nums[k]`.
    *   Increment the `write` pointer `k` to prepare for the next unique element.
6.  **If `nums[i]` is the same as `nums[k-1]`:**
    *   It's a duplicate. Do nothing and simply continue the loop, effectively skipping over this duplicate element.
7.  After the loop finishes, `k` will hold the count of the unique elements, which is the new logical length of the modified array. Return `k`.

### 4. Production-Ready Implementation:

```kotlin
class Solution {
    fun removeDuplicates(nums: IntArray): Int {
        // Guard Clause: Handle the edge case of an empty array.
        if (nums.isEmpty()) {
            return 0
        }

        // 'k' is the write pointer. It tracks the position for the next unique element.
        // It starts at 1 because the first element is always considered unique.
        var k = 1

        // 'i' is the read pointer. It iterates through the array to find unique elements.
        for (i in 1 until nums.size) {
            // If the current element is different from the last unique element found,
            // it's a new unique element.
            if (nums[i] != nums[k - 1]) {
                // Place the new unique element at the position indicated by 'k'.
                nums[k] = nums[i]
                // Move the write pointer to the next available slot.
                k++
            }
        }

        // 'k' now represents the number of unique elements.
        return k
    }
}
```

### 5. Verification & Complexity Finalization:

*   **Dry Run:**
    *   Let `nums = [0, 0, 1, 1, 1, 2, 2, 3, 3, 4]`.
    *   **Initial State:** `k = 1`, `i = 1`. `nums` is unchanged.
    *   **i = 1:** `nums[1]` (0) is equal to `nums[k-1]` (0). Do nothing.
    *   **i = 2:** `nums[2]` (1) is not equal to `nums[k-1]` (0).
        *   Set `nums[k]` (i.e., `nums[1]`) to `nums[2]` (1). `nums` becomes `[0, 1, 1, 1, 1, 2, 2, 3, 3, 4]`.
        *   Increment `k` to `2`.
    *   **i = 3:** `nums[3]` (1) is equal to `nums[k-1]` (1). Do nothing.
    *   **i = 4:** `nums[4]` (1) is equal to `nums[k-1]` (1). Do nothing.
    *   **i = 5:** `nums[5]` (2) is not equal to `nums[k-1]` (1).
        *   Set `nums[k]` (i.e., `nums[2]`) to `nums[5]` (2). `nums` becomes `[0, 1, 2, 1, 1, 2, 2, 3, 3, 4]`.
        *   Increment `k` to `3`.
    *   ...The process continues.
    *   **Final State:** After the loop, `k` will be `5`. The first 5 elements of `nums` will be `[0, 1, 2, 3, 4]`. The function returns `5`. The logic is correct.

*   **Time Complexity:** `O(n)`. The `read` pointer `i` traverses the array once from beginning to end.
*   **Space Complexity:** `O(1)`. We are not using any extra data structures that scale with the input size. The modification is done in-place.

````
