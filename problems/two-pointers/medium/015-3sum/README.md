# 15 - 3Sum

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/3sum/

---

## Problem Description:

Given an integer array `nums`, return all the triplets `[nums[i], nums[j], nums[k]]` such that `i != j`, `i != k`, and `j != k`, and `nums[i] + nums[j] + nums[k] == 0`.

Notice that the solution set must not contain duplicate triplets.

**Example 1:**
```
Input: nums = [-1,0,1,2,-1,-4]
Output: [[-1,-1,2],[-1,0,1]]
Explanation: 
nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0.
nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0.
nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0.
The distinct triplets are [-1,0,1] and [-1,-1,2].
Notice that the order of the output and the order of the triplets does not matter.
```

**Example 2:**
```
Input: nums = [0,1,1]
Output: []
Explanation: The only possible triplet does not sum up to 0.
```

**Example 3:**
```
Input: nums = [0,0,0]
Output: [[0,0,0]]
Explanation: The only possible triplet sums up to 0.
```

**Constraints:**
- `3 <= nums.length <= 3000`
- `-10^5 <= nums[i] <= 10^5`

---

## 1. Clarification & Edge Cases

### Key Constraints:
- **Input size**: Array length is between 3 and 3000 elements
- **Data range**: Values can range from -10^5 to 10^5 (both negative and positive integers)
- **Array order**: Input array is NOT sorted initially
- **Uniqueness requirement**: The solution set must not contain duplicate triplets
- **Index requirement**: The three indices i, j, k must all be different

### Edge Cases to Handle:
1. **Minimum size array**: Array with exactly 3 elements
   - Example: `[0,0,0]` → should return `[[0,0,0]]`
   
2. **No valid triplets**: When no combination sums to zero
   - Example: `[1,2,3]` → should return `[]`
   
3. **All positive numbers**: Cannot sum to zero
   - Example: `[1,2,3,4,5]` → should return `[]`
   
4. **All negative numbers**: Cannot sum to zero
   - Example: `[-5,-4,-3,-2,-1]` → should return `[]`
   
5. **Multiple zeros**: Should only return one `[0,0,0]` triplet
   - Example: `[0,0,0,0]` → should return `[[0,0,0]]`
   
6. **Duplicate elements leading to duplicate triplets**: Must avoid duplicate triplets
   - Example: `[-1,-1,2,2]` → should return `[[-1,-1,2]]` only once
   
7. **Array with multiple valid triplets**: Ensure all unique triplets are found
   - Example: `[-2,0,1,1,2]` → should return `[[-2,0,2],[-2,1,1]]`
   
8. **Single element repeated**: 
   - Example: `[1,1,1]` → should return `[]`

---

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force Approach:
**Logic:** Use three nested loops to check every possible combination of three numbers.

```
for i from 0 to n-3:
    for j from i+1 to n-2:
        for k from j+1 to n-1:
            if nums[i] + nums[j] + nums[k] == 0:
                add [nums[i], nums[j], nums[k]] to result (avoiding duplicates using a Set)
```

**Time Complexity:** O(n³) 
- Three nested loops iterate through all possible combinations
- For n=3000, this would be approximately 27 billion operations

**Space Complexity:** O(n) or O(n²)
- Need a Set to track duplicate triplets (could be O(n²) in worst case for storing unique triplets)

**Why it's problematic:** This approach is too slow for the given constraints (n up to 3000). It would likely result in Time Limit Exceeded (TLE) on LeetCode.

---

### Optimized Approach (Two Pointers):
**Logic:** 
1. Sort the array first: O(n log n)
2. Fix one element at index `i`
3. Use two pointers (`left` and `right`) to find pairs in the remaining array that sum to `-nums[i]`
4. Skip duplicate values to avoid duplicate triplets

**Time Complexity:** O(n²)
- Sorting: O(n log n)
- Outer loop: O(n) iterations
- Inner two-pointer search: O(n) for each iteration
- Total: O(n log n) + O(n²) = **O(n²)**
- For n=3000, this is approximately 9 million operations (acceptable)

