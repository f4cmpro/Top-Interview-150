package `002-add-two-numbers`

class Solution {
    fun addTwoNumbers(l1: ListNode?, l2: ListNode?): ListNode? {
        var a = l1
        var b = l2
        var redundant = 0
        var sum: ListNode? = null
        var lastNode: ListNode? = null
        while (a != null || b != null || redundant > 0) {
            val vala = a?.`val` ?: 0
            val valb = b?.`val` ?: 0
            //step 1: add vala and valb and last redundant
            val sumDigit = (vala + valb + redundant) % 10
            //step 2: save sumDigit into the next of last item in link list
            if (sum == null) {
                sum = ListNode(sumDigit)
            } else if (sum.next == null) {
                sum.next = ListNode(sumDigit)
                lastNode = sum.next
            } else {
                lastNode?.next = ListNode(sumDigit)
                lastNode = lastNode?.next
            }
            //step 3: update redundant
            redundant = (vala + valb + redundant) / 10
            a = a?.next
            b = b?.next
        }
        return sum
    }
}

class ListNode(var `val`: Int) {
    var next: ListNode? = null
}