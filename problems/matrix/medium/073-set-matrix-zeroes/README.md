# 73 - Set Matrix Zeroes

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/set-matrix-zeroes/

---

## Problem Description:
Given an `m x n` integer matrix `matrix`, if an element is `0`, set its entire row and column to `0`'s.

You must do it **in place**.

**Example 1:**
```
Input: matrix = [[1,1,1],[1,0,1],[1,1,1]]
Output: [[1,0,1],[0,0,0],[1,0,1]]
```

**Example 2:**
```
Input: matrix = [[0,1,2,0],[3,4,5,2],[1,3,1,5]]
Output: [[0,0,0,0],[0,4,5,0],[0,3,1,0]]
```

**Constraints:**
- `m == matrix.length`
- `n == matrix[0].length`
- `1 <= m, n <= 200`
- `-2^31 <= matrix[i][j] <= 2^31 - 1`

**Follow up:**
- A straightforward solution using O(mn) space is probably a bad idea.
- A simple improvement uses O(m + n) space, but still not the best solution.
- Could you devise a constant space solution?

---

## Solution Design

### 1. Clarification & Edge Cases:

**Key Constraints:**
- Matrix dimensions: `1 <= m, n <= 200` (can be non-square)
- Must modify the matrix **in-place**
- Elements can be any 32-bit integer (including negative values and zero)
- If a cell is `0`, its entire row AND column must become `0`

**Edge Cases:**
1. **Single cell matrix** (`1x1`): If it's `0`, leave as `0`; otherwise, no change
2. **Single row matrix** (`1xn`): If any cell is `0`, entire row becomes `0`
3. **Single column matrix** (`mx1`): If any cell is `0`, entire column becomes `0`
4. **All zeros**: Matrix already all zeros, should remain unchanged
5. **No zeros**: Matrix should remain completely unchanged
6. **First row/column contains zeros**: Must handle specially to avoid losing marker information
7. **Multiple zeros in same row/column**: Should not cause issues (idempotent operation)

---

### 2. High-Level Approach Analysis (Trade-offs):

#### **Brute Force Approach (Extra Matrix Copy):**
- **Method**: Create a copy of the original matrix, scan for zeros in original, set rows/columns to zero in the copy
- **Logic**: 
  1. Create a new `m x n` matrix as a copy
  2. Iterate through original matrix to find all zeros
  3. For each zero found at `[i][j]`, set entire row `i` and column `j` to zero in the copy
  4. Copy the result back to original matrix (or return copy)
- **Time Complexity**: O(m × n × (m + n)) - for each cell, potentially marking entire row and column
- **Space Complexity**: O(m × n) - requires full matrix copy
- **Drawback**: Violates in-place requirement, uses excessive space

#### **Better Approach (Using Sets to Track):**
- **Method**: Use two sets to track which rows and columns should be zeroed
- **Logic**:
  1. First pass: Scan matrix and record indices of rows and columns that contain zeros
  2. Second pass: Zero out all cells in recorded rows and columns
- **Time Complexity**: O(m × n) - two passes through the matrix
- **Space Complexity**: O(m + n) - two sets storing row and column indices
- **Improvement**: More efficient than brute force, but still uses extra space

#### **Optimized Approach (Use First Row/Column as Markers):**
- **Method**: Use the first row and first column of the matrix itself as storage for zero markers
- **Logic**:
  1. Use two boolean flags to track if first row and first column originally had zeros
  2. Scan the rest of matrix (`matrix[1..m-1][1..n-1]`); when finding zero at `[i][j]`, mark `matrix[i][0] = 0` and `matrix[0][j] = 0`
  3. Use these markers to zero out corresponding rows and columns
  4. Finally, handle first row and column based on the boolean flags
- **Time Complexity**: O(m × n) - multiple passes but all linear
- **Space Complexity**: O(1) - only two boolean variables
- **Advantage**: Achieves optimal space complexity while maintaining efficient time complexity

