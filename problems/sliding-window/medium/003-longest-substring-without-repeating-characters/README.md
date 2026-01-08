# 3 - Longest Substring Without Repeating Characters

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/longest-substring-without-repeating-characters/

---

## Problem Description

Given a string `s`, find the length of the **longest substring** without repeating characters.

**Example 1:**
```
Input: s = "abcabcbb"
Output: 3
Explanation: The answer is "abc", with the length of 3.
```

**Example 2:**
```
Input: s = "bbbbb"
Output: 1
Explanation: The answer is "b", with the length of 1.
```

**Example 3:**
```
Input: s = "pwwkew"
Output: 3
Explanation: The answer is "wke", with the length of 3.
Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
```

**Constraints:**
- `0 <= s.length <= 5 * 10^4`
- `s` consists of English letters, digits, symbols and spaces.

---

## Solution Design

### 1. Clarification & Edge Cases

**Key Constraints:**
- Input size: String length can be from 0 to 50,000 characters
- Character set: ASCII characters (letters, digits, symbols, spaces) - 128 possible characters
- Must find substring (contiguous characters), not subsequence
- Need to return the length, not the actual substring

**Edge Cases to Handle:**
1. **Empty string**: `s = ""` → return 0
2. **Single character**: `s = "a"` → return 1
3. **All unique characters**: `s = "abcdef"` → return 6
4. **All repeating characters**: `s = "aaaa"` → return 1
5. **Repeating pattern**: `s = "abcabcbb"` → return 3
6. **Mixed with spaces and special characters**: `s = "a b!c"` → need to treat all as valid characters

---

### 2. High-Level Approach Analysis (Trade-offs)

#### Brute Force Approach:
- Generate all possible substrings of the input string
- For each substring, check if all characters are unique using a Set or frequency map
- Track the maximum length found

**Complexity:**
- **Time**: O(n³) - O(n²) to generate all substrings, O(n) to check each for uniqueness
- **Space**: O(min(n, m)) where m is the character set size (for the set to check uniqueness)

#### Optimized Approach (Sliding Window):
- Use a sliding window technique with two pointers (left and right)
- Maintain a data structure to track the last seen position of each character
- Expand the window by moving right pointer, and shrink it when duplicates are found
- Track the maximum window size encountered

**Complexity:**
- **Time**: O(n) - Single pass through the string with right pointer, left pointer moves at most n times total
- **Space**: O(min(n, m)) where m is the character set size (128 for ASCII)

**Why Optimized is Better:**
The sliding window approach avoids redundant checks by:
1. **Single pass**: We traverse the string only once with the right pointer
2. **Intelligent shrinking**: Instead of checking every substring, we only adjust the window when we encounter a duplicate
3. **O(1) lookups**: Using an array indexed by character code gives constant-time lookups
4. **No redundant recalculation**: We don't re-check characters we've already processed

This reduces time complexity from O(n³) to O(n), making it drastically faster for large inputs.

---

### 3. Algorithm Design

**Data Structure:** Integer array of size 128 (for ASCII characters) to store the last seen index of each character. Initialized to -1 to indicate "not seen yet".

**Algorithm Steps:**

1. **Initialize:**
   - Create an integer array `ascii` of size 128, filled with -1
   - Set `left` pointer to 0 (start of current window)
   - Set `maxLength` to 0 (tracks the answer)

2. **Iterate with right pointer** (0 to n-1):
   - For each character at position `right`:
     - Get the character's last seen position from `ascii` array
     - If the character was seen within the current window (lastPos >= left):
       - Move `left` pointer to `lastPos + 1` to exclude the duplicate
     - Update the character's last seen position to `right` in the `ascii` array
     - Calculate current window size: `right - left + 1`
     - Update `maxLength` if current window is larger

3. **Return** `maxLength`

**Why This Works:**
- The `left` pointer always points to the start of a valid substring (no duplicates)
- When we find a duplicate, we skip directly to the position after the previous occurrence
- Using `max(left, lastPos + 1)` ensures we never move `left` backwards, maintaining window validity
- The array indexed by character code gives O(1) lookup and update operations

