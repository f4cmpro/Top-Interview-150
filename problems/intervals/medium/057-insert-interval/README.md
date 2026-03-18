# 57 - Insert Interval

**Difficulty**: Medium
**LeetCode**: https://leetcode.com/problems/insert-interval/

---

**Act as a Senior Software Engineer and Technical Interviewer at a Big Tech company.**

I need a comprehensive **Solution Design** for the following DSA problem:

## Problem Description:
> You are given an array of non-overlapping intervals `intervals` where `intervals[i] = [starti, endi]` represent the start and the end of the `i`th interval and `intervals` is sorted in ascending order by `starti`. You are also given an interval `newInterval = [start, end]` that represents the start and end of another interval.
>
> Insert `newInterval` into `intervals` such that `intervals` is still sorted in ascending order by `starti` and `intervals` still does not have any overlapping intervals (merge overlapping intervals if necessary).
>
> Return `intervals` after the insertion.
> https://leetcode.com/problems/insert-interval/

**Preferred Language:** Kotlin

---

### 1. Clarification & Edge Cases:
*   **Key Constraints:**
    *   `intervals` is sorted by the start time.
    *   `intervals` are non-overlapping.
    *   Input `intervals` can be empty.
    *   `0 <= intervals.length <= 10^4`
    *   `intervals[i].length == 2`
    *   `0 <= starti <= endi <= 10^5`
    *   `newInterval.length == 2`
    *   `0 <= start <= end <= 10^5`
*   **Edge Cases:**
    *   `intervals` is empty: The result should be an array containing just `newInterval`.
    *   `newInterval` is completely before all other intervals.
    *   `newInterval` is completely after all other intervals.
    *   `newInterval` completely contains an existing interval.
    *   An existing interval completely contains `newInterval`.
    *   `newInterval` overlaps with multiple intervals.

### 2. High-Level Approach Analysis (Trade-offs):
*   **Brute Force Approach:**
    A brute-force way would be to add `newInterval` to the list of intervals and then sort the entire list based on the start times. After sorting, iterate through the list and merge any overlapping intervals. Sorting would take O(N log N) time, and the merging pass would take O(N) time.

*   **Optimized Approach (Single Pass):**
    Since the initial `intervals` array is already sorted, we can avoid a full resort. We can iterate through the intervals and build a new list of merged intervals in a single pass. We handle three cases: intervals that come before `newInterval` (no overlap), intervals that overlap with `newInterval` (merge them), and intervals that come after `newInterval` (no overlap).

*   **Comparison:**
    *   **Time Complexity:** The brute force is O(N log N) due to sorting. The optimized approach is O(N) because we iterate through the list only once.
    *   **Space Complexity:** Both approaches use O(N) space to store the result.
    *   **Why Optimized is Better:** The optimized approach leverages the pre-sorted nature of the input array to achieve a linear time complexity, which is significantly faster than the O(N log N) of the brute-force method for large inputs.

### 3. Algorithm Design:
The logic of the optimized solution is as follows:
1.  Initialize an empty list, `ans`, to store the resulting intervals.
2.  Initialize a variable `i` to 0 to iterate through `intervals`.
3.  **Phase 1: Add all intervals that end before `newInterval` starts.**
    *   Iterate through `intervals` as long as the current interval's end is less than `newInterval`'s start.
    *   Add these intervals directly to `ans`.
4.  **Phase 2: Merge overlapping intervals.**
    *   Iterate through `intervals` as long as the current interval's start is less than or equal to `newInterval`'s end. This condition identifies all overlapping or adjacent intervals.
    *   For each overlapping interval, update `newInterval` to be the union of the current `newInterval` and the overlapping interval. This is done by setting `newInterval.start = min(newInterval.start, currentInterval.start)` and `newInterval.end = max(newInterval.end, currentInterval.end)`.
    *   After the loop, add the merged `newInterval` to `ans`.
5.  **Phase 3: Add all remaining intervals.**
    *   Iterate through the rest of the `intervals` and add them to `ans`.
6.  Convert the `ans` list to an array and return it.

**Data Structures:**
*   A `mutableListOf<IntArray>` is used for `ans` because its size is not known beforehand, and it allows for efficient addition of elements.

### 4. Production-Ready Implementation:
```kotlin
package `057-insert-interval`

class Solution {
    fun insert(intervals: Array<IntArray>, newInterval: IntArray): Array<IntArray> {
        val ans = mutableListOf<IntArray>()
        var i = 0
        var start = newInterval[0]
        var end = newInterval[1]
        val n = intervals.size

        // Case 1: No overlapping before merging intervals
        // Add all intervals that end before the new interval starts
        while(i < n && intervals[i][1] < start){
            ans.add(intervals[i])
            i++
        }

        // Case 2: Overlapping and merging intervals
        // Merge all overlapping intervals with the new interval
        while(i < n && intervals[i][0] <= end){
            start = minOf(intervals[i][0], start)
            end = maxOf(intervals[i][1], end)
            i++
        }
        // Add the merged interval
        ans.add(intArrayOf(start, end))

        // Case 3: No overlapping after merging newInterval
        // Add all remaining intervals
        while(i < n){
            ans.add(intervals[i])
            i++
        }

        return ans.toTypedArray()
    }
}
```

### 5. Verification & Complexity Finalization:
*   **Dry Run:**
    *   `intervals` = `[[1,2],[3,5],[6,7],[8,10],[12,16]]`, `newInterval` = `[4,8]`
    *   **Phase 1:** `intervals[0]` is `[1,2]`. `2 < 4`, so add `[1,2]` to `ans`. `ans` is `[[1,2]]`. `i` is 1.
    *   **Phase 2:**
        *   `i=1`: `intervals[1]` is `[3,5]`. `3 <= 8`. Overlap. `start` = `min(3,4)` -> `3`. `end` = `max(5,8)` -> `8`. `newInterval` is now effectively `[3,8]`. `i` is 2.
        *   `i=2`: `intervals[2]` is `[6,7]`. `6 <= 8`. Overlap. `start` = `min(6,3)` -> `3`. `end` = `max(7,8)` -> `8`. `newInterval` is still `[3,8]`. `i` is 3.
        *   `i=3`: `intervals[3]` is `[8,10]`. `8 <= 8`. Overlap. `start` = `min(8,3)` -> `3`. `end` = `max(10,8)` -> `10`. `newInterval` is now `[3,10]`. `i` is 4.
        *   `i=4`: `intervals[4]` is `[12,16]`. `12 > 10`. Loop terminates.
    *   Add the merged interval `[3,10]` to `ans`. `ans` is `[[1,2], [3,10]]`.
    *   **Phase 3:**
        *   `i=4`: `intervals[4]` is `[12,16]`. Add it to `ans`. `ans` is `[[1,2], [3,10], [12,16]]`. `i` is 5.
    *   Loop terminates.
    *   Return `ans.toTypedArray()` -> `[[1,2], [3,10], [12,16]]`. Correct.

*   **Time Complexity:** **O(N)**, where N is the number of intervals, because we iterate through the list of intervals once.
*   **Space Complexity:** **O(N)**, as we use a list to store the resulting intervals. In the worst case, no intervals are merged, and the list will contain N+1 intervals.
