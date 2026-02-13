# 242 - Valid Anagram

**Difficulty**: Easy
**LeetCode**: https://leetcode.com/problems/valid-anagram/

---

## Problem Description:
Given two strings `s` and `t`, return `true` if `t` is an anagram of `s`, and `false` otherwise.

An **Anagram** is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.

**Example 1:**
```
Input: s = "anagram", t = "nagaram"
Output: true
```

**Example 2:**
```
Input: s = "rat", t = "car"
Output: false
```

**Constraints:**
* `1 <= s.length, t.length <= 5 * 10^4`
* `s` and `t` consist of lowercase English letters.

---

## 1. Clarification & Edge Cases:

**Key Constraints:**
* Input size: Both strings can be up to 50,000 characters
* Character set: Only lowercase English letters (26 characters: 'a' to 'z')
* Both strings are non-empty (minimum length is 1)

**Edge Cases to Handle:**
1. **Different lengths**: If `s` and `t` have different lengths, they cannot be anagrams → return `false` immediately
2. **Empty strings**: According to constraints, strings are at least length 1, but defensive programming suggests handling this case
3. **Single character**: `s = "a", t = "a"` → should return `true`
4. **Same string**: `s = "abc", t = "abc"` → should return `true` (anagram of itself)
5. **All same characters**: `s = "aaa", t = "aaa"` → should return `true`
6. **Case sensitivity**: Problem states lowercase only, but good to verify input assumptions

---

## 2. High-Level Approach Analysis (Trade-offs):

### Brute Force Approach:
**Method:** Sort both strings and compare if they are equal.
* Convert both strings to character arrays
* Sort both arrays
* Compare if the sorted arrays are identical

**Complexity:**
* **Time:** O(n log n) where n is the length of the string (dominated by sorting)
* **Space:** O(n) for creating character arrays (or O(log n) to O(n) depending on sorting algorithm)

### Optimized Approach (Best Solution):
**Method:** Use a frequency counter (hash map or array) to track character occurrences.
* Use a fixed-size array of 26 integers (for 26 lowercase letters)
* Iterate through string `s` and increment the count for each character
* Iterate through string `t` and decrement the count for each character
* If all counts are zero at the end, the strings are anagrams

**Complexity:**
* **Time:** O(n) where n is the length of the string (two linear passes)
* **Space:** O(1) - fixed array of 26 integers (constant space, independent of input size)

### Why Optimized is Better:
The optimized approach is **better** because:
1. **Time Efficiency**: O(n) vs O(n log n) - linear time is significantly faster for large inputs
2. **Space Efficiency**: O(1) vs O(n) - constant space using fixed array vs sorting that requires additional space
3. **Single Pass Philosophy**: We can validate as we go (early exit if count goes negative)
4. **Cache Friendly**: Fixed array of 26 integers is more cache-efficient than sorting operations

---

## 3. Algorithm Design:

### Optimized Solution Logic:

**Data Structure:** Fixed-size integer array of size 26 (representing 'a' to 'z')

**Step-by-Step Algorithm:**

1. **Early Exit Guard Clause**: 
   - Check if `s.length != t.length`
   - If different, return `false` immediately (cannot be anagrams)

2. **Initialize Frequency Counter**:
   - Create an array `letters` of size 26, initialized to 0
   - Each index represents a letter: index 0 = 'a', index 1 = 'b', ..., index 25 = 'z'

3. **Count Characters in String `s`**:
   - For each character in `s`:
     - Calculate its index: `char.code - 'a'.code`
     - Increment the counter at that index: `letters[index]++`
   - This builds a frequency map of all characters in `s`

4. **Validate Against String `t` (with Early Exit)**:
   - For each character in `t`:
     - Calculate its index: `char.code - 'a'.code`
     - Decrement the counter at that index: `letters[index]--`
     - **Early Exit**: If count becomes negative, `t` has more of this character than `s` → return `false`
   - If we complete the loop without early exit, continue to final check

5. **Final Verification**:
   - Verify that all counts are zero: `letters.sum() == 0`
   - If sum is 0, all characters matched perfectly → return `true`
   - Otherwise, return `false`

