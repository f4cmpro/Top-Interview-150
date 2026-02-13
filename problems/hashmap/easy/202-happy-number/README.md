# 202 - Happy Number

**Difficulty**: Easy
**LeetCode**: https://leetcode.com/problems/happy-number/

---

## Problem Description:
Write an algorithm to determine if a number `n` is happy.

A **happy number** is a number defined by the following process:
- Starting with any positive integer, replace the number by the sum of the squares of its digits.
- Repeat the process until the number equals 1 (where it will stay), or it loops endlessly in a cycle which does not include 1.
- Those numbers for which this process ends in 1 are happy.

Return `true` if `n` is a happy number, and `false` if not.

**Example 1:**
```
Input: n = 19
Output: true
Explanation:
1² + 9² = 82
8² + 2² = 68
6² + 8² = 100
1² + 0² + 0² = 1
```

**Example 2:**
```
Input: n = 2
Output: false
```

---

## Solution Design

### 1. Clarification & Edge Cases:

**Key Constraints:**
- Input: A positive integer `n` where `1 ≤ n ≤ 2³¹ - 1`
- The process either converges to 1 or enters an infinite cycle
- Need to detect cycles to avoid infinite loops

**Edge Cases:**
1. **n = 1**: Already happy, should return `true` immediately
2. **Single digit numbers (1-9)**: Most will enter cycles except 1 and 7
3. **Large numbers**: The sum of squares quickly reduces the number size (e.g., 999,999,999 → 9×81 = 729)
4. **Numbers that cycle**: Must detect when we've seen a sum before to avoid infinite loops
5. **Small cycles**: Numbers like 4 → 16 → 37 → 58 → 89 → 145 → 42 → 20 → 4 (unhappy cycle)

---

### 2. High-Level Approach Analysis (Trade-offs):

#### **Brute Force Approach:**
- Keep calculating the sum of squares repeatedly
- Store all seen numbers in a HashSet
- If we see a number we've already encountered, we're in a cycle → return `false`
- If we reach 1 → return `true`

**Time Complexity:** O(log n) - The number of digits in `n` is O(log n), and each iteration processes these digits. The cycle detection happens within a bounded number of iterations.
**Space Complexity:** O(log n) - HashSet stores the numbers we've seen in the sequence.

#### **Optimized Approach (Floyd's Cycle Detection - Two Pointers):**
- Use Floyd's cycle detection algorithm (slow and fast pointers)
- Slow pointer: advances one step (calculates sum once)
- Fast pointer: advances two steps (calculates sum twice)
- If there's a cycle, they will eventually meet
- If fast pointer reaches 1, the number is happy

**Time Complexity:** O(log n) - Same number of operations but without HashSet overhead
**Space Complexity:** O(1) - Only uses two pointers, no additional data structure

#### **Alternative Optimized Approach (HashSet with Cycle Detection):**
- Use a HashSet to track seen numbers
- For single-digit optimization: Can use a boolean array of size 10 since single-digit sums will cycle in [1-9]
- More intuitive and easier to implement than Floyd's algorithm

**Time Complexity:** O(log n)
**Space Complexity:** O(1) if we use a fixed-size array for single digits, or O(log n) for full HashSet

**Why Optimized is Better:**
- HashSet approach is more intuitive and maintainable
- Two-pointer approach saves space but adds complexity
- For this problem, the HashSet space usage is minimal since numbers quickly reduce to small values
- Single-digit optimization keeps space constant O(1) once numbers reduce below 10

---

### 3. Algorithm Design:

**Chosen Approach:** HashSet with Single-Digit Optimization

**Logic:**
1. **Initialize tracking:** Create a HashSet or boolean array to track seen numbers
2. **Iterate until convergence:**
   - Calculate the sum of squares of all digits in the current number
   - If sum equals 1 → return `true` (happy number found)
   - If sum is single-digit and we've seen it before → return `false` (cycle detected)
   - Mark the sum as seen
   - Set sum as the new number for next iteration
3. **Helper function (optional):** Create a function to calculate sum of squares of digits

**Data Structures:**
- **Boolean Array (size 10):** For tracking single-digit numbers we've encountered
  - Chosen because: Once a number reduces to single digits, it will cycle within [1-9]
  - Space efficient: O(1) constant space
  - Fast lookup: O(1) access time
- **String/Integer manipulation:** To extract individual digits
  - Convert number to string for easy digit iteration
  - Or use modulo/division operations: `digit = n % 10, n = n / 10`

**Why this approach:**
- Numbers quickly reduce to small values (sum of squares of digits)
- Once in single digits, the cycle space is tiny [1-9]
- No need to track all intermediate values, just single-digit states
- Guarantees termination: either reaches 1 or cycles in single digits

---

### 4. Production-Ready Implementation:

