package `138-copy-list-with-random-pointer`

class Solution {
    fun copyRandomList(node: Node?): Node? {
        if (node == null) return null
        val nodeMap = HashMap<Node, Node>()
        var current: Node? = node
        while (current != null) {
            val copyNode = Node(current.`val`)
            nodeMap[current] = copyNode
            current = current.next
        }
        val dummyNode = Node(0)
        var copyCurrent: Node? = dummyNode
        current = node
        while (current != null) {
            copyCurrent?.next = nodeMap[current]
            copyCurrent?.next?.next = nodeMap[current.next]
            copyCurrent?.next?.random = nodeMap[current.random]
            copyCurrent = copyCurrent?.next
            current = current.next
        }
        return dummyNode.next
    }
}

class Node(var `val`: Int) {
    var next: Node? = null
    var random: Node? = null
}