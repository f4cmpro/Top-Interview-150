# 226 - Invert Binary Tree

**Difficulty**: 🟢 Easy  
**LeetCode**: https://leetcode.com/problems/invert-binary-tree/

---

## Problem Description

Given the root of a binary tree, invert the tree, and return its root.

**Example 1:**
```
Input:  [4,2,7,1,3,6,9]
Output: [4,7,2,9,6,3,1]
```

**Example 2:**
```
Input:  [2,1,3]
Output: [2,3,1]
```

**Example 3:**
```
Input:  []
Output: []
```

---

## Solution Design

### 1. Clarification & Edge Cases

**Constraints:**
- Number of nodes: `0 <= n <= 100`
- Node values: `-100 <= Node.val <= 100`
- The tree is a standard binary tree (not necessarily balanced or BST)

**Edge Cases:**
- `root == nullptr` — empty tree; return `nullptr` immediately
- Single node (no children) — nothing to swap; return the node unchanged
- Skewed trees (only left or only right children at every level) — still works correctly since we swap `nullptr` with a real child

---

### 2. High-Level Approach Analysis (Trade-offs)

| Approach | Strategy | Time | Space |
|---|---|---|---|
| **Brute Force (BFS/Iterative)** | Use a queue to visit every node level-by-level and swap children | O(n) | O(n) — queue holds up to n/2 nodes at the widest level |
| **Optimized (DFS Recursive)** | Recursively invert left and right subtrees, then swap the two child pointers | O(n) | O(h) — call stack depth equals tree height h |

**Why the optimized approach is better:**  
Both visit every node exactly once giving O(n) time. The recursive DFS approach uses O(h) stack space where `h` is the tree height — O(log n) for a balanced tree vs. O(n) worst case for a skewed tree. The BFS queue always holds up to O(n) nodes in the worst case. For a balanced tree the recursive approach is therefore strictly more space-efficient, and the code is simpler and more idiomatic.

---

### 3. Algorithm Design

**Core Insight:** Inverting a binary tree means every node's left and right children are swapped at every level. This is naturally recursive: invert the left subtree, invert the right subtree, then swap the two child pointers of the current node.

**Steps:**
1. **Base case:** If `root` is `nullptr`, return `nullptr` — nothing to do.
2. **Recurse left:** Call `invertTree(root->left)` — fully inverts the left subtree and returns its new root.
3. **Recurse right:** Call `invertTree(root->right)` — fully inverts the right subtree and returns its new root.
4. **Swap:** Exchange `root->left` and `root->right` using a temporary pointer.
5. **Return** `root`.

**Data Structure:** The implicit call stack (recursion) — no explicit auxiliary structure needed.

---

### 4. Production-Ready Implementation

```cpp
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

class Solution {
public:
    TreeNode* invertTree(TreeNode* root) {
        // Guard clause: empty tree or leaf node with no children to swap
        if (root == nullptr) {
            return nullptr;
        }

        // Recursively invert both subtrees first
        TreeNode* invertedLeft  = invertTree(root->left);
        TreeNode* invertedRight = invertTree(root->right);

        // Swap the child pointers at this node
        root->left  = invertedRight;
        root->right = invertedLeft;

        return root;
    }
};
```

---

### 5. Verification & Complexity Finalization

**Dry Run** — Input: `[4, 2, 7, 1, 3, 6, 9]`

```
Original:          Inverted:
      4                  4
     / \                / \
    2   7              7   2
   / \ / \            / \ / \
  1  3 6  9          9  6 3  1
```

Call trace (post-order):
1. `invertTree(1)` → no children → return node 1
2. `invertTree(3)` → no children → return node 3
3. `invertTree(2)`: left=1, right=3 → swap → left=3, right=1 → return node 2
4. `invertTree(6)` → no children → return node 6
5. `invertTree(9)` → no children → return node 9
6. `invertTree(7)`: left=6, right=9 → swap → left=9, right=6 → return node 7
7. `invertTree(4)`: left=2(inverted), right=7(inverted) → swap → left=7, right=2 → return node 4

Result: `[4, 7, 2, 9, 6, 3, 1]` ✓

**Final Complexity:**

| | Complexity | Reason |
|---|---|---|
| **Time** | O(n) | Every node is visited exactly once |
| **Space** | O(h) | Recursion stack depth equals tree height; O(log n) balanced, O(n) worst case |