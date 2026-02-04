# 205 - Isomorphic Strings

**Difficulty**: Easy
**LeetCode**: https://leetcode.com/problems/isomorphic-strings/

---

## Problem Description

Given two strings `s` and `t`, determine if they are isomorphic.

Two strings `s` and `t` are isomorphic if the characters in `s` can be replaced to get `t`.

All occurrences of a character must be replaced with another character while preserving the order of characters. No two characters may map to the same character, but a character may map to itself.

**Example 1:**
```
Input: s = "egg", t = "add"
Output: true
Explanation: The strings are isomorphic because 'e' maps to 'a' and 'g' maps to 'd'.
```

**Example 2:**
```
Input: s = "foo", t = "bar"
Output: false
Explanation: The strings are not isomorphic because 'o' appears twice but 'a' and 'r' are different.
```

**Example 3:**
```
Input: s = "paper", t = "title"
Output: true
Explanation: 'p' -> 't', 'a' -> 'i', 'e' -> 'l', 'r' -> 'e'
```

**Constraints:**
- `1 <= s.length <= 5 * 10^4`
- `t.length == s.length`
- `s` and `t` consist of any valid ASCII character.

---

## Solution Design

### 1. Clarification & Edge Cases

**Key Constraints:**
- Input size: Both strings can be up to 50,000 characters
- Character set: Any valid ASCII character (not just lowercase letters)
- Both strings have the same length (guaranteed)
- Mapping must be bijective (one-to-one): each character in `s` maps to exactly one character in `t`, and no two characters in `s` can map to the same character in `t`

**Edge Cases to Handle:**
- Single character strings (s = "a", t = "a") → Should return `true`
- All same characters (s = "aaa", t = "bbb") → Should return `true`
- Multiple mappings to same character (s = "ab", t = "aa") → Should return `false`
- Character maps to itself (s = "a", t = "a") → Should return `true`
- Reverse mapping violation (s = "ab", t = "ca", but also s = "ba") → Need to ensure bijection
- Complex patterns (s = "badc", t = "baba") → Should return `false` because 'd' -> 'b' and 'b' -> 'b'

### 2. High-Level Approach Analysis (Trade-offs)

#### Brute Force Approach:
For each character in string `s`, scan through all previous characters to check if the current character has been seen before. If yes, verify that it maps to the same character in `t`. Also check if the character in `t` has already been mapped from a different character in `s`.

**Complexity:**
- **Time:** O(n²) where n = s.length
  - For each character position (n iterations), we scan all previous positions (up to n operations)
- **Space:** O(1) if we don't use additional data structures

#### Optimized Approach (Two HashMaps):
1. Use a HashMap to track the mapping from `s` → `t` (forward mapping)
2. Use another HashMap or check values to ensure no two characters in `s` map to the same character in `t` (reverse mapping validation)
3. Iterate through both strings once, validating both mappings

**Alternative Optimized Approach (Single HashMap + Values Check):**
1. Use one HashMap to store `s` → `t` mappings
2. For each character, check if it's already mapped to a different character
3. Also check if the target character in `t` is already mapped from a different character (by checking values)

**Complexity:**
- **Time:** O(n) - single pass through both strings
  - HashMap lookup is O(1) average case
  - Checking if a value exists in HashMap values is O(m) where m is the number of entries, but worst case this is O(n)
  - **Note:** The current implementation uses `values.contains()` which is O(m), making it O(n) per lookup in worst case, resulting in O(n²) worst case
- **Space:** O(k) where k is the number of unique characters in `s` (at most min(n, 128) for ASCII)

**Better Optimized Approach (Two HashMaps):**
Use two separate HashMaps to track both directions of mapping, achieving true O(n) time.

**Why Optimized is Better:**
- Reduces time from O(n²) to O(n) with proper implementation
- HashMap provides O(1) lookups instead of O(n) scans
- Space trade-off is acceptable since we only store unique character mappings

### 3. Algorithm Design

**Step-by-Step Logic (Current Implementation - Single HashMap with Values Check):**

1. **Initialize Data Structure:**
   - Create a HashMap `stMap` to store character mappings from `s` → `t`

