# 36 - Valid Sudoku

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/valid-sudoku/

# Solution Design: Valid Sudoku

## Problem Description:
Determine if a 9x9 Sudoku board is valid. Only the filled cells need to be validated according to the following rules:
1. Each row must contain the digits 1-9 without repetition.
2. Each column must contain the digits 1-9 without repetition.
3. Each of the nine 3x3 sub-boxes of the grid must contain the digits 1-9 without repetition.

**Note:** A Sudoku board (partially filled) could be valid but is not necessarily solvable. Only the filled cells need to be validated according to the mentioned rules.

**Problem Link:** https://leetcode.com/problems/valid-sudoku/

**Preferred Language:** Kotlin

---

## 1. Clarification & Edge Cases

### Key Constraints:
* **Input size:** Always a 9x9 board (fixed size)
* **Data range:** Each cell contains either a digit '1'-'9' or '.' (empty cell)
* **Validation scope:** Only filled cells need validation; empty cells ('.') are ignored
* **No mutation required:** We only validate, don't solve the Sudoku

### Edge Cases to Handle:
1. **All empty board** - A board with all '.' characters should return `true` (valid but empty)
2. **Single filled cell** - Should return `true` as there are no duplicates
3. **Duplicate in row** - Should return `false` immediately upon detection
4. **Duplicate in column** - Should return `false` immediately upon detection
5. **Duplicate in 3x3 box** - Should return `false` immediately upon detection
6. **Multiple violations** - Early termination on first violation found
7. **Valid complete board** - All cells filled with valid configuration should return `true`

---

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force Approach:
**Strategy:** For each cell with a digit, validate all three rules separately:
1. Check the entire row for duplicates
2. Check the entire column for duplicates
3. Check the entire 3x3 box for duplicates

**Time Complexity:** O(n³) where n = 9
- For each of the 81 cells: O(n²)
- Each validation requires checking up to 9 cells: O(n)
- Total: O(n² × n) = O(n³) = O(729) operations

**Space Complexity:** O(1) - No additional data structures needed

**Drawbacks:**
- Redundant checking: Each digit is validated multiple times
- Inefficient: Rescanning rows/columns/boxes repeatedly

### Optimized Approach (Best Solution):
**Strategy:** Single-pass validation with tracking data structures
1. Maintain three sets of tracking arrays:
    - `rows[9][9]`: Boolean array tracking which digits appear in each row
    - `cols[9][9]`: Boolean array tracking which digits appear in each column
    - `boxes[9][9]`: Boolean array tracking which digits appear in each 3x3 box
2. Iterate through the board once (single pass)
3. For each filled cell, check if the digit already exists in the corresponding row, column, or box
4. If duplicate found, return `false` immediately
5. Mark the digit as seen in all three tracking structures
6. If loop completes, return `true`

**Time Complexity:** O(n²) where n = 9 → O(81) = O(1) constant time
- Single pass through all 81 cells
- Each lookup/update operation is O(1)

**Space Complexity:** O(n²) → O(243) = O(1) constant space
- Three arrays of size 9×9 = 243 boolean values
- Since n is fixed at 9, this is effectively constant space

**Why Optimized is Better:**
- **Time:** O(n³) vs O(n²) - Eliminates redundant validation passes
- **Space:** O(1) vs O(1) - Both constant, but optimized enables O(n²) time
- **Early termination:** Can detect violations immediately without rechecking
- **Clarity:** More structured and easier to understand the validation logic

---

## 3. Algorithm Design

### Core Logic (Step-by-Step):

1. **Initialize Tracking Structures:**
   ```
   - Create rows[9][9] boolean array (all false)
   - Create cols[9][9] boolean array (all false)
   - Create boxes[9][9] boolean array (all false)
   ```
   These arrays track which digits (1-9) have been seen in each row, column, and 3x3 box.

2. **Single Pass Iteration:**
    - Iterate through each cell (i, j) where i is row index and j is column index
    - Range: i ∈ [0,8], j ∈ [0,8]

3. **Skip Empty Cells:**
    - If `board[i][j] == '.'`, continue to next cell
    - Only process cells with digits '1'-'9'

4. **Extract and Convert Digit:**
    - Convert character to integer: `digit = board[i][j].digitToInt()`
    - Calculate array index: `index = digit - 1` (for 0-based array indexing)

5. **Calculate 3x3 Box Index:**
   ```
   subBoxRow = i / 3       // Which horizontal band (0, 1, or 2)
   subBoxCol = j / 3       // Which vertical band (0, 1, or 2)
   boxIndex = 3 * subBoxRow + subBoxCol
   ```
   This maps each cell to one of 9 boxes (numbered 0-8):
   ```
   Box indices:
   [0][1][2]
   [3][4][5]
   [6][7][8]
   ```

6. **Validation Check (Collision Detection):**
    - Check if `rows[i][index]` is true → digit already in this row → return `false`
    - Check if `cols[j][index]` is true → digit already in this column → return `false`
    - Check if `boxes[boxIndex][index]` is true → digit already in this box → return `false`

