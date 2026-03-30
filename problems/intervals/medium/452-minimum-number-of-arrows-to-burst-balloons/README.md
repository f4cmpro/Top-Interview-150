# 452 - Minimum Number of Arrows to Burst Balloons

**Difficulty**: Medium
**LeetCode**: https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/

---

## 1. Clarification & Edge Cases

### Key constraints / clarifications
* `points[i] = [xStart, xEnd]` represents a balloon spanning the **inclusive** interval `[xStart, xEnd]` on the X-axis.
* An arrow shot at coordinate `x` bursts **all** balloons whose intervals contain `x` (i.e., `xStart <= x <= xEnd`).
* Input is not guaranteed to be sorted.
* Constraints (from LeetCode):
  * `1 <= points.length <= 10^5`
  * `points[i].length == 2`
  * `-2^31 <= xStart <= xEnd <= 2^31 - 1`

### Edge cases to handle
* **Empty input** (defensive programming): return `0`.
* **Single balloon**: answer is `1`.
* **Fully overlapping balloons**: answer is `1`.
* **No overlaps at all**: answer equals number of balloons.
* **Touching endpoints**: `[1,2]` and `[2,3]` overlap at `x=2`, so they can be burst with **one** arrow.
* **Very large / very small coordinates**: must avoid overflow-prone arithmetic in comparators.

---

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force (baseline)
Idea: try to pick arrow positions, simulate which balloons are burst, and repeat until all are removed.
* A naive version might:
  * repeatedly choose an arrow position (e.g., try all interval endpoints),
  * remove all balloons hit,
  * continue.
* This quickly becomes expensive because choices are combinatorial; even greedy-but-unsorted scans can degrade to `O(n^2)`.

**Time Complexity:** typically `O(n^2)` or worse depending on implementation.
**Space Complexity:** `O(1)` to `O(n)`.

### Optimized Approach (best solution): Greedy + Sorting by interval end
Key insight: if we always shoot the next arrow at the **earliest finishing** balloon’s end coordinate, we maximize how many upcoming balloons that arrow can also burst.

**Time Complexity:** `O(n log n)` due to sorting.
**Space Complexity:** `O(1)` extra (ignoring sort implementation details).

### Why the optimized approach is better
At `n = 10^5`, quadratic approaches are infeasible. Sorting once and doing a single greedy sweep is efficient and proven optimal for “minimum points to hit intervals” style problems.

---

## 3. Algorithm Design

We want the minimum number of arrows to “hit” all intervals.

### Greedy logic (step-by-step)
1. **Sort** all balloons by their end coordinate `xEnd` ascending.
2. Shoot the first arrow at the end of the first interval:
   * `currentArrowX = firstEnd`
   * `arrows = 1`
3. Sweep through the remaining intervals in sorted order:
   * If the next balloon starts at `xStart` such that `xStart <= currentArrowX`, it is already burst by the current arrow — do nothing.
   * Otherwise (`xStart > currentArrowX`), it does **not** overlap the current arrow position. We must shoot a new arrow:
	 * `arrows++`
	 * `currentArrowX = this balloon's xEnd`
4. Return `arrows`.

### Data structures used
* Sorting the input array of `IntArray`.
* Constant extra variables (`currentArrowX`, `arrows`).

---

## 4. Production-Ready Implementation (Kotlin)

```kotlin
class Solution {
	fun findMinArrowShots(points: Array<IntArray>): Int {
		// Guard clause (defensive): if there are no balloons, no arrows are needed.
		if (points.isEmpty()) return 0

		// Sort by end coordinate (xEnd) ascending.
		// Use compareValues to avoid overflow that can happen with subtraction-based comparators.
		points.sortWith { a, b -> compareValues(a[1], b[1]) }

		var arrows = 1
		var currentArrowX = points[0][1]

		for (i in 1 until points.size) {
			val start = points[i][0]
			val end = points[i][1]

			// If the balloon starts after the current arrow position,
			// it cannot be burst by the existing arrow (no overlap).
			if (start > currentArrowX) {
				arrows++
				currentArrowX = end
			}
		}

		return arrows
	}
}
```

---

## 5. Verification & Complexity Finalization

### Dry Run
Example: `points = [[10,16],[2,8],[1,6],[7,12]]`

1. Sort by end:
   * `[[1,6], [2,8], [7,12], [10,16]]`
2. Initialize:
   * `arrows = 1`, `currentArrowX = 6`
3. Iterate:
   * `[2,8]`: start `2 <= 6` → overlaps → keep `arrows = 1`
   * `[7,12]`: start `7 > 6` → no overlap → new arrow
	 * `arrows = 2`, `currentArrowX = 12`
   * `[10,16]`: start `10 <= 12` → overlaps → keep `arrows = 2`
4. Return `2` ✅

Edge check (touching endpoints): `[[1,2],[2,3]]`
* Sort by end: same
* `currentArrowX = 2`
* second start `2 <= 2` → overlaps at `x=2` → answer `1` ✅

### Final Complexity
* **Time:** `O(n log n)` for sorting + `O(n)` sweep → `O(n log n)`
* **Space:** `O(1)` extra space (input is sorted in-place; sorting may use internal stack space depending on implementation)
