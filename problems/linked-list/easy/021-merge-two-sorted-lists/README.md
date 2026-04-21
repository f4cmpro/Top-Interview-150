# 21 - Merge Two Sorted Lists

**Difficulty**: 🟢 Easy  
**LeetCode**: https://leetcode.com/problems/merge-two-sorted-lists/

## Solution Design

### 1. Clarification & Edge Cases:
*   **Constraints**:
    *   The input lists are already sorted in non-decreasing order.
    *   The number of nodes in both lists is between 0 and 50.
    *   The value of each node is between -100 and 100.
*   **Edge Cases**:
    *   One or both input lists are empty.
    *   One list is much longer than the other.
    *   Lists contain duplicate values.

### 2. High-Level Approach Analysis (Trade-offs):
*   **Brute Force Approach**:
    1.  Traverse both linked lists and store all node values in a single array.
    2.  Sort the array.
    3.  Create a new linked list from the sorted array elements.
    *   **Time Complexity**: `O((N+M) * log(N+M))`, where N and M are the number of nodes in `list1` and `list2` respectively. This is dominated by the sorting step.
    *   **Space Complexity**: `O(N+M)` to store the values in an array and for the new list.

*   **Optimized Approach (Iterative Merge with a Dummy Head)**:
    1.  Create a `dummyHead` node to simplify the code, avoiding the need for a special check for the head of the merged list.
    2.  Use a `current` pointer, initialized to `dummyHead`, to build the new list.
    3.  Iterate through both lists simultaneously with two pointers (`node1` and `node2`).
    4.  In each iteration, compare the values at `node1` and `node2`. Append the smaller node to `current.next` and advance the pointer of the list from which the node was taken.
    5.  Move `current` to the newly added node.
    6.  After the loop, one of the lists might still have remaining nodes. Append the rest of the non-empty list to the end of the merged list.
    7.  Return `dummyHead.next`, which is the actual head of the merged list.

*   **Comparison**:
    The optimized approach is significantly better. It avoids the overhead of creating an intermediate array and sorting it. By iterating through both lists only once, it achieves linear time complexity, which is optimal. The space complexity is also optimal as it only uses a few extra pointers, reusing the existing nodes.

### 3. Algorithm Design:
The optimized solution uses a **Two Pointers** technique combined with a **dummy head** node for the resulting list.

1.  **Initialization**:
    *   `node1`: Pointer to the head of `list1`.
    *   `node2`: Pointer to the head of `list2`.
    *   `dummyHead`: A new `ListNode` that acts as a placeholder for the start of the result list. This helps avoid edge cases for an empty result list.
    *   `current`: A pointer that will track the last node in the result list, initialized to `dummyHead`.

2.  **Iteration**:
    *   Use a `while` loop that continues as long as both `node1` and `node2` are not `null`.
    *   Inside the loop, compare `node1.val` and `node2.val`.
    *   If `node1.val` is smaller or equal, attach `node1` to `current.next` and advance `node1` to `node1.next`.
    *   Otherwise, attach `node2` to `current.next` and advance `node2` to `node2.next`.
    *   In either case, advance `current` to `current.next` to extend the result list.

3.  **Append Remaining Nodes**:
    *   After the loop, one of the lists may be exhausted while the other still contains nodes.
    *   Since the lists are sorted, the remaining nodes can be appended directly to the end of the result list.
    *   Check if `node1` is not `null`. If so, set `current.next = node1`.
    *   Check if `node2` is not `null`. If so, set `current.next = node2`.

4.  **Return Result**:
    *   The merged list starts at the node *after* the `dummyHead`.
    *   Return `dummyHead.next`.

### 4. Production-Ready Implementation:
```kotlin
package `021-merge-two-sorted-lists`

class Solution {
    fun mergeTwoLists(list1: ListNode?, list2: ListNode?): ListNode? {
        // Guard clauses for empty lists
        if (list1 == null) return list2
        if (list2 == null) return list1

        // Use a dummy head to simplify insertion logic
        val dummyHead = ListNode(0)
        var current: ListNode? = dummyHead

        var node1 = list1
        var node2 = list2

        // Traverse both lists and append the smaller node
        while (node1 != null && node2 != null) {
            if (node2.`val` < node1.`val`) {
                current?.next = node2
                node2 = node2.next
            } else {
                current?.next = node1
                node1 = node1.next
            }
            current = current?.next
        }

        // Append the remaining nodes from the non-empty list
        if (node1 != null) {
            current?.next = node1
        }
        if (node2 != null) {
            current?.next = node2
        }

        // The merged list starts after the dummy head
        return dummyHead.next
    }
}

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}
```

### 5. Verification & Complexity Finalization:
*   **Dry Run**:
    *   `list1` = `[1, 3]`, `list2` = `[2, 4]`
    *   **Init**: `dummyHead` -> `0`, `current` -> `dummyHead`, `node1` -> `1`, `node2` -> `2`.
    *   **Loop 1**: `2 < 1` is false. `current.next` -> `1`. `current` -> `1`. `node1` -> `3`. Result: `0 -> 1`.
    *   **Loop 2**: `2 < 3` is true. `current.next` -> `2`. `current` -> `2`. `node2` -> `4`. Result: `0 -> 1 -> 2`.
    *   **Loop 3**: `4 < 3` is false. `current.next` -> `3`. `current` -> `3`. `node1` -> `null`. Result: `0 -> 1 -> 2 -> 3`.
    *   **End Loop**: `node1` is `null`.
    *   **Append Remainder**: `node2` is not `null` (`4`). `current.next` -> `4`. Result: `0 -> 1 -> 2 -> 3 -> 4`.
    *   **Return**: `dummyHead.next`, which is `1 -> 2 -> 3 -> 4`. The logic is correct.

*   **Final Complexity**:
    *   **Time Complexity**: `O(N + M)`, where N and M are the lengths of `list1` and `list2`. We iterate through each list exactly once.
    *   **Space Complexity**: `O(1)`. The algorithm uses a constant amount of extra space for the `dummyHead` and `current` pointers. The new list is formed by rearranging the existing nodes, not creating new ones.