2. **Iterate Through Both Strings:**
   - For each index `i` from 0 to s.length - 1:
     - Get character `char1` from `s[i]`
     - Get character `char2` from `t[i]`

3. **Validate Mapping Consistency:**
   - **Check existing mapping conflict:** If `char1` is already in the map AND maps to a different character than `char2`, return `false`
   - **Check reverse mapping conflict:** If `char1` is NOT in the map yet AND `char2` is already a value in the map (mapped from another character), return `false`
   - This ensures bijection: one-to-one mapping

4. **Store/Update Mapping:**
   - Store or update `stMap[char1] = char2`

5. **Success:**
   - If we complete the iteration without conflicts, return `true`

**Data Structure Choice:**
- **HashMap (Char → Char):**
  - Provides O(1) average lookup for checking existing mappings
  - Stores forward mapping from `s` to `t`
  - Can check values (though O(m) operation) to ensure no duplicate target mappings

**Improved Design (Two HashMaps for True O(n)):**
- Use two HashMaps: `sToT` and `tToS`
- Each lookup is O(1), ensuring true O(n) time complexity

### 4. Production-Ready Implementation

**Current Implementation (Single HashMap):**
```kotlin
package problem.hashmap.easy.isomorphic

class Solution {
    fun isIsomorphic(s: String, t: String): Boolean {
        val stMap = HashMap<Char, Char>()
        
        for (i in s.indices) {
            val char1 = s[i]
            val char2 = t[i]
            
            // Check if existing mapping conflicts
            if (stMap[char1] != null && stMap[char1]!! != char2) {
                return false
            }
            
            // Check if char2 is already mapped from a different character
            // This ensures no two characters in s map to the same character in t
            if (stMap[char1] == null && stMap.values.contains(char2)) {
                return false
            }
            
            // Store the mapping
            stMap[char1] = char2
        }
        
        return true
    }
}
```

**Optimized Implementation (Two HashMaps - True O(n)):**
```kotlin
package problem.hashmap.easy.isomorphic

class Solution {
    fun isIsomorphic(s: String, t: String): Boolean {
        // Guard clause: strings must have same length (guaranteed by problem)
        if (s.length != t.length) {
            return false
        }
        
        // Two maps to ensure bijection
        val sToT = HashMap<Char, Char>()  // s -> t mapping
        val tToS = HashMap<Char, Char>()  // t -> s mapping (reverse)
        
        for (i in s.indices) {
            val char1 = s[i]
            val char2 = t[i]
            
            // Check forward mapping consistency
            if (sToT.containsKey(char1)) {
                if (sToT[char1] != char2) {
                    return false  // char1 already maps to different character
                }
            } else {
                // Establish new forward mapping
                sToT[char1] = char2
            }
            
            // Check reverse mapping consistency
            if (tToS.containsKey(char2)) {
                if (tToS[char2] != char1) {
                    return false  // char2 already maps from different character
                }
            } else {
                // Establish new reverse mapping
                tToS[char2] = char1
            }
        }
        
        return true
    }
}
```

**Alternative Array-Based Implementation (for ASCII characters):**
```kotlin
package problem.hashmap.easy.isomorphic

class Solution {
    fun isIsomorphic(s: String, t: String): Boolean {
        // Use arrays for ASCII characters (256 possible values)
        val sToT = IntArray(256) { -1 }  // -1 means unmapped
        val tToS = IntArray(256) { -1 }
        
        for (i in s.indices) {
            val char1 = s[i].code
            val char2 = t[i].code
            
            // Check forward mapping
            if (sToT[char1] != -1) {
                if (sToT[char1] != char2) {
                    return false
                }
            } else {
                sToT[char1] = char2
            }
            
            // Check reverse mapping
            if (tToS[char2] != -1) {
                if (tToS[char2] != char1) {
                    return false
                }
            } else {
                tToS[char2] = char1
            }
        }
        
        return true
    }
}
```

### 5. Verification & Complexity Finalization

#### Dry Run Example:
**Input:** `s = "paper"`, `t = "title"`

**Using Two HashMaps Approach:**

