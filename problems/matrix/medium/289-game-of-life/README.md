# 289 - Game of Life

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/game-of-life/

---

## Solution Design

### 1. Clarification & Edge Cases:

**Key Constraints:**
* The board is an `m x n` grid where each cell is either 0 (dead) or 1 (alive)
* All cells must be updated simultaneously according to Conway's Game of Life rules
* The problem requires in-place modification (follow-up: O(1) space complexity)
* Board dimensions: `m, n >= 1`

**Game of Life Rules:**
1. Any live cell with fewer than 2 live neighbors dies (underpopulation)
2. Any live cell with 2 or 3 live neighbors lives on
3. Any live cell with more than 3 live neighbors dies (overpopulation)
4. Any dead cell with exactly 3 live neighbors becomes alive (reproduction)

**Edge Cases:**
* Single cell board (1x1) - no neighbors, dies if alive
* All dead cells - should remain all dead
* All alive cells - most will die except those with 2-3 neighbors
* Edge/corner cells - have fewer neighbors (3, 5, or 8 neighbors depending on position)
* Small boards (2x2, 2x3) - fewer neighbors for all cells

---

### 2. High-Level Approach Analysis (Trade-offs):

**Brute Force Approach:**
* Create a copy of the entire board
* Iterate through the original board, count neighbors for each cell
* Apply rules and update the copy
* Copy the results back to the original board
* **Time Complexity:** O(m × n) - iterate through all cells
* **Space Complexity:** O(m × n) - need a complete copy of the board

**Optimized Approach (In-Place with Encoding):**
* Use bit manipulation to encode both current and next state in a single integer
* Store original state in the LSB (least significant bit)
* Store next state in the 2nd bit
* Count neighbors using `board[x][y] & 1` to read original state
* After processing all cells, decode by right-shifting: `board[i][j] >>= 1`
* **Time Complexity:** O(m × n) - still iterate through all cells once for counting + once for decoding
* **Space Complexity:** O(1) - only use constant extra space (no copy of board)

**Why Optimized is Better:**
While both approaches have the same time complexity, the optimized approach achieves O(1) space complexity by cleverly encoding both states within the existing cell values. This is crucial for large boards and satisfies the follow-up constraint.

**Encoding Scheme:**
* `0` → 0: was dead (0), stays dead (0)
* `1` → 0: was alive (1), becomes dead (0) 
* `2` → 1: was dead (0), becomes alive (1)
* `3` → 1: was alive (1), stays alive (1)

After decoding (`>> 1`), we get the final next state.

---

### 3. Algorithm Design:

**Step-by-Step Logic:**

1. **Initialization:**
   - Get board dimensions (m rows, n columns)
   - Define 8 direction vectors for all neighbors (diagonal, horizontal, vertical)

2. **Encode Phase - Process each cell:**
   - For each cell at position (i, j):
     - Read current state: `cell = board[i][j]`
     - Count live neighbors:
       - Check all 8 directions
       - Use `board[x][y] & 1` to extract original state (handles encoded values)
       - Only count if neighbor is within bounds
     - Apply Game of Life rules and encode:
       - Live cell (1) with < 2 or > 3 neighbors → encode as 1 (dies)
       - Live cell (1) with 2-3 neighbors → encode as 3 (survives)
       - Dead cell (0) with exactly 3 neighbors → encode as 2 (becomes alive)
       - All other cases → encode as 0 (stays dead)

3. **Decode Phase:**
   - Iterate through all cells again
   - Right shift each cell by 1: `board[i][j] >>= 1`
   - This extracts the next state (2nd bit becomes 1st bit)

**Data Structures Used:**
* **Array of Pairs (Direction Vectors):** To systematically check all 8 neighbors without code duplication
* **In-place Board Modification:** The board itself serves as both input and output
* **Bit Manipulation:** Use bitwise AND (`&`) to read original state and right shift (`>>`) to decode

**Why These Choices:**
* Direction vectors make the code clean and maintainable vs 8 separate if-statements
* Bit manipulation allows us to store 2 states in one integer without extra space
* In-place modification satisfies the O(1) space requirement

---

### 4. Production-Ready Implementation:

```kotlin
class Solution {
    fun gameOfLife(board: Array<IntArray>) {
        // Guard clause: empty board
        if (board.isEmpty() || board[0].isEmpty()) return
        
        val m = board.size
        val n = board[0].size
        
        // Define 8 direction vectors for all neighbors
        val directions = arrayOf(
            Pair(-1, -1), // top-left
            Pair(-1, 0),  // top
            Pair(-1, 1),  // top-right
            Pair(0, -1),  // left
            Pair(0, 1),   // right
            Pair(1, -1),  // bottom-left
            Pair(1, 0),   // bottom
            Pair(1, 1)    // bottom-right
        )
        
        // Phase 1: Encode next state into current cell values
        for (i in 0 until m) {
            for (j in 0 until n) {
                val currentState = board[i][j]
                var liveNeighbors = 0
                
                // Count live neighbors
                for (direction in directions) {
                    val newRow = i + direction.first
                    val newCol = j + direction.second
                    
                    // Check bounds and count original state using bit mask
                    if (newRow in 0 until m && newCol in 0 until n) {
                        liveNeighbors += board[newRow][newCol] and 1
                    }
                }
                
                // Apply Conway's Game of Life rules with encoding
                when {
                    // Rule 1 & 3: Live cell dies (underpopulation or overpopulation)
                    currentState == 1 && (liveNeighbors < 2 || liveNeighbors > 3) -> {
                        board[i][j] = 1 // encode: 1 -> 0 (dies)
                    }
                    // Rule 2: Live cell survives
                    currentState == 1 -> {
                        board[i][j] = 3 // encode: 1 -> 1 (lives on)
                    }
                    // Rule 4: Dead cell becomes alive (reproduction)
                    currentState == 0 && liveNeighbors == 3 -> {
                        board[i][j] = 2 // encode: 0 -> 1 (becomes alive)
                    }
                    // Dead cell stays dead
                    else -> {
                        board[i][j] = 0 // encode: 0 -> 0 (stays dead)
                    }
                }
            }
        }
        
        // Phase 2: Decode to get final next state
        for (i in 0 until m) {
            for (j in 0 until n) {
                board[i][j] = board[i][j] shr 1 // right shift to extract next state
            }
        }
    }
}
```

**Key Implementation Details:**
* **Guard clause** at the start handles empty boards gracefully
* **Meaningful variable names** (`currentState`, `liveNeighbors`, `directions`) improve readability
* **Bit masking (`and 1`)** ensures we read the original state even after encoding
* **When expression** clearly maps to the 4 Game of Life rules with comments
* **Two-phase approach** separates encoding and decoding for clarity

---

### 5. Verification & Complexity Finalization:

**Dry Run Example:**

Input board:
```
[0, 1, 0]
[0, 0, 1]
[1, 1, 1]
[0, 0, 0]
```

**Phase 1 - Encoding (process each cell):**

Cell (0,0) = 0: neighbors = 1 → stays dead → encode as 0  
Cell (0,1) = 1: neighbors = 1 → dies (< 2) → encode as 1  
Cell (0,2) = 0: neighbors = 2 → stays dead → encode as 0  
Cell (1,0) = 0: neighbors = 3 → becomes alive! → encode as 2  
Cell (1,1) = 0: neighbors = 5 → stays dead → encode as 0  
Cell (1,2) = 1: neighbors = 3 → lives on → encode as 3  
Cell (2,0) = 1: neighbors = 1 → dies (< 2) → encode as 1  
Cell (2,1) = 1: neighbors = 3 → lives on → encode as 3  
Cell (2,2) = 1: neighbors = 2 → lives on → encode as 3  
Cell (3,0) = 0: neighbors = 2 → stays dead → encode as 0  
Cell (3,1) = 0: neighbors = 3 → becomes alive! → encode as 2  
Cell (3,2) = 0: neighbors = 2 → stays dead → encode as 0  

After encoding:
```
[0, 1, 0]
[2, 0, 3]
[1, 3, 3]
[0, 2, 0]
```

**Phase 2 - Decoding (right shift by 1):**
```
[0, 0, 0]    (0>>1=0, 1>>1=0, 0>>1=0)
[1, 0, 1]    (2>>1=1, 0>>1=0, 3>>1=1)
[0, 1, 1]    (1>>1=0, 3>>1=1, 3>>1=1)
[0, 1, 0]    (0>>1=0, 2>>1=1, 0>>1=0)
```

**Verification:** ✓ Correct according to Game of Life rules!

---

**Final Complexity Analysis:**

**Time Complexity: O(m × n)**
* First pass: Visit each cell once and check 8 neighbors → O(m × n × 8) = O(m × n)
* Second pass: Visit each cell once for decoding → O(m × n)
* Total: O(m × n) + O(m × n) = O(m × n)

**Space Complexity: O(1)**
* Only use constant extra variables: `m`, `n`, `currentState`, `liveNeighbors`, loop indices
* Direction vectors array is constant size (8 elements)
* No additional data structures that scale with input size
* Modify the board in-place using bit manipulation encoding

This solution is optimal for the follow-up requirement of O(1) space complexity while maintaining linear time complexity.
