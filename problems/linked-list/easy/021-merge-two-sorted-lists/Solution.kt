package `021-merge-two-sorted-lists`

class Solution {
    fun mergeTwoLists(list1: ListNode?, list2: ListNode?): ListNode? {
        var node1 = list1
        var node2 = list2
        val dummyHead = ListNode(0)
        var current: ListNode? = dummyHead
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

        if (node1 != null) {
            current?.next = node1
        }
        if (node2 != null) {
            current?.next = node2
        }
        return dummyHead.next
    }
}

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}