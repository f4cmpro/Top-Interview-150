class Solution {
    fun reverseBetween(head: ListNode?, left: Int, right: Int): ListNode? {
        if (left == right) return head
        val listNode = ArrayList<ListNode>()
        var current = head
        var start = left - 1
        var end = right - 1
        while (current != null) {
            listNode.add(current)
            current = current.next
        }
        while (start < end) {
            val temp = listNode[start]
            listNode[start] = listNode[end]
            listNode[end] = temp
            start++
            end--
        }
        val dummyHead = ListNode(0)
        current = dummyHead
        for (node in listNode) {
            current?.next = node
            current = current?.next
        }
        current?.next = null
        return dummyHead.next
    }
}

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}