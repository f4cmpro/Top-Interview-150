# 224 - Basic Calculator

**Difficulty**: Hard
**LeetCode**: https://leetcode.com/problems/basic-calculator/

### 1. Clarification & Edge Cases:
*   **Input:** A string `s` representing a valid mathematical expression.
*   **Characters:** The string contains digits, `'+'`, `'-'`, `'('`, `')'`, and `' '`.
*   **Numbers:** Integers are non-negative.
*   **Operators:** Only addition (`+`) and subtraction (`-`) are included.
*   **Parentheses:** The expression can have nested parentheses.
*   **Constraints:** The input expression is always valid. `1 <= s.length <= 3 * 10^5`. The numbers and intermediate results will fit in a 32-bit signed integer.

*   **Edge Cases to consider:**
    *   An expression with a single number (e.g., `"5"`).
    *   An expression with only spaces (e.g., `"   "`).
    *   Expressions with leading/trailing/multiple spaces between tokens (e.g., `" 1 +   2 "`).
    *   Expressions involving parentheses (e.g., `"(1+(4+5+2)-3)+(6+8)"`).
    *   Unary minus, especially at the beginning or after an opening parenthesis (e.g., `"-2+ 1"` or `"(3 - -2)"`). The problem statement implies non-negative integers, but expressions like `(5-8)` result in a negative intermediate value. We must handle the signs correctly.
    *   Complex nested parentheses (e.g., `"1 - (2 - (3 - 4))"`).

### 2. High-Level Approach Analysis (Trade-offs):

*   **Brute Force (Recursive Approach):**
    A naive approach would be to use recursion. When we encounter an opening parenthesis, we can make a recursive call to evaluate the sub-expression within the parentheses. We would need to parse the string to find the matching closing parenthesis. This approach can become complex to manage, especially with tracking the current position in the string across recursive calls and handling signs.

*   **Optimized Approach (Stack-based Calculation):**
    A more robust and standard approach for expression evaluation is to use a stack. We can iterate through the expression string and use a stack to manage the state of the calculation, particularly when dealing with parentheses. We maintain a running `result` and a `sign`. When we see a number, we add it to the `result` (multiplied by the current `sign`). When we see a `+` or `-`, we update the `sign`. When we encounter an opening parenthesis `(`, we push the current `result` and `sign` onto the stack and reset them for the sub-expression. When we see a closing parenthesis `)`, we pop the previous `result` and `sign` from the stack and combine them with the result of the sub-expression.

*   **Comparison:**
    *   **Time Complexity:** Both approaches would ideally be O(N), where N is the length of the string, as we process each character once. However, the recursive approach might involve more overhead due to function calls and string slicing/substring operations, potentially leading to a worse practical performance. The stack-based approach is a clean, single-pass O(N) solution.
    *   **Space Complexity:** The recursive approach has a space complexity of O(N) in the worst case due to the recursion depth (e.g., for deeply nested parentheses). The stack-based approach also has a space complexity of O(N) in the worst case, as the stack can grow up to the depth of nested parentheses.
    *   **Why Optimized is Better:** The stack-based approach is more idiomatic for this type of problem. It's iterative, which avoids potential stack overflow issues with very deep recursion, and it's generally easier to reason about and implement correctly than a complex recursive parser. It elegantly handles the hierarchy of operations imposed by parentheses.

### 3. Algorithm Design:
The optimized algorithm uses one stack and several variables to track the state of the calculation.

1.  Initialize `result = 0`, `sign = 1` (for positive), and a `stack` of integers.
2.  Iterate through the input string `s` character by character.
3.  For each character:
    *   If it's a **digit**:
        *   Parse the full number (it could have multiple digits).
        *   Add this number to the `result`, multiplied by the current `sign`. `result += sign * current_number`.
    *   If it's a **'+'**:
        *   Set `sign = 1`.
    *   If it's a **'-'**:
        *   Set `sign = -1`.
    *   If it's an **'('**:
        *   This is the start of a new sub-expression. We need to save the `result` and `sign` calculated so far.
        *   Push the current `result` onto the stack.
        *   Push the current `sign` onto the stack.
        *   Reset `result = 0` and `sign = 1` for the new sub-expression.
    *   If it's a **')'**:
        *   This is the end of a sub-expression. The current `result` is the value of this sub-expression.
        *   Pop the `sign` from the stack that was active before the sub-expression.
        *   Pop the `result` from the stack that was accumulated before the sub-expression.
        *   Update the main `result`: `result = previous_result + (previous_sign * current_result)`.
    *   If it's a **space**, ignore it.
