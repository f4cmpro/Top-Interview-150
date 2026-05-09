# 274 - H-Index

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/h-index/

---

### 1. Clarification & Edge Cases:
*   **Input:** An array of integers `citations`.
*   **Output:** A single integer representing the h-index.
*   **Constraints:**
    *   `citations.length` will be between 1 and 5000.
    *   Each citation count will be between 0 and 1000.
*   **Edge Cases:**
    *   **Empty Array:** The problem constraints state the array will have at least one element, but it's good practice to consider. If it were possible, the h-index would be 0.
    *   **Single Element:** e.g., `[100]` -> h-index is 1; `[0]` -> h-index is 0.
    *   **All Same Citations:** e.g., `[5, 5, 5, 5, 5]` -> h-index is 5.
    *   **Citations with Zeros:** e.g., `[3, 0, 6, 1, 5]` -> h-index is 3.

### 2. High-Level Approach Analysis (Trade-offs):

*   **Brute Force Approach:**
    The h-index `h` can range from 0 to `n` (the number of papers). We can iterate through each possible value of `h` from `n` down to 1. For each `h`, we count how many papers have at least `h` citations. The first `h` for which this count is greater than or equal to `h` is our answer.
    *   **Time Complexity:** O(n²) - For each of the `n` possible h-values, we iterate through the `n` papers.
    *   **Space Complexity:** O(1).

*   **Optimized Approach (Sorting):**
    *   **Description:** Sort the `citations` array in ascending order. Iterate through the sorted array from left to right. For each index `i`, the number of papers with at least `citations[i]` is `n - i`. The first index `i` where `citations[i] >= n - i` gives us the h-index, which is `n - i`.
    *   **Time Complexity:** O(n log n) - Dominated by the sorting step.
    *   **Space Complexity:** O(1) to O(n) - Depending on the sort implementation.

*   **Best Approach (Bucket/Counting Sort):**
    *   **Description:** Since the h-index is capped by the total number of papers `n`, we can use a counting-sort-like approach. We create an auxiliary array (buckets) of size `n + 1` to store the counts of papers for each citation number (capping citations > `n` at `n`). Then, we iterate backward from `n` to 0, accumulating the counts. The first value `h` for which the accumulated count is greater than or equal to `h` is the h-index.
    *   **Time Complexity:** O(n) - We iterate through the citations array once and the buckets array once.
    *   **Space Complexity:** O(n) - For the auxiliary buckets array.

*   **Comparison:**
    The O(n log n) sorting approach is a good and intuitive optimization over brute force. However, the **Bucket Sort approach is asymptotically faster** with O(n) time complexity, making it the best solution for the given constraints, though it does use O(n) extra space.

### 3. Algorithm Design:

The best solution using a bucket/counting sort approach proceeds as follows:

1.  **Initialize Buckets:** Let `n` be the total number of papers. Create an integer array `buckets` of size `n + 1`, initialized to all zeros. This array will store the frequency of citations.
2.  **Populate Buckets:** Iterate through the input `citations` array. For each `citation` value:
    *   If `citation >= n`, it means this paper has more than or equal to `n` citations. We count it in the last bucket by incrementing `buckets[n]`.
    *   Otherwise, increment `buckets[citation]`.
3.  **Find H-Index:** We need to find the largest `h` such that there are at least `h` papers with `h` or more citations.
    *   Initialize a variable `papersCount = 0`.
    *   Iterate backward from the end of the `buckets` array (from `h = n` down to `0`).
    *   In each step, add the number of papers from the current bucket to our running total: `papersCount += buckets[h]`. `papersCount` now holds the number of papers with at least `h` citations.
    *   Check if `papersCount >= h`. If it is, we have found our answer. This `h` is the highest possible value that satisfies the condition, because we are iterating downwards. Return `h` immediately.
4.  **Edge Case:** If the loop finishes without returning, it means no `h > 0` satisfies the condition (e.g., all citations were 0). In this case, the h-index is 0.

### 4. Production-Ready Implementation:

```kotlin
class Solution {
    /**
     * Calculates the h-index using a bucket/counting sort approach, which is O(n).
     */
    fun hIndex(citations: IntArray): Int {
        val n = citations.size
        // Guard clause for empty array, though constraints say n >= 1.
        if (n == 0) {
            return 0
        }

        // Step 1: Initialize buckets.
        // buckets[i] = number of papers with exactly i citations.
        // Citations > n are treated as n, as h-index cannot exceed n.
        val buckets = IntArray(n + 1)

        // Step 2: Populate buckets.
        for (citation in citations) {
            if (citation >= n) {
                buckets[n]++
            } else {
                buckets[citation]++
            }
        }

        // Step 3: Find h-index by iterating backward.
        var papersCount = 0
        for (h in n downTo 0) {
            // papersCount is the total number of papers with at least h citations.
            papersCount += buckets[h]
            
            // If the number of papers with at least h citations is >= h,
            // we have found the h-index.
            if (papersCount >= h) {
                return h
            }
        }

        // Should be unreachable given the problem logic, but as a fallback.
        return 0
    }
}

// A more idiomatic Kotlin version
class Solution2 {
    fun hIndex(citations: IntArray): Int {
        citations.sort()
        val n = citations.size
        for (i in 0 until n) {
            if (citations[i] >= n - i) {
                return n - i
            }
        }
        return 0
    }
}
```

### 5. Verification & Complexity Finalization:

*   **Dry Run:**
    *   Input: `citations = [3, 0, 6, 1, 5]`
    *   `n = 5`
    *   **Populate Buckets (`buckets` array of size 6):**
        *   `citation = 3`: `buckets[3]++` -> `buckets` is `[0,0,0,1,0,0]`
        *   `citation = 0`: `buckets[0]++` -> `buckets` is `[1,0,0,1,0,0]`
        *   `citation = 6`: `citation >= 5`, so `buckets[5]++` -> `buckets` is `[1,0,0,1,0,1]`
        *   `citation = 1`: `buckets[1]++` -> `buckets` is `[1,1,0,1,0,1]`
        *   `citation = 5`: `citation >= 5`, so `buckets[5]++` -> `buckets` is `[1,1,0,1,0,2]`
    *   Final `buckets`: `[1, 1, 0, 1, 0, 2]` (1 paper with 0, 1 with 1, 1 with 3, 2 with 5+)
    *   **Find H-Index (Iterate backward):**
        *   `h = 5`: `papersCount = buckets[5] = 2`. `2 >= 5` is false.
        *   `h = 4`: `papersCount = 2 + buckets[4] = 2 + 0 = 2`. `2 >= 4` is false.
        *   `h = 3`: `papersCount = 2 + buckets[3] = 2 + 1 = 3`. `3 >= 3` is true. **Return `h = 3`**.

    *   Input: `citations = [1, 3, 1]`
    *   `n = 3`
    *   **Sort:** `[1, 1, 3]`
    *   **Iteration:**
        *   `i = 0`: `citations[0] = 1`. `h = 3 - 0 = 3`. `1 >= 3` is false.
        *   `i = 1`: `citations[1] = 1`. `h = 3 - 1 = 2`. `1 >= 2` is false.
        *   `i = 2`: `citations[2] = 3`. `h = 3 - 2 = 1`. `3 >= 1` is true. Return `h = 1`.

*   **Time Complexity:** O(n) - We perform two separate loops that each run proportional to `n` (one over `citations`, one over `buckets`).
*   **Space Complexity:** O(n) - Required for the `buckets` array of size `n + 1`.
