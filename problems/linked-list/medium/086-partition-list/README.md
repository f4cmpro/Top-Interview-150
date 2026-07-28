# 86 - Partition List

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/partition-list/

---

## Problem Description

Given the `head` of a linked list and a value `x`, partition the list such that all nodes with values **less than** `x` come **before** nodes with values **greater than or equal to** `x`.

You must preserve the **original relative order** of the nodes in each of the two partitions.

### Example:
```
Input: head = [1,4,3,2,5,2], x = 3
Output: [1,2,2,4,3,5]
```

---

## 1. Clarification & Edge Cases

### Key Constraints:
- The number of nodes in the list is in the range `[0, 200]`.
- `-100 <= Node.val <= 100`
- `-200 <= x <= 200`
- The list can be empty or have only one node.
- Nodes can have duplicate values.
- Must preserve the original relative order within each partition.

### Edge Cases to Handle:
1. **Empty list** (`head == nullptr`): Return empty list.
2. **Single node** (`head->next == nullptr`): Return the single node.
3. **All nodes < x**: Entire list should be returned as-is.
4. **All nodes >= x**: Entire list should be returned as-is.
5. **Duplicate values**: Maintain relative order of nodes with the same value.
6. **Negative numbers**: Handle negative values correctly in comparisons.
7. **x value not in list**: Still partition correctly based on comparison.

---

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force Approach:
**Collect, Sort, Reconstruct:**
1. Extract all node values into an array.
2. Sort the array by value (stable sort to preserve relative order).
3. Create a new linked list with sorted values.

**Time Complexity**: O(n log n) — due to sorting  
**Space Complexity**: O(n) — to store all node values in an array and create new nodes

### Optimized Approach (Best Solution):
**Two Dummy Node Method with Single Pass:**
1. Create two dummy nodes: one for nodes `< x` and one for nodes `>= x`.
2. Traverse the original list once, appending each node to the appropriate dummy list.
3. Connect the two lists and return the result.

**Time Complexity**: O(n) — single pass through the list  
**Space Complexity**: O(1) — only use two dummy nodes (constant extra space)

### Why Optimized is Better:
- **Linear time** vs logarithmic time: We eliminate the need for sorting by using two separate chains.
- **Constant space** vs linear space: We don't create new nodes; we reuse existing ones by rearranging pointers.
- **Stable partition**: By processing nodes in order and appending to each chain, we naturally preserve relative order.

---

## 3. Algorithm Design

### Step-by-Step Logic:

1. **Initialize Two Dummy Nodes**: Create two dummy `ListNode` objects with value 0. These act as anchors:
   - `leftDummy` → head of the "less than x" partition
   - `rightDummy` → head of the "greater than or equal to x" partition

2. **Initialize Pointers**: Create two pointers (`leftCurrent`, `rightCurrent`) initialized to their respective dummy nodes. These pointers track where to append the next node in each partition.

3. **Single Pass Traversal**: Iterate through the original list:
   - For each node with `value < x`: append it to the left chain by setting `leftCurrent->next = current` and move `leftCurrent` forward.
   - For each node with `value >= x`: append it to the right chain by setting `rightCurrent->next = current` and move `rightCurrent` forward.
   - Move to the next node in the original list.

4. **Break the Right Chain**: Set `rightCurrent->next = nullptr` to avoid cycles (important if the original last node was appended to the right chain).

5. **Connect the Two Chains**: Append the right chain to the left chain: `leftCurrent->next = rightDummy->next`.

6. **Handle Edge Cases**:
   - If left chain is empty, return `rightDummy->next`.
   - If right chain is empty, return `leftDummy->next`.
   - Otherwise, return `leftDummy->next`.

### Data Structures Used:
- **Linked List**: To represent and manipulate the node chain.
- **Dummy Nodes**: Two auxiliary nodes to simplify pointer manipulation (avoids null checks for head assignments).
- **Two Pointers**: To efficiently track the end of each partition.

### Why This Approach:
- **Dummy nodes eliminate edge case complexity**: No need to check if the new head is null.
- **Two pointers keep track of partition ends efficiently**: O(1) append operation for each node.
- **Single pass is optimal**: We can't do better than O(n) since we must visit each node at least once.