**Space Complexity:** O(log n) or O(n)
- Depending on the sorting algorithm used (QuickSort uses O(log n) stack space, MergeSort uses O(n))
- No additional data structures needed besides the result list

---

### Comparison & Why Optimized is Better:

| Aspect | Brute Force | Two Pointers (Optimized) |
|--------|-------------|--------------------------|
| **Time Complexity** | O(n³) ≈ 27B ops for n=3000 | O(n²) ≈ 9M ops for n=3000 |
| **Space Complexity** | O(n) to O(n²) for Set | O(log n) to O(n) for sorting |
| **Duplicate Handling** | Needs HashSet/Set | Natural with sorted array |
| **Scalability** | Fails for large inputs | Handles max constraints |
| **Code Complexity** | Simple but inefficient | Moderate with pointer logic |

**Why the optimized approach is better:**
- **3000x faster**: Reduces one dimension of iteration by converting the problem to "for each number, find two numbers that sum to its negative"
- **Two Pointers on sorted array**: O(n) search instead of O(n²) nested loops
- **Natural duplicate handling**: Sorted array makes it easy to skip consecutive duplicates
- **Meets constraints**: Can handle n=3000 within time limits

---

## 3. Algorithm Design

### Data Structures Used:
1. **Sorted Array**: Sort the input array to enable two-pointer technique
2. **Result List**: `ArrayList` to store the unique triplets
3. **Three Pointers**:
   - `fixedIndex`: The first element of the triplet
   - `left`: Starts right after fixedIndex
   - `right`: Starts at the end of the array

### Step-by-Step Logic:

**Step 1: Sort the Array**
- Sort `nums` in ascending order using `nums.sort()`
- This enables the two-pointer technique and makes duplicate detection easier
- Time: O(n log n)

**Step 2: Iterate with Fixed Pointer**
- Loop `fixedIndex` from 0 to `n-3` (need at least 3 elements remaining)
- **Key Optimization**: If `nums[fixedIndex] > 0`, break early (all remaining numbers are positive, cannot sum to 0)
- **Skip duplicates**: If `nums[fixedIndex] == nums[fixedIndex-1]`, skip to avoid duplicate triplets

**Step 3: Initialize Two Pointers**
- For each fixed element:
  - `left = fixedIndex + 1` (element immediately after fixed)
  - `right = nums.lastIndex` (last element in array)

**Step 4: Two Pointers Search**
- While `left < right`:
  - Calculate `sum = nums[fixedIndex] + nums[left] + nums[right]`
  - **If sum > 0**: Decrease `right` (need a smaller number)
  - **If sum < 0**: Increase `left` (need a larger number)
  - **If sum == 0**: Found a valid triplet!
    - Add triplet to result list
    - Move both pointers: `left++` and `right--`
    - Skip duplicates on both sides

**Step 5: Duplicate Handling**
- **For fixed pointer**: Skip consecutive duplicates after processing each element
- **For left pointer**: After finding a match, skip all consecutive duplicate values
- **For right pointer**: After finding a match, skip all consecutive duplicate values
- This ensures no duplicate triplets in the result

### Why This Approach Works:

1. **Sorted Array Property**: 
   - Moving `left` pointer right → increases the sum
   - Moving `right` pointer left → decreases the sum
   - This gives us control to converge toward the target sum of 0

2. **Reduction to Two Sum**:
   - For each fixed element, we're solving: "Find two numbers that sum to -nums[fixedIndex]"
   - This is the classic Two Sum II problem (on sorted array)

3. **Completeness**:
   - By fixing each element and searching the remaining array, we examine all possible triplets
   - The two-pointer technique guarantees we check all valid pairs for each fixed element

4. **Duplicate Avoidance**:
   - Sorted array allows us to detect duplicates by comparing consecutive elements
   - Skipping duplicates at all three levels (fixed, left, right) prevents duplicate triplets

---

## 4. Production-Ready Implementation

