# 155 - Min Stack

**Difficulty**: Medium
**LeetCode**: https://leetcode.com/problems/min-stack/

---

### 1. Clarification & Edge Cases:
*   **Key Constraints:**
    *   The methods `push`, `pop`, `top`, and `getMin` must operate in constant time, O(1).
    *   The stack can store integer values, including negative numbers and duplicates.
    *   The number of calls to the methods will be within a certain range (e.g., up to 10^4).
*   **Potential Edge Cases:**
    *   Calling `pop`, `top`, or `getMin` on an empty stack.
    *   Pushing and popping multiple elements, including duplicates of the minimum value.
    *   The sequence of operations can be mixed, e.g., push, push, pop, getMin, top, etc.

### 2. High-Level Approach Analysis (Trade-offs):

*   **Brute Force Approach:**
    *   Use a standard stack (or an `ArrayList`).
    *   For `push`, `pop`, and `top`, the operations are the standard O(1) stack operations.
    *   For `getMin`, we would have to iterate through all the elements in the stack to find the minimum one. This would take O(n) time, where n is the number of elements in the stack. This approach does not meet the O(1) time requirement for `getMin`.

*   **Optimized Approach (Two Stacks):**
    *   Use two stacks. One main stack to store all the values, and a second "min-stack" to keep track of the current minimum value at each stage.
    *   **`push(val)`**: Push `val` onto the main stack. If the min-stack is empty or `val` is less than or equal to the top of the min-stack, push `val` onto the min-stack as well.
    *   **`pop()`**: Pop from the main stack. If the popped value is equal to the top of the min-stack, pop from the min-stack as well.
    *   **`top()`**: Return the top of the main stack.
    *   **`getMin()`**: Return the top of the min-stack.

*   **Comparison:**
    *   **Time Complexity:** The optimized approach achieves O(1) for all operations (`push`, `pop`, `top`, `getMin`), which is a significant improvement over the brute-force `getMin`'s O(n) complexity.
    *   **Space Complexity:** The brute force approach uses O(n) space for the stack. The optimized approach uses O(n) for the main stack and, in the worst case (e.g., pushing a strictly decreasing sequence of numbers), O(n) for the min-stack, resulting in a total of O(2n) which simplifies to O(n). While it uses more space, it meets the critical time complexity requirements.

### 3. Algorithm Design:

The logic of the optimized solution is to maintain a second stack that only stores the minimum values encountered so far.

1.  **Data Structures:**
    *   `stack`: An `ArrayList` (acting as a stack) to store the actual sequence of numbers pushed.
    *   `mins`: Another `ArrayList` (acting as a stack) to store the minimum values. The top of this stack (`mins.last()`) will always be the minimum element in the current `stack`.

2.  **Step-by-step Logic:**
    *   **`push(val)`**:
        1.  Add the new value `val` to the `stack`.
        2.  Check if `mins` is empty or if `val` is less than or equal to the current minimum (`mins.last()`).
        3.  If it is, this new value is a new minimum (or a duplicate of the minimum), so add it to `mins`. This ensures that if we pop a minimum, we know what the *previous* minimum was.
    *   **`pop()`**:
        1.  Check if the value being popped from `stack` is the current minimum (i.e., `stack.last() == mins.last()`).
        2.  If it is, we must also remove it from `mins` to expose the previous minimum.
        3.  Finally, remove the element from `stack`.
    *   **`top()`**:
        1.  Return the last element of `stack` without removing it.
    *   **`getMin()`**:
        1.  Return the last element of `mins` without removing it. This is our constant-time minimum retrieval.

### 4. Production-Ready Implementation:

```kotlin
package `155-min-stack`

class MinStack {
    // Main stack to store all elements
    private val stack = ArrayList<Int>()
    // A secondary stack to keep track of the minimum values
    private val minStack = ArrayList<Int>()

    /**
     * Pushes an element onto the stack.
     * Time Complexity: O(1)
     */
    fun push(`val`: Int) {
        stack.add(`val`)
        // If the minStack is empty or the new value is a new minimum,
        // add it to the minStack.
        if (minStack.isEmpty() || `val` <= minStack.last()) {
            minStack.add(`val`)
        }
    }

    /**
     * Removes the element on the top of the stack.
     * Time Complexity: O(1)
     */
    fun pop() {
        // Guard clause for an empty stack
        if (stack.isEmpty()) {
            return
        }

        // If the element being popped is the current minimum,
        // it must also be popped from the minStack to reveal the previous minimum.
        if (stack.last() == minStack.last()) {
            minStack.removeAt(minStack.lastIndex)
        }
        stack.removeAt(stack.lastIndex)
    }

    /**
     * Gets the top element of the stack.
     * Time Complexity: O(1)
     */
    fun top(): Int {
        // Assuming as per LeetCode problem constraints that top() will not be called on an empty stack.
        // For production code, an exception or nullable return would be better.
        return stack.last()
    }

    /**
     * Retrieves the minimum element in the stack.
     * Time Complexity: O(1)
     */
    fun getMin(): Int {
        // Assuming as per LeetCode problem constraints that getMin() will not be called on an empty stack.
        return minStack.last()
    }
}
```

### 5. Verification & Complexity Finalization:

*   **Dry Run Example:**
    Let's trace the state of the stacks with the following operations:
    1.  `push(-2)`: `stack` = `[-2]`, `minStack` = `[-2]`
    2.  `push(0)`: `stack` = `[-2, 0]`, `minStack` = `[-2]` (0 is not <= -2)
    3.  `push(-3)`: `stack` = `[-2, 0, -3]`, `minStack` = `[-2, -3]` (-3 is <= -2)
    4.  `getMin()`: returns `minStack.last()` -> **-3**
    5.  `pop()`: `stack.last()` is -3, which equals `minStack.last()`.
        *   `stack` becomes `[-2, 0]`
        *   `minStack` becomes `[-2]`
    6.  `top()`: returns `stack.last()` -> **0**
    7.  `getMin()`: returns `minStack.last()` -> **-2**

    The logic holds and correctly tracks the minimum value.

*   **Final Complexity:**
    *   **Time Complexity:** O(1) for `push`, `pop`, `top`, and `getMin`. Each operation involves a constant number of actions on the `ArrayLists` (adding/removing from the end).
    *   **Space Complexity:** O(n), where n is the number of elements in the stack. In the worst-case scenario (pushing elements in decreasing order), the `minStack` will also store n elements.

