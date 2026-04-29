# 25 - Reverse Nodes in k-Group

**Difficulty**: 🔴 Hard  
**LeetCode**: https://leetcode.com/problems/reverse-nodes-in-k-group/

## Solution Design

### 1. Clarification & Edge Cases:
*   **Input**: The head of a singly linked list (`head`) and an integer (`k`).
*   **Output**: The head of the modified linked list with nodes reversed in groups of `k`.
*   **Constraints**:
    *   The number of nodes in the list (`n`) is between 1 and 5000.
    *   `1 <= k <= n`.
    *   The value of each node is between 0 and 1000.
*   **Edge Cases**:
    *   **Empty List**: If `head` is `null`, the function should return `null`.
    *   **`k = 1`**: If `k` is 1, reversing groups of 1 node results in the original list. The function should return the original `head`.
    *   **List length < k**: If the total number of nodes is less than `k`, no reversal should happen. The original list should be returned.
    *   **Non-multiple of k**: If the list length is not a multiple of `k`, the last remaining nodes (fewer than `k`) should not be reversed.

### 2. High-Level Approach Analysis (Trade-offs):
*   **Brute Force Approach**:
    One way is to iterate through the list, and for each group of `k` nodes, extract their values into an auxiliary array. Then, reverse the array and create new nodes with these reversed values. This approach is inefficient because it involves creating new nodes and does not modify the list in-place, which is often a requirement in interviews. It also uses extra space for the array.

*   **Optimized Approach (In-place Reversal)**:
    The best approach is to reverse the nodes in-place, group by group. We can iterate through the list, and for each group of `k` nodes, we reverse that sub-list. We need to carefully manage pointers to connect the previously reversed group with the current one, and the current one with the rest of the list. This is done iteratively without recursion to save stack space.

*   **Comparison**:
    *   **Time Complexity**:
        *   Brute Force: `O(N)` for traversing and `O(k)` for creating new nodes for each group, leading to `O(N)`.
        *   Optimized: `O(N)`. We visit each node a constant number of times.
    *   **Space Complexity**:
        *   Brute Force: `O(k)` to store the values for each group in an auxiliary array.
        *   Optimized: `O(1)`. The reversal is done in-place with only a few pointers, regardless of the list size.

    The **Optimized Approach** is superior because it avoids the overhead of creating new nodes and uses constant extra space, directly manipulating the given list structure as requested by the problem.

### 3. Algorithm Design:
The optimized algorithm uses a "dummy head" node to simplify handling the beginning of the list. We process the list in chunks of `k` nodes.

1.  **Initialization**:
    *   Create a `dummy` node and point its `next` to the original `head`. This `dummy` node acts as a stable predecessor to the first group, making the connection logic uniform for all groups.
    *   Initialize a `groupPrev` pointer to `dummy`. This pointer will mark the last node of the *previous* reversed group, which needs to be connected to the *current* group's new head.

2.  **Iterate and Reverse in Groups**:
    *   Start a loop that continues as long as there are groups of `k` nodes to process.
    *   In each iteration, first, find the `k`-th node of the current group. Let's call it `kthNode`.
        *   If we can't find a `kthNode` (i.e., we reach the end of the list), it means the remaining part has fewer than `k` nodes. We break the loop, as no more reversals are needed.
    *   Let `groupNext` be the node right after `kthNode`. This is the starting node of the *next* group, which we need to connect to later.
    *   Reverse the sub-list from `groupPrev.next` up to `kthNode`.
        *   We use standard iterative reversal logic with `prev`, `curr`, and `next` pointers.
        *   The reversal starts with `prev = groupNext` (the node that will follow the reversed group) and `curr = groupPrev.next` (the first node of the current group).
    *   **Reconnect the list**: After reversing the group, the pointers need to be rewired:
        *   The original first node of the group (`groupPrev.next`) is now the *last* node of the reversed group. It should point to `groupNext`.
        *   The `groupPrev` node (from the previous segment) should now point to `kthNode`, which is the new *head* of the current reversed group.
        *   Update `groupPrev` to point to the original first node of the group, which is now the tail of the reversed group, to prepare for the next iteration.

3.  **Return Result**:
    *   Once the loop finishes, the `dummy.next` pointer will hold the head of the fully modified list. Return `dummy.next`.