**Why Optimized is Better:**
The optimized approach reduces space from **O(m + n)** to **O(1)** while keeping the same **O(m × n)** time complexity. By cleverly using the matrix's own first row and column as marker storage, we eliminate the need for external data structures entirely.

---

### 3. Algorithm Design:

**Core Strategy: Use First Row and First Column as Marker Arrays**

**Step-by-Step Logic:**

1. **Initialize tracking flags for first row and column:**
   - `firstRowHasZero`: Boolean to track if the first row originally contains any zero
   - `firstColHasZero`: Boolean to track if the first column originally contains any zero
   - These flags prevent us from losing information when we use row 0 and column 0 as markers

2. **Check if first column has any zeros:**
   - Iterate through `matrix[i][0]` for all rows `i = 0 to m-1`
   - If any cell is `0`, set `firstColHasZero = true` and break

3. **Check if first row has any zeros:**
   - Iterate through `matrix[0][j]` for all columns `j = 0 to n-1`
   - If any cell is `0`, set `firstRowHasZero = true` and break

4. **Scan the rest of the matrix and mark zeros:**
   - For each cell `matrix[i][j]` where `i ∈ [1, m-1]` and `j ∈ [1, n-1]`:
     - If `matrix[i][j] == 0`:
       - Mark the row: `matrix[i][0] = 0` (use first column as row marker)
       - Mark the column: `matrix[0][j] = 0` (use first row as column marker)

5. **Zero out rows based on markers:**
   - For each row `i ∈ [1, m-1]`:
     - If `matrix[i][0] == 0`, set all cells in row `i` to zero (columns `1 to n-1`)

6. **Zero out columns based on markers:**
   - For each column `j ∈ [1, n-1]`:
     - If `matrix[0][j] == 0`, set all cells in column `j` to zero (rows `1 to m-1`)

7. **Handle first column if needed:**
   - If `firstColHasZero == true`, set all `matrix[i][0] = 0` for `i = 0 to m-1`

8. **Handle first row if needed:**
   - If `firstRowHasZero == true`, set all `matrix[0][j] = 0` for `j = 0 to n-1`

**Data Structures Used:**
- **Input matrix itself**: Modified in-place, first row/column serve dual purpose as data and markers
- **Two boolean flags**: `firstRowHasZero`, `firstColHasZero` (only O(1) extra space)
- **Why**: By using the matrix's own boundary cells as markers, we avoid needing separate arrays or sets

**Key Insight:**
The trick is to treat the first row and column as "metadata storage" while preserving their original zero status with boolean flags. This allows us to use O(1) space while avoiding the complexity of creating copies or additional data structures.

---

### 4. Production-Ready Implementation:

```kotlin
package setmatrixzeroes

class Solution {
    fun setZeroes(matrix: Array<IntArray>): Unit {
        // Guard clause: handle edge case of empty matrix (though constraints say m,n >= 1)
        if (matrix.isEmpty() || matrix[0].isEmpty()) return
        
        val m = matrix.size
        val n = matrix[0].size
        
        // Step 1: Track if first row and first column originally have zeros
        var firstColHasZero = false
        var firstRowHasZero = false
        
        // Step 2: Check if first column has any zeros
        for (i in 0 until m) {
            if (matrix[i][0] == 0) {
                firstColHasZero = true
                break
            }
        }
        
        // Step 3: Check if first row has any zeros
        for (j in 0 until n) {
            if (matrix[0][j] == 0) {
                firstRowHasZero = true
                break
            }
        }
        
        // Step 4: Use first row and column as markers for the rest of the matrix
        // Scan internal cells (excluding first row and column)
        for (i in 1 until m) {
            for (j in 1 until n) {
                if (matrix[i][j] == 0) {
                    // Mark this row and column for zeroing
                    matrix[i][0] = 0  // Mark row i
                    matrix[0][j] = 0  // Mark column j
                }
            }
        }
        
        // Step 5: Zero out rows based on markers in first column
        for (i in 1 until m) {
            if (matrix[i][0] == 0) {
                for (j in 1 until n) {
                    matrix[i][j] = 0
                }
            }
        }
        
        // Step 6: Zero out columns based on markers in first row
        for (j in 1 until n) {
            if (matrix[0][j] == 0) {
                for (i in 1 until m) {
                    matrix[i][j] = 0
                }
            }
        }
        
        // Step 7: Handle first column if it originally had zeros
        if (firstColHasZero) {
            for (i in 0 until m) {
                matrix[i][0] = 0
            }
        }
        
        // Step 8: Handle first row if it originally had zeros
        if (firstRowHasZero) {
            for (j in 0 until n) {
                matrix[0][j] = 0
            }
        }
    }
}

fun main() {
    val solution = Solution()
    
    // Test Case 1: 3x3 matrix with zero in the center
    println("=== Test Case 1: 3x3 with center zero ===")
    val matrix1 = arrayOf(
        intArrayOf(1, 1, 1),
        intArrayOf(1, 0, 1),
        intArrayOf(1, 1, 1)
    )
    println("Before:")
    matrix1.forEach { row -> println(row.joinToString(" ")) }
    
    solution.setZeroes(matrix1)
    
    println("\nAfter:")
    matrix1.forEach { row -> println(row.joinToString(" ")) }
    println("Expected: [[1,0,1],[0,0,0],[1,0,1]]")
    
    // Test Case 2: 3x4 matrix with zeros at corners
    println("\n=== Test Case 2: 3x4 with corner zeros ===")
    val matrix2 = arrayOf(
        intArrayOf(0, 1, 2, 0),
        intArrayOf(3, 4, 5, 2),
        intArrayOf(1, 3, 1, 5)
    )
    println("Before:")
    matrix2.forEach { row -> println(row.joinToString(" ")) }
    
    solution.setZeroes(matrix2)
    
    println("\nAfter:")
    matrix2.forEach { row -> println(row.joinToString(" ")) }
    println("Expected: [[0,0,0,0],[0,4,5,0],[0,3,1,0]]")
    
    // Test Case 3: Edge case - single row
    println("\n=== Test Case 3: Single row with zero ===")
    val matrix3 = arrayOf(intArrayOf(1, 0, 3))
    println("Before: ${matrix3[0].joinToString(" ")}")
    solution.setZeroes(matrix3)
    println("After: ${matrix3[0].joinToString(" ")}")
    println("Expected: [0,0,0]")
    
    // Test Case 4: No zeros
    println("\n=== Test Case 4: No zeros ===")
    val matrix4 = arrayOf(
        intArrayOf(1, 2, 3),
        intArrayOf(4, 5, 6)
    )
    println("Before:")
    matrix4.forEach { row -> println(row.joinToString(" ")) }
    
    solution.setZeroes(matrix4)
    
    println("\nAfter:")
    matrix4.forEach { row -> println(row.joinToString(" ")) }
    println("Expected: Same as input (no changes)")
}
```

---

### 5. Verification & Complexity Finalization:

#### **Dry Run with Example: `[[1,1,1],[1,0,1],[1,1,1]]`**

**Initial State:**
```
1 1 1
1 0 1
1 1 1
```

**Execution:**

**Step 1-3: Check first row and column for zeros**
- First column: `matrix[0][0]=1, matrix[1][0]=1, matrix[2][0]=1` → `firstColHasZero = false`
- First row: `matrix[0][0]=1, matrix[0][1]=1, matrix[0][2]=1` → `firstRowHasZero = false`

**Step 4: Scan and mark (rows 1-2, columns 1-2)**
- `matrix[1][1] = 0` → Mark `matrix[1][0] = 0` and `matrix[0][1] = 0`
- All other cells are non-zero

After marking:
```
1 0 1
0 0 1
1 1 1
```

