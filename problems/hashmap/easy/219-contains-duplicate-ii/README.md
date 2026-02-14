# 219 - Contains Duplicate II

**Difficulty**: Easy
**LeetCode**: https://leetcode.com/problems/contains-duplicate-ii/

---

## Problem Description:
Given an integer array `nums` and an integer `k`, return `true` if there are two distinct indices `i` and `j` in the array such that `nums[i] == nums[j]` and `abs(i - j) <= k`.

**Example 1:**
```
Input: nums = [1,2,3,1], k = 3
Output: true
```

**Example 2:**
```
Input: nums = [1,0,1,1], k = 1
Output: true
```

**Example 3:**
```
Input: nums = [1,2,3,1,2,3], k = 2
Output: false
```

**Constraints:**
- 1 ≤ nums.length ≤ 10⁵
- -10⁹ ≤ nums[i] ≤ 10⁹
- 0 ≤ k ≤ 10⁵

---

## Solution Design

### 1. Clarification & Edge Cases:

**Key Constraints:**
- **Input size:** Array length ranges from 1 to 10⁵ (needs efficient solution)
- **Data range:** Elements can be any integer from -10⁹ to 10⁹
- **Distance constraint:** k ranges from 0 to 10⁵
- **Array order:** Not sorted, elements can appear in any order
- **Duplicates:** Array may contain duplicate values
- **Distinct indices:** i and j must be different (i ≠ j)

**Edge Cases:**
1. **Single element array:** `nums = [1], k = 0` → Return `false` (no two distinct indices exist)
2. **k = 0:** `nums = [1,1], k = 0` → Return `false` (abs(i-j) must be ≤ 0, impossible for distinct indices)
3. **All unique elements:** `nums = [1,2,3,4], k = 3` → Return `false` (no duplicates)
4. **Immediate duplicates:** `nums = [1,1], k = 1` → Return `true` (distance = 1)
5. **Multiple duplicates of same value:** `nums = [1,2,1,1], k = 1` → Return `true` (need to check closest pair)
6. **k larger than array length:** `nums = [1,2,1], k = 100` → Return `true` (any duplicate within array qualifies)
7. **Negative numbers:** `nums = [-1,-2,-1], k = 2` → Return `true` (works same as positive)
8. **Duplicate exists but too far:** `nums = [1,2,3,1], k = 2` → Return `false` (distance = 3 > k)

---

### 2. High-Level Approach Analysis (Trade-offs):

#### **Brute Force Approach:**
- Use nested loops: for each element at index i, check all elements at indices j where j > i
- Calculate distance abs(i-j) and check if nums[i] == nums[j] and distance ≤ k
- Return true if any such pair is found, otherwise false

**Time Complexity:** O(n²) - For each element, we potentially check all subsequent elements
**Space Complexity:** O(1) - No extra space needed beyond variables

**Why it's inefficient:** For large arrays (up to 10⁵ elements), this results in up to 10¹⁰ operations, which is too slow.

---

#### **Optimized Approach (Sliding Window with HashMap):**
- Use a HashMap to store the most recent index of each element
- Traverse the array once from left to right
- For each element:
  - Check if it exists in HashMap
  - If yes, calculate distance from stored index
  - If distance ≤ k, return true
  - Update/add the current index in HashMap
- Return false if no valid pair found

**Time Complexity:** O(n) - Single pass through the array, O(1) HashMap operations
**Space Complexity:** O(min(n, k)) - In worst case, HashMap stores at most min(n, k+1) elements

**Why Optimized is Better:**
- **Time:** O(n) vs O(n²) - Linear time is dramatically faster for large inputs
- **Space:** O(min(n,k)) space is acceptable trade-off for 10⁵x speedup
- **Single Pass:** We only need to traverse the array once
- **Early Exit:** Can return true immediately upon finding valid pair
- HashMap provides O(1) average lookup and insertion

---

#### **Alternative Approach (Sliding Window with HashSet):**
- Maintain a HashSet as a sliding window of size k
- As we iterate, keep only the last k elements in the set
- If current element exists in set, return true
- Otherwise, add to set and remove elements outside window

**Time Complexity:** O(n)
**Space Complexity:** O(min(n, k))

**Comparison:** HashSet approach is slightly more complex (managing window boundaries) but also valid. HashMap is simpler and more intuitive.

