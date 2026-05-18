# 135 - Candy

**Difficulty**: 🔴 Hard  
**LeetCode**: https://leetcode.com/problems/candy/

---

### 1. Clarification & Edge Cases:
*   **Input:** An array of integers `ratings`.
*   **Output:** An integer representing the minimum number of candies.
*   **Constraints:**
    *   `ratings.length >= 1`
    *   The rating values can be positive integers.
*   **Edge Cases:**
    *   An empty `ratings` array (the problem statement says length is at least 1, but it's good to consider).
    *   An array with a single child.
    *   Ratings can have duplicates.
    *   Ratings can be all the same.
    *   Ratings can be strictly increasing or decreasing.

### 2. High-Level Approach Analysis (Trade-offs):
*   **Brute Force:**
    We could start by giving one candy to each child. Then, we iterate through the ratings and if `ratings[i] > ratings[i-1]` and `candies[i] <= candies[i-1]`, we increment `candies[i]`. Similarly, if `ratings[i] > ratings[i+1]` and `candies[i] <= candies[i+1]`, we increment `candies[i]`. We would need to repeat this process until no more changes are made to the `candies` array. This is inefficient because we might have to traverse the array multiple times.
    *   **Time Complexity:** O(n^2) in the worst case, where n is the number of children.
    *   **Space Complexity:** O(n) to store the candies for each child.

*   **Optimized Approach (Two Passes):**
    A more optimal approach involves two passes over the `ratings` array.
    1.  **First Pass (Left to Right):** Initialize a `candies` array of the same size as `ratings` with all values set to 1. Iterate from left to right. If a child has a higher rating than the child to their left, give them one more candy than the child to their left. This ensures the condition is met for children with increasing ratings.
    2.  **Second Pass (Right to Left):** Now, iterate from right to left. If a child has a higher rating than the child to their right, we need to make sure they have more candies. We update their candy count to be the maximum of their current candy count and one more than the child to their right's candy count. This second pass handles the decreasing rating sequences and ensures that a child with a high rating between two lower-rated children gets enough candies.

*   **Comparison:**
    The two-pass approach is significantly better. It guarantees a solution in just two linear scans of the array, making its time complexity O(n). The brute force approach could take up to O(n^2) time in worst-case scenarios (like a long V-shaped rating pattern). Both approaches use O(n) space for the `candies` array.

### 3. Algorithm Design:
1.  Create an integer array `candies` of the same size as the input `ratings` array.
2.  Initialize all elements of the `candies` array to 1. This satisfies the first requirement that every child gets at least one candy.
3.  **Left-to-Right Pass:**
    *   Iterate through the `ratings` array from the second child (index 1) to the end.
    *   For each child `i`, if `ratings[i] > ratings[i-1]`, it means the current child has a higher rating than the one to their left.
    *   To satisfy the second requirement, we must give this child more candies than their left neighbor. So, we set `candies[i] = candies[i-1] + 1`.
4.  **Right-to-Left Pass:**
    *   Iterate through the `ratings` array from the second-to-last child (index `n-2`) down to the beginning.
    *   For each child `i`, if `ratings[i] > ratings[i+1]`, it means the current child has a higher rating than the one to their right.
    *   We need to ensure this child has more candies than their right neighbor. The number of candies should be `candies[i+1] + 1`. However, this child might have already been assigned more candies during the left-to-right pass. To ensure both conditions (left and right neighbors) are met while minimizing the total candies, we take the maximum of the current `candies[i]` and `candies[i+1] + 1`. So, we set `candies[i] = max(candies[i], candies[i+1] + 1)`.
5.  **Final Sum:**
    *   After both passes, the `candies` array will have the minimum number of candies for each child that satisfies all conditions.
    *   The final result is the sum of all values in the `candies` array.

### 4. Production-Ready Implementation:
```kotlin
import kotlin.math.max

class Solution {
    fun candy(ratings: IntArray): Int {
        // Guard clause for empty input, though constraints say length >= 1
        if (ratings.isEmpty()) {
            return 0
        }

        val n = ratings.size
        val candies = IntArray(n) { 1 }

        // First pass: left to right
        // Ensure children with higher ratings than their left neighbor get more candies.
        for (i in 1 until n) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1
            }
        }

        // Second pass: right to left
        // Ensure children with higher ratings than their right neighbor get more candies.
        // This also handles cases where a child is a peak between two lower-rated neighbors.
        for (i in n - 2 downTo 0) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = max(candies[i], candies[i + 1] + 1)
            }
        }

        return candies.sum()
    }
}
```

### 5. Verification & Complexity Finalization:
*   **Dry Run:**
    Let's take an example: `ratings = [1, 0, 2]`
    1.  `n = 3`. Initialize `candies = [1, 1, 1]`.
    2.  **Left-to-Right Pass:**
        *   `i = 1`: `ratings[1]` (0) is not greater than `ratings[0]` (1). `candies` remains `[1, 1, 1]`.
        *   `i = 2`: `ratings[2]` (2) is greater than `ratings[1]` (0). `candies[2] = candies[1] + 1 = 2`. `candies` is now `[1, 1, 2]`.
    3.  **Right-to-Left Pass:**
        *   `i = 1`: `ratings[1]` (0) is not greater than `ratings[2]` (2). `candies` remains `[1, 1, 2]`.
        *   `i = 0`: `ratings[0]` (1) is greater than `ratings[1]` (0). `candies[0] = max(candies[0], candies[1] + 1) = max(1, 1 + 1) = 2`. `candies` is now `[2, 1, 2]`.
    4.  **Sum:** `2 + 1 + 2 = 5`. The minimum number of candies is 5.

*   **Final Complexity:**
    *   **Time Complexity:** O(n), because we perform two separate linear scans of the `ratings` array.
    *   **Space Complexity:** O(n), for the additional `candies` array we created.
