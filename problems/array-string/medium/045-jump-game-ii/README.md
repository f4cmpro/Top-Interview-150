# 45 - Jump Game II

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/jump-game-ii/

---

## Problem Description

Given a 0-indexed array of integers `nums` of length `n`, where `nums[i]` represents the maximum jump length from index `i`, return the **minimum number of jumps** to reach `nums[n - 1]` (the last index).

You can assume the input is always valid and that it is always possible to reach the last index.

**Example:**
```
Input:  nums = [2, 3, 1, 1, 4]
Output: 2
Explanation: Jump 1 step from index 0 to 1, then 3 steps to the last index.
```

---

## 1. Clarification & Edge Cases

**Constraints:**
- `1 <= nums.length <= 10^4`
- `0 <= nums[i] <= 1000`
- It is always possible to reach the last index.

**Edge Cases:**
- Single element array `[0]` → already at the last index, answer is `0`.
- All elements are `1` → must jump every step, answer is `n - 1`.
- First element is large enough to jump directly to the end → answer is `1`.

---

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force — BFS / Recursion
Explore every possible jump from each index recursively or via BFS, tracking the minimum number of steps to reach the last index.

- **Time Complexity:** O(n²) — for each position, iterate over all reachable next positions.
- **Space Complexity:** O(n) — recursion stack or BFS queue.

### Optimized — Greedy
Use a greedy approach. At each step, track the **farthest reachable index** within the current jump range. When you exhaust the current range, increment the jump counter and extend the range to the farthest position found.

- **Time Complexity:** O(n) — single pass.
- **Space Complexity:** O(1) — only a few integer variables.

**Why greedy is better:** It makes locally optimal decisions (always extend to the farthest reachable position) without exploring all paths, yielding linear time with constant space.

---

## 3. Algorithm Design

1. If the array has only one element, return `0`.
2. Maintain three variables:
   - `jumps` — number of jumps taken so far.
   - `currentEnd` — the farthest index reachable within the current jump.
   - `farthest` — the farthest index reachable from any position in the current jump window.
3. Iterate from index `0` to `n - 2` (no need to jump from the last index):
   - Update `farthest = max(farthest, i + nums[i])`.
   - When `i == currentEnd` (current window exhausted):
     - Increment `jumps`.
     - Set `currentEnd = farthest`.
     - If `currentEnd >= n - 1`, break early.
4. Return `jumps`.

**Data Structures:** Only primitive integer variables — no extra data structures needed.

---

## 4. Production-Ready Implementation

```kotlin
class Solution {
    fun jump(nums: IntArray): Int {
        // Edge case: already at the last index
        if (nums.size == 1) return 0

        var jumps = 0
        var currentEnd = 0   // end of the current jump window
        var farthest = 0     // farthest index reachable so far

        for (i in 0 until nums.size - 1) {
            // Update the farthest index reachable from current position
            farthest = maxOf(farthest, i + nums[i])

            // We've reached the end of the current jump window
            if (i == currentEnd) {
                jumps++
                currentEnd = farthest

                // Early exit if we can already reach or pass the last index
                if (currentEnd >= nums.size - 1) break
            }
        }

        return jumps
    }
}
```

---

## 5. Verification & Complexity Finalization

### Dry Run

**Input:** `nums = [2, 3, 1, 1, 4]`, `n = 5`

| i | nums[i] | farthest | currentEnd | jumps |
|---|---------|----------|------------|-------|
| 0 | 2       | 2        | 0 → jump! → 2 | 1   |
| 1 | 3       | 4        | 2          | 1     |
| 2 | 1       | 4        | 2 → jump! → 4 | 2  |
| (break: currentEnd=4 >= n-1=4) |

**Output:** `2` ✅

---

### Complexity

| | Complexity |
|---|---|
| **Time**  | O(n) — single linear pass through the array |
| **Space** | O(1) — only constant extra variables used |