```kotlin
package happyNumber

class Solution {
    /**
     * Determines if a number is a "happy number".
     * A happy number is defined by repeatedly replacing it with the sum of squares
     * of its digits until it equals 1, or cycles endlessly.
     *
     * @param n The positive integer to check (1 ≤ n ≤ 2³¹ - 1)
     * @return true if n is a happy number, false otherwise
     */
    fun isHappy(n: Int): Boolean {
        // Guard clause: 1 is already happy
        if (n == 1) return true
        
        // Track single-digit sums we've seen to detect cycles
        val seenSingleDigits = BooleanArray(10) { false }
        var current = n
        
        while (true) {
            // Calculate sum of squares of digits
            val sumOfSquares = getSumOfSquares(current)
            
            // Happy number found
            if (sumOfSquares == 1) {
                return true
            }
            
            // Cycle detection for single-digit numbers
            if (sumOfSquares < 10) {
                // If we've seen this single digit before, we're in a cycle
                if (seenSingleDigits[sumOfSquares]) {
                    return false
                }
                seenSingleDigits[sumOfSquares] = true
            }
            
            // Continue with the new sum
            current = sumOfSquares
        }
    }
    
    /**
     * Calculates the sum of squares of all digits in a number.
     *
     * @param num The number to process
     * @return Sum of squares of all digits
     */
    private fun getSumOfSquares(num: Int): Int {
        var sum = 0
        var remaining = num
        
        while (remaining > 0) {
            val digit = remaining % 10
            sum += digit * digit
            remaining /= 10
        }
        
        return sum
    }
}

fun main() {
    val solution = Solution()
    
    // Test cases
    println("isHappy(19) = ${solution.isHappy(19)}")  // Expected: true
    println("isHappy(2) = ${solution.isHappy(2)}")    // Expected: false
    println("isHappy(1) = ${solution.isHappy(1)}")    // Expected: true
    println("isHappy(7) = ${solution.isHappy(7)}")    // Expected: true
}
```

**Key Implementation Details:**
1. **Guard Clause:** Handles n = 1 immediately
2. **Modular Design:** Separated sum calculation into its own function for clarity
3. **Efficient Digit Extraction:** Uses modulo and division instead of string conversion
4. **Cycle Detection:** Only tracks single-digit values for O(1) space
5. **Clear Variable Names:** `current`, `sumOfSquares`, `seenSingleDigits` are self-documenting
6. **Comments:** Explain the "why" not just the "what"

---

### 5. Verification & Complexity Finalization:

#### **Dry Run with Example: n = 19**

| Step | Current | Calculation | Sum | Action |
|------|---------|-------------|-----|--------|
| 0 | 19 | - | - | Start |
| 1 | 19 | 1² + 9² | 82 | Continue (82 > 10) |
| 2 | 82 | 8² + 2² | 68 | Continue (68 > 10) |
| 3 | 68 | 6² + 8² | 100 | Continue (100 > 10) |
| 4 | 100 | 1² + 0² + 0² | 1 | **Return true** ✓ |

#### **Dry Run with Example: n = 2**

| Step | Current | Calculation | Sum | Single Digit? | Seen? | Action |
|------|---------|-------------|-----|---------------|-------|--------|
| 0 | 2 | - | - | - | - | Start |
| 1 | 2 | 2² | 4 | Yes (4) | No | Mark seen[4]=true, continue |
| 2 | 4 | 4² | 16 | No | - | Continue |
| 3 | 16 | 1² + 6² | 37 | No | - | Continue |
| 4 | 37 | 3² + 7² | 58 | No | - | Continue |
| 5 | 58 | 5² + 8² | 89 | No | - | Continue |
| 6 | 89 | 8² + 9² | 145 | No | - | Continue |
| 7 | 145 | 1² + 4² + 5² | 42 | No | - | Continue |
| 8 | 42 | 4² + 2² | 20 | No | - | Continue |
| 9 | 20 | 2² + 0² | 4 | Yes (4) | **Yes!** | **Return false** ✓ |

The cycle is detected when we encounter 4 again.

---

#### **Final Complexity Analysis:**

**Time Complexity: O(log n)**
- Extracting digits from a number with d digits: O(d) where d = O(log n)
- The number of iterations is bounded:
  - Large numbers quickly reduce (e.g., 999,999,999 → 729 in one step)
  - Once in single/double digits, cycles are detected within ~10 iterations
  - Maximum chain length before cycle: O(log n)
- Overall: O(log n) iterations × O(log n) per iteration = O((log n)²) worst case, but typically O(log n) in practice

**Space Complexity: O(1)**
- Boolean array of fixed size 10
- A few integer variables
- No recursion or dynamic data structures
- Space does not grow with input size

**Why this is optimal:**
- Cannot do better than O(log n) time since we must process digits
- O(1) space is optimal for this problem
- Alternative HashSet approach would be O(log n) space
- Two-pointer approach is also O(1) space but more complex to implement