```kotlin
package problems.two_pointers.three_sum

class Solution {
    /**
     * Finds all unique triplets in the array that sum to zero.
     * Uses sorting and two-pointer technique for optimal performance.
     * 
     * @param nums Input integer array
     * @return List of unique triplets that sum to 0
     * 
     * Time Complexity: O(n²)
     * Space Complexity: O(log n) to O(n) for sorting
     */
    fun threeSum(nums: IntArray): List<List<Int>> {
        // Guard clause: handle minimum size requirement
        if (nums.size < 3) return emptyList()
        
        // Sort array to enable two-pointer technique
        nums.sort()
        
        val result = mutableListOf<List<Int>>()
        
        // Iterate through array with fixed pointer
        var fixedIndex = 0
        while (fixedIndex < nums.size - 2) {
            // Early termination: if smallest number > 0, no solution possible
            // because all remaining numbers will be positive
            if (nums[fixedIndex] > 0) break
            
            // Skip duplicates for fixed pointer to avoid duplicate triplets
            if (fixedIndex > 0 && nums[fixedIndex] == nums[fixedIndex - 1]) {
                fixedIndex++
                continue
            }
            
            // Two pointers approach for remaining array
            var left = fixedIndex + 1
            var right = nums.lastIndex
            
            while (left < right) {
                val sum = nums[fixedIndex] + nums[left] + nums[right]
                
                when {
                    sum < 0 -> {
                        // Sum too small, need larger number
                        left++
                    }
                    
                    sum > 0 -> {
                        // Sum too large, need smaller number
                        right--
                    }
                    
                    else -> {
                        // Found a valid triplet that sums to 0
                        result.add(listOf(nums[fixedIndex], nums[left], nums[right]))
                        
                        // Move both pointers
                        left++
                        right--
                        
                        // Skip duplicate values for left pointer
                        while (left < right && nums[left] == nums[left - 1]) {
                            left++
                        }
                        
                        // Skip duplicate values for right pointer
                        while (left < right && nums[right] == nums[right + 1]) {
                            right--
                        }
                    }
                }
            }
            
            fixedIndex++
        }
        
        return result
    }
}

fun main() {
    val solution = Solution()
    
    // Test case 1: Standard case with duplicates
    println("Test 1: ${solution.threeSum(intArrayOf(-1, 0, 1, 2, -1, -4))}")
    // Expected: [[-1, -1, 2], [-1, 0, 1]]
    
    // Test case 2: No valid triplets
    println("Test 2: ${solution.threeSum(intArrayOf(0, 1, 1))}")
    // Expected: []
    
    // Test case 3: All zeros
    println("Test 3: ${solution.threeSum(intArrayOf(0, 0, 0))}")
    // Expected: [[0, 0, 0]]
    
    // Test case 4: Multiple duplicates
    println("Test 4: ${solution.threeSum(intArrayOf(0, 0, 0, 0))}")
    // Expected: [[0, 0, 0]]
    
    // Test case 5: Multiple valid triplets
    println("Test 5: ${solution.threeSum(intArrayOf(-2, 0, 1, 1, 2))}")
    // Expected: [[-2, 0, 2], [-2, 1, 1]]
    
    // Test case 6: All positive numbers
    println("Test 6: ${solution.threeSum(intArrayOf(1, 2, 3))}")
    // Expected: []
    
    // Test case 7: All negative numbers
    println("Test 7: ${solution.threeSum(intArrayOf(-3, -2, -1))}")
    // Expected: []
}
```

### Key Implementation Features:

1. **Guard Clause**: Handles arrays with less than 3 elements at the start
2. **Early Termination**: Breaks when `nums[fixedIndex] > 0` (optimization)
3. **Triple Duplicate Skipping**: 
   - Fixed pointer duplicates
   - Left pointer duplicates after finding match
   - Right pointer duplicates after finding match
4. **Clean Variable Names**: `fixedIndex`, `left`, `right`, `sum` are self-documenting
5. **Kotlin Idioms**: 
   - Uses `when` expression for cleaner conditional logic
   - Uses `lastIndex` instead of `nums.size - 1`
   - Uses immutable `listOf()` for triplets
6. **Comprehensive Comments**: Explains the "why" behind complex logic
7. **Test Cases**: Includes 7 different test cases covering all edge cases

---

## 5. Verification & Complexity Finalization

### Dry Run with Example:

**Input:** `nums = [-1, 0, 1, 2, -1, -4]`

**Step 1: Sort the array**
```
Sorted: [-4, -1, -1, 0, 1, 2]
         ↑
     fixedIndex
```

**Step 2: fixedIndex=0, nums[0]=-4**
```
[-4, -1, -1, 0, 1, 2]
  ↑   L           R
  
sum = -4 + (-1) + 2 = -3 < 0 → left++
```

```
[-4, -1, -1, 0, 1, 2]
  ↑       L       R
  
sum = -4 + (-1) + 2 = -3 < 0 → left++
```

```
[-4, -1, -1, 0, 1, 2]
  ↑           L   R
  
sum = -4 + 0 + 2 = -2 < 0 → left++
```

```
[-4, -1, -1, 0, 1, 2]
  ↑               L,R
  
left >= right → exit inner loop
```

**Step 3: fixedIndex=1, nums[1]=-1**
```
[-4, -1, -1, 0, 1, 2]
      ↑   L       R
      
sum = -1 + (-1) + 2 = 0 → FOUND! ✓
Add triplet: [-1, -1, 2]
left++, right--
Skip duplicates on left (nums[2] == nums[1])
```

```
[-4, -1, -1, 0, 1, 2]
      ↑       L R
      
sum = -1 + 0 + 1 = 0 → FOUND! ✓
Add triplet: [-1, 0, 1]
left++, right--
```

```
[-4, -1, -1, 0, 1, 2]
      ↑         L,R
      
left >= right → exit inner loop
```

**Step 4: fixedIndex=2, nums[2]=-1**
- Skip because `nums[2] == nums[1]` (duplicate)

**Step 5: fixedIndex=3, nums[3]=0**
```
[-4, -1, -1, 0, 1, 2]
              ↑  L  R
              
sum = 0 + 1 + 2 = 3 > 0 → right--
left >= right → exit inner loop
```

**Step 6: fixedIndex=4, nums[4]=1**
- Break because `nums[4] > 0` (early termination)

**Final Result:**
```
[[-1, -1, 2], [-1, 0, 1]]
```
✅ **Correct!**

---

### Time Complexity: **O(n²)**

**Detailed Breakdown:**
1. **Sorting**: O(n log n) using QuickSort or MergeSort
2. **Outer loop (fixedIndex)**: O(n) iterations in worst case
3. **Inner two-pointer loop**: O(n) for each fixed element
   - Pointers move toward each other, each can move at most n times total
4. **Duplicate skipping**: O(1) amortized per operation
5. **Total**: O(n log n) + O(n) × O(n) = O(n log n) + O(n²) = **O(n²)**

**Practical Analysis:**
- For n = 3000: ~9,000,000 operations
- For n = 1000: ~1,000,000 operations
- Well within time limits for most online judges

---

### Space Complexity: **O(log n) to O(n)**

**Detailed Breakdown:**
1. **Sorting space**: 
   - QuickSort (in-place): O(log n) for recursion stack
   - MergeSort: O(n) for temporary arrays
   - Kotlin's `sort()` uses TimSort (hybrid), typically O(n) worst case
2. **Pointers and variables**: O(1) - just integer variables
3. **Result list**: O(k) where k is number of triplets
   - Not counted as auxiliary space (it's the output)
   - In worst case, k can be O(n²) but this is part of the result

**Total Auxiliary Space (excluding output):** **O(log n) to O(n)**

- Most commonly: **O(n)** due to sorting algorithm used in Kotlin

---

### Summary:
- ✅ **Time Complexity:** O(n²) - Optimal for this problem
- ✅ **Space Complexity:** O(log n) to O(n) - Efficient
- ✅ **All edge cases handled:** Duplicates, empty results, all zeros, etc.
- ✅ **Production-ready:** Clean code with proper error handling and comments
- ✅ **Verified:** Dry run confirms correctness of the algorithm
