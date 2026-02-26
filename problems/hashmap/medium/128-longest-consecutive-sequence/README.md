# 128 - Longest Consecutive Sequence

**Difficulty**: Medium
**LeetCode**: https://leetcode.com/problems/longest-consecutive-sequence/

---

## Solution Design

### 1. Clarification & Edge Cases

**Constraints:**
- `0 <= nums.length <= 10^5`
- `-10^9 <= nums[i] <= 10^9`
- Input is **unsorted**
- Must run in **O(n)** time

**Edge Cases:**
- Empty array `[]` — return `0`.
- Single element `[x]` — return `1`.
- All duplicates `[2, 2, 2]` — duplicates should be ignored; return `1`.
- Negative numbers `[-3, -2, -1, 0]` — valid consecutive sequence; return `4`.
- No consecutive numbers `[1, 3, 5, 7]` — every element is its own sequence; return `1`.
- Already sorted or reverse-sorted input — algorithm must not rely on order.

---

### 2. High-Level Approach Analysis (Trade-offs)

#### Brute Force
- Sort the array, then do a single linear scan counting consecutive runs.
- **Time:** O(n log n) — dominated by the sort.
- **Space:** O(1) extra (or O(n) if a copy is made), but sorting violates the O(n) requirement.

#### Optimized Approach ✅ — HashSet
- Load all numbers into a `HashSet` (O(1) average lookup, deduplication for free).
- For each number, only **start counting** if `num - 1` is **not** in the set (i.e., `num` is the beginning of a sequence).
- Extend the sequence forward by repeatedly checking `num + count` in the set.
- Track the global maximum length.
- **Time:** O(n) — each element is visited at most twice (once as a potential start, once during a forward extension).
- **Space:** O(n) — the HashSet stores all unique values.

**Why optimized is better:**
Sorting costs O(n log n). The HashSet approach achieves O(n) by using O(1) average-case lookups to avoid re-sorting. The "start of sequence" guard (`!contains(key - 1)`) ensures each number is counted in exactly one sequence walk, keeping the total work linear.

---

### 3. Algorithm Design

1. **Guard clause:** if `nums` is empty or has one element, return its size directly.
2. **Build the set:** Insert all elements of `nums` into a `HashSet<Int>`. Duplicates are automatically discarded.
3. **Iterate the set:** For each `key` in the set:
   - Skip it if `key - 1` is also in the set (it is not the start of a sequence).
   - Otherwise, initialize a `count = 1` and walk forward: while `key + count` is in the set, increment `count`.
   - Update `max = max(max, count)`.
4. Return `max`.

**Data Structures:**
- **HashSet** — O(1) average insert and lookup; provides O(n) overall time; handles deduplication automatically.

---

### 4. Production-Ready Implementation

```kotlin
class Solution {
    fun longestConsecutive(nums: IntArray): Int {
        // Guard clauses for trivial cases
        if (nums.isEmpty() || nums.size == 1) return nums.size

        // Build a set of all unique numbers for O(1) lookups
        val consecutiveSet = HashSet<Int>()
        for (num in nums) {
            consecutiveSet.add(num)   // duplicates are ignored automatically
        }

        var max = 1
        for (key in consecutiveSet) {
            // Only start counting from the beginning of a sequence
            if (consecutiveSet.contains(key - 1)) continue

            var count = 1
            // Extend the sequence forward as far as possible
            while (consecutiveSet.contains(key + count)) {
                count++
            }
            max = maxOf(max, count)
        }
        return max
    }
}
```

---

### 5. Verification & Complexity Finalization

#### Dry Run

**Input:** `[100, 4, 200, 1, 3, 2]`

Set after insertion: `{100, 4, 200, 1, 3, 2}`

| key | `key-1` in set? | count walk          | max |
|-----|----------------|---------------------|-----|
| 100 | 99 → No ✅      | 101 → No → count=1  | 1   |
| 4   | 3 → Yes ❌     | skipped             | 1   |
| 200 | 199 → No ✅    | 201 → No → count=1  | 1   |
| 1   | 0 → No ✅      | 2✓,3✓,4✓,5→No → count=4 | 4 |
| 3   | 2 → Yes ❌     | skipped             | 4   |
| 2   | 1 → Yes ❌     | skipped             | 4   |

**Output:** `4` ✅ (sequence: `1 → 2 → 3 → 4`)

#### Final Complexity

|           | Complexity                                              |
|-----------|---------------------------------------------------------|
| **Time**  | O(n) — each element is inserted once and walked at most once |
| **Space** | O(n) — HashSet stores all unique values                 |
