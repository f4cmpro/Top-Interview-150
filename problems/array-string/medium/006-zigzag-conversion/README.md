# 6 - Zigzag Conversion

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/zigzag-conversion/

## 1. Clarification & Edge Cases

### Key Constraints
- `s` is the input string and can contain any printable characters.
- `numRows` is at least `1`.
- The goal is to return the zigzag reading order, not to print the grid itself.
- The relative order of characters within each zigzag row must be preserved.

### Edge Cases
- `numRows == 1`: the zigzag pattern collapses into a single row, so the output is the same as the input.
- `numRows >= s.length()`: there are not enough characters to form a full zigzag cycle, so the output is also the same as the input.
- Empty string: return an empty string immediately.
- Very short strings such as `"A"` or `"AB"`: the traversal logic should still work correctly.

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force Approach
One direct approach is to simulate the entire zigzag board using a 2D grid, place each character at the correct row and column, and then read the grid row by row.

This is simple to understand, but it wastes space because most grid cells are empty. It also adds unnecessary bookkeeping for column placement.

### Optimized Approach
The best solution is to build the answer row by row using one string builder per row.

We traverse the input once, append each character to the current row, and change direction when we reach the top or bottom row. After processing all characters, we concatenate all rows.

### Comparison
- Brute force time complexity: `O(n)` to place characters and `O(n)` to collect them, but with higher constant overhead.
- Brute force space complexity: `O(n * numRows)` in the worst case if a dense board is used.
- Optimized time complexity: `O(n)`.
- Optimized space complexity: `O(n)`.

The optimized approach is better because it avoids storing a sparse grid and directly constructs the final string with only the required memory.

## 3. Algorithm Design

### Step-by-Step Logic
1. Handle trivial cases first:
   - If the string is empty, return an empty string.
   - If `numRows == 1`, return the original string.
   - If `numRows >= s.length()`, return the original string.
2. Create a list of `numRows` mutable row buffers.
3. Start at row `0` and initially move downward.
4. For each character in the string:
   - Append it to the current row buffer.
   - If we are at the top row, switch direction to downward.
   - If we are at the bottom row, switch direction to upward.
   - Move to the next row based on the current direction.
5. Concatenate all row buffers in order to form the result.

### Data Structures Used
- `Array<StringBuilder>`: each row collects the characters assigned to it.
- A direction flag: tracks whether the traversal is moving downward or upward.

These choices are efficient because appending to a `StringBuilder` is fast, and the direction flag keeps the traversal logic simple.

## 4. Production-Ready Implementation

```kotlin
class Solution {
	fun convert(s: String, numRows: Int): String {
		if (s.isEmpty() || numRows == 1 || numRows >= s.length) {
			return s
		}

		val rows = Array(numRows) { StringBuilder() }
		var currentRow = 0
		var movingDown = false

		for (character in s) {
			rows[currentRow].append(character)

			if (currentRow == 0 || currentRow == numRows - 1) {
				movingDown = !movingDown
			}

			currentRow += if (movingDown) 1 else -1
		}

		val result = StringBuilder()
		for (row in rows) {
			result.append(row)
		}

		return result.toString()
	}
}
```

## 5. Verification & Complexity Finalization

### Dry Run
Example: `s = "PAYPALISHIRING"`, `numRows = 3`

The rows evolve as follows:
- Row 0: `P   A   H   N`
- Row 1: `A P L S I I G`
- Row 2: `Y   I   R`

Reading rows from top to bottom gives:
- `PAHN`
- `APLSIIG`
- `YIR`

Final answer: `PAHNAPLSIIGYIR`

### Final Complexity
- Time: `O(n)`, where `n` is the length of `s`.
- Space: `O(n)` for the row buffers and the final result.