7. **Mark as Seen:**
    - Set `rows[i][index] = true`
    - Set `cols[j][index] = true`
    - Set `boxes[boxIndex][index] = true`

8. **Complete Validation:**
    - If we finish iterating all cells without finding duplicates, return `true`

### Data Structures & Rationale:

| Data Structure | Purpose | Why Chosen |
|---------------|---------|------------|
| **Boolean Arrays** | Track digit presence | O(1) lookup/update, memory efficient for fixed range |
| **2D Arrays (9×9)** | Organize tracking by row/col/box | Direct indexing without hash collisions |
| **Integer Division** | Calculate box index | Efficient mathematical mapping to 3x3 regions |

### Key Insights:
- **Box indexing formula:** `boxIndex = 3 * (i/3) + (j/3)` elegantly maps any (row, col) to its box
- **Early termination:** Return false immediately on first duplicate found
- **Guard clause pattern:** Skip empty cells early to avoid unnecessary processing

---

## 4. Production-Ready Implementation

```kotlin
package sudoku

class Solution {
    fun isValidSudoku(board: Array<CharArray>): Boolean {
        // Guard clause: handle null or invalid board size (though problem guarantees 9x9)
        if (board.isEmpty() || board.size != 9) return false
        
        // Initialize tracking structures for rows, columns, and 3x3 boxes
        // Each array tracks which digits (1-9) have been seen
        val rows = Array(9) { BooleanArray(9) { false } }
        val cols = Array(9) { BooleanArray(9) { false } }
        val boxes = Array(9) { BooleanArray(9) { false } }
        
        // Single pass through all cells
        for (i in 0..8) {
            // Guard clause: ensure row has correct size
            if (board[i].size != 9) return false
            
            for (j in 0..8) {
                val cell = board[i][j]
                
                // Guard clause: skip empty cells
                if (cell == '.') continue
                
                // Convert character to digit (1-9) and get array index (0-8)
                val digit = cell.digitToIntOrNull() 
                    ?: return false // Guard: invalid character
                
                // Guard clause: digit must be in range 1-9
                if (digit !in 1..9) return false
                
                val index = digit - 1 // Convert to 0-based index
                
                // Calculate which 3x3 box this cell belongs to (0-8)
                // Box layout: [0][1][2]
                //            [3][4][5]
                //            [6][7][8]
                val boxIndex = (i / 3) * 3 + (j / 3)
                
                // Validation: check for duplicates in row, column, or box
                if (rows[i][index]) return false // Duplicate in row
                if (cols[j][index]) return false // Duplicate in column
                if (boxes[boxIndex][index]) return false // Duplicate in box
                
                // Mark digit as seen in all three tracking structures
                rows[i][index] = true
                cols[j][index] = true
                boxes[boxIndex][index] = true
            }
        }
        
        // No duplicates found - valid Sudoku
        return true
    }
}

// Test function with example
fun main() {
    val solution = Solution()
    
    // Example 1: Valid Sudoku
    val validBoard = arrayOf(
        charArrayOf('5', '3', '.', '.', '7', '.', '.', '.', '.'),
        charArrayOf('6', '.', '.', '1', '9', '5', '.', '.', '.'),
        charArrayOf('.', '9', '8', '.', '.', '.', '.', '6', '.'),
        charArrayOf('8', '.', '.', '.', '6', '.', '.', '.', '3'),
        charArrayOf('4', '.', '.', '8', '.', '3', '.', '.', '1'),
        charArrayOf('7', '.', '.', '.', '2', '.', '.', '.', '6'),
        charArrayOf('.', '6', '.', '.', '.', '.', '2', '8', '.'),
        charArrayOf('.', '.', '.', '4', '1', '9', '.', '.', '5'),
        charArrayOf('.', '.', '.', '.', '8', '.', '.', '7', '9')
    )
    
    // Example 2: Invalid Sudoku (duplicate 8 in first column)
    val invalidBoard = arrayOf(
        charArrayOf('8', '3', '.', '.', '7', '.', '.', '.', '.'),
        charArrayOf('6', '.', '.', '1', '9', '5', '.', '.', '.'),
        charArrayOf('.', '9', '8', '.', '.', '.', '.', '6', '.'),
        charArrayOf('8', '.', '.', '.', '6', '.', '.', '.', '3'),
        charArrayOf('4', '.', '.', '8', '.', '3', '.', '.', '1'),
        charArrayOf('7', '.', '.', '.', '2', '.', '.', '.', '6'),
        charArrayOf('.', '6', '.', '.', '.', '.', '2', '8', '.'),
        charArrayOf('.', '.', '.', '4', '1', '9', '.', '.', '5'),
        charArrayOf('.', '.', '.', '.', '8', '.', '.', '7', '9')
    )
    
    // Example 3: Edge case - all empty
    val emptyBoard = Array(9) { CharArray(9) { '.' } }
    
    println("Valid board test: ${solution.isValidSudoku(validBoard)}") // Expected: true
    println("Invalid board test: ${solution.isValidSudoku(invalidBoard)}") // Expected: false
    println("Empty board test: ${solution.isValidSudoku(emptyBoard)}") // Expected: true
}
```

