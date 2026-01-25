# 48 - Rotate Image

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/rotate-image/

---

## Problem Description:
You are given an `n x n` 2D matrix representing an image, rotate the image by 90 degrees (clockwise).

You have to rotate the image **in-place**, which means you have to modify the input 2D matrix directly. **DO NOT** allocate another 2D matrix and do the rotation.

**Example 1:**
```
Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
Output: [[7,4,1],[8,5,2],[9,6,3]]
```

**Example 2:**
```
Input: matrix = [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
Output: [[15,13,2,5],[14,3,4,1],[12,6,8,9],[16,7,10,11]]
```

**Constraints:**
- `n == matrix.length == matrix[i].length`
- `1 <= n <= 20`
- `-1000 <= matrix[i][j] <= 1000`

---

## Solution Design

### 1. Clarification & Edge Cases:

**Key Constraints:**
- The matrix is always square (`n x n`)
- Must be done **in-place** (cannot allocate new matrix)
- Matrix size ranges from 1×1 to 20×20
- Elements can be negative, zero, or positive integers

**Edge Cases:**
1. **Single element matrix** (`n = 1`): No rotation needed, matrix stays the same
2. **2×2 matrix**: Simplest non-trivial case requiring rotation
3. **Odd-sized matrices** (e.g., 3×3, 5×5): Center element stays in place
4. **Even-sized matrices** (e.g., 2×2, 4×4): No center element
5. **Negative numbers**: Should be handled the same as positive numbers
6. **All identical elements**: Rotation still must occur structurally

---

### 2. High-Level Approach Analysis (Trade-offs):

#### **Brute Force Approach:**
- **Method**: Create a new `n x n` matrix and copy elements to their rotated positions
- **Logic**: For each element at position `[i][j]`, place it at position `[j][n-1-i]` in the new matrix
- **Time Complexity**: O(n²) - must visit every element once
- **Space Complexity**: O(n²) - requires auxiliary matrix of same size
- **Drawback**: Violates the in-place requirement

#### **Optimized Approach (Layer-by-Layer Rotation):**
- **Method**: Rotate the matrix in concentric layers from outside to inside, performing 4-way swaps
- **Logic**: 
  - Process layers from outermost to innermost (`n/2` layers total)
  - For each layer, rotate groups of 4 elements at a time using cyclic swaps
  - Move top → right, right → bottom, bottom → left, left → top
- **Time Complexity**: O(n²) - still must process every element
- **Space Complexity**: O(1) - only uses a single temporary variable for swapping
- **Advantage**: Meets the in-place requirement with constant extra space

**Why Optimized is Better:**
While both approaches have the same time complexity, the optimized approach uses **O(1) space** instead of **O(n²)**, making it far more memory-efficient and meeting the problem's in-place constraint.

---

### 3. Algorithm Design:

**Core Strategy: Layer-by-Layer Rotation with 4-Way Cyclic Swaps**

**Step-by-Step Logic:**

1. **Determine number of layers**: For an `n x n` matrix, there are `n/2` concentric layers to process
   - Outer layer: `layer = 0`
   - Inner layers: `layer = 1, 2, ...` until `layer < n/2`

2. **For each layer:**
   - Calculate the number of elements to rotate in this layer: `n - 2*layer - 1`
   - This represents how many groups of 4 elements need to be swapped

3. **For each position `i` in the current layer:**
   - Identify 4 corresponding positions that form a rotation cycle:
     - **Top edge (A)**: `[layer][layer + i]`
     - **Right edge (B)**: `[layer + i][n - 1 - layer]`
     - **Bottom edge (C)**: `[n - 1 - layer][n - 1 - layer - i]`
     - **Left edge (D)**: `[n - 1 - layer - i][layer]`

4. **Perform cyclic swap**: A ← D ← C ← B ← A
   - Save A to temp
   - A ← D (left moves to top)
   - D ← C (bottom moves to left)
   - C ← B (right moves to bottom)
   - B ← temp (top moves to right)

5. **Repeat** for all positions in all layers

**Data Structures Used:**
- **Input matrix itself**: Modified in-place (array of arrays)
- **Primitive variables**: `layer`, `i`, `temp`, `n` for iteration and temporary storage
- **Why**: No additional data structures needed since we're swapping in-place

**Key Insight:**
By processing concentric layers and using 4-way swaps, we rotate the matrix without needing extra space for a duplicate matrix.

---

### 4. Production-Ready Implementation:

```kotlin
package rotateimage

class Solution {
    fun rotate(matrix: Array<IntArray>): Unit {
        // Guard clause: handle 1x1 matrix (no rotation needed)
        val n = matrix.size
        if (n <= 1) return
        
        // Process each concentric layer from outside to inside
        for (layer in 0 until n / 2) {
            val first = layer
            val last = n - 1 - layer
            
            // Rotate elements in the current layer
            // Number of swaps needed: (last - first)
            for (i in 0 until last - first) {
                val offset = i
                
                // Save top element
                val top = matrix[first][first + offset]
                
                // Left -> Top
                matrix[first][first + offset] = matrix[last - offset][first]
                
                // Bottom -> Left
                matrix[last - offset][first] = matrix[last][last - offset]
                
                // Right -> Bottom
                matrix[last][last - offset] = matrix[first + offset][last]
                
                // Top -> Right (from saved temp)
                matrix[first + offset][last] = top
            }
        }
    }
}

fun main() {
    val solution = Solution()
    
    // Test Case 1: 3x3 matrix
    val matrix1 = arrayOf(
        intArrayOf(1, 2, 3),
        intArrayOf(4, 5, 6),
        intArrayOf(7, 8, 9)
    )
    println("Original matrix:")
    matrix1.forEach { row -> println(row.joinToString(" ")) }
    
    solution.rotate(matrix1)
    
    println("\nRotated matrix:")
    matrix1.forEach { row -> println(row.joinToString(" ")) }
    
    // Test Case 2: 4x4 matrix
    println("\n--- Test Case 2 ---")
    val matrix2 = arrayOf(
        intArrayOf(5, 1, 9, 11),
        intArrayOf(2, 4, 8, 10),
        intArrayOf(13, 3, 6, 7),
        intArrayOf(15, 14, 12, 16)
    )
    println("Original matrix:")
    matrix2.forEach { row -> println(row.joinToString(" ")) }
    
    solution.rotate(matrix2)
    
    println("\nRotated matrix:")
    matrix2.forEach { row -> println(row.joinToString(" ")) }
    
    // Test Case 3: Edge case - 1x1 matrix
    println("\n--- Test Case 3 (1x1) ---")
    val matrix3 = arrayOf(intArrayOf(1))
    println("Original: ${matrix3[0].joinToString(" ")}")
    solution.rotate(matrix3)
    println("Rotated: ${matrix3[0].joinToString(" ")}")
}
```

---

### 5. Verification & Complexity Finalization:

#### **Dry Run with Example: `[[1,2,3],[4,5,6],[7,8,9]]`**

**Initial State:**
```
1 2 3
4 5 6
7 8 9
```

**Execution:**
- `n = 3`, so we have `n/2 = 1` layer to process

**Layer 0:**
- `first = 0`, `last = 2`
- Need `last - first = 2` iterations

**Iteration i=0 (offset=0):**
- Positions: Top[0][0]=1, Right[0][2]=3, Bottom[2][2]=9, Left[2][0]=7
- Save top: `temp = 1`
- Left→Top: `matrix[0][0] = 7`
- Bottom→Left: `matrix[2][0] = 9`
- Right→Bottom: `matrix[2][2] = 3`
- Top→Right: `matrix[0][2] = 1`

After iteration 0:
```
7 2 1
4 5 6
9 8 3
```

**Iteration i=1 (offset=1):**
- Positions: Top[0][1]=2, Right[1][2]=6, Bottom[2][1]=8, Left[1][0]=4
- Save top: `temp = 2`
- Left→Top: `matrix[0][1] = 4`
- Bottom→Left: `matrix[1][0] = 8`
- Right→Bottom: `matrix[2][1] = 6`
- Top→Right: `matrix[1][2] = 2`

**Final State:**
```
7 4 1
8 5 2
9 6 3
```
✅ **Correct!** Matches expected output.

---

#### **Final Complexity Analysis:**

**Time Complexity: O(n²)**
- We process each element in the matrix exactly once
- Outer loop runs `n/2` times (layers)
- Inner loop runs a decreasing number of times, but total operations = n²/2 rotations
- Each rotation involves constant-time swaps
- Overall: O(n²)

**Space Complexity: O(1)**
- Only using a constant number of variables (`layer`, `first`, `last`, `i`, `offset`, `top`)
- No additional data structures that scale with input size
- Matrix is modified in-place
- Overall: O(1) auxiliary space

---

## Alternative Approach: Transpose + Reverse

Another elegant O(n²) time, O(1) space solution:
1. **Transpose** the matrix (swap `matrix[i][j]` with `matrix[j][i]`)
2. **Reverse** each row

This mathematically achieves a 90° clockwise rotation. While equally efficient, the layer-by-layer approach is more intuitive for understanding the rotation mechanics.