```
Initial State:
sToT = {}
tToS = {}

Iteration 0: char1 = 'p', char2 = 't'
  - sToT['p'] not in map → add sToT['p'] = 't'
  - tToS['t'] not in map → add tToS['t'] = 'p'
  State: sToT = {'p': 't'}, tToS = {'t': 'p'}

Iteration 1: char1 = 'a', char2 = 'i'
  - sToT['a'] not in map → add sToT['a'] = 'i'
  - tToS['i'] not in map → add tToS['i'] = 'a'
  State: sToT = {'p': 't', 'a': 'i'}, tToS = {'t': 'p', 'i': 'a'}

Iteration 2: char1 = 'p', char2 = 't'
  - sToT['p'] = 't' ✓ (matches char2)
  - tToS['t'] = 'p' ✓ (matches char1)
  State: unchanged

Iteration 3: char1 = 'e', char2 = 'l'
  - sToT['e'] not in map → add sToT['e'] = 'l'
  - tToS['l'] not in map → add tToS['l'] = 'e'
  State: sToT = {'p': 't', 'a': 'i', 'e': 'l'}, tToS = {'t': 'p', 'i': 'a', 'l': 'e'}

Iteration 4: char1 = 'r', char2 = 'e'
  - sToT['r'] not in map → add sToT['r'] = 'e'
  - tToS['e'] not in map → add tToS['e'] = 'r'
  State: sToT = {'p': 't', 'a': 'i', 'e': 'l', 'r': 'e'}, 
         tToS = {'t': 'p', 'i': 'a', 'l': 'e', 'e': 'r'}

All iterations complete without conflicts!
Return: true ✓
```

**Negative Example:** `s = "foo"`, `t = "bar"`
```
Iteration 0: char1 = 'f', char2 = 'b'
  - sToT['f'] = 'b', tToS['b'] = 'f'

Iteration 1: char1 = 'o', char2 = 'a'
  - sToT['o'] = 'a', tToS['a'] = 'o'

Iteration 2: char1 = 'o', char2 = 'r'
  - sToT['o'] = 'a' but char2 = 'r' (conflict!) ✗
  - Return: false ✓
```

**Edge Case:** `s = "badc"`, `t = "baba"`
```
Iteration 0: 'b' -> 'b', sToT['b'] = 'b', tToS['b'] = 'b'
Iteration 1: 'a' -> 'a', sToT['a'] = 'a', tToS['a'] = 'a'
Iteration 2: 'd' -> 'b', sToT['d'] = 'b', tToS['b'] already = 'b' ✓
Iteration 3: 'c' -> 'a', sToT['c'] = 'a', tToS['a'] already = 'a' ✓

Wait, this would return true, but let's check logic again...
Actually at Iteration 2: 'd' -> 'b'
  - sToT['d'] not in map → add sToT['d'] = 'b'
  - tToS['b'] = 'b' (exists, check if equals 'd')
  - tToS['b'] = 'b' != 'd' → return false ✓

Correct!
```

#### Final Complexity Analysis:

**Current Implementation (Single HashMap with values.contains()):**
- **Time Complexity: O(n × m)** where n = string length, m = unique characters
  - O(n) for the main loop
  - O(m) for `values.contains()` check on each iteration
  - Worst case: O(n²) when all characters are unique
- **Space Complexity: O(k)** where k = unique characters in s
  - At most min(n, 128) for ASCII characters
  - Effectively O(1) if we consider ASCII as constant

**Optimized Implementation (Two HashMaps):**
- **Time Complexity: O(n)**
  - Single pass through both strings
  - All HashMap operations (containsKey, get, put) are O(1) average case
  - No values.contains() operation needed
- **Space Complexity: O(k)**
  - Two HashMaps storing at most k unique characters each
  - k ≤ min(n, 128) for ASCII
  - O(2k) = O(k), effectively O(1) for bounded character sets

**Array-Based Implementation:**
- **Time Complexity: O(n)** - single pass with O(1) array access
- **Space Complexity: O(1)** - fixed 256-element arrays for ASCII

**Recommendation:**
The two-HashMap approach provides true O(n) time complexity while maintaining code clarity. The array-based approach is slightly faster in practice for ASCII characters but less flexible for extended Unicode characters.
