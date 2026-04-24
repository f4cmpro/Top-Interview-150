class Solution {
    fun reverseBetween(head: ListNode?, left: Int, right: Int): ListNode? {
        if (left == right) return head
        val nodeMap = HashMap<Int, ListNode>()
        var index = 1
        var current = head
        while (current != null) {
            nodeMap[index] = current
            index++
            current = current.next
        }
        val dummyHead = ListNode(0)
        var copyCurrent: ListNode? = dummyHead
        for (i in 1 until index) {
            if (i >= left && i <= right) {
                val reversedIndex = left + right - i
                copyCurrent?.next = nodeMap[reversedIndex]
            } else {
                copyCurrent?.next = nodeMap[i]
            }
            copyCurrent = copyCurrent?.next
        }
        copyCurrent?.next = null
        return dummyHead.next
    }
}

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}