**Data Structures Used**:
*   **Pointers**: The core of the solution relies on manipulating `next` pointers within the `ListNode`s. We use several pointers (`dummy`, `groupPrev`, `kthNode`, `groupNext`) to keep track of segments and connections. This is a classic **Two Pointers** pattern applied in a more complex way.

### 4. Production-Ready Implementation:
```kotlin
/**
 * Definition for singly-linked list.
 * class ListNode(var `val`: Int) {
 *     var next: ListNode? = null
 * }
 */
class Solution {
    fun reverseKGroup(head: ListNode?, k: Int): ListNode? {
        // Guard Clause: If the list is empty, has only one node, or k is 1,
        // no reversal is needed. Return the original list.
        if (head?.next == null || k == 1) {
            return head
        }

        val dummy = ListNode(0)
        dummy.next = head
        var groupPrev: ListNode? = dummy

        while (true) {
            // 1. Find the k-th node of the current group
            val kthNode = getKthNode(groupPrev, k) ?: break // If null, remaining nodes < k

            val groupNext = kthNode.next

            // 2. Reverse the group of k nodes
            // The sub-list to reverse is from groupPrev.next to kthNode
            var prev: ListNode? = groupNext // The node after the reversed group
            var curr = groupPrev?.next
            while (curr != groupNext) {
                val nextTemp = curr?.next
                curr?.next = prev
                prev = curr
                curr = nextTemp
            }

            // 3. Reconnect the pointers
            val temp = groupPrev?.next // Original head of the group, now the tail
            groupPrev?.next = kthNode  // Connect previous part to the new head of the group
            groupPrev = temp           // Move groupPrev to the end of the just-reversed group
        }

        return dummy.next
    }

    /**
     * Helper function to get the k-th node from the current position.
     * Returns null if there are fewer than k nodes left.
     */
    private fun getKthNode(curr: ListNode?, k: Int): ListNode? {
        var current = curr
        var count = k
        while (current != null && count > 0) {
            current = current.next
            count--
        }
        return current
    }
}
```

### 5. Verification & Complexity Finalization:
*   **Dry Run**:
    *   **Input**: `head = [1, 2, 3, 4, 5]`, `k = 2`
    *   **Initial**: `dummy -> 1 -> 2 -> 3 -> 4 -> 5`, `groupPrev` is `dummy`.
    *   **Iteration 1**:
        *   `kthNode` is `2`. `groupNext` is `3`.
        *   Reverse group `1 -> 2`. It becomes `2 -> 1`.
        *   `prev` starts as `3`, `curr` starts as `1`.
        *   After reversal loop: `1.next` is `3`, `2.next` is `1`.
        *   Reconnect: `dummy.next` becomes `2`. `groupPrev` becomes `1`.
        *   List is now: `dummy -> 2 -> 1 -> 3 -> 4 -> 5`.
    *   **Iteration 2**:
        *   `groupPrev` is `1`. `kthNode` is `4`. `groupNext` is `5`.
        *   Reverse group `3 -> 4`. It becomes `4 -> 3`.
        *   Reconnect: `1.next` becomes `4`. `groupPrev` becomes `3`.
        *   List is now: `dummy -> 2 -> 1 -> 4 -> 3 -> 5`.
    *   **Iteration 3**:
        *   `groupPrev` is `3`. `getKthNode` is called to find the 2nd node from `3`. It finds only `5` (1 node).
        *   `getKthNode` returns `null`. The `while` loop breaks.
    *   **Final**: Return `dummy.next`, which is `2`. The final list is `[2, 1, 4, 3, 5]`. Correct.

*   **Time Complexity**: `O(N)`
    *   Although we have a nested loop structure, each node is visited a constant number of times. The outer `while` loop iterates `N/k` times, and the inner reversal loop processes `k` nodes. The `getKthNode` also traverses `k` nodes. In total, each node is touched a fixed number of times, leading to a linear time complexity.

*   **Space Complexity**: `O(1)`
    *   The solution uses a fixed number of pointers (`dummy`, `groupPrev`, `kthNode`, `groupNext`, `prev`, `curr`) to perform the reversal in-place. The space required does not scale with the input list size.
