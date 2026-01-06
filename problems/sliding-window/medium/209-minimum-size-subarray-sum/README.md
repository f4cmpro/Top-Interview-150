# 209 - Minimum Size Subarray Sum

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/minimum-size-subarray-sum/

---

## Problem Description:
Given an array of positive integers `nums` and a positive integer `target`, return the minimal length of a subarray whose sum is greater than or equal to `target`. If there is no such subarray, return `0` instead.

**Example 1:**
```
Input: target = 7, nums = [2,3,1,2,4,3]
Output: 2
Explanation: The subarray [4,3] has the minimal length under the problem constraint.
```

**Example 2:**
```
Input: target = 4, nums = [1,4,4]
Output: 1
```

**Example 3:**
```
Input: target = 11, nums = [1,1,1,1,1,1,1,1]
Output: 0
```

---

## Solution Design

### 1. Clarification & Edge Cases:

**Key Constraints:**
- `1 <= target <= 10^9`
- `1 <= nums.length <= 10^5`
- `1 <= nums[i] <= 10^4`
- All numbers are **positive integers**
- Array is **unsorted**

**Edge Cases to Handle:**
1. **Empty array**: Not possible per constraints (length >= 1)
2. **Single element array**: Check if that element >= target
3. **No valid subarray**: When sum of entire array < target, return 0
4. **Target equals single element**: Minimum length would be 1
5. **Entire array needed**: When only the complete array sum >= target
6. **Very large target**: May require entire array or no solution

---

### 2. High-Level Approach Analysis (Trade-offs):

#### **Brute Force Approach:**
- Generate all possible subarrays
- Calculate sum for each subarray
- Track the minimum length where sum >= target
- **Time Complexity:** O(n²) - nested loops to generate subarrays and calculate sums
- **Space Complexity:** O(1) - only storing min length

#### **Optimized Approach (Sliding Window):**
- Use two pointers (left, right) to maintain a dynamic window
- Expand window by moving right pointer and adding elements
- Shrink window from left when sum >= target to find minimum length
- **Time Complexity:** O(n) - each element visited at most twice (once by right, once by left)
- **Space Complexity:** O(1) - only using pointers and variables

**Why Optimized is Better:**
The sliding window approach reduces time complexity from O(n²) to O(n) because:
- We don't recalculate sums from scratch for each subarray
- We maintain a running sum and adjust it incrementally
- Each element is processed at most twice (added once, removed once)
- We leverage the property that all numbers are positive, so adding elements always increases the sum

---

### 3. Algorithm Design:

**Data Structures Used:**
- **Two Pointers (left, right):** To define the sliding window boundaries
- **Integer variables:** For tracking current sum and minimum length

**Step-by-Step Logic:**

1. **Initialize:**
   - `left = 0` (window start)
   - `sum = 0` (current window sum)
   - `minLength = Int.MAX_VALUE` (track minimum length found)

2. **Expand Window (right pointer):**
   - Iterate through array with `right` pointer from 0 to n-1
   - Add `nums[right]` to current `sum`

3. **Shrink Window (left pointer):**
   - While `sum >= target`:
     - Update `minLength` with current window size `(right - left + 1)`
     - Remove `nums[left]` from `sum`
     - Increment `left` to shrink window
   - This finds the smallest valid window ending at current `right`

4. **Return Result:**
   - If `minLength` was never updated (still Int.MAX_VALUE), return 0
   - Otherwise, return `minLength`

**Why This Works:**
- The outer loop (right pointer) ensures we consider all possible window endings
- The inner while loop ensures we find the minimum window for each ending position
- Since all numbers are positive, shrinking from the left never helps us reach the target again (sum only decreases)
- This guarantees we find the optimal solution in one pass

---

### 4. Production-Ready Implementation:

