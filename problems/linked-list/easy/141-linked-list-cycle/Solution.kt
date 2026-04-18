package `141-linked-list-cycle`


class Solution {
    fun hasCycle(head: ListNode?): Boolean {
        if(head == null || head.next == null) return false
        var slow = head
        var fast = head
        while(fast != null && fast.next != null) {
            fast = fast?.next?.next
            slow = slow?.next
            if(fast == slow) return true
        }
        return false
    }
}

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}