---

### 3. Algorithm Design:

**Chosen Approach:** Sliding Window with HashMap (Index Tracking)

**Logic:**
1. **Initialize HashMap:** Create a HashMap<Int, Int> to map each number to its most recent index
2. **Guard Clause:** Handle edge case where k = 0 (impossible to have valid pair)
3. **Single Pass Iteration:**
   - For each index i from 0 to n-1:
     - Get current number: `num = nums[i]`
     - Check if num exists in HashMap:
       - **If YES:** Get the previous index stored in HashMap
         - Calculate distance: `distance = i - previousIndex`
         - If distance ≤ k: **Return true** (found valid duplicate pair)
       - Update HashMap: `map[num] = i` (store current index as most recent)
4. **No Valid Pair Found:** If loop completes, return false

**Data Structures:**
- **HashMap<Int, Int>:**
  - **Key:** The number value from array
  - **Value:** The most recent index where this number was seen
  - **Why chosen:**
    - O(1) average time for lookup and insertion
    - Automatically handles duplicates by overwriting with most recent index
    - We only need to track the most recent occurrence (closer occurrences have better chance of satisfying k constraint)
    - Space efficient: stores at most n entries, often fewer due to duplicates

**Key Insights:**
- We only need to check the **most recent** occurrence of each number
- If current occurrence is within k distance of the most recent one, we found our answer
- If not, we update to current index and continue (current becomes new "most recent")
- No need to track all historical indices, just the latest one

---

### 4. Production-Ready Implementation:

```kotlin
package containDuplicateII

class Solution {
    /**
     * Determines if there are two distinct indices i and j in the array such that
     * nums[i] == nums[j] and abs(i - j) <= k.
     *
     * @param nums The input array of integers
     * @param k The maximum allowed distance between duplicate indices
     * @return true if a valid duplicate pair exists within distance k, false otherwise
     */
    fun containsNearbyDuplicate(nums: IntArray, k: Int): Boolean {
        // Guard clause: If k is 0, no two distinct indices can have distance <= 0
        if (k == 0) return false
        
        // Guard clause: Single element array cannot have two distinct indices
        if (nums.size == 1) return false
        
        // Map to store the most recent index of each number
        val indexMap = HashMap<Int, Int>()
        
        // Single pass through the array
        for (i in nums.indices) {
            val currentNum = nums[i]
            
            // Check if we've seen this number before
            if (indexMap.containsKey(currentNum)) {
                val previousIndex = indexMap[currentNum]!!
                val distance = i - previousIndex
                
                // If distance is within k, we found a valid pair
                if (distance <= k) {
                    return true
                }
            }
            
            // Update the map with current index (most recent occurrence)
            indexMap[currentNum] = i
        }
        
        // No valid duplicate pair found
        return false
    }
}

fun main() {
    val solution = Solution()
    
    // Test cases
    println("containsNearbyDuplicate([1,2,3,1], k=3) = ${solution.containsNearbyDuplicate(intArrayOf(1,2,3,1), 3)}")  // Expected: true
    println("containsNearbyDuplicate([1,0,1,1], k=1) = ${solution.containsNearbyDuplicate(intArrayOf(1,0,1,1), 1)}")  // Expected: true
    println("containsNearbyDuplicate([1,2,3,1,2,3], k=2) = ${solution.containsNearbyDuplicate(intArrayOf(1,2,3,1,2,3), 2)}")  // Expected: false
    println("containsNearbyDuplicate([1], k=1) = ${solution.containsNearbyDuplicate(intArrayOf(1), 1)}")  // Expected: false
    println("containsNearbyDuplicate([1,2,3,4], k=3) = ${solution.containsNearbyDuplicate(intArrayOf(1,2,3,4), 3)}")  // Expected: false
}
```

**Key Implementation Details:**
1. **Guard Clauses:**
   - Handle k = 0 case upfront (impossible to satisfy)
   - Handle single element array (no two distinct indices)
2. **Clear Variable Names:**
   - `indexMap` instead of generic `map`
   - `currentNum` for clarity
   - `previousIndex` and `distance` for readability
3. **Efficient Lookup:**
   - Use `containsKey()` for explicit null-safety
   - Calculate distance only when previous occurrence exists
4. **Early Return:**
   - Return true immediately when valid pair found (no need to continue)
