# 1 - Two Sum

**Difficulty**: Easy
**LeetCode**: https://leetcode.com/problems/two-sum/

---

## Problem Description:
Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to `target`.

You may assume that each input would have exactly one solution, and you may not use the same element twice.

You can return the answer in any order.

**Example 1:**
```
Input: nums = [2,7,11,15], target = 9
Output: [0,1]
Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].
```

**Example 2:**
```
Input: nums = [3,2,4], target = 6
Output: [1,2]
```

**Example 3:**
```
Input: nums = [3,3], target = 6
Output: [0,1]
```

**Constraints:**
- `2 <= nums.length <= 10^4`
- `-10^9 <= nums[i] <= 10^9`
- `-10^9 <= target <= 10^9`
- Only one valid answer exists.

---

## Solution Design

### 1. Clarification & Edge Cases:

**Key Constraints:**
- Input size: Array length ranges from 2 to 10^4 elements
- Data range: Both array elements and target can range from -10^9 to 10^9
- Array is **unsorted**
- Exactly **one valid solution** exists (guaranteed by problem statement)
- Cannot use the same element twice (must be two different indices)

**Edge Cases to Handle:**
- **Minimum input:** Array with exactly 2 elements (e.g., `[1, 2]`, target = 3)
- **Negative numbers:** Array contains negative values (e.g., `[-1, -2, -3, -4]`, target = -6)
- **Duplicates:** Array contains duplicate values (e.g., `[3, 3]`, target = 6)
- **Zero values:** Array contains zeros (e.g., `[0, 4, 3, 0]`, target = 0)
- **Large numbers:** Values near the constraint limits (±10^9)
- **Target at boundaries:** Very large or very small target values

---

### 2. High-Level Approach Analysis (Trade-offs):

#### **Brute Force Approach:**
- Use nested loops to check every pair of elements
- For each element at index `i`, check all elements at indices `j` where `j > i`
- Return `[i, j]` when `nums[i] + nums[j] == target`
- **Time Complexity:** O(n²) - Must check n×(n-1)/2 pairs
- **Space Complexity:** O(1) - No additional data structures needed

#### **Optimized Approach (Hash Map):**
- Use a HashMap to store seen elements and their indices
- For each element, calculate its complement (`target - current_element`)
- Check if complement exists in HashMap (O(1) lookup)
- If found, return the pair; otherwise, add current element to HashMap
- **Time Complexity:** O(n) - Single pass through the array
- **Space Complexity:** O(n) - HashMap stores up to n elements

#### **Comparison & Why Optimized is Better:**
The optimized approach trades space for time. Instead of checking every pair (quadratic time), we leverage the HashMap's O(1) lookup to find complements in a single pass. This is significantly better for large inputs:
- For n = 10,000: Brute force performs ~50 million operations vs. ~10,000 for HashMap
- The linear time complexity makes the solution scalable for the upper constraint limit
- O(n) space is acceptable as it grows linearly and remains manageable

---

### 3. Algorithm Design:

**Step-by-Step Logic (Optimized Solution):**

1. **Initialize Data Structure:**
   - Create an empty HashMap to store `<value, index>` pairs

2. **Iterate Through Array:**
   - For each element at index `i`:
     - Calculate the complement: `complement = target - nums[i]`
     - Check if complement exists in HashMap
     
3. **Check for Match:**
   - **If complement exists:** We found the pair! Return `[map[complement], i]`
   - **If complement doesn't exist:** Store current element in HashMap: `map[nums[i]] = i`

4. **Continue Until Match Found:**
   - Since exactly one solution exists (guaranteed), we will find it before loop ends

5. **Return Empty Array (Safety):**
   - As a fallback (though never reached given constraints), return empty array

**Data Structure Choice - HashMap:**
- **Why HashMap?** Provides O(1) average-case lookup time for checking if complement exists
- **Key:** The array value itself (`nums[i]`)
- **Value:** The index where this value appears
- **Alternative considered:** Sorting + Two Pointers (O(n log n) time, but loses original indices)

---

### 4. Production-Ready Implementation:

```kotlin
package twoSum

class Solution {
    fun twoSum(nums: IntArray, target: Int): IntArray {
        // Guard clause: minimum input validation (though guaranteed by constraints)
        if (nums.size < 2) return intArrayOf()
        
        // HashMap to store seen numbers and their indices
        val map = HashMap<Int, Int>()
        
        for (i in nums.indices) {
            val complement = target - nums[i]
            
            // Check if complement exists in our map
            if (map.containsKey(complement)) {
                // Found the pair: return [previous_index, current_index]
                return intArrayOf(map[complement]!!, i)
            }
            
            // Store current number with its index for future lookups
            map[nums[i]] = i
        }
        
        // Fallback (never reached given problem guarantees)
        return intArrayOf()
    }
}
```

**Code Quality Features:**
- **Guard Clause:** Validates minimum input size (defensive programming)
- **Meaningful Variables:** `complement`, `map` clearly convey purpose
- **Inline Comments:** Explain key logic steps
- **Non-null Assertion:** `map[complement]!!` is safe as we verify key exists
- **Clean Structure:** Single responsibility, no side effects

---

### 5. Verification & Complexity Finalization:

#### **Dry Run with Example:**

**Input:** `nums = [2, 7, 11, 15]`, `target = 9`

| Iteration | i | nums[i] | complement | map state | Action |
|-----------|---|---------|------------|-----------|--------|
| 1 | 0 | 2 | 7 | {} | 7 not in map → add {2: 0} |
| 2 | 1 | 7 | 2 | {2: 0} | 2 found in map! → return [0, 1] |

**Output:** `[0, 1]` ✓

**Verification:** `nums[0] + nums[1] = 2 + 7 = 9 = target` ✓

---

#### **Edge Case Dry Run:**

**Input:** `nums = [3, 3]`, `target = 6`

| Iteration | i | nums[i] | complement | map state | Action |
|-----------|---|---------|------------|-----------|--------|
| 1 | 0 | 3 | 3 | {} | 3 not in map → add {3: 0} |
| 2 | 1 | 3 | 3 | {3: 0} | 3 found in map! → return [0, 1] |

**Output:** `[0, 1]` ✓

**Verification:** `nums[0] + nums[1] = 3 + 3 = 6 = target` ✓  
(Handles duplicates correctly - different indices)

---

#### **Final Complexity Analysis:**

**Time Complexity: O(n)**
- Single pass through the array (worst case visits all n elements)
- Each HashMap operation (containsKey, get, put) is O(1) average case
- Total: O(n) × O(1) = O(n)

**Space Complexity: O(n)**
- HashMap stores at most n-1 elements (worst case: solution is last pair)
- No other significant space usage
- Total: O(n)

**Space-Time Trade-off:** This solution achieves optimal time complexity by using linear extra space, which is the best possible for this problem without additional constraints.
