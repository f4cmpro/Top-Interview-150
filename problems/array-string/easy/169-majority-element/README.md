# 169 - Majority Element

**Difficulty**: 🟢 Easy  
**LeetCode**: https://leetcode.com/problems/majority-element/

---

### 1. Clarification & Edge Cases:
*   **Input:** A non-empty array of integers, `nums`.
*   **Output:** The integer that appears more than `⌊n / 2⌋` times.
*   **Constraints:**
    *   The array is non-empty.
    *   The majority element is guaranteed to exist.
    *   `n` is the size of the array.
*   **Edge Cases:**
    *   **Single-element array:** If `nums = [1]`, the majority element is `1`.
    *   **Array with all same elements:** If `nums = [2, 2, 2]`, the majority element is `2`.

### 2. High-Level Approach Analysis (Trade-offs):
*   **Brute Force:**
    *   Iterate through each element `x` in the array. For each `x`, iterate through the entire array again to count its occurrences. If the count is greater than `n / 2`, return `x`.
    *   **Time Complexity:** O(n²) - Two nested loops.
    *   **Space Complexity:** O(1) - No extra space used.

*   **Optimized Approach (Boyer-Moore Voting Algorithm):**
    *   This algorithm finds the majority element in linear time and constant space. It works by maintaining a `candidate` element and a `count`. The count is incremented when we see the candidate and decremented otherwise. If the count reaches zero, we pick a new candidate.
    *   **Time Complexity:** O(n) - We iterate through the array only once.
    *   **Space Complexity:** O(1) - We only use two variables (`candidate`, `count`).
*   **Comparison:** The Boyer-Moore algorithm is significantly more efficient than the brute-force approach for larger inputs, as its time complexity grows linearly rather than quadratically. Given the constraints, O(n²) would be too slow.

### 3. Algorithm Design:
The Boyer-Moore Voting Algorithm works as follows:
1.  Initialize two variables: `candidate` to hold the current majority candidate, and `count` to 0.
2.  Iterate through the input array `nums`.
3.  For each element `num`:
    *   If `count` is 0, it means we need to choose a new candidate. Set `candidate = num` and `count = 1`.
    *   If `num` is the same as the current `candidate`, increment `count`.
    *   If `num` is different from the `candidate`, decrement `count`.
4.  After the loop finishes, the `candidate` variable will hold the majority element. The problem statement guarantees that a majority element always exists, so a second pass to verify the candidate is not necessary.

### 4. Production-Ready Implementation:
```kotlin
class Solution {
    fun majorityElement(nums: IntArray): Int {
        // Guard Clause: Handle the trivial case of a single-element array.
        if (nums.size == 1) {
            return nums[0]
        }

        var candidate: Int? = null
        var count = 0

        for (num in nums) {
            if (count == 0) {
                // If count is zero, we select the current element as the new candidate.
                candidate = num
            }
            
            // If the current element matches the candidate, increment count. Otherwise, decrement.
            count += if (num == candidate) 1 else -1
        }

        // The problem guarantees a majority element always exists,
        // so the candidate at the end of the single pass is the answer.
        return candidate!!
    }
}
```

### 5. Verification & Complexity Finalization:
*   **Dry Run:** Let's trace with `nums = [2, 2, 1, 1, 1, 2, 2]`.
    1.  `num = 2`: `count` is 0. `candidate` becomes 2, `count` becomes 1.
    2.  `num = 2`: `num == candidate`. `count` becomes 2.
    3.  `num = 1`: `num != candidate`. `count` becomes 1.
    4.  `num = 1`: `num != candidate`. `count` becomes 0.
    5.  `num = 1`: `count` is 0. `candidate` becomes 1, `count` becomes 1.
    6.  `num = 2`: `num != candidate`. `count` becomes 0.
    7.  `num = 2`: `count` is 0. `candidate` becomes 2, `count` becomes 1.
    *   End of loop. The final `candidate` is `2`. The majority element is indeed 2. Let's re-verify the logic. Ah, the dry run was slightly off. Let's re-trace.
    *   `nums = [2, 2, 1, 1, 1, 2, 2]`, n=7. Majority needed > 3.5. It's 2 (appears 4 times).
    1. `num=2`: `count=0` -> `candidate=2`, `count=1`.
    2. `num=2`: `num==candidate` -> `count=2`.
    3. `num=1`: `num!=candidate` -> `count=1`.
    4. `num=1`: `num!=candidate` -> `count=0`.
    5. `num=1`: `count=0` -> `candidate=1`, `count=1`.
    6. `num=2`: `num!=candidate` -> `count=0`.
    7. `num=2`: `count=0` -> `candidate=2`, `count=1`.
    *   Final candidate is 2. Correct.

*   **Final Complexity:**
    *   **Time Complexity:** O(n) because we iterate through the array a single time.
    *   **Space Complexity:** O(1) because we only use a few variables to store the candidate and count, regardless of the input array size.
