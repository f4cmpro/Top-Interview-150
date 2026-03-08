# 56 - Merge Intervals

**Difficulty**: Medium
**LeetCode**: https://leetcode.com/problems/merge-intervals/

---

## Solution Design

### 1. Clarification & Edge Cases

**Key Constraints:**
- `1 <= intervals.length <= 10^4`
- `intervals[i].length == 2`
- `0 <= start_i <= end_i <= 10^4`
- Input is **not guaranteed** to be sorted.
- Two intervals `[a, b]` and `[c, d]` overlap when `c <= b`.

**Edge Cases:**
| Case | Example | Expected Output |
|---|---|---|
| Single interval | `[[1,3]]` | `[[1,3]]` |
| No overlaps at all | `[[1,2],[3,4]]` | `[[1,2],[3,4]]` |
| All intervals merge into one | `[[1,4],[2,5],[3,6]]` | `[[1,6]]` |
| Interval fully contained inside another | `[[1,10],[2,5]]` | `[[1,10]]` |
| Adjacent (touching) intervals | `[[1,3],[3,5]]` | `[[1,5]]` |
| Already sorted input | `[[1,3],[2,6],[8,10]]` | `[[1,6],[8,10]]` |

---

### 2. High-Level Approach Analysis (Trade-offs)

#### Brute Force
Compare every pair of intervals `(i, j)` and merge them if they overlap, then repeat until no more merges are possible.
- **Time Complexity:** O(n²) per pass, and multiple passes may be needed → O(n³) worst case.
- **Space Complexity:** O(n) for the result list.

#### Optimized Approach — Sort + Linear Scan
1. **Sort** intervals by their start value.
2. **Linearly scan** and extend the current interval's end while the next interval's start overlaps.

- **Time Complexity:** O(n log n) — dominated by sorting.
- **Space Complexity:** O(n) — for the output list.

**Why it's better:** After sorting, overlapping intervals are always adjacent, eliminating the need for nested comparisons. A single O(n) pass is then sufficient, giving an overall O(n log n) solution vs O(n³).

---

### 3. Algorithm Design

1. **Guard clause:** If there is only one interval, return it immediately — nothing to merge.
2. **Sort** the intervals array in-place by each interval's start value.
3. Initialize an empty `nonOverlaps` list to collect merged results, and a pointer `i = 0`.
4. For each interval at position `i`:
   - Record `start = intervals[i][0]` and `end = intervals[i][1]`.
   - **Inner loop:** While the *next* interval exists and its start `<= end` (overlap/touch), advance `i` and expand `end` to `max(end, intervals[i][1])`. This handles fully-contained intervals by only updating `end` when the next interval's end is larger.
   - Add `[start, end]` to `nonOverlaps`.
   - Advance `i` to the next unprocessed interval.
5. Return `nonOverlaps` as an array.

**Data Structure Used:** `MutableList<IntArray>` — dynamic list that allows O(1) append while building the result incrementally.

---

### 4. Production-Ready Implementation

```kotlin
package `056-merge-intervals`

class Solution {
    fun merge(intervals: Array<IntArray>): Array<IntArray> {
        // Guard clause: nothing to merge with a single interval
        if (intervals.size == 1) return intervals

        // Sort by start value so overlapping intervals become adjacent
        intervals.sortBy { it.first() }

        val nonOverlaps: MutableList<IntArray> = mutableListOf()
        var i = 0

        while (i < intervals.size) {
            val start = intervals[i][0]
            var end = intervals[i][1]

            // Extend range while consecutive intervals overlap or touch
            while (i + 1 < intervals.size && end >= intervals[i + 1][0]) {
                i++
                // Only expand end if the next interval reaches further right
                if (end < intervals[i][1]) {
                    end = intervals[i][1]
                }
            }

            nonOverlaps.add(intArrayOf(start, end))
            i++
        }

        return nonOverlaps.toTypedArray()
    }
}
```

---

### 5. Verification & Complexity Finalization

#### Dry Run
**Input:** `[[1,3],[2,6],[8,10],[15,18]]`

After sorting (already sorted): `[[1,3],[2,6],[8,10],[15,18]]`

| Outer `i` | `start` | `end` | Inner loop check | Action | `nonOverlaps` |
|---|---|---|---|---|---|
| 0 | 1 | 3 | `3 >= 2` (i=1) → end=6; `6 >= 8`? No | Add `[1,6]`, i=2 | `[[1,6]]` |
| 2 | 8 | 10 | `10 >= 15`? No | Add `[8,10]`, i=3 | `[[1,6],[8,10]]` |
| 3 | 15 | 18 | `i+1` out of bounds | Add `[15,18]`, i=4 | `[[1,6],[8,10],[15,18]]` |

**Output:** `[[1,6],[8,10],[15,18]]` ✅

#### Final Complexity
| | Complexity |
|---|---|
| **Time** | **O(n log n)** — sorting dominates; linear scan is O(n) |
| **Space** | **O(n)** — output list holds at most `n` merged intervals |
