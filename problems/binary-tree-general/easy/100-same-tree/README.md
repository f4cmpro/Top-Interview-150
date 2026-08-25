# 100 - Same Tree

**Difficulty**: 🟢 Easy  
**LeetCode**: https://leetcode.com/problems/same-tree/

---

## Problem Description

Given the roots of two binary trees `p` and `q`, write a function to check if they are the same or not.

Two binary trees are considered the same if they are structurally identical, and the nodes have the same value.

**Example 1:**
```
Input: p = [1,2,3], q = [1,2,3]
Output: true
```

**Example 2:**
```
Input: p = [1,2], q = [1,null,2]
Output: false
```

**Example 3:**
```
Input: p = [1,2,1], q = [1,1,2]
Output: false
```

**Constraints:**
- The number of nodes in both trees is in the range `[0, 100]`.
- `-10^4 <= Node.val <= 10^4`

---

## Solution Design

### 1. Clarification & Edge Cases

**Key Constraints:**
- Trees can have 0 to 100 nodes each.
- Node values range from -10,000 to 10,000 (negative values must be handled).
- Both `p` and `q` can independently be `null`.

**Edge Cases:**
- Both trees are `null` → return `true` (two empty trees are identical).
- One tree is `null` and the other is not → return `false`.
- Trees have the same values but different structure (e.g., `[1,2]` vs `[1,null,2]`) → return `false`.
- Single-node trees with different values → return `false`.

---

### 2. High-Level Approach Analysis (Trade-offs)

**Brute Force — Serialize Both Trees:**
Serialize both trees into arrays (e.g., level-order or pre-order with null markers), then compare the two arrays element by element.
- Time: O(n), Space: O(n) — but requires extra memory for the serialized arrays and added implementation complexity.

**Optimized Approach — Recursive DFS (Simultaneous Traversal):**
Traverse both trees simultaneously using recursion. At each step, check:
1. If both nodes are `null` → subtrees match at this point.
2. If exactly one is `null`, or the values differ → trees differ.
3. Otherwise, recurse on both left subtrees and both right subtrees.

| | Brute Force (Serialize) | Optimized (Recursive DFS) |
|---|---|---|
| Time Complexity | O(n) | O(n) |
| Space Complexity | O(n) extra | O(h) call stack (h = tree height) |

The recursive DFS is superior because it uses only O(h) stack space (O(log n) for balanced trees) instead of O(n) for serialization arrays, and terminates early as soon as a mismatch is detected.

---

### 3. Algorithm Design

**Logic (step-by-step):**

1. **Base case — both null:** If `p == null && q == null`, return `true`.
2. **Base case — one null:** If exactly one of `p` or `q` is `null`, return `false` (structural mismatch).
3. **Value check:** If `p->val != q->val`, return `false`.
4. **Recurse left:** Check whether `p->left` and `q->left` form the same subtree.
5. **Recurse right:** Check whether `p->right` and `q->right` form the same subtree.
6. Return `true` only if both recursive calls return `true`.

**Data Structure:** The implicit call stack of the recursive DFS is the only data structure used — no auxiliary containers needed.

---

### 4. Production-Ready Implementation

```cpp
#include <algorithm>

// Definition for a binary tree node (provided by LeetCode).
struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode* left, TreeNode* right) : val(x), left(left), right(right) {}
};

class Solution {
public:
    bool isSameTree(TreeNode* p, TreeNode* q) {
        // Guard clause: both nodes are null — subtrees are identical
        if (p == nullptr && q == nullptr) return true;

        // Guard clause: one node is null — structural mismatch
        if (p == nullptr || q == nullptr) return false;

        // Guard clause: node values differ
        if (p->val != q->val) return false;

        // Recursively verify left and right subtrees
        return isSameTree(p->left, q->left) &&
               isSameTree(p->right, q->right);
    }
};
```

---

### 5. Verification & Complexity Finalization

**Dry Run — Example 2:** `p = [1,2]`, `q = [1,null,2]`

```
Call isSameTree(p=1, q=1)
  p->val == q->val (1 == 1) ✓
  Call isSameTree(p->left=2, q->left=null)
    p != null, q == null → return false ✗
  Short-circuit: overall result = false ✓
```

Output: `false` — correct.

**Dry Run — Example 1:** `p = [1,2,3]`, `q = [1,2,3]`

```
Call isSameTree(1, 1): values match
  Call isSameTree(2, 2): values match
    Call isSameTree(null, null) → true
    Call isSameTree(null, null) → true
    return true
  Call isSameTree(3, 3): values match
    Call isSameTree(null, null) → true
    Call isSameTree(null, null) → true
    return true
  return true ✓
```

Output: `true` — correct.

**Final Complexity:**

| | Complexity |
|---|---|
| **Time** | O(n) — every node in both trees is visited exactly once |
| **Space** | O(h) — recursive call stack depth equals tree height h; O(log n) for balanced trees, O(n) worst case for skewed trees |
