# 82 - Remove Duplicates from Sorted List II

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/

---

## Problem Description

Given the `head` of a sorted linked list, delete all nodes that have duplicate numbers, leaving only **distinct** numbers from the original list. Return the linked list **sorted** as well.

**Example 1:**
```
Input:  1 → 2 → 3 → 3 → 4 → 4 → 5
Output: 1 → 2 → 5
```

**Example 2:**
```
Input:  1 → 1 → 1 → 2 → 3
Output: 2 → 3
```

**Constraints:**
- The number of nodes in the list is in the range `[0, 300]`.
- `-100 <= Node.val <= 200`
- The list is guaranteed to be sorted in ascending order.

---

## Solution Design

### 1. Clarification & Edge Cases

**Key Constraints:**
- List is **sorted** in ascending order — duplicates are always contiguous.
- Node values range from `-100` to `200`, and list length up to `300`.
- We must remove **all** occurrences of any duplicated value (not just the extras).

**Edge Cases:**
| Case | Example Input | Expected Output |
|---|---|---|
| Empty list | `[]` | `[]` |
| Single node | `[1]` | `[1]` |
| All nodes duplicated | `[1,1,2,2,3,3]` | `[]` |
| All nodes distinct | `[1,2,3]` | `[1,2,3]` |
| Duplicates at the head | `[1,1,2,3]` | `[2,3]` |
| Duplicates at the tail | `[1,2,3,3]` | `[1,2]` |
| Single duplicate group | `[1,1,1]` | `[]` |

---

### 2. High-Level Approach Analysis (Trade-offs)

#### Brute Force
Traverse the list, collect all node values into an array, count occurrences with a hash map, then rebuild the list using only values with count = 1.

- **Time:** O(n) — two passes
- **Space:** O(n) — extra hash map + array

#### Optimized Approach — Sentinel + Two-Pointer (In-Place)
Use a **sentinel (dummy) node** pointing to `head` so the head itself can be cleanly removed. Maintain a `prev` pointer to the last confirmed distinct node. For each group of equal nodes, if the group has more than one node, skip the entire group; otherwise link it to `prev`.

- **Time:** O(n) — single pass
- **Space:** O(1) — no auxiliary storage, purely pointer manipulation

**Why optimized is better:** The brute force allocates O(n) extra memory and rebuilds the list. The in-place approach uses constant extra space and only one traversal, which is both time- and cache-efficient.

---

### 3. Algorithm Design

1. Create a **sentinel node** with `next = head`. This eliminates special-casing when the head itself must be removed.
2. Set `prev = sentinel`. `prev` always points to the last node confirmed to be kept.
3. Enter the main loop. Let `curr = prev->next`.
4. **Detect duplicates:** Check if `curr->next` exists and `curr->val == curr->next->val`.
   - If **yes** (duplicate group found): advance `curr` until `curr->next` is null or has a different value (consuming the entire group). Then set `prev->next = curr->next` to skip the whole group.
   - If **no** (unique node): advance `prev = prev->next`.
5. In both cases advance `curr = prev->next` for the next iteration.
6. Return `sentinel->next`.

**Data Structure chosen:** Singly linked list with an extra sentinel node — O(1) space, no extra containers needed. The sorted property guarantees duplicates are adjacent, enabling a single linear scan.

---

### 4. Production-Ready Implementation

```cpp
#include <iostream>

struct ListNode {
    int val;
    ListNode* next;
    ListNode(int x) : val(x), next(nullptr) {}
};

class Solution {
public:
    ListNode* deleteDuplicates(ListNode* head) {
        // Guard clause: empty list or single node — nothing to remove
        if (!head || !head->next) return head;

        // Sentinel node simplifies edge cases where head must be removed
        ListNode sentinel(0);
        sentinel.next = head;

        ListNode* prev = &sentinel;   // last confirmed distinct node

        while (prev->next) {
            ListNode* curr = prev->next;

            // Check if curr starts a duplicate group
            if (curr->next && curr->val == curr->next->val) {
                // Advance curr past all nodes with the same value
                while (curr->next && curr->val == curr->next->val) {
                    curr = curr->next;
                }
                // Skip the entire duplicate group
                prev->next = curr->next;
                // Do NOT advance prev — the new prev->next must be re-evaluated
            } else {
                // curr is a unique node; confirm it and move prev forward
                prev = prev->next;
            }
        }

        return sentinel.next;
    }
};
```

---

### 5. Verification & Complexity Finalization

#### Dry Run: `1 → 2 → 3 → 3 → 4 → 4 → 5`

| Step | prev | curr | Action |
|---|---|---|---|
| Init | sentinel(0) | 1 | — |
| 1 | sentinel(0) | 1 | `1 != 2` → unique, advance `prev` to 1 |
| 2 | 1 | 2 | `2 != 3` → unique, advance `prev` to 2 |
| 3 | 2 | 3 | `3 == 3` → duplicate; advance `curr` to second 3; set `prev(2)->next = 4` |
| 4 | 2 | 4 | `4 == 4` → duplicate; advance `curr` to second 4; set `prev(2)->next = 5` |
| 5 | 2 | 5 | `5->next == null` → unique, advance `prev` to 5 |
| End | 5 | null | loop exits |

Result: `sentinel → 1 → 2 → 5` ✓

#### Dry Run: `1 → 1 → 1 → 2 → 3`

| Step | prev | curr | Action |
|---|---|---|---|
| Init | sentinel(0) | 1 | — |
| 1 | sentinel(0) | 1 | `1 == 1` → advance curr past all 1s (third 1); set `sentinel->next = 2` |
| 2 | sentinel(0) | 2 | `2 != 3` → unique, advance `prev` to 2 |
| 3 | 2 | 3 | `3->next == null` → unique, advance `prev` to 3 |
| End | 3 | null | loop exits |

Result: `sentinel → 2 → 3` ✓

#### Final Complexity

| | Complexity |
|---|---|
| **Time** | O(n) — each node is visited at most twice (once by `curr`, once as `curr->next` lookahead) |
| **Space** | O(1) — only a fixed number of pointers (`sentinel`, `prev`, `curr`); no additional data structures |