**Why Integer Array Instead of HashMap:**
- ASCII has only 128 characters (or 256 for extended ASCII)
- Array access is O(1) and faster than HashMap operations
- Fixed size means predictable memory usage
- No hash collisions or overhead

---

### 4. Production-Ready Implementation

```kotlin
import kotlin.math.max

class Solution {
    fun lengthOfLongestSubstring(s: String): Int {
        // Guard clause: handle empty string
        if (s.isEmpty()) return 0
        
        // ASCII array to store last seen index of each character
        // Initialized to -1 to indicate "not seen"
        val ascii = IntArray(128) { -1 }
        
        var left = 0        // Left boundary of sliding window
        var maxLength = 0   // Maximum length found so far
        
        // Expand window with right pointer
        for (right in s.indices) {
            val char = s[right]
            val charLatestPos = ascii[char.code]
            
            // If character was seen in current window, move left pointer
            // to exclude the previous occurrence
            left = max(left, charLatestPos + 1)
            
            // Update the last seen position of current character
            ascii[char.code] = right
            
            // Calculate current window size and update max
            maxLength = max(maxLength, right - left + 1)
        }
        
        return maxLength
    }
}
```

**Code Quality Features:**
- **Guard clause**: Handles empty string at the start
- **Meaningful names**: `left`, `right`, `maxLength`, `charLatestPos` clearly indicate their purpose
- **Comments**: Explain the purpose of each section and complex logic
- **Efficiency**: Uses array instead of HashMap for O(1) operations
- **Clean logic**: Single responsibility per statement, easy to follow

---

### 5. Verification & Complexity Finalization

#### Dry Run with Example: `s = "abcabcbb"`

| Step | right | char | charLatestPos | left (before) | left (after) | Window | maxLength |
|------|-------|------|---------------|---------------|--------------|---------|-----------|
| Init | -     | -    | -             | 0             | 0            | ""      | 0         |
| 1    | 0     | 'a'  | -1            | 0             | max(0,0)=0   | "a"     | 1         |
| 2    | 1     | 'b'  | -1            | 0             | max(0,0)=0   | "ab"    | 2         |
| 3    | 2     | 'c'  | -1            | 0             | max(0,0)=0   | "abc"   | 3         |
| 4    | 3     | 'a'  | 0             | 0             | max(0,1)=1   | "bca"   | 3         |
| 5    | 4     | 'b'  | 1             | 1             | max(1,2)=2   | "cab"   | 3         |
| 6    | 5     | 'c'  | 2             | 2             | max(2,3)=3   | "abc"   | 3         |
| 7    | 6     | 'b'  | 4             | 3             | max(3,5)=5   | "cb"    | 3         |
| 8    | 7     | 'b'  | 6             | 5             | max(5,7)=7   | "b"     | 3         |

**Result**: 3 ✓ (Correct! The longest substring without repeating characters is "abc")

#### Edge Case Verification:

1. **Empty string** `""`: Guard clause returns 0 immediately ✓
2. **Single character** `"a"`: Window size becomes 1, returns 1 ✓
3. **All unique** `"abcdef"`: Window keeps expanding, returns 6 ✓
4. **All same** `"aaaa"`: Left keeps jumping to right, maxLength stays 1 ✓

#### Final Complexity Analysis:

**Time Complexity: O(n)**
- Single pass through the string with the `right` pointer: O(n)
- The `left` pointer moves at most n times across the entire execution
- Each character is visited at most twice (once by right, once by left)
- Array operations (read/write) are O(1)
- Total: O(2n) = O(n)

**Space Complexity: O(1)**
- The `ascii` array has fixed size 128, regardless of input size
- Other variables (`left`, `maxLength`, etc.) use constant space
- O(128) = O(1) constant space

**Note**: If we consider the character set size `m`, the space complexity is O(min(n, m)), but since ASCII is fixed at 128, it's effectively O(1).
