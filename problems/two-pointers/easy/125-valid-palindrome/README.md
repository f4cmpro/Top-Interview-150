# 125 - Valid Palindrome

**Difficulty**: 🟢 Easy  
**LeetCode**: https://leetcode.com/problems/valid-palindrome/

## Problem Statement
A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward. Given a string `s`, return `true` if it is a palindrome, or `false` otherwise.

## Solution Design

### Approach: Two Pointers
The solution uses the **two-pointer technique** to efficiently check if the string is a palindrome by comparing characters from both ends moving towards the center.

### Algorithm Steps
1. **Initialize two pointers**: 
   - `left` starts at the beginning (index 0)
   - `right` starts at the end (index s.length - 1)

2. **Convert string to character array** for easy access to individual characters

3. **Iterate while left < right**:
   - **Skip non-alphanumeric characters** from the left side by incrementing `left`
   - **Skip non-alphanumeric characters** from the right side by decrementing `right`
   - **Compare valid characters**:
     - Convert both characters to lowercase
     - If they match: move both pointers inward (`left++`, `right--`)
     - If they don't match: return `false` (not a palindrome)

4. **Return true** if all comparisons pass (palindrome confirmed)

### Key Features
- ✅ **In-place comparison**: No extra space needed for filtered string
- ✅ **Case-insensitive**: Converts characters to lowercase before comparison
- ✅ **Handles non-alphanumeric**: Skips special characters and spaces
- ✅ **Early termination**: Returns false immediately on mismatch

### Complexity Analysis
- **Time Complexity**: O(n)
  - Single pass through the string with two pointers
  - Each character is visited at most once
  
- **Space Complexity**: O(n)
  - Converting string to character array requires O(n) space
  - Could be optimized to O(1) by directly accessing string characters

### Example Walkthrough
**Input**: `"A man, a plan, a canal: Panama"`

```
Step 1: left=0 ('A'), right=30 ('a') → 'a' == 'a' ✓
Step 2: left=1 (' '), skip non-alphanumeric
Step 3: left=2 ('m'), right=29 ('m') → 'm' == 'm' ✓
Step 4: left=3 ('a'), right=28 ('a') → 'a' == 'a' ✓
...continues until pointers meet...
Result: true
```

### Edge Cases Handled
- ✅ Empty string → returns `true`
- ✅ Single character → returns `true`
- ✅ Only non-alphanumeric characters → returns `true`
- ✅ Mixed case letters → handled by lowercase conversion
