# 19 - Remove Nth Node From End of List

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/remove-nth-node-from-end-of-list/

---

## Solution Design

### 1. Clarification & Edge Cases

**Constraints:**
- List length: `1 <= sz <= 30`
- Node values: `0 <= val <= 100`
- `1 <= n <= sz` (guaranteed valid `n`)

**Edge Cases:**
- `n == sz`: removing the **head** node (no previous node to update)
- Single-node list (`sz == 1`, `n == 1`): result is empty list
- `n == 1`: removing the **tail** node

---

### 2. High-Level Approach Analysis

| | Brute Force | Optimized (Two Pointers) |
|---|---|---|
| **Approach** | Two passes: first count length, second traverse to `(len - n)`th node | One pass: fast pointer leads slow pointer by `n` steps |
| **Time** | O(L) | O(L) |
| **Space** | O(1) | O(1) |

Both are O(L), but the two-pointer approach does it in a **single pass**, which is preferred in interviews and more elegant. The brute force requires knowing the length first, causing two full traversals.

---

### 3. Algorithm Design

**Two-Pointer (Fast & Slow) with a Dummy Head:**

1. Create a **dummy node** pointing to `head`. This eliminates special-casing head removal.
2. Initialize both `fast` and `slow` pointers at the dummy node.
3. Advance `fast` by `n + 1` steps (so that the gap between `fast` and `slow` is exactly `n`).
4. Move both `fast` and `slow` one step at a time until `fast` reaches `nullptr`.
5. Now `slow` points to the node **just before** the target. Set `slow->next = slow->next->next` to unlink it.
6. Return `dummy->next`.

**Why a dummy node?** It unifies the head-removal edge case — `slow` can always point to a valid predecessor.

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
    ListNode* removeNthFromEnd(ListNode* head, int n) {
        // Dummy node simplifies edge case where head itself is removed
        ListNode dummy(0);
        dummy.next = head;

        ListNode* fast = &dummy;
        ListNode* slow = &dummy;

        // Advance fast pointer by n+1 steps to create a gap of n between fast and slow
        for (int i = 0; i <= n; ++i) {
            fast = fast->next;
        }

        // Move both pointers until fast reaches the end
        while (fast != nullptr) {
            fast = fast->next;
            slow = slow->next;
        }

        // slow->next is the target node; unlink it
        ListNode* toDelete = slow->next;
        slow->next = slow->next->next;
        delete toDelete;

        return dummy.next;
    }
};
```

---

### 5. Verification & Complexity Finalization

**Dry Run** — Input: `[1 → 2 → 3 → 4 → 5]`, `n = 2`

```
dummy → 1 → 2 → 3 → 4 → 5 → nullptr
fast starts at dummy, advance n+1 = 3 steps:
  step 1: fast = 1
  step 2: fast = 2
  step 3: fast = 3

slow = dummy, fast = 3

Move both until fast == nullptr:
  fast=4, slow=1
  fast=5, slow=2
  fast=nullptr, slow=3

slow->next = 4 (the target), slow->next->next = 5
Unlink: slow->next = 5

Result: 1 → 2 → 3 → 5  ✓
```

**Head removal** — Input: `[1 → 2]`, `n = 2`
- fast advances 3 steps → `fast = nullptr` immediately after the loop
- `slow` stays at dummy, `slow->next = head (1)`
- Unlinks `1`, result: `[2]` ✓

**Final Complexity:**

| | Complexity |
|---|---|
| **Time** | O(L) — single pass through the list |
| **Space** | O(1) — only two pointers, dummy node on the stack |