---

## 4. Production-Ready Implementation

```cpp
/**
 * Definition for singly-linked list node.
 */
struct ListNode {
    int val;
    ListNode* next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode* next) : val(x), next(next) {}
};

class Solution {
public:
    /**
     * Partition a linked list around value x.
     * All nodes with values < x come before nodes with values >= x.
     * Preserves the original relative order within each partition.
     *
     * @param head: Pointer to the head of the linked list.
     * @param x: The partition value.
     * @return: Pointer to the head of the partitioned list.
     */
    ListNode* partition(ListNode* head, int x) {
        // Guard Clause: Handle empty list or single node
        if (!head || !head->next) {
            return head;
        }

        // Initialize two dummy nodes to anchor each partition
        ListNode* leftDummy = new ListNode(0);
        ListNode* rightDummy = new ListNode(0);

        // Pointers to track the current end of each partition
        ListNode* leftCurrent = leftDummy;
        ListNode* rightCurrent = rightDummy;

        // Traverse the original list and partition nodes
        ListNode* current = head;
        while (current) {
            if (current->val < x) {
                // Append to the left partition
                leftCurrent->next = current;
                leftCurrent = leftCurrent->next;
            } else {
                // Append to the right partition
                rightCurrent->next = current;
                rightCurrent = rightCurrent->next;
            }
            // Move to the next node in the original list
            current = current->next;
        }

        // Break the right chain to avoid potential cycles
        rightCurrent->next = nullptr;

        // Connect the two partitions
        leftCurrent->next = rightDummy->next;

        // Extract the result (skip the dummy node)
        ListNode* result = leftDummy->next;

        // Clean up the dummy nodes to avoid memory leaks
        delete leftDummy;
        delete rightDummy;

        return result;
    }
};
```

### Key Implementation Details:
- **Guard Clause**: Early return for empty or single-node lists (optimization).
- **Dummy Nodes as Sentinels**: Simplifies pointer operations without null checks.
- **Null Assignment**: `rightCurrent->next = nullptr` prevents cycles.
- **Memory Management**: Delete dummy nodes after use to prevent memory leaks.
- **Clear Variable Names**: `leftDummy`, `rightDummy`, `leftCurrent`, `rightCurrent` make intent obvious.
- **Comments**: Explain the purpose of each major step.

---

## 5. Verification & Complexity Finalization

### Dry Run Example:
**Input**: `head = [1,4,3,2,5,2]`, `x = 3`

**Initial State**:
```
List: 1 -> 4 -> 3 -> 2 -> 5 -> 2 -> nullptr
leftDummy -> nullptr
rightDummy -> nullptr
```

**Iteration**:
1. Node(1): `1 < 3` → leftDummy -> 1; leftCurrent = Node(1)
2. Node(4): `4 >= 3` → rightDummy -> 4; rightCurrent = Node(4)
3. Node(3): `3 >= 3` → rightCurrent.next = 3; rightCurrent = Node(3)
4. Node(2): `2 < 3` → leftCurrent.next = 2; leftCurrent = Node(2)
5. Node(5): `5 >= 3` → rightCurrent.next = 5; rightCurrent = Node(5)
6. Node(2): `2 < 3` → leftCurrent.next = 2; leftCurrent = Node(2)

**After Loop**:
```
leftDummy -> 1 -> 2 -> 2 -> (nullptr before connection)
rightDummy -> 4 -> 3 -> 5 -> 2 -> (nullptr set by rightCurrent->next = nullptr)
```

**After Connection** (`leftCurrent->next = rightDummy->next`):
```
leftDummy -> 1 -> 2 -> 2 -> 4 -> 3 -> 5 -> 2 -> nullptr
```

**Result**: `[1, 2, 2, 4, 3, 5, 2]` ✓ Correct!

### Final Complexity Analysis:

**Time Complexity**: **O(n)**
- Single traversal of the linked list.
- Each node is visited exactly once.
- All operations (comparisons, pointer assignments) are O(1).

**Space Complexity**: **O(1)**
- Only two dummy nodes (constant space).
- No additional data structures (arrays, hash maps, etc.).
- No recursive call stack.
- Pointers are reused; no new nodes are created.