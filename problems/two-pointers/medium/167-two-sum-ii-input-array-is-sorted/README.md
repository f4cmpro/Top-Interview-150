# Solution Design - Two Sum II (Problem 167)

## 🎯 Problem Overview

**Problem**: Find two numbers in a sorted array that sum to a target value.  
**Key Constraint**: Must use only O(1) extra space.  
**Input**: Sorted array (non-decreasing order) and a target integer.  
**Output**: 1-indexed positions of the two numbers.

---

## 🧠 Solution Strategy

### Chosen Approach: Two Pointers

**Why Two Pointers?**
1. ✅ Array is already sorted - we can leverage this property
2. ✅ O(1) space requirement - two pointers only use constant space
3. ✅ O(n) time complexity - single pass through the array
4. ✅ Simple and intuitive logic

### Alternative Approaches (Rejected)

| Approach | Time | Space | Why Rejected |
|----------|------|-------|--------------|
| **Brute Force** (nested loops) | O(n²) | O(1) | Too slow, doesn't use sorted property |
| **Binary Search** | O(n log n) | O(1) | More complex, slower than two pointers |
| **HashMap** | O(n) | O(n) | Violates O(1) space constraint |

---

## 📐 Algorithm Design

### High-Level Flow

```
START
  ↓
Initialize left = 0, right = n-1
  ↓
While left < right:
  ↓
  Calculate sum = numbers[left] + numbers[right]
  ↓
  ┌─────────────┬─────────────┬─────────────┐
  │ sum > target│ sum < target│ sum == target│
  │      ↓      │      ↓      │      ↓      │
  │  right--    │   left++    │   RETURN    │
  └─────────────┴─────────────┴─────────────┘
  ↓
END
```

### Step-by-Step Walkthrough

**Example**: `numbers = [2, 7, 11, 15]`, `target = 9`

```
Step 1: left=0, right=3
        [2, 7, 11, 15]
         ↑          ↑
        sum = 2 + 15 = 17 > 9 → move right left

Step 2: left=0, right=2
        [2, 7, 11, 15]
         ↑      ↑
        sum = 2 + 11 = 13 > 9 → move right left

Step 3: left=0, right=1
        [2, 7, 11, 15]
         ↑  ↑
        sum = 2 + 7 = 9 == 9 → FOUND!
        return [1, 2] (1-indexed)
```

### Pseudocode

```
FUNCTION twoSum(numbers, target):
    left ← 0
    right ← numbers.length - 1
    
    WHILE left < right:
        sum ← numbers[left] + numbers[right]
        
        IF sum > target THEN
            right ← right - 1
        ELSE IF sum < target THEN
            left ← left + 1
        ELSE
            RETURN [left + 1, right + 1]  // Convert to 1-indexed
        END IF
    END WHILE
    
    // Should never reach here due to problem guarantee
    RETURN [left + 1, right + 1]
END FUNCTION
```

---

## 💡 Key Insights

### Why This Algorithm Works

1. **Sorted Array Property**:
    - Moving `left` pointer right → increases sum
    - Moving `right` pointer left → decreases sum

2. **Convergence Guarantee**:
    - We always move closer to the target
    - Pointers eventually meet at the solution

3. **No Missed Solutions**:
    - If we move `right` left (sum too large), we'll never need that large value again
    - If we move `left` right (sum too small), we'll never need that small value again
    - This ensures we don't skip over the correct pair

### Mathematical Proof Sketch

**Claim**: The algorithm finds the solution if it exists.

**Proof by contradiction**:
- Assume the solution is at indices `(i, j)` where `i < j`
- Suppose our algorithm passes through a state where `left < i` or `right > j`
- If `left < i` and we skip to `left+1`, then `numbers[left] + numbers[right]` must be `< target`
    - But for the solution, `numbers[i] + numbers[j] = target`
    - Since array is sorted and `i > left`, this is consistent
- Similar logic for `right > j`
- Therefore, the algorithm will eventually reach `left = i` and `right = j`

---

## 🔧 Implementation Details

### Kotlin Implementation

```kotlin
package problems.two_pointers

class Solution {
    fun twoSum(numbers: IntArray, target: Int): IntArray {
        var left = 0
        var right = numbers.size - 1
        
        while (left < right) {
            val sum = numbers[left] + numbers[right]
            when {
                sum > target -> right--
                sum < target -> left++
                else -> return intArrayOf(left + 1, right + 1)
            }
        }
        
        // Should never reach here due to problem guarantee
        return intArrayOf(left + 1, right + 1)
    }
}
```

### Key Implementation Choices

1. **Using `when` expression**: More idiomatic Kotlin than if-else chain
2. **Inline sum calculation**: Could cache, but array access is O(1)
3. **Loop condition `left < right`**: Ensures we don't use same element twice
4. **Return in the else branch**: Cleaner than breaking and returning outside
5. **Final return statement**: Satisfies compiler, though unreachable

---

## 📊 Complexity Analysis