```kotlin
package problems

class Solution {
    fun minSubArrayLen(target: Int, nums: IntArray): Int {
        // Guard Clause: Handle edge case where array might be empty (defensive)
        if (nums.isEmpty()) return 0
        
        var left = 0
        var sum = 0
        var minLength = Int.MAX_VALUE

        // Sliding Window: Expand with right pointer
        for (right in nums.indices) {
            // Expand: Add current element to window sum
            sum += nums[right]

            // Shrink: While current window sum meets target, try to minimize length
            while (sum >= target) {
                // Update minimum length if current window is smaller
                minLength = minLength.coerceAtMost(right - left + 1)
                
                // Shrink window from left
                sum -= nums[left]
                left++
            }
        }

        // Guard Clause: Return 0 if no valid subarray found
        return if (minLength == Int.MAX_VALUE) 0 else minLength
    }
}

fun main() {
    val solution = Solution()
    
    // Test Case 1: Standard case
    println(solution.minSubArrayLen(7, intArrayOf(2, 3, 1, 2, 4, 3))) // Output: 2
    
    // Test Case 2: Single element suffices
    println(solution.minSubArrayLen(4, intArrayOf(1, 4, 4))) // Output: 1
    
    // Test Case 3: No valid subarray
    println(solution.minSubArrayLen(11, intArrayOf(1, 1, 1, 1, 1, 1, 1, 1))) // Output: 0
    
    // Test Case 4: Entire array needed
    println(solution.minSubArrayLen(15, intArrayOf(1, 2, 3, 4, 5))) // Output: 5
    
    // Test Case 5: Single element array
    println(solution.minSubArrayLen(3, intArrayOf(5))) // Output: 1
}
```

**Key Design Decisions:**
- **Guard Clauses:** Check for empty array (defensive programming)
- **Meaningful Names:** `left`, `right`, `sum`, `minLength` clearly convey purpose
- **Kotlin Idioms:** Use `nums.indices`, `coerceAtMost()` for cleaner code
- **Comments:** Explain the expand/shrink pattern for maintainability

---

### 5. Verification & Complexity Finalization:

#### **Dry Run Example:**
**Input:** `target = 7, nums = [2, 3, 1, 2, 4, 3]`

| Step | right | nums[right] | sum | sum >= 7? | Action | left | minLength | Window |
|------|-------|-------------|-----|-----------|--------|------|-----------|--------|
| Init | - | - | 0 | - | - | 0 | ∞ | [] |
| 1 | 0 | 2 | 2 | No | Expand | 0 | ∞ | [2] |
| 2 | 1 | 3 | 5 | No | Expand | 0 | ∞ | [2,3] |
| 3 | 2 | 1 | 6 | No | Expand | 0 | ∞ | [2,3,1] |
| 4 | 3 | 2 | 8 | **Yes** | Update min=4, Shrink | 0→1 | 4 | [2,3,1,2] |
| 4a | 3 | - | 6 | No | Continue | 1 | 4 | [3,1,2] |
| 5 | 4 | 4 | 10 | **Yes** | Update min=4, Shrink | 1→2 | 4 | [3,1,2,4] |
| 5a | 4 | - | 7 | **Yes** | Update min=3, Shrink | 2→3 | 3 | [1,2,4] |
| 5b | 4 | - | 5 | No | Continue | 3 | 3 | [2,4] |
| 6 | 5 | 3 | 8 | **Yes** | Update min=2, Shrink | 3→4 | **2** | [2,4,3] |
| 6a | 5 | - | 6 | No | Continue | 4 | 2 | [4,3] |

**Final Result:** `minLength = 2` ✓ (subarray [4,3])

#### **Complexity Analysis:**

**Time Complexity: O(n)**
- The `right` pointer traverses the array once: O(n)
- The `left` pointer also traverses the array at most once: O(n)
- Each element is visited at most twice (once added, once removed)
- Total: O(n) + O(n) = O(n)

**Space Complexity: O(1)**
- Only using constant extra space: `left`, `sum`, `minLength`
- No additional data structures that scale with input size
- In-place algorithm with fixed memory usage

---

## Key Takeaways:
1. **Sliding Window Pattern:** Effective for contiguous subarray problems with positive numbers
2. **Two Pointers:** Maintain dynamic window boundaries efficiently
3. **Greedy Approach:** Always try to minimize window size when valid
4. **Optimization:** Avoid recalculating sums by maintaining running total
