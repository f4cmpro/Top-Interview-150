# 88 - Merge Sorted Array

**Difficulty**: 🟢 Easy  
**Topic**: Array / String  
**LeetCode**: https://leetcode.com/problems/merge-sorted-array/

---

## 📋 Problem Description

You are given two integer arrays `nums1` and `nums2`, sorted in non-decreasing order, and two integers `m` and `n`, representing the number of elements in `nums1` and `nums2` respectively.

Merge `nums1` and `nums2` into a single array sorted in non-decreasing order.

The final sorted array should not be returned by the function, but instead be stored inside the array `nums1`. To accommodate this, `nums1` has a length of `m + n`, where the first `m` elements denote the elements that should be merged, and the last `n` elements are set to 0 and should be ignored. `nums2` has a length of `n`.

---

## 💡 Examples

### Example 1:
```
Input: nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
Output: [1,2,2,3,5,6]
Explanation: The arrays we are merging are [1,2,3] and [2,5,6].
```

### Example 2:
```
Input: nums1 = [1], m = 1, nums2 = [], n = 0
Output: [1]
```

---

## 🔍 Constraints

- nums1.length == m + n
- nums2.length == n
- 0 <= m, n <= 200
- 1 <= m + n <= 200

---

## 🎯 Approach

### Initial Thoughts
Use two pointers starting from the end of both arrays to avoid overwriting elements.

### Algorithm
1. Start from the end of both arrays
2. Compare elements and place the larger one at the end of nums1
3. Move pointers accordingly

---

## 📊 Complexity Analysis

### Time Complexity
- **O(m + n)** - Single pass through both arrays

### Space Complexity
- **O(1)** - In-place modification

---

## 📝 Notes & Learnings

### Key Takeaways
- Working backwards avoids overwriting elements
- In-place merge is possible with proper pointer management

---

**Status**: 🚧 In Progress