### Time Complexity: **O(n)**

**Analysis**:
- Each iteration moves at least one pointer
- Pointers start `n` positions apart
- Maximum iterations: `n` (when pointers meet)
- Each iteration does O(1) work
- **Total**: O(n)

**Best Case**: O(1) - solution at boundaries  
**Average Case**: O(n/2) = O(n)  
**Worst Case**: O(n) - solution in middle or near middle

### Space Complexity: **O(1)**

**Analysis**:
- Variables used: `left`, `right`, `sum` (in loop)
- Result array: Fixed size (2 elements)
- No recursion, no auxiliary data structures
- **Total**: O(1)

---

## 🧪 Testing Strategy

### Test Categories

1. **Basic Functionality**
    - Normal cases with positive numbers
    - Solution in different positions

2. **Edge Cases**
    - Minimum size array (n=2)
    - Solution at boundaries
    - Large arrays (up to 30,000 elements)

3. **Special Values**
    - Negative numbers
    - Zero in array
    - Negative target
    - Large numbers (±1000)

### Test Cases

```kotlin
// Test 1: Basic case
Input:  numbers = [2,7,11,15], target = 9
Output: [1,2]
Reason: Standard case from problem

// Test 2: Minimum size
Input:  numbers = [2,3], target = 5
Output: [1,2]
Reason: Smallest possible array

// Test 3: Solution at end
Input:  numbers = [1,2,3,4,5], target = 9
Output: [4,5]
Reason: Tests right boundary

// Test 4: Negative numbers
Input:  numbers = [-5,-3,0,2,4], target = -3
Output: [2,5]
Reason: Tests negative values

// Test 5: Negative target
Input:  numbers = [-10,-5,-2,0,3], target = -15
Output: [1,2]
Reason: Tests negative target

// Test 6: Zero sum
Input:  numbers = [-3,-1,0,2,3], target = 0
Output: [2,5]
Reason: Tests zero as target
```

---

## ⚠️ Edge Cases & Pitfalls

### Common Mistakes

1. **Index Conversion Error**
   ```kotlin
   // ❌ Wrong: Forgot to add 1
   return intArrayOf(left, right)
   
   // ✅ Correct: Convert to 1-indexed
   return intArrayOf(left + 1, right + 1)
   ```

2. **Loop Condition Error**
   ```kotlin
   // ❌ Wrong: Would allow same element twice
   while (left <= right)
   
   // ✅ Correct: Ensures distinct elements
   while (left < right)
   ```

3. **Premature Optimization**
   ```kotlin
   // ❌ Unnecessary: Problem guarantees solution exists
   if (left >= right) return intArrayOf(-1, -1)
   ```

### Edge Cases Handled

- ✅ Array size = 2 (minimum)
- ✅ Solution uses first element
- ✅ Solution uses last element
- ✅ Negative numbers
- ✅ Negative target
- ✅ Zero in array

---

## 🔄 Comparison with Related Problems

### Two Sum I vs Two Sum II

| Aspect | Two Sum I | Two Sum II (This) |
|--------|-----------|-------------------|
| Array | Unsorted | **Sorted** |
| Optimal Approach | HashMap | **Two Pointers** |
| Time Complexity | O(n) | **O(n)** |
| Space Complexity | O(n) | **O(1)** ✓ |
| Indexing | 0-indexed | **1-indexed** |

### When to Use Two Pointers

✅ **Use when**:
- Array/string is sorted
- Need to find pairs with specific properties
- Space constraint (O(1) required)
- Linear time acceptable

❌ **Don't use when**:
- Array is unsorted (HashMap might be better)
- Need to find more than pairs without additional passes
- Random access pattern needed

---

## 📚 Key Learnings

### Algorithmic Patterns

1. **Two Pointers on Sorted Array**: Classic pattern for pair finding
2. **Greedy Pointer Movement**: Each decision is locally optimal
3. **Space-Time Tradeoff**: Sacrificed HashMap O(n) space for two-pointer O(1) space

### Kotlin-Specific

1. **`when` expression**: More elegant than multiple if-else
2. **`IntArray`**: Primitive array for performance
3. **`var` for mutable pointers**: `left` and `right` need to change

### Problem-Solving Approach

1. Identify problem constraints (sorted, O(1) space)
2. Choose algorithm that leverages constraints
3. Verify correctness with walkthrough
4. Consider edge cases and boundary conditions
5. Test with diverse inputs

---

## 🎓 Conclusion

This solution demonstrates the power of the **two-pointer technique** on sorted arrays. By leveraging the sorted property, we achieve:
- ✅ Optimal O(n) time complexity
- ✅ Minimal O(1) space complexity
- ✅ Simple, readable code
- ✅ Handles all edge cases

The key insight is recognizing that in a sorted array, we can make intelligent decisions about which direction to move our pointers based on comparing the current sum with the target.

---

**Date Created**: January 2, 2026  
**Language**: Kotlin  
**Difficulty**: Medium  
**Status**: ✅ Completed

