package `025-reverse-nodes-in-k-group`

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

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}