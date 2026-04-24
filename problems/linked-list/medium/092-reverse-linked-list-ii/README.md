# 92 - Reverse Linked List II

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/reverse-linked-list-ii/

### 1. Clarification & Edge Cases:
*   **Key Constraints:**
    *   Number of nodes: `1` to `500`.
    *   Node values: `-500` to `500`.
    *   `1 <= left <= right <= n` (where `n` is the total number of nodes).
*   **Potential Edge Cases:**
    *   `left == right`: No reversal is needed; the list remains unchanged.
    *   Reversing the entire list (e.g., `left = 1`, `right = n`).
    *   Reversing a sublist that starts from the head (e.g., `left = 1`).
    *   Reversing a sublist that ends at the tail (e.g., `right = n`).
    *   The list has only one node.

### 2. High-Level Approach Analysis (Trade-offs):
*   **Brute Force Approach (Using extra space):**
    1.  Iterate through the linked list once to store all the nodes in a HashMap or an array, mapping their 1-based index to the node object.
    2.  Create a new dummy head.
    3.  Iterate from `1` to `n`. For indices between `left` and `right`, append the nodes from the map in reverse order (`map[left + right - i]`). For all other indices, append the original node (`map[i]`).
    4.  This approach is simple to conceptualize but requires extra memory to store all nodes.

*   **Optimized Approach (One-Pass In-place Reversal):**
    1.  Use a dummy node to simplify handling the case where the reversal starts at `left = 1`.
    2.  Iterate `left - 1` times to find the node just before the reversal segment (`pre_reverse_head`).
    3.  Use three pointers (`prev`, `current`, `next_node`) to reverse the sublist from `left` to `right` in-place. This involves `right - left` pointer re-wirings.
    4.  After the reversal, connect the surrounding parts of the list with the now-reversed sublist.

*   **Comparison:**
    *   **Time Complexity:**
        *   Brute Force: `O(N)` for the first traversal and `O(N)` for the second, resulting in `O(N)`.
        *   Optimized: `O(N)` because we traverse the list at most once.
    *   **Space Complexity:**
        *   Brute Force: `O(N)` to store all nodes in a map/array.
        *   **Optimized: `O(1)`** because we only use a few pointers, regardless of the list size.

    The optimized approach is significantly better due to its constant space complexity, making it more efficient for large lists.

### 3. Algorithm Design:
The optimized one-pass algorithm works as follows:

1.  **Initialization:**
    *   Create a `dummy` node and point its `next` to the `head` of the list. This handles the edge case where `left = 1`.
    *   Create a pointer `pre_reverse_head` and initialize it to `dummy`.

2.  **Locate Reversal Start:**
    *   Iterate `left - 1` times, advancing `pre_reverse_head` to the node just before the segment to be reversed.

3.  **In-Place Reversal:**
    *   Initialize three pointers:
        *   `current`: The first node in the reversal segment (`pre_reverse_head.next`).
        *   `prev`: Initially `null`. This will become the new tail of the reversed sublist.
        *   `next_node`: To temporarily store the next node during pointer manipulation.
    *   Loop `right - left + 1` times to reverse the pointers:
        *   Store `current.next` in `next_node`.
        *   Set `current.next` to `prev`.
        *   Move `prev` to `current`.
        *   Move `current` to `next_node`.

4.  **Connect the List Parts:**
    *   After the loop, `prev` points to the new head of the reversed sublist, and `current` points to the node that was originally at position `right + 1`.
    *   The original start of the sublist (which is now the *end* of the reversed sublist) needs to be connected to the rest of the list. We can access this node via `pre_reverse_head.next`.
    *   Set `pre_reverse_head.next.next = current`.
    *   Connect the part before the reversal to the new head of the reversed sublist: `pre_reverse_head.next = prev`.

5.  **Return Result:**
    *   Return `dummy.next`, which is the head of the modified list.

### 4. Production-Ready Implementation:
```kotlin
/**
 * Definition for singly-linked list.
 * class ListNode(var `val`: Int) {
 *     var next: ListNode? = null
 * }
 */
class Solution {
    fun reverseBetween(head: ListNode?, left: Int, right: Int): ListNode? {
        // Guard Clause: If no reversal is needed, return the original list.
        if (head == null || left == right) {
            return head
        }

        // Use a dummy node to simplify handling the case where left = 1.
        val dummy = ListNode(0)
        dummy.next = head
        
        // 1. Move `pre_reverse_head` to the node just before the reversal segment.
        var pre_reverse_head: ListNode? = dummy
        for (i in 0 until left - 1) {
            pre_reverse_head = pre_reverse_head?.next
        }

        // `start_node` is the first node of the sublist to be reversed.
        val start_node = pre_reverse_head?.next
        
        // `current` will iterate through the sublist to reverse it.
        var current = start_node
        var prev: ListNode? = null

        // 2. Reverse the sublist from `left` to `right`.
        for (i in 0 until right - left + 1) {
            val next_node = current?.next
            current?.next = prev
            prev = current
            current = next_node
        }

        // 3. Connect the reversed sublist back to the main list.
        // `pre_reverse_head.next` still points to the original start of the sublist,
        // which is now the tail of the reversed part.
        pre_reverse_head?.next = prev
        
        // `start_node` is now the tail of the reversed sublist. Connect it to the
        // part of the list that comes after the reversed segment.
        start_node?.next = current

        return dummy.next
    }
}
```

### 5. Verification & Complexity Finalization:
*   **Dry Run:**
    *   Input: `head = [1,2,3,4,5]`, `left = 2`, `right = 4`
    *   `dummy` -> `0` -> `1` -> `2` -> `3` -> `4` -> `5`
    *   **Step 1:** `pre_reverse_head` moves `2 - 1 = 1` time. It points to node `1`.
    *   **Step 2 (Reversal):**
        *   `start_node` is node `2`. `current` is node `2`. `prev` is `null`.
        *   **i = 0:** `next`=3, `2.next`=null, `prev`=2, `current`=3
        *   **i = 1:** `next`=4, `3.next`=2, `prev`=3, `current`=4
        *   **i = 2:** `next`=5, `4.next`=3, `prev`=4, `current`=5
        *   Loop ends. `prev` is `4` (new head of sublist), `current` is `5`.
    *   **Step 3 (Connecting):**
        *   `pre_reverse_head` (node `1`) `next` is set to `prev` (node `4`). List is now `1 -> 4 -> 3 -> 2`.
        *   `start_node` (node `2`) `next` is set to `current` (node `5`). List is now `1 -> 4 -> 3 -> 2 -> 5`.
    *   **Result:** `dummy.next` points to `1`, so the final list is `[1,4,3,2,5]`. Correct.

*   **Time Complexity:** `O(N)`
    *   We traverse to the `left`-th node, and then perform `right - left` reversals. In the worst case (`left=1`, `right=N`), this is a single pass through the entire list.

*   **Space Complexity:** `O(1)`
    *   We only use a few pointers (`dummy`, `pre_reverse_head`, `current`, `prev`, `next_node`) to perform the reversal in-place. The space used is constant and does not depend on the size of the input list.