5. **Comments:**
   - KDoc for function documentation
   - Inline comments explaining logic at key decision points

---

### 5. Verification & Complexity Finalization:

#### **Dry Run with Example: nums = [1,2,3,1], k = 3**

| Step | i | nums[i] | indexMap Before | Action | Distance | Result | indexMap After |
|------|---|---------|-----------------|--------|----------|--------|----------------|
| Init | - | - | {} | - | - | - | {} |
| 1 | 0 | 1 | {} | 1 not in map, add | - | continue | {1→0} |
| 2 | 1 | 2 | {1→0} | 2 not in map, add | - | continue | {1→0, 2→1} |
| 3 | 2 | 3 | {1→0, 2→1} | 3 not in map, add | - | continue | {1→0, 2→1, 3→2} |
| 4 | 3 | 1 | {1→0, 2→1, 3→2} | 1 in map at index 0 | 3-0=3 | 3≤3 ✓ **Return true** | - |

**Result:** Returns `true` correctly (distance = 3, which equals k)

---

#### **Dry Run with Example: nums = [1,2,3,1,2,3], k = 2**

| Step | i | nums[i] | indexMap Before | Action | Distance | Check | indexMap After |
|------|---|---------|-----------------|--------|----------|-------|----------------|
| Init | - | - | {} | - | - | - | {} |
| 1 | 0 | 1 | {} | 1 not in map | - | - | {1→0} |
| 2 | 1 | 2 | {1→0} | 2 not in map | - | - | {1→0, 2→1} |
| 3 | 2 | 3 | {1→0, 2→1} | 3 not in map | - | - | {1→0, 2→1, 3→2} |
| 4 | 3 | 1 | {1→0, 2→1, 3→2} | 1 in map at 0 | 3-0=3 | 3≤2? NO, update | {1→3, 2→1, 3→2} |
| 5 | 4 | 2 | {1→3, 2→1, 3→2} | 2 in map at 1 | 4-1=3 | 3≤2? NO, update | {1→3, 2→4, 3→2} |
| 6 | 5 | 3 | {1→3, 2→4, 3→2} | 3 in map at 2 | 5-2=3 | 3≤2? NO, update | {1→3, 2→4, 3→5} |
| End | - | - | - | Loop complete | - | - | **Return false** |

**Result:** Returns `false` correctly (all distances are 3, which exceeds k=2)

---

#### **Dry Run with Example: nums = [1,0,1,1], k = 1**

| Step | i | nums[i] | indexMap Before | Action | Distance | Check | Result |
|------|---|---------|-----------------|--------|----------|-------|--------|
| Init | - | - | {} | - | - | - | - |
| 1 | 0 | 1 | {} | 1 not in map | - | - | {1→0} |
| 2 | 1 | 0 | {1→0} | 0 not in map | - | - | {1→0, 0→1} |
| 3 | 2 | 1 | {1→0, 0→1} | 1 in map at 0 | 2-0=2 | 2≤1? NO | {1→2, 0→1} |
| 4 | 3 | 1 | {1→2, 0→1} | 1 in map at 2 | 3-2=1 | 1≤1? YES ✓ | **Return true** |

**Result:** Returns `true` correctly (indices 2 and 3 both have value 1, distance = 1)

---

#### **Final Complexity Analysis:**

**Time Complexity: O(n)**
- Single pass through the array: n iterations
- For each iteration:
  - HashMap lookup: O(1) average
  - HashMap insertion/update: O(1) average
  - Distance calculation: O(1)
- Total: O(n) × O(1) = **O(n)**
- Best case: O(1) if valid pair found at start
- Worst case: O(n) if no valid pair (must check entire array)

**Space Complexity: O(min(n, k+1))**
- HashMap stores at most n distinct elements in worst case (all unique)
- However, with sliding window logic, we only care about elements within distance k
- If we optimize further, we could remove elements > k distance away
- Current implementation: O(n) worst case (all unique elements)
- Practical case: Often much less due to duplicates
- **Simplified: O(n)** worst case, **O(min(n, k))** with window optimization

**Why this is optimal:**
- Cannot do better than O(n) time since we must examine each element at least once
- O(n) space is acceptable for the dramatic speedup from O(n²)
- Alternative O(min(n,k)) space with HashSet sliding window is possible but more complex
- This solution balances simplicity, efficiency, and readability