### Code Quality Highlights:
✅ **Guard clauses** for edge cases (null checks, size validation, invalid characters)  
✅ **Meaningful variable names** (rows, cols, boxes, boxIndex, cell, digit)  
✅ **Comments** explaining complex logic (box index calculation)  
✅ **Modular structure** with clear separation of concerns  
✅ **Early returns** for efficiency and readability  
✅ **Comprehensive tests** covering valid, invalid, and edge cases

---

## 5. Verification & Complexity Finalization

### Dry Run with Example:

**Input Board (First 3 rows shown):**
```
Row 0: ['5', '3', '.', '.', '7', '.', '.', '.', '.']
Row 1: ['6', '.', '.', '1', '9', '5', '.', '.', '.']
Row 2: ['.', '9', '8', '.', '.', '.', '.', '6', '.']
```

**Execution Trace:**

| Step | i | j | Cell | Digit | Index | BoxIndex | Check | Action |
|------|---|---|------|-------|-------|----------|-------|--------|
| 1 | 0 | 0 | '5' | 5 | 4 | 0 | rows[0][4]=F, cols[0][4]=F, boxes[0][4]=F | Mark all as T |
| 2 | 0 | 1 | '3' | 3 | 2 | 0 | rows[0][2]=F, cols[1][2]=F, boxes[0][2]=F | Mark all as T |
| 3 | 0 | 2 | '.' | - | - | - | Skip empty | Continue |
| 4 | 0 | 4 | '7' | 7 | 6 | 1 | rows[0][6]=F, cols[4][6]=F, boxes[1][6]=F | Mark all as T |
| 5 | 1 | 0 | '6' | 6 | 5 | 0 | rows[1][5]=F, cols[0][5]=F, boxes[0][5]=F | Mark all as T |
| 6 | 1 | 3 | '1' | 1 | 0 | 1 | rows[1][0]=F, cols[3][0]=F, boxes[1][0]=F | Mark all as T |
| ... | ... | ... | ... | ... | ... | ... | ... | ... |

**Key Observation:**
- Each digit is checked against three conditions in O(1) time
- Tracking arrays are updated immediately
- No duplicates found → returns `true` after checking all 81 cells

**Example of Invalid Case:**
If row 3 had '5' at position [3][0], when processing:
- `i=3, j=0, digit=5, index=4, boxIndex=3`
- Check: `cols[0][4]` is already `true` (because row 0, col 0 had '5')
- **Return `false` immediately** ✗

### Final Complexity Analysis:

#### Time Complexity: **O(1)** or more precisely **O(n²)** where n = 9

**Breakdown:**
- Outer loop: 9 iterations (rows)
- Inner loop: 9 iterations (columns)
- Operations per cell: O(1)
    - Character check: O(1)
    - Digit conversion: O(1)
    - Box index calculation: O(1)
    - Array lookups: O(1) × 3 = O(1)
    - Array updates: O(1) × 3 = O(1)
- **Total:** 9 × 9 × O(1) = O(81) = **O(1)** constant time

Since the board size is always fixed at 9×9, this is effectively constant time.

#### Space Complexity: **O(1)** or more precisely **O(n²)** where n = 9

**Breakdown:**
- `rows` array: 9 × 9 = 81 booleans
- `cols` array: 9 × 9 = 81 booleans
- `boxes` array: 9 × 9 = 81 booleans
- Other variables: O(1) (i, j, cell, digit, index, boxIndex)
- **Total:** 243 booleans + O(1) = **O(1)** constant space

Since n is fixed at 9, the space usage is constant regardless of input.

### Performance Characteristics:
✅ **Best Case:** O(1) - Invalid board with duplicate in first few cells  
✅ **Average Case:** O(81) - Processes about half the board before finding issue  
✅ **Worst Case:** O(81) - Valid board, checks all cells  
✅ **Space:** O(243) - Fixed memory footprint, no dynamic allocation

### Optimization Notes:
- Could use `HashSet<Int>` instead of boolean arrays, but boolean arrays are more memory efficient
- Could use bit manipulation (9-bit integers) to reduce space by ~8x, but boolean arrays are more readable
- Early termination on first duplicate provides practical speedup
- No recursion, no additional call stack overhead

---

## Conclusion

This solution provides an optimal balance of **time efficiency** (single-pass O(n²)), **space efficiency** (fixed O(n²) storage), and **code clarity** (straightforward tracking logic). The use of three synchronized tracking arrays elegantly solves the three-constraint validation problem in a single iteration.

The implementation is production-ready with comprehensive guard clauses, clear comments, and follows Kotlin best practices. The algorithm is interview-friendly, easy to explain, and demonstrates strong understanding of array indexing, mathematical mapping, and constraint satisfaction problems.

