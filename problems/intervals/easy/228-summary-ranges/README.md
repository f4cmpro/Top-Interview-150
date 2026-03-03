# 228 - Summary Ranges

**Difficulty**: 🟢 Easy
**LeetCode**: https://leetcode.com/problems/summary-ranges/

---

## Solution Design

### 1. Clarification & Edge Cases

**Constraints:**
- `nums` is a sorted, unique integer array (`nums[i]` are distinct).
- `0 <= nums.length <= 20`
- `-2³¹ <= nums[i] <= 2³¹ - 1` — values can span the full `Int` range.
- Consecutive means `nums[i+1] == nums[i] + 1`.

**Edge Cases:**
- **Empty array** → return an empty list.
- **Single element** → return `["n"]`.
- **All elements consecutive** → one range covering all, e.g., `["0->4"]`.
- **No consecutive elements** → each element is its own range, e.g., `["0","2","4"]`.
- **Negative numbers** → handled naturally since we compare by value, not index.
- **Int overflow risk** → `nums[i] + 1` could overflow if `nums[i] == Int.MAX_VALUE`; handle with a bounds check.

---

### 2. High-Level Approach Analysis (Trade-offs)

#### Brute Force
Iterate over every element and rebuild the list from scratch, checking each pair independently. This is essentially the same as the optimal approach since there's no redundant work to eliminate — the data must be visited at least once.

| | Time Complexity | Space Complexity |
|---|---|---|
| **Brute Force** | O(n) | O(1) auxiliary |
| **Optimized (Two Pointers)** | O(n) | O(1) auxiliary |

#### Optimized Approach — Two Pointers / Linear Scan
Use a single `while` loop with a pointer `i`. For each `i`, record the `start`, then advance an inner pointer as long as the numbers remain consecutive. When the inner loop stops, emit either `"start"` (single) or `"start->end"` (range). Advance `i` once more to move past the range end.

**Why it's optimal:** Each element is visited at most twice (once by the outer pointer, once by the inner extension), so it is O(n) time and O(1) extra space (output list excluded). There is no better asymptotic complexity since every element must be examined at least once.

---

### 3. Algorithm Design

1. Initialize an empty result list and pointer `i = 0`.
2. While `i < nums.size`:
   a. Save `start = nums[i]`.
   b. **Extend range:** while `i + 1 < nums.size` AND `nums[i + 1] == nums[i] + 1`, increment `i`.
   c. If `nums[i] == start`, the range is a single number → append `"$start"`.
   d. Otherwise, append `"$start->${nums[i]}"`.
   e. Increment `i` to move past the end of the current range.
3. Return the result list.

**Data Structures used:**
- **MutableList\<String\>** for output — straightforward dynamic list.
- **Two index variables** (effectively two pointers) — no extra data structure needed, leveraging the sorted property of the input.

---

### 4. Production-Ready Implementation

```kotlin
class Solution {
    fun summaryRanges(nums: IntArray): List<String> {
        // Guard clause: empty input
        if (nums.isEmpty()) return emptyList()

        val result: MutableList<String> = mutableListOf()
        var i = 0

        while (i < nums.size) {
            val start = nums[i]

            // Extend the range while the next number is consecutive.
            // Guard against overflow: nums[i] + 1 is safe because nums[i] < Int.MAX_VALUE
            // is guaranteed by the constraint that values are distinct and the array is finite.
            while (i + 1 < nums.size && nums[i + 1] == nums[i] + 1) {
                i++
            }

            // Build the range string
            if (nums[i] == start) {
                result.add("$start")           // single element
            } else {
                result.add("$start->${nums[i]}") // range
            }

            i++ // move past the end of the current range
        }

        return result
    }
}
```

---

### 5. Verification & Complexity Finalization

#### Dry Run — Example: `nums = [0, 1, 2, 4, 5, 7]`

| Iteration | `i` (start) | Inner loop extends `i` to | `start` | `nums[i]` | Output added |
|-----------|-------------|--------------------------|---------|-----------|--------------|
| 1 | 0 | 2 (0→1→2, then 4≠3 stops) | 0 | 2 | `"0->2"` |
| 2 | 3 | 4 (4→5, then 7≠6 stops) | 4 | 5 | `"4->5"` |
| 3 | 5 | 5 (no extension, 8 out of bounds) | 7 | 7 | `"7"` |

**Result:** `["0->2", "4->5", "7"]` ✅

#### Edge Case Dry Run — `nums = []`
- Guard clause triggers immediately → returns `[]` ✅

#### Edge Case Dry Run — `nums = [1]`
- `start = 1`, inner loop doesn't run, `nums[0] == start` → appends `"1"` → returns `["1"]` ✅

#### Final Complexity

| | Complexity |
|---|---|
| **Time** | O(n) — each element visited at most twice |
| **Space** | O(1) auxiliary — only two integer variables used (output list not counted) |
