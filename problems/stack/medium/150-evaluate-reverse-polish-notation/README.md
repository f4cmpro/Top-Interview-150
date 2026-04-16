# 150 - Evaluate Reverse Polish Notation

**Difficulty**: Medium
**LeetCode**: https://leetcode.com/problems/evaluate-reverse-polish-notation/

### 1. Clarification & Edge Cases:
*   **Input:** An array of strings `tokens` representing an RPN expression.
*   **Output:** An integer representing the result of the expression.
*   **Constraints:**
    *   `1 <= tokens.length <= 10^4`
    *   `tokens[i]` is either an operator (`"+"`, `"-"`, `"`*`"`, `"/"`) or an integer in the range `[-200, 200]`.
    *   The given RPN expression is always valid. This means there will not be any division by zero or insufficient operands for an operator.
*   **Edge Cases:**
    *   **Single Number:** The input array contains just one number (e.g., `["5"]`). The result should be that number.
    *   **Negative Numbers:** Operands can be negative.
    *   **Order of Operands:** For subtraction and division, the order matters. The first operand popped is the right-hand side (e.g., `op2 - op1`).

### 2. High-Level Approach Analysis (Trade-offs):
*   **Brute Force Approach:** A brute-force approach is not straightforward for this problem. One could try to convert the RPN expression to an infix expression and then evaluate it, but this is overly complex and inefficient. The direct evaluation of RPN is already the most natural and efficient method.

*   **Optimized Approach (Stack-based):** The standard and optimal solution for evaluating RPN is to use a stack. We iterate through the tokens. If a token is a number, we push it onto the stack. If it's an operator, we pop the top two numbers from the stack, perform the operation, and push the result back.

*   **Comparison:**
    *   **Time Complexity:** The stack-based approach is **O(n)** because we process each token in the input array exactly once.
    *   **Space Complexity:** The space complexity is **O(n)** in the worst case. For an expression like `["2", "3", "4", "5", "+", "+", "+"]`, the stack will hold all the numbers before the operators are processed.

The stack-based approach is superior because it's a direct, single-pass algorithm that perfectly matches the structure of Reverse Polish Notation, making it highly efficient.

### 3. Algorithm Design:
1.  Initialize an empty `Stack` to store integers.
2.  Iterate through each `token` in the input `tokens` array.
3.  For each `token`:
    *   Check if it is an operator (`+`, `-`, `*`, `/`).
    *   **If it is an operator:**
        *   Pop the top element from the stack. This is the second operand (`operand2`).
        *   Pop the new top element from the stack. This is the first operand (`operand1`).
        *   Perform the operation: `operand1 operator operand2`.
        *   Push the result back onto the stack.
    *   **If it is not an operator:**
        *   It must be a number. Convert the `token` string to an integer.
        *   Push the integer onto the stack.
4.  After the loop finishes, the stack will contain a single number, which is the final result of the expression.
5.  Pop this result from the stack and return it.

**Data Structure Choice:**
*   A **Stack** is the ideal data structure because RPN evaluation relies on the Last-In, First-Out (LIFO) principle. Operands are stored and then retrieved in the reverse order they were encountered to be used with the next operator.

### 4. Production-Ready Implementation:
```kotlin
package `150-evaluate-reverse-polish-notation`

import java.util.Stack

class Solution {
    fun evalRPN(tokens: Array<String>): Int {
        // Guard Clause: Although the problem statement guarantees a valid expression,
        // it's good practice to handle empty or single-element cases.
        if (tokens.size == 1) {
            return tokens[0].toInt()
        }

        val stack = Stack<Int>()
        for (token in tokens) {
            when (token) {
                "+" -> {
                    val operand2 = stack.pop()
                    val operand1 = stack.pop()
                    stack.push(operand1 + operand2)
                }
                "-" -> {
                    val operand2 = stack.pop()
                    val operand1 = stack.pop()
                    stack.push(operand1 - operand2)
                }
                "*" -> {
                    val operand2 = stack.pop()
                    val operand1 = stack.pop()
                    stack.push(operand1 * operand2)
                }
                "/" -> {
                    val operand2 = stack.pop()
                    val operand1 = stack.pop()
                    stack.push(operand1 / operand2)
                }
                else -> {
                    // If the token is not an operator, it's a number.
                    stack.push(token.toInt())
                }
            }
        }
        // The final result is the last remaining element on the stack.
        return stack.pop()
    }
}
```

### 5. Verification & Complexity Finalization:
*   **Dry Run:**
    *   **Input:** `tokens = ["2", "1", "+", "3", "*"]`
    1.  `token = "2"`: Push `2`. Stack: `[2]`
    2.  `token = "1"`: Push `1`. Stack: `[2, 1]`
    3.  `token = "+"`: Pop `1` (op2), pop `2` (op1). Calculate `2 + 1 = 3`. Push `3`. Stack: `[3]`
    4.  `token = "3"`: Push `3`. Stack: `[3, 3]`
    5.  `token = "*"`: Pop `3` (op2), pop `3` (op1). Calculate `3 * 3 = 9`. Push `9`. Stack: `[9]`
    6.  Loop ends. Pop `9` and return it.
    *   **Result:** `9`. The logic is correct.

*   **Final Complexity:**
    *   **Time Complexity:** **O(n)**, where `n` is the number of tokens. We iterate through the array once.
    *   **Space Complexity:** **O(n)**. In the worst-case scenario (many operands followed by operators), the stack could hold a number of elements proportional to `n`. For example, `["1", "2", "3", ..., "n", "+", "+", ..., "+"]`.