4.  After the loop finishes, the final `result` is the answer.

**Data Structures Used:**
*   **Stack:** A `java.util.Stack` (or equivalent) is chosen to manage the nested scopes of calculations created by parentheses. It follows a Last-In-First-Out (LIFO) order, which is perfect for resolving nested expressions—the innermost expression is evaluated first, and its result is used in the containing expression.

### 4. Production-Ready Implementation:
```kotlin
import java.util.Stack

class Solution {
    /**
     * Calculates the result of a string expression using a stack.
     *
     * Time Complexity: O(N), where N is the length of the string. We do a single pass.
     * Space Complexity: O(N), where N is the nesting depth of parentheses.
     */
    fun calculate(s: String): Int {
        // Stack to store results and signs for parenthesized expressions
        val stack = Stack<Int>()
        var result = 0
        var sign = 1 // 1 for '+', -1 for '-'
        var i = 0

        while (i < s.length) {
            val char = s[i]
            when {
                char.isDigit() -> {
                    var num = 0
                    var j = i
                    // Parse the full number which may have multiple digits
                    while (j < s.length && s[j].isDigit()) {
                        num = num * 10 + (s[j] - '0')
                        j++
                    }
                    result += sign * num
                    i = j - 1 // Move pointer to the last digit of the number
                }
                char == '+' -> {
                    sign = 1
                }
                char == '-' -> {
                    sign = -1
                }
                char == '(' -> {
                    // Start of a new sub-expression, save current state
                    stack.push(result)
                    stack.push(sign)
                    // Reset state for the sub-expression
                    result = 0
                    sign = 1
                }
                char == ')' -> {
                    // End of a sub-expression, calculate its value and merge
                    val prevSign = stack.pop()
                    val prevResult = stack.pop()
                    result = prevResult + prevSign * result
                }
                // Ignore spaces
            }
            i++
        }
        return result
    }
}
```

### 5. Verification & Complexity Finalization:

*   **Dry Run with `s = "(1+(4+5)-3)"`:**
    1.  `i=0, char='('`: `stack.push(0)`, `stack.push(1)`. `result=0`, `sign=1`. `stack: [0, 1]`
    2.  `i=1, char='1'`: `result = 0 + 1 * 1 = 1`.
    3.  `i=2, char='+'`: `sign = 1`.
    4.  `i=3, char='('`: `stack.push(1)`, `stack.push(1)`. `result=0`, `sign=1`. `stack: [0, 1, 1, 1]`
    5.  `i=4, char='4'`: `result = 0 + 1 * 4 = 4`.
    6.  `i=5, char='+'`: `sign = 1`.
    7.  `i=6, char='5'`: `result = 4 + 1 * 5 = 9`.
    8.  `i=7, char=')'`: `prevSign=stack.pop()=1`, `prevResult=stack.pop()=1`. `result = 1 + 1 * 9 = 10`. `stack: [0, 1]`
    9.  `i=8, char='-'`: `sign = -1`.
    10. `i=9, char='3'`: `result = 10 + (-1 * 3) = 7`.
    11. `i=10, char=')'`: `prevSign=stack.pop()=1`, `prevResult=stack.pop()=0`. `result = 0 + 1 * 7 = 7`. `stack: []`
    12. Loop ends. Return `result = 7`. Correct.

*   **Final Time Complexity:** **O(N)**, where N is the length of the input string `s`. We iterate through the string once.
*   **Final Space Complexity:** **O(D)**, where D is the maximum nesting depth of the parentheses. In the worst case, like `((((...))))`, the depth can be proportional to N, so the complexity is **O(N)**.
