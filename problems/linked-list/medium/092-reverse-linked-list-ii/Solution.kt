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
        current = head
        index = 1
        val dummyHead = ListNode(0)
        var copyCurrent: ListNode? = dummyHead
        while (current != null) {
            if (index >= left && index <= right) {
                val reversedIndex = left + right - index
                copyCurrent?.next = nodeMap[reversedIndex]
            } else {
                copyCurrent?.next = nodeMap[index]
            }
            println("index: $index, current: ${current.`val`}, copyCurrent: ${copyCurrent?.next?.`val`}")
            current = current.next
            copyCurrent = copyCurrent?.next
            index++
        }
        return dummyHead.next
    }
}

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}