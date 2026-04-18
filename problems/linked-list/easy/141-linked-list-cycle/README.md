# 141 - Linked List Cycle

**Difficulty**: 🟢 Easy  
**LeetCode**: https://leetcode.com/problems/linked-list-cycle/

**Act as a Senior Software Engineer and Technical Interviewer at a Big Tech company.**

I need a comprehensive **Solution Design** for the following DSA problem:

## Problem Description:
> Given `head`, the head of a linked list, determine if the linked list has a cycle in it.
>
> There is a cycle in a linked list if there is some node in the list that can be reached again by continuously following the `next` pointer. Internally, `pos` is used to denote the index of the node that tail's `next` pointer is connected to. **Note that `pos` is not passed as a parameter.**
>
> Return `true` if there is a cycle in the linked list. Otherwise, return `false`.
>
> https://leetcode.com/problems/linked-list-cycle/

**Preferred Language:** Kotlin

---

**Please structure your response exactly according to the following 5 stages:**

### 1. Clarification & Edge Cases:
*   **Key Constraints:**
    *   The number of nodes in the list is in the range `[0, 10^4]`.
    *   `Node.val` is in the range `[-10^5, 10^5]`.
    *   `pos` is `-1` or a **valid index** in the linked-list.
*   **Edge Cases:**
    *   **Empty List:** The head is `null`.
    *   **Single Node List:** The list has only one node, which can either point to `null` or back to itself.
    *   **No Cycle:** A standard linear linked list.
    *   **Cycle:** The last node points back to a previous node in the list.

### 2. High-Level Approach Analysis (Trade-offs):
*   **Brute Force Approach (Using a HashSet):**
    *   Iterate through the linked list, node by node.
    *   Store each visited node in a `HashSet`.
    *   Before visiting a node, check if it's already in the `HashSet`. If it is, we've found a cycle.
    *   If we reach the end of the list (`null`), there is no cycle.
*   **Optimized Approach (Floyd's Tortoise and Hare Algorithm):**
    *   Use two pointers, a `slow` pointer and a `fast` pointer.
    *   The `slow` pointer moves one step at a time.
    *   The `fast` pointer moves two steps at a time.
    *   If there is a cycle, the `fast` pointer will eventually catch up to and meet the `slow` pointer.
    *   If there is no cycle, the `fast` pointer (or `fast.next`) will reach the end of the list (`null`).
*   **Comparison:**
    *   **Time Complexity:**
        *   Brute Force: O(n), as we visit each node once.
        *   Optimized: O(n), because in the worst case, the slow pointer travels through all nodes. The fast pointer travels at most 2n.
    *   **Space Complexity:**
        *   Brute Force: O(n), as the `HashSet` could store all `n` nodes in the worst case (a list with no cycle).
        *   Optimized: O(1), as we only use two pointers, requiring constant extra space.
    *   **Why Optimized is Better:** The optimized approach is significantly better in terms of space complexity. For very large linked lists, storing every node in a hash set could lead to high memory consumption, whereas the two-pointer approach uses a constant amount of memory regardless of the list size.

### 3. Algorithm Design:
*   **Logic:** The optimized solution uses the **Two Pointers** technique, specifically Floyd's Cycle-Finding Algorithm.
*   **Data Structures:** We only need two `ListNode?` variables, `slow` and `fast`, to act as our pointers. No other complex data structures are required.
*   **Step-by-step:**
    1.  Initialize two pointers, `slow` and `fast`, both pointing to the `head` of the list.
    2.  Handle the edge case of an empty or single-node list upfront by checking if `head` or `head.next` is `null`. If so, no cycle can exist, so return `false`.
    3.  Enter a `while` loop that continues as long as `fast` and `fast.next` are not `null`. This condition ensures we can safely advance the `fast` pointer by two steps.
    4.  Inside the loop, advance `slow` by one node (`slow = slow.next`).
    5.  Advance `fast` by two nodes (`fast = fast.next.next`).
    6.  After moving the pointers, check if `slow` and `fast` are pointing to the same node (`slow == fast`).
    7.  If they are the same, it confirms a cycle exists. Return `true`.
    8.  If the loop terminates (meaning `fast` or `fast.next` became `null`), it means we've reached the end of the list without the pointers meeting. Therefore, no cycle exists. Return `false`.

### 4. Production-Ready Implementation:
```kotlin
package `141-linked-list-cycle`

/**
 * Definition for singly-linked list.
 * class ListNode(var `val`: Int) {
 *     var next: ListNode? = null
 * }
 */
class Solution {
    /**
     * Detects if a cycle exists in a linked list using Floyd's Tortoise and Hare algorithm.
     *
     * @param head The head of the linked list.
     * @return True if a cycle is present, false otherwise.
     */
    fun hasCycle(head: ListNode?): Boolean {
        // Guard Clause: An empty list or a list with a single node cannot have a cycle.
        if (head?.next == null) {
            return false
        }

        var slow = head
        var fast = head

        // The loop continues as long as the fast pointer and its next node are not null.
        // This prevents NullPointerException when advancing the fast pointer by two steps.
        while (fast?.next != null) {
            slow = slow?.next
            fast = fast.next?.next

            // If the slow and fast pointers meet, a cycle is detected.
            if (slow == fast) {
                return true
            }
        }

        // If the loop completes, the fast pointer has reached the end of the list,
        // meaning no cycle was found.
        return false
    }
}

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}
```

### 5. Verification & Complexity Finalization:
*   **Dry Run:**
    *   **Input:** `head = [3,2,0,-4]`, `pos = 1` (cycle: -4 points to 2)
        1.  `slow` = 3, `fast` = 3.
        2.  **Loop 1:** `slow` -> 2, `fast` -> 0. They are not equal.
        3.  **Loop 2:** `slow` -> 0, `fast` -> 2. They are not equal.
        4.  **Loop 3:** `slow` -> -4, `fast` -> -4. They are equal (`slow == fast`).
        5.  Return `true`. The algorithm correctly identifies the cycle.
    *   **Input:** `head = [1,2]`, `pos = -1` (no cycle)
        1.  `slow` = 1, `fast` = 1.
        2.  **Loop 1:** `slow` -> 2, `fast` -> `null` (`fast.next` was 2, `fast.next.next` is `null`).
        3.  The loop condition `fast?.next != null` is now false. The loop terminates.
        4.  Return `false`. The algorithm correctly identifies no cycle.
*   **Final Complexity:**
    *   **Time Complexity:** **O(n)**, where `n` is the number of nodes in the list. In the worst-case scenario (no cycle), the `fast` pointer traverses the entire list.
    *   **Space Complexity:** **O(1)**. We only use two pointers (`slow` and `fast`), which is constant extra space.
