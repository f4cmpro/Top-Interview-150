# 138 - Copy List with Random Pointer

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/copy-list-with-random-pointer/

---

### 1. Clarification & Edge Cases:
*   **Key Constraints:**
    *   The number of nodes in the linked list is in the range `[0, 1000]`.
    *   The value of each node (`Node.val`) is between `-10000` and `10000`.
    *   `Node.random` can point to any node in the list, or it can be `null`.
    *   The `next` pointer of the last node is `null`.

*   **Edge Cases:**
    *   **Empty List:** The input `head` is `null`. The function should return `null`.
    *   **Single Node List:** A list with only one node. Its `next` pointer will be `null`, and its `random` pointer could be `null` or point to itself.
    *   **Null Random Pointers:** All or some `random` pointers are `null`.
    *   **Self-Pointing Random Pointer:** A node's `random` pointer points to the node itself.
    *   **Cycles in Random Pointers:** The `random` pointers form a cycle (e.g., Node A's random points to B, and Node B's random points to A).

### 2. High-Level Approach Analysis (Trade-offs):

*   **Brute Force Approach:**
    1.  Iterate through the original list and create a new node for each original node, forming a new list linked only by `next` pointers.
    2.  Iterate through the original list again. For each original node, find where its `random` pointer points.
    3.  To set the corresponding `random` pointer in the new list, traverse the *original list* from the beginning to find the index of the random target node.
    4.  Then, traverse the *new list* from the beginning for that same index to get the corresponding new node and set the `random` pointer.
    *   **Complexity:** This would be O(N^2) time complexity because for each of the N nodes, we might traverse up to N nodes to find the random pointer's corresponding new node. Space complexity would be O(1) if we don't count the new list itself.

*   **Optimized Approach (Using a HashMap):**
    1.  Traverse the original list once. For each node, create a corresponding deep copy and store the mapping between the original node and its copy in a `HashMap<OriginalNode, CopiedNode>`.
    2.  Traverse the original list a second time. For each original node, use the `HashMap` to retrieve its copied node. Then, find the `next` and `random` pointers of the original node and use the map again to find their corresponding copied nodes. Set the `next` and `random` pointers for the copied node.
    *   **Comparison:** The optimized approach has a **Time Complexity of O(N)** because it involves two separate passes through the list, and `HashMap` lookups take O(1) time on average. The **Space Complexity is O(N)** to store the node mappings. This is vastly better than the O(N^2) time complexity of the brute-force method, especially for large lists, at the cost of using extra space for the map.

### 3. Algorithm Design:

The optimized solution uses a `HashMap` to efficiently map each node in the original list to its newly created copy. This avoids the need for repeated traversals to locate nodes.

1.  **Initialization:**
    *   First, handle the edge case of an empty list. If the input `node` is `null`, return `null` immediately.
    *   Create a `HashMap<Node, Node>` called `nodeMap`. This map will store the association between an original node (key) and its corresponding new, copied node (value).

2.  **First Pass (Node Creation):**
    *   Initialize a pointer `current` to the head of the original list (`node`).
    *   Iterate through the original list using the `current` pointer (`while (current != null)`).
    *   In each iteration, create a new `Node` with the same value as the `current` node (`Node(current.`val`)`).
    *   Store the mapping in the hash map: `nodeMap[current] = copiedNode`.
    *   Move to the next node: `current = current.next`.
    *   After this loop, `nodeMap` will contain all the original nodes as keys and their newly created (but not yet connected) copies as values.

3.  **Second Pass (Connecting Pointers):**
    *   Reset the `current` pointer back to the head of the original list (`node`).
    *   Iterate through the original list again.
    *   For each `current` node:
        *   Get the corresponding copied node from the map: `copiedNode = nodeMap[current]`.
        *   Set the `next` pointer for the copied node: `copiedNode.next = nodeMap[current.next]`. The map will return the copied `next` node or `null` if `current.next` is `null`.
        *   Set the `random` pointer for the copied node: `copiedNode.random = nodeMap[current.random]`. The map will return the copied `random` node or `null` if `current.random` is `null`.
    *   Move to the next node: `current = current.next`.