**Why This Works:**
- Incrementing for `s` and decrementing for `t` means perfect anagrams will result in all zeros
- Early exit optimization catches mismatches sooner
- The length check ensures we don't miss cases where `s` has extra characters

---

## 4. Production-Ready Implementation:

```kotlin
package validAnagram

class Solution {
    /**
     * Determines if two strings are anagrams of each other.
     * 
     * An anagram is a word formed by rearranging the letters of another word,
     * using all original letters exactly once.
     * 
     * Time Complexity: O(n) where n is the length of the strings
     * Space Complexity: O(1) - fixed array of 26 integers
     * 
     * @param s First string (lowercase English letters only)
     * @param t Second string (lowercase English letters only)
     * @return true if t is an anagram of s, false otherwise
     */
    fun isAnagram(s: String, t: String): Boolean {
        // Guard Clause: Different lengths cannot be anagrams
        if (s.length != t.length) return false
        
        // Frequency counter for 26 lowercase English letters
        val letters = Array(26) { 0 }
        
        // Count character occurrences in string s
        for (char in s) {
            letters[char.code - 'a'.code]++
        }
        
        // Validate against string t with early exit optimization
        for (char in t) {
            val index = char.code - 'a'.code
            letters[index]--
            
            // Early exit: t has more of this character than s
            if (letters[index] < 0) {
                return false
            }
        }
        
        // Final verification: all counts should be zero
        return letters.sum() == 0
    }
}
```

**Code Quality Features:**
* **Guard Clause**: Immediate length check prevents unnecessary computation
* **Meaningful Names**: `letters` clearly indicates character frequency storage
* **Early Exit**: Returns `false` as soon as a mismatch is detected
* **Comments**: Explains the purpose of each major step
* **Defensive**: The final `sum() == 0` check ensures complete validation

---

## 5. Verification & Complexity Finalization:

### Dry Run with Example:

**Input:** `s = "anagram"`, `t = "nagaram"`

**Step 1:** Length check
- `s.length = 7`, `t.length = 7` → lengths match, continue

**Step 2:** Initialize frequency array
- `letters = [0, 0, 0, ..., 0]` (26 zeros)

**Step 3:** Count characters in `s = "anagram"`
```
'a' (index 0): letters[0]++ → [1, 0, 0, ...]
'n' (index 13): letters[13]++ → [1, 0, 0, ..., 1, ...]
'a' (index 0): letters[0]++ → [2, 0, 0, ..., 1, ...]
'g' (index 6): letters[6]++ → [2, 0, 0, 0, 0, 0, 1, ..., 1, ...]
'r' (index 17): letters[17]++ → [2, 0, ..., 1, ..., 1, ...]
'a' (index 0): letters[0]++ → [3, 0, ..., 1, ..., 1, ...]
'm' (index 12): letters[12]++ → [3, 0, ..., 1, 1, 1, ...]
```
Result: `letters = [3, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, ...]`

**Step 4:** Validate against `t = "nagaram"`
```
'n' (index 13): letters[13]-- → 0 (not negative, continue)
'a' (index 0): letters[0]-- → 2 (not negative, continue)
'g' (index 6): letters[6]-- → 0 (not negative, continue)
'a' (index 0): letters[0]-- → 1 (not negative, continue)
'r' (index 17): letters[17]-- → 0 (not negative, continue)
'a' (index 0): letters[0]-- → 0 (not negative, continue)
'm' (index 12): letters[12]-- → 0 (not negative, continue)
```
All characters processed without going negative.

**Step 5:** Final check
- `letters.sum() = 0` → return `true`

**Output:** `true` ✓

---

### Final Complexity Analysis:

**Time Complexity: O(n)**
- Where `n` is the length of the strings (since they must be equal length to be anagrams)
- First loop iterates through `s`: O(n)
- Second loop iterates through `t`: O(n)
- Final sum calculation: O(26) = O(1)
- Total: O(n) + O(n) + O(1) = O(n)

**Space Complexity: O(1)**
- We use a fixed-size array of 26 integers
- This does not scale with input size
- Therefore, constant space: O(1)

**Why This is Optimal:**
- Cannot do better than O(n) time since we must examine every character at least once
- Cannot do better than O(1) space for the given constraint (only lowercase English letters)
- This solution is **time-optimal** and **space-optimal** for the problem constraints