**Step 5: Zero out rows based on first column markers**
- Row 0: `matrix[0][0] = 1` (not 0), skip
- Row 1: `matrix[1][0] = 0` → Zero out `matrix[1][1]` and `matrix[1][2]`
- Row 2: `matrix[2][0] = 1` (not 0), skip

After step 5:
```
1 0 1
0 0 0
1 1 1
```

**Step 6: Zero out columns based on first row markers**
- Column 0: Already handled in first column
- Column 1: `matrix[0][1] = 0` → Zero out `matrix[1][1]` and `matrix[2][1]`
- Column 2: `matrix[0][2] = 1` (not 0), skip

After step 6:
```
1 0 1
0 0 0
1 0 1
```

**Step 7-8: Handle first row and column**
- `firstColHasZero = false` → No action
- `firstRowHasZero = false` → No action

**Final State:**
```
1 0 1
0 0 0
1 0 1
```
✅ **Correct!** Matches expected output.

---

#### **Dry Run with Example 2: `[[0,1,2,0],[3,4,5,2],[1,3,1,5]]`**

**Initial State:**
```
0 1 2 0
3 4 5 2
1 3 1 5
```

**Step 1-3: Check first row and column**
- First column: `matrix[0][0] = 0` → `firstColHasZero = true`
- First row: `matrix[0][0] = 0` and `matrix[0][3] = 0` → `firstRowHasZero = true`

**Step 4: Scan internal cells (rows 1-2, columns 1-3)**
- All internal cells are non-zero, no new markers added

After marking (no changes from scanning internal cells):
```
0 1 2 0
3 4 5 2
1 3 1 5
```

**Step 5: Zero out rows** (none marked in first column except row 0)
- Row 1: `matrix[1][0] = 3` (not 0), skip
- Row 2: `matrix[2][0] = 1` (not 0), skip

**Step 6: Zero out columns** (columns 1-3 where first row is 0)
- Column 1: `matrix[0][1] = 1` (not 0), skip
- Column 2: `matrix[0][2] = 2` (not 0), skip
- Column 3: `matrix[0][3] = 0` → Zero out `matrix[1][3]` and `matrix[2][3]`

After step 6:
```
0 1 2 0
3 4 5 0
1 3 1 0
```

**Step 7: Handle first column** (`firstColHasZero = true`)
- Zero out entire first column: `matrix[0][0]=0, matrix[1][0]=0, matrix[2][0]=0`

After step 7:
```
0 1 2 0
0 4 5 0
0 3 1 0
```

**Step 8: Handle first row** (`firstRowHasZero = true`)
- Zero out entire first row: `matrix[0][0]=0, matrix[0][1]=0, matrix[0][2]=0, matrix[0][3]=0`

**Final State:**
```
0 0 0 0
0 4 5 0
0 3 1 0
```
✅ **Correct!** Matches expected output.

---

#### **Final Complexity Analysis:**

**Time Complexity: O(m × n)**
- **Step 2**: Check first column → O(m)
- **Step 3**: Check first row → O(n)
- **Step 4**: Scan internal matrix → O(m × n)
- **Step 5**: Zero out rows → O(m × n) in worst case
- **Step 6**: Zero out columns → O(m × n) in worst case
- **Step 7**: Handle first column → O(m)
- **Step 8**: Handle first row → O(n)
- **Total**: O(m × n) - dominated by matrix scanning and zeroing operations

**Space Complexity: O(1)**
- Only two boolean variables: `firstRowHasZero` and `firstColHasZero`
- Variables `m`, `n`, loop counters `i`, `j` are constant space
- No additional data structures that scale with input size
- Matrix is modified in-place
- **Overall**: O(1) auxiliary space

---

## Key Takeaways:

1. **In-place algorithms** often require creative use of existing data structures
2. **Boundary cells** (first row/column) can serve as marker storage
3. **Order matters**: Process internal cells first, then boundaries
4. **Flag preservation**: Save original state before using cells as markers
5. This problem demonstrates how **O(m + n) → O(1)** space optimization is achievable through clever technique
