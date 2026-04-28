# 27 - Remove Element

**Difficulty**: 🟢 Easy  
**LeetCode**: https://leetcode.com/problems/remove-element/

---

## Problem Description

Given an integer array `nums` and an integer `val`, remove all occurrences of `val` **in-place**. The order of the elements may be changed. Return the number of elements in `nums` which are **not equal** to `val`.

The judge will check the first `k` elements of `nums` (where `k` is the returned value) to verify correctness.

---

## 1. Clarification & Edge Cases

**Constraints:**
- `0 <= nums.length <= 100`
- `0 <= nums[i] <= 50`
- `0 <= val <= 50`
- Modification must be **in-place** (O(1) extra space).

**Edge Cases:**
- Empty array (`nums = []`) → return `0`.
- `val` not present in `nums` → return `nums.size` unchanged.
- All elements equal `val` → return `0`.
- Single element array, either matching or not matching `val`.
- Array with all duplicate values equal to `val`.

---

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force
- Shift elements left every time a matching value is found.
- **Time Complexity:** O(n²) — for each removal, all subsequent elements are shifted.
- **Space Complexity:** O(1).

### Optimized Approach — Two Pointers
- Use a **slow pointer** (`writeIndex`) that tracks the position to write the next valid element.
- Use a **fast pointer** that scans each element.
- When `nums[fast] != val`, copy it to `nums[writeIndex]` and advance both.
- When `nums[fast] == val`, skip it (only advance fast).
- **Time Complexity:** O(n) — single pass through the array.
- **Space Complexity:** O(1) — in-place, no extra allocation.

**Why optimized is better:** Eliminates the O(n) shifting cost per removal by overwriting in a single linear scan.

---

## 3. Algorithm Design

1. Initialize `writeIndex = 0`.
2. Iterate `fast` from `0` to `nums.size - 1`:
   - If `nums[fast] != val`:
     - Set `nums[writeIndex] = nums[fast]`.
     - Increment `writeIndex`.
   - Otherwise, skip.
3. Return `writeIndex` — it represents the count of valid elements.

**Data Structure:** Two Pointers on an array — chosen because the problem requires in-place modification with O(1) space.

---

## 4. Production-Ready Implementation

```kotlin
class Solution {
    fun removeElement(nums: IntArray, `val`: Int): Int {
        // Guard clause: empty array
        if (nums.isEmpty()) return 0

        var writeIndex = 0

        for (fast in nums.indices) {
            // Only keep elements that are NOT equal to val
            if (nums[fast] != `val`) {
                nums[writeIndex] = nums[fast]
                writeIndex++
            }
        }

        return writeIndex
    }
}
```

---

## 5. Verification & Complexity Finalization

### Dry Run

**Input:** `nums = [3, 2, 2, 3]`, `val = 3`

| fast | nums[fast] | Action                        | writeIndex |
|------|------------|-------------------------------|------------|
| 0    | 3          | equals val, skip              | 0          |
| 1    | 2          | write nums[0]=2, writeIndex++ | 1          |
| 2    | 2          | write nums[1]=2, writeIndex++ | 2          |
| 3    | 3          | equals val, skip              | 2          |

**Result:** `writeIndex = 2`, `nums = [2, 2, ...]` ✅

### Complexity

| | Complexity |
|---|---|
| **Time** | O(n) — single pass |
| **Space** | O(1) — in-place, no extra data structures |
