# 2 - Add Two Numbers

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/add-two-numbers/

## Solution Design

### 1. Clarification & Edge Cases:
*   **Input:** Two nullable linked-lists, `l1` and `l2`, where each node contains a single digit (`0-9`). The digits are stored in **reverse order**.
*   **Output:** A new linked-list representing the sum of the two input numbers.
*   **Constraints:**
    *   The number of nodes in each list is between 1 and 100.
    *   `Node.val` is between 0 and 9.
*   **Edge Cases:**
    *   **Unequal Lengths:** One linked list is longer than the other.
    *   **Final Carry:** The sum results in a final carry digit that requires a new node at the end.
    *   **Null/Empty Inputs:** One or both of the lists could be null, which needs to be handled gracefully (treated as zero).
    *   **Large Numbers:** The numbers can have up to 100 digits, which will exceed the capacity of standard integer types like `Long`.

### 2. High-Level Approach Analysis (Trade-offs):
*   **Brute Force Approach:**
    1.  Traverse both linked lists to convert them into actual numbers (e.g., `Int` or `Long`).
    2.  Add the two numbers together.
    3.  Convert the resulting sum back into a new linked list.
    *   **Critique:** This approach is not viable. With up to 100 digits, the numbers can be far too large to fit into standard 64-bit `Long` data types, leading to overflow and incorrect results.

*   **Optimized Approach (Elementary Math Simulation):**
    1.  Initialize a `dummyHead` for the result list and a `current` pointer.
    2.  Initialize a `carry` variable to 0.
    3.  Iterate through both lists simultaneously, from head to tail, as long as there are nodes in `l1`, `l2`, or the `carry` is not zero.
    4.  In each iteration, sum the values of the current nodes from `l1` and `l2` (if they exist) and the `carry`.
    5.  The new digit for the result list is `sum % 10`.
    6.  The new `carry` is `sum / 10`.
    7.  Create a new node with the calculated digit and attach it to the result list.
    8.  Advance the pointers for `l1`, `l2`, and the `current` result pointer.
    9.  Return `dummyHead.next`.

*   **Comparison:**
    *   **Time Complexity:**
        *   Brute Force: `O(max(N, M))` for conversion + `O(L)` for creating the new list, where `L` is the number of digits in the sum. Roughly `O(max(N, M))`.
        *   Optimized: `O(max(N, M))`, where `N` and `M` are the lengths of `l1` and `l2`. We traverse each list once.
    *   **Space Complexity:**
        *   Brute Force: `O(L)` for the new list.
        *   Optimized: `O(max(N, M))` for the new result list.
    *   **Conclusion:** The optimized approach is vastly superior because it correctly handles arbitrarily large numbers without overflow, which is a hard blocker for the brute-force method.

### 3. Algorithm Design:
The logic mimics manual, grade-school addition from right to left. Since the lists are already in reverse, we can process them from the head.

1.  **Data Structures:**
    *   **`ListNode`:** Used for the input and output lists.
    *   **Three Pointers:** One for `l1`, one for `l2`, and one (`current`) to build the new result list.
    *   **`dummyHead`:** A sentinel node to simplify handling the start of the result list. The actual result will be `dummyHead.next`.
    *   **`carry` (Int):** An integer variable to hold the carry-over value (either 0 or 1) between iterations.

2.  **Step-by-Step Logic:**
    *   Initialize a `dummyHead = ListNode(0)` and `current = dummyHead`.
    *   Initialize `carry = 0`.
    *   Start a `while` loop that continues as long as `l1` is not null, `l2` is not null, or `carry > 0`.
    *   Inside the loop:
        *   Get the value of the current `l1` node, or 0 if `l1` is null.
        *   Get the value of the current `l2` node, or 0 if `l2` is null.
        *   Calculate `sum = val1 + val2 + carry`.
        *   Update `carry = sum / 10`.
        *   Create a `newNode` with the value `sum % 10`.
        *   Set `current.next = newNode` and then advance `current = current.next`.
        *   Advance `l1` and `l2` to their next nodes if they are not null.
    *   After the loop, return `dummyHead.next`.

### 4. Production-Ready Implementation:
```kotlin
/**
 * Definition for singly-linked list.
 * class ListNode(var `val`: Int) {
 *     var next: ListNode? = null
 * }
 */
class Solution {
    fun addTwoNumbers(l1: ListNode?, l2: ListNode?): ListNode? {
        // Guard clause for two null inputs
        if (l1 == null && l2 == null) {
            return null
        }

        var p1 = l1
        var p2 = l2
        val dummyHead = ListNode(0) // Sentinel node to simplify list creation
        var current = dummyHead
        var carry = 0

        // Loop until we've processed all nodes and there's no lingering carry
        while (p1 != null || p2 != null || carry > 0) {
            // Use 0 if a list is shorter than the other
            val val1 = p1?.`val` ?: 0
            val val2 = p2?.`val` ?: 0

            val sum = val1 + val2 + carry

            // The new digit is the remainder of the sum
            val newDigit = sum % 10
            // The new carry is the quotient
            carry = sum / 10

            // Create and append the new digit node to our result list
            current.next = ListNode(newDigit)
            current = current.next!!

            // Advance pointers if the lists are not exhausted
            p1 = p1?.next
            p2 = p2?.next
        }

        // The dummyHead's next node is the true start of the result list
        return dummyHead.next
    }
}
```

### 5. Verification & Complexity Finalization:
*   **Dry Run:**
    *   `l1` = `[2, 4, 3]` (represents 342)
    *   `l2` = `[5, 6, 4]` (represents 465)
    *   **Expected Output:** `[7, 0, 8]` (represents 807)

    1.  **`p1`=[2,4,3], `p2`=[5,6,4], `carry`=0, `result`=[]**
        *   `sum` = 2 + 5 + 0 = 7. `newDigit`=7, `carry`=0.
        *   `result` becomes `[7]`. `current` points to the 7-node.
    2.  **`p1`=[4,3], `p2`=[6,4], `carry`=0, `result`=[7]**
        *   `sum` = 4 + 6 + 0 = 10. `newDigit`=0, `carry`=1.
        *   `result` becomes `[7, 0]`. `current` points to the 0-node.
    3.  **`p1`=[3], `p2`=[4], `carry`=1, `result`=[7,0]**
        *   `sum` = 3 + 4 + 1 = 8. `newDigit`=8, `carry`=0.
        *   `result` becomes `[7, 0, 8]`. `current` points to the 8-node.
    4.  **`p1`=null, `p2`=null, `carry`=0.**
        *   Loop terminates.
    *   **Final Result:** `dummyHead.next` points to `[7, 0, 8]`. The logic is correct.

*   **Complexity Finalization:**
    *   **Time Complexity:** `O(max(N, M))`, where `N` and `M` are the number of nodes in `l1` and `l2`, respectively. The `while` loop runs a number of times equal to the length of the longer list, plus one possible iteration for a final carry.
    *   **Space Complexity:** `O(max(N, M))`. The length of the new result list is at most `max(N, M) + 1`, which is the space required to store the output.
