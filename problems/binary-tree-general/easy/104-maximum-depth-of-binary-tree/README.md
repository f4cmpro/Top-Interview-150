# 104 - Maximum Depth of Binary Tree

**Difficulty**: 🟢 Easy  
**LeetCode**: https://leetcode.com/problems/maximum-depth-of-binary-tree/

---

## Problem Description:
Given the `root` of a binary tree, return its **maximum depth**.

A binary tree's **maximum depth** is the number of nodes along the longest path from the root node down to the farthest leaf node.

**Example 1:**
```
Input: root = [3,9,20,null,null,15,7]
Output: 3
```

**Example 2:**
```
Input: root = [1,null,2]
Output: 2
```

**Constraints:**
- The number of nodes in the tree is in the range `[0, 10^4]`.
- `-100 <= Node.val <= 100`

---

## Solution Design

### 1. Clarification & Edge Cases:

**Key Constraints:**
- Input size: The tree can have between `0` and `10^4` nodes.
- Data range: Node values range from `-100` to `100` (values are irrelevant for depth — only structure matters).
- The tree is **not guaranteed to be balanced**; it can degenerate into a linked list (all nodes on one side).
- Because depth can be up to `10^4`, a recursive solution could reach a recursion depth of `10^4`, which is acceptable but worth noting for stack usage.

**Edge Cases to Handle:**
- **Empty tree:** `root == nullptr` → depth is `0`.
- **Single node:** Tree with only a root → depth is `1`.
- **Skewed tree:** All nodes lean left or right (e.g., `[1,2,null,3]`) → depth equals the number of nodes.
- **Unbalanced subtrees:** Left and right subtrees have different depths — must take the maximum.

---

### 2. High-Level Approach Analysis (Trade-offs):

#### **Brute Force / Naive Approach:**
- There is no meaningfully "wasteful" brute force here — the depth inherently requires visiting every node once.
- A naive attempt might compute the height by collecting all root-to-leaf path lengths into a list and then scanning for the maximum, which adds unnecessary bookkeeping.
- **Time Complexity:** O(n) — every node is still visited.
- **Space Complexity:** O(n) for storing all path lengths, plus O(h) recursion stack.

#### **Optimized Approach (Recursive DFS):**
- Use **Depth-First Search (post-order recursion)**.
- The depth of a node = `1 + max(depth(left), depth(right))`.
- Recurse to the leaves, then combine results on the way back up — no extra containers needed.
- **Time Complexity:** O(n) — each node visited exactly once.
- **Space Complexity:** O(h) — recursion stack proportional to tree height (`h`), where `h` ranges from `O(log n)` for a balanced tree to `O(n)` for a skewed tree.

#### **Comparison & Why Optimized is Better:**
Both approaches are O(n) in time because computing depth requires touching every node. The optimized recursive DFS wins on **space** and **simplicity**: it uses only the implicit call stack (O(h)) instead of an auxiliary list (O(n)), and the code directly mirrors the recursive definition of tree height, making it clean and easy to reason about.

> **Alternative:** An iterative **BFS (level-order)** using a queue also runs in O(n) time. Its space is O(w) where `w` is the maximum tree width (up to O(n) for the last level). BFS is preferable when the tree is extremely deep and you want to avoid stack overflow from deep recursion.

---

### 3. Algorithm Design:

**Step-by-Step Logic (Recursive DFS):**

1. **Base Case:**
   - If the current node is `nullptr`, return `0` (an empty subtree contributes no depth).

2. **Recurse on Children:**
   - Compute `leftDepth = maxDepth(node->left)`.
   - Compute `rightDepth = maxDepth(node->right)`.

3. **Combine Results:**
   - The depth at the current node is `1 + max(leftDepth, rightDepth)`.
   - The `+1` accounts for the current node itself.

4. **Return Up the Call Stack:**
   - Each frame returns its subtree depth to its parent until the root produces the final answer.

**Data Structure Choice — The Call Stack (Recursion):**
- **Why recursion?** Tree height is defined recursively, so a recursive DFS maps 1:1 to the problem definition.
- No explicit auxiliary data structure is required; the system call stack tracks the current path from root to the node being processed.
- **Alternative considered:** BFS with an explicit `queue` (counts levels iteratively) — avoids recursion-depth limits at the cost of O(w) queue space.

---

### 4. Production-Ready Implementation:

```cpp
#include <algorithm>

/**
 * Definition for a binary tree node.
 */
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right)
        : val(x), left(left), right(right) {}
};

class Solution {
public:
    int maxDepth(TreeNode* root) {
        // Guard clause: an empty tree (or empty subtree) has depth 0.
        if (root == nullptr) {
            return 0;
        }

        // Recursively compute the depth of each subtree.
        int leftDepth  = maxDepth(root->left);
        int rightDepth = maxDepth(root->right);

        // Current depth = 1 (this node) + the deeper of the two subtrees.
        return 1 + std::max(leftDepth, rightDepth);
    }
};
```

**Code Quality Features:**
- **Guard Clause:** `root == nullptr` cleanly handles the empty-tree edge case and doubles as the recursion base case.
- **Meaningful Variables:** `leftDepth` / `rightDepth` clearly convey intent.
- **Single Responsibility:** The function does exactly one thing — return the depth.
- **Standard Library:** Uses `std::max` for a safe, readable comparison.

---

### 5. Verification & Complexity Finalization:

#### **Dry Run with Example:**

**Input:** `root = [3, 9, 20, null, null, 15, 7]`

```
        3
       / \
      9   20
         /  \
        15   7
```

| Call            | node | leftDepth | rightDepth | returns              |
|-----------------|------|-----------|------------|----------------------|
| maxDepth(9)     | 9    | 0         | 0          | 1 + max(0,0) = **1** |
| maxDepth(15)    | 15   | 0         | 0          | 1 + max(0,0) = **1** |
| maxDepth(7)     | 7    | 0         | 0          | 1 + max(0,0) = **1** |
| maxDepth(20)    | 20   | 1 (15)    | 1 (7)      | 1 + max(1,1) = **2** |
| maxDepth(3)     | 3    | 1 (9)     | 2 (20)     | 1 + max(1,2) = **3** |

**Output:** `3` ✓

---

#### **Edge Case Dry Run:**

**Input:** `root = nullptr` (empty tree)

- Guard clause triggers immediately → returns `0`.

**Output:** `0` ✓

**Input:** `root = [1, null, 2]` (skewed right)

```
    1
     \
      2
```

| Call         | node | leftDepth | rightDepth | returns              |
|--------------|------|-----------|------------|----------------------|
| maxDepth(2)  | 2    | 0         | 0          | 1 + max(0,0) = **1** |
| maxDepth(1)  | 1    | 0         | 1 (2)      | 1 + max(0,1) = **2** |

**Output:** `2` ✓

---

#### **Final Complexity Analysis:**

**Time Complexity: O(n)**
- Each of the `n` nodes is visited exactly once, and each visit does O(1) work (a comparison and an addition).

**Space Complexity: O(h)**
- The recursion stack holds at most `h` frames, where `h` is the tree height.
- Balanced tree: `h = O(log n)` → best case.
- Skewed tree: `h = O(n)` → worst case.

**Trade-off:** Recursive DFS gives the cleanest, most direct solution with optimal O(n) time. If the tree is guaranteed to be very deep and stack overflow is a concern, switch to an iterative BFS (O(n) time, O(w) space).