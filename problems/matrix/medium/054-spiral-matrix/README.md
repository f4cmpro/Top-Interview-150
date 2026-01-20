# 54 - Spiral Matrix

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/spiral-matrix/

## Problem Description:
Given an `m x n` matrix, return all elements of the matrix in spiral order.

**Example 1:**
```
Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
Output: [1,2,3,6,9,8,7,4,5]
```

**Example 2:**
```
Input: matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
Output: [1,2,3,4,8,12,11,10,9,5,6,7]
```

**Constraints:**
- `m == matrix.length`
- `n == matrix[i].length`
- `1 <= m, n <= 10`
- `-100 <= matrix[i][j] <= 100`

---

## 1. Clarification & Edge Cases

### Key Constraints:
- **Input Size:** Matrix dimensions are at least 1x1 and at most 10x10 (small to medium size)
- **Data Range:** Elements can be negative, zero, or positive integers (-100 to 100)
- **Matrix Shape:** Can be rectangular (m ≠ n) or square (m = n)
- **Non-empty:** Guaranteed at least one element (m, n >= 1)

### Edge Cases to Handle:
1. **Single Row Matrix:** `[[1,2,3,4]]` → Should return `[1,2,3,4]`
2. **Single Column Matrix:** `[[1],[2],[3],[4]]` → Should return `[1,2,3,4]`
3. **Single Element Matrix:** `[[5]]` → Should return `[5]`
4. **2x2 Matrix:** `[[1,2],[3,4]]` → Should return `[1,2,4,3]`
5. **Rectangular Matrix (more columns):** `[[1,2,3],[4,5,6]]` → Should return `[1,2,3,6,5,4]`
6. **Rectangular Matrix (more rows):** `[[1,2],[3,4],[5,6]]` → Should return `[1,2,4,6,5,3]`
7. **Negative Numbers:** `[[-1,-2],[-3,-4]]` → Should work correctly with negative values

---

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force Approach:
**Concept:** Use a visited matrix to track which cells have been processed. Start from (0,0) and move in direction order: right → down → left → up. When hitting a boundary or visited cell, change direction.

**Time Complexity:** O(m × n) - Visit each cell exactly once  
**Space Complexity:** O(m × n) - Need additional visited matrix of same size

### Optimized Approach (Best Solution):
**Concept:** Use four boundary pointers (top, right, bottom, left) to define the current "layer" of the spiral. Process elements layer by layer from outside to inside, shrinking boundaries after each complete layer traversal.

**Time Complexity:** O(m × n) - Visit each cell exactly once  
**Space Complexity:** O(1) - Only use constant extra space for pointers (excluding output list)

### Trade-off Analysis:
Both approaches have the same time complexity (O(m × n)), which is optimal since we must visit every element. However, the **optimized boundary approach is superior** because:
- **Better Space Efficiency:** O(1) vs O(m × n) - No need for auxiliary visited matrix
- **Cleaner Logic:** Direction changes are implicit in the boundary shrinking pattern
- **Less Overhead:** No need to check visited status at each step
- **Production Ready:** More maintainable and easier to understand

---

## 3. Algorithm Design

### Data Structures Used:
- **Result List (ArrayList):** To store elements in spiral order - O(1) append operation
- **Four Boundary Pointers (Integers):** To track current layer boundaries
    - `top` - current topmost unprocessed row
    - `right` - current rightmost unprocessed column
    - `bottom` - current bottommost unprocessed row
    - `left` - current leftmost unprocessed column

### Step-by-Step Logic:

**Initialization:**
1. Create an empty result list
2. Initialize boundary pointers:
    - `top = 0` (start from first row)
    - `right = matrix[0].lastIndex` (last column index)
    - `bottom = matrix.lastIndex` (last row index)
    - `left = 0` (start from first column)

**Main Loop (while top <= bottom AND left <= right):**

Each iteration processes one complete layer of the spiral:

**Step 1 - Traverse Top Row (Left to Right):**
- Loop from `left` to `right` on the `top` row
- Add `matrix[top][i]` to result
- After completion, increment `top++` (this row is done)

**Step 2 - Traverse Right Column (Top to Bottom):**
- Loop from `top` to `bottom` on the `right` column
- Add `matrix[i][right]` to result
- After completion, decrement `right--` (this column is done)

**Step 3 - Traverse Bottom Row (Right to Left):**
- **Guard Condition:** Only if `top <= bottom` (ensures we still have rows to process)
- Loop from `right` to `left` on the `bottom` row
- Add `matrix[bottom][i]` to result
- After completion, decrement `bottom--` (this row is done)

**Step 4 - Traverse Left Column (Bottom to Top):**
- **Guard Condition:** Only if `left <= right` (ensures we still have columns to process)
- Loop from `bottom` to `top` on the `left` column
- Add `matrix[i][left]` to result
- After completion, increment `left++` (this column is done)

**Why Guard Conditions?**
- For single-row matrices: Without the guard in Step 3, we'd process the same row twice
- For single-column matrices: Without the guard in Step 4, we'd process the same column twice
- These prevent duplicate processing when the spiral collapses to a single line

**Loop Continuation:**
- After processing all 4 sides, boundaries have shrunk inward
- Continue until boundaries cross (top > bottom or left > right)

**Return:** The result list containing all elements in spiral order

---

## 4. Production-Ready Implementation

```kotlin
package spiralmatrix

class Solution {
    fun spiralOrder(matrix: Array<IntArray>): List<Int> {
        // Guard clause: Handle empty matrix (though constraints guarantee non-empty)
        if (matrix.isEmpty() || matrix[0].isEmpty()) {
            return emptyList()
        }
        
        val result = ArrayList<Int>(matrix.size * matrix[0].size)
        
        // Initialize boundary pointers
        var top = 0
        var right = matrix[0].lastIndex
        var bottom = matrix.lastIndex
        var left = 0
        
        // Process matrix layer by layer from outside to inside
        while (top <= bottom && left <= right) {
            
            // Step 1: Traverse top row from left to right
            for (col in left..right) {
                result.add(matrix[top][col])
            }
            top++ // Move top boundary down
            
            // Step 2: Traverse right column from top to bottom
            for (row in top..bottom) {
                result.add(matrix[row][right])
            }
            right-- // Move right boundary left
            
            // Step 3: Traverse bottom row from right to left
            // Guard: Only process if there's still a valid bottom row
            if (top <= bottom) {
                for (col in right downTo left) {
                    result.add(matrix[bottom][col])
                }
                bottom-- // Move bottom boundary up
            }
            
            // Step 4: Traverse left column from bottom to top
            // Guard: Only process if there's still a valid left column
            if (left <= right) {
                for (row in bottom downTo top) {
                    result.add(matrix[row][left])
                }
                left++ // Move left boundary right
            }
        }
        
        return result
    }
}
```

### Code Quality Features:
- ✅ **Meaningful Variable Names:** `top`, `right`, `bottom`, `left` clearly indicate boundary positions
- ✅ **Guard Clauses:** Empty matrix check at the beginning
- ✅ **Conditional Guards:** Prevent duplicate processing in Steps 3 & 4
- ✅ **Comments:** Explain each step of the spiral traversal
- ✅ **Modular Logic:** Each direction is clearly separated
- ✅ **Pre-sized ArrayList:** Efficient memory allocation for result

---

## 5. Verification & Complexity Finalization

### Dry Run Example:
**Input:** `matrix = [[1,2,3],[4,5,6],[7,8,9]]`

```
Initial Matrix:     Initial State:
[1, 2, 3]          top = 0, right = 2
[4, 5, 6]          bottom = 2, left = 0
[7, 8, 9]          result = []
```

**Iteration 1:**
1. **Traverse Top Row (row 0, cols 0→2):** Add 1, 2, 3 → `result = [1,2,3]`, `top = 1`
2. **Traverse Right Column (col 2, rows 1→2):** Add 6, 9 → `result = [1,2,3,6,9]`, `right = 1`
3. **Traverse Bottom Row (row 2, cols 1→0):** Add 8, 7 → `result = [1,2,3,6,9,8,7]`, `bottom = 1`
4. **Traverse Left Column (col 0, row 1):** Add 4 → `result = [1,2,3,6,9,8,7,4]`, `left = 1`

**State after Iteration 1:** `top = 1, right = 1, bottom = 1, left = 1`

**Iteration 2:**
1. **Traverse Top Row (row 1, col 1):** Add 5 → `result = [1,2,3,6,9,8,7,4,5]`, `top = 2`
2. **Traverse Right Column:** Skip (top > bottom)
3. **Guard Check (top <= bottom):** False (2 > 1) → Skip
4. **Guard Check (left <= right):** True (1 <= 1) but no elements to process

**State after Iteration 2:** `top = 2, bottom = 1` → Loop exits (top > bottom)

**Final Output:** `[1,2,3,6,9,8,7,4,5]` ✅ Correct!

---

### Edge Case Verification:

**Single Row:** `[[1,2,3]]`
- Iteration 1: Add 1,2,3 (top row), top=1, then right column (empty), bottom guard fails
- Result: `[1,2,3]` ✅

**Single Column:** `[[1],[2],[3]]`
- Iteration 1: Add 1 (top), top=1; Add 2,3 (right col), right=-1; bottom guard passes, left guard fails
- Result: `[1,2,3]` ✅

---

### Final Complexity Analysis:

**Time Complexity: O(m × n)**
- We visit each cell exactly once
- The four traversal loops collectively process all m × n elements
- No repeated visits or backtracking
- **Optimal:** Cannot do better than O(m × n) since we must read every element

**Space Complexity: O(1)**
- Only using 4 integer pointers (top, right, bottom, left)
- Loop counters are constant space
- **Note:** The output list is not counted as extra space as it's required by the problem
- If counting output: O(m × n) for storing the result
- **Optimal:** Best possible auxiliary space for this approach

---

## Summary

This solution uses a **layer-by-layer boundary shrinking technique** that is:
- **Efficient:** O(m × n) time with O(1) auxiliary space
- **Elegant:** No need for visited tracking or complex direction logic
- **Robust:** Handles all edge cases with minimal conditional checks
- **Production-Ready:** Clean, readable code with proper documentation

The key insight is that spiral order naturally follows a pattern of processing rectangular boundaries from outside to inside, which can be elegantly expressed using four moving pointers.

