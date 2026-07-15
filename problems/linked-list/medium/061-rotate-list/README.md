# 61 - Rotate List

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/rotate-list/

---

## 1. Clarification & Edge Cases

### Key Constraints
- The list length is in range `[0, 500]`.
- Node values are typically in range `[-100, 100]` (value range does not affect pointer logic).
- `k` can be large (up to about `2 * 10^9`), so repeated one-step rotation is inefficient.
- This is a singly linked list, so backward traversal is not available.

### Edge Cases to Handle
- Empty list (`head == nullptr`) -> return `head`.
- Single-node list -> rotation has no visible effect.
- `k == 0` -> no rotation.
- `k` is a multiple of list length (`k % n == 0`) -> list remains unchanged.
- Very large `k` -> reduce with modulo before rotating.

---

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force Approach
Rotate right by 1, exactly `k` times:
1. Find tail and node before tail.
2. Detach tail and place it at head.
3. Repeat `k` times.

Complexity:
- Time: `O(k * n)` (or `O((k % n) * n)` if modulo optimization is added)
- Space: `O(1)`

### Optimized Approach (Best Solution)
1. Compute list length `n` and keep pointer to tail.
2. Reduce rotations: `r = k % n`.
3. If `r == 0`, return original head.
4. Find new tail at position `n - r` (1-based).
5. New head is `newTail->next`.
6. Connect old tail to old head, then break after new tail.

Complexity:
- Time: `O(n)`
- Space: `O(1)`

Why optimized is better:
- It avoids repeated full traversals.
- Even for very large `k`, work is bounded by a constant number of linear passes.

---

## 3. Algorithm Design

We use pointer manipulation on a singly linked list.

Step-by-step:
1. Guard clauses:
   - If list is empty, one node, or `k == 0`, return immediately.
2. Traverse once to:
   - Count total nodes `n`.
   - Keep pointer to final node (`tail`).
3. Compute `r = k % n`.
4. If `r == 0`, no structural change is needed.
5. Compute how many steps from head to reach new tail:
   - `stepsToNewTail = n - r - 1` (0-based movement from current head).
6. Move pointer to `newTail`.
7. Set `newHead = newTail->next`.
8. Make rotation by reconnecting pointers:
   - `tail->next = head` (temporarily circular)
   - `newTail->next = nullptr` (break circle)
9. Return `newHead`.

Data structures used:
- Singly linked list pointers only (`head`, `tail`, `newTail`, `newHead`).
- No auxiliary container needed.

---

## 4. Production-Ready Implementation (C++)

```cpp
/**
 * Definition for singly-linked list.
 * struct ListNode {
 *     int val;
 *     ListNode *next;
 *     ListNode() : val(0), next(nullptr) {}
 *     ListNode(int x) : val(x), next(nullptr) {}
 *     ListNode(int x, ListNode *next) : val(x), next(next) {}
 * };
 */
class Solution {
public:
	ListNode* rotateRight(ListNode* head, int k) {
		// Guard clauses for trivial cases.
		if (head == nullptr || head->next == nullptr || k == 0) {
			return head;
		}

		// Pass 1: compute length and get tail.
		int n = 1;
		ListNode* tail = head;
		while (tail->next != nullptr) {
			tail = tail->next;
			n++;
		}

		// Reduce rotations to avoid unnecessary work.
		int r = k % n;
		if (r == 0) {
			return head;
		}

		// Find new tail: (n - r - 1) steps from current head.
		int stepsToNewTail = n - r - 1;
		ListNode* newTail = head;
		for (int i = 0; i < stepsToNewTail; i++) {
			newTail = newTail->next;
		}

		// Rewire pointers to rotate.
		ListNode* newHead = newTail->next;
		tail->next = head;
		newTail->next = nullptr;

		return newHead;
	}
};
```

---

## 5. Verification & Complexity Finalization

### Dry Run
Example: `head = [1,2,3,4,5]`, `k = 2`

1. `n = 5`, `tail = 5`.
2. `r = 2 % 5 = 2`.
3. `stepsToNewTail = 5 - 2 - 1 = 2`.
4. Move 2 steps from head:
   - start at `1`
   - step 1 -> `2`
   - step 2 -> `3` (`newTail = 3`)
5. `newHead = 4`.
6. Link `tail->next = head`: `5 -> 1`.
7. Cut at `newTail`: `3->next = nullptr`.
8. Final list: `[4,5,1,2,3]`.

### Final Complexity
- Time Complexity: `O(n)`
- Space Complexity: `O(1)`