4.  **Return Result:**
    *   After the second pass, all the new nodes are correctly linked.
    *   The head of the new, copied list is the value associated with the head of the original list in the map.
    *   Return `nodeMap[node]`.

### 4. Production-Ready Implementation:

```kotlin
package `138-copy-list-with-random-pointer`

class Solution {
    fun copyRandomList(node: Node?): Node? {
        // Guard Clause: Handle the edge case of an empty list.
        if (node == null) return null

        // A map to store the association between original nodes and their copies.
        val nodeMap = HashMap<Node, Node>()

        // 1st Pass: Create a copy of each node and store the mapping.
        var current: Node? = node
        while (current != null) {
            val copyNode = Node(current.`val`)
            nodeMap[current] = copyNode
            current = current.next
        }

        // 2nd Pass: Connect the `next` and `random` pointers for the copied nodes.
        current = node
        while (current != null) {
            // Get the copied node from the map.
            val copiedNode = nodeMap[current]

            // Set the `next` pointer of the copied node.
            // `nodeMap[current.next]` will be null if `current.next` is null.
            copiedNode?.next = nodeMap[current.next]

            // Set the `random` pointer of the copied node.
            // `nodeMap[current.random]` will be null if `current.random` is null.
            copiedNode?.random = nodeMap[current.random]
            
            current = current.next
        }

        // The head of the new list is the copy of the original head.
        return nodeMap[node]
    }
}

class Node(var `val`: Int) {
    var next: Node? = null
    var random: Node? = null
}
```

### 5. Verification & Complexity Finalization:

*   **Dry Run:**
    *   Let's consider a simple list: `Node1(val=7) -> Node2(val=13) -> null`.
    *   `Node1.random` points to `null`.
    *   `Node2.random` points to `Node1`.

    1.  **Guard Clause:** `node` is not `null`.
    2.  **1st Pass (Node Creation):**
        *   `current` is `Node1`. Create `NewNode1(7)`. `nodeMap` becomes `{ Node1: NewNode1 }`.
        *   `current` is `Node2`. Create `NewNode2(13)`. `nodeMap` becomes `{ Node1: NewNode1, Node2: NewNode2 }`.
        *   `current` becomes `null`. Loop ends.
    3.  **2nd Pass (Connecting Pointers):**
        *   `current` is `Node1`.
            *   `copiedNode` is `NewNode1`.
            *   `copiedNode.next` = `nodeMap[Node1.next]` = `nodeMap[Node2]` = `NewNode2`. So, `NewNode1.next` points to `NewNode2`.
            *   `copiedNode.random` = `nodeMap[Node1.random]` = `nodeMap[null]` = `null`. So, `NewNode1.random` is `null`.
        *   `current` is `Node2`.
            *   `copiedNode` is `NewNode2`.
            *   `copiedNode.next` = `nodeMap[Node2.next]` = `nodeMap[null]` = `null`. So, `NewNode2.next` is `null`.
            *   `copiedNode.random` = `nodeMap[Node2.random]` = `nodeMap[Node1]` = `NewNode1`. So, `NewNode2.random` points to `NewNode1`.
        *   `current` becomes `null`. Loop ends.
    4.  **Return:** `nodeMap[node]` which is `nodeMap[Node1]` -> `NewNode1`.
    *   The resulting list `NewNode1 -> NewNode2 -> null` is a correct deep copy.

*   **Final Complexity:**
    *   **Time Complexity: O(N)**, where N is the number of nodes in the list. We iterate through the list twice, once to create the copies and once to assign the pointers. Each pass takes O(N) time.
    *   **Space Complexity: O(N)**. The `HashMap` stores a mapping for each of the N nodes, so it requires space proportional to the number of nodes.
