class Solution {
    fun copyRandomList(node: Node?): Node? {
        if (node == null) return null
        var current = node
        val copyNode : Node? = null
        var copyCurrent = copyNode
        while (current != null) {
            val newNode = Node(current.`val`)
            newNode.next = if (current.next != null) Node(node.next!!.`val`) else null
            newNode.random = if (current.random != null) Node(current.random!!.`val`) else null
            copyCurrent = newNode
        }
    }
}

class Node(var `val`: Int) {
    var next: Node? = null
    var random: Node? = null
}