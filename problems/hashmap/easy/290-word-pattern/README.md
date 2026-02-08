# 290 - Word Pattern

**Difficulty**: Easy
**LeetCode**: https://leetcode.com/problems/word-pattern/

---

## Problem Description

Given a `pattern` and a string `s`, find if `s` follows the same pattern.

Here **follow** means a full match, such that there is a bijection between a letter in `pattern` and a **non-empty** word in `s`.

**Example 1:**
```
Input: pattern = "abba", s = "dog cat cat dog"
Output: true
Explanation: The bijection can be established as: 'a' -> "dog", 'b' -> "cat"
```

**Example 2:**
```
Input: pattern = "abba", s = "dog cat cat fish"
Output: false
Explanation: 'a' maps to "dog" and 'b' maps to "cat", but the second 'a' should map to "dog", not "fish"
```

**Example 3:**
```
Input: pattern = "aaaa", s = "dog cat cat dog"
Output: false
Explanation: 'a' cannot map to both "dog" and "cat"
```

**Example 4:**
```
Input: pattern = "abba", s = "dog dog dog dog"
Output: false
Explanation: 'a' and 'b' cannot both map to "dog" (bijection violation)
```

**Constraints:**
- `1 <= pattern.length <= 300`
- `pattern` contains only lower-case English letters
- `1 <= s.length <= 3000`
- `s` contains only lowercase English letters and spaces `' '`
- `s` does not contain any leading or trailing spaces
- All the words in `s` are separated by a single space

---

## Solution Design

### 1. Clarification & Edge Cases

**Key Constraints:**
- Pattern length: up to 300 characters
- String `s` length: up to 3000 characters
- Pattern contains only lowercase English letters (26 possible characters)
- String `s` contains words separated by single spaces
- Bijection required: one-to-one mapping between pattern characters and words
- Each pattern character must map to exactly one word, and each word must map to exactly one pattern character

**Edge Cases to Handle:**
- Pattern length ≠ number of words in `s` → Should return `false`
- Single character pattern with single word (e.g., pattern = "a", s = "dog") → Should return `true`
- All same pattern characters (e.g., pattern = "aaaa", s = "dog dog dog dog") → Should return `true`
- Different pattern characters mapping to same word (e.g., pattern = "ab", s = "dog dog") → Should return `false` (bijection violation)
- Same pattern character mapping to different words (e.g., pattern = "aa", s = "dog cat") → Should return `false`
- Empty inputs (though constraints say length ≥ 1)
- Multiple spaces between words (though problem states single space separation)

### 2. High-Level Approach Analysis (Trade-offs)

#### Brute Force Approach:
For each position in the pattern:
1. Check if the current pattern character has been seen before by scanning all previous positions
2. If seen, verify it maps to the same word at this position
3. Check if the current word has been mapped from a different pattern character by scanning all previous mappings

**Complexity:**
- **Time:** O(n² × w) where n = pattern.length, w = average word length
  - For each position (n iterations), scan all previous positions (up to n operations)
  - String comparisons take O(w) time
- **Space:** O(1) if we don't use additional storage (just scanning)

#### Optimized Approach (Two HashMaps):
1. Split string `s` into words array
2. Check if pattern length matches number of words (early return if not)
3. Use two HashMaps to track bidirectional mapping:
   - `pToS`: pattern character → word mapping
   - `sToP`: word → pattern character mapping (reverse)
4. Iterate through pattern once, validating both mappings simultaneously

**Complexity:**
- **Time:** O(n + m) where n = pattern.length, m = total characters in `s`
  - O(m) to split string `s` into words
  - O(n) to iterate through pattern
  - All HashMap operations are O(1) average case
  - Total: O(n + m)
- **Space:** O(n + k) where k = unique words count
  - Two HashMaps store at most n entries
  - Words array stores references to words

**Why Optimized is Better:**
- Reduces time from O(n² × w) to O(n + m) - linear instead of quadratic
- HashMap provides O(1) lookups instead of O(n) scanning
- Single pass through data instead of repeated scanning
- Clear separation of concerns with two maps ensuring bijection

### 3. Algorithm Design

**Step-by-Step Logic:**

1. **Split and Validate Input:**
   - Split string `s` by spaces to get words array
   - Check if pattern.length equals words array size
   - If not equal, return `false` immediately (impossible to have bijection)

2. **Initialize Data Structures:**
   - Create HashMap `pToS` for pattern → word mapping
   - Create HashMap `sToP` for word → pattern mapping (reverse)

3. **Iterate and Validate Bijection:**
   - For each index `i` from 0 to pattern.length - 1:
     - Get current pattern character `char = pattern[i]`
     - Get current word `str = words[i]`
     
4. **Check Forward Mapping:**
   - If `char` exists in `pToS`:
     - Verify `pToS[char]` equals `str`
     - If not, return `false` (pattern character maps to different word)
   
5. **Check Reverse Mapping:**
   - If `str` exists in `sToP`:
     - Verify `sToP[str]` equals `char`
     - If not, return `false` (word maps from different pattern character)

6. **Establish Mappings:**
   - Store `pToS[char] = str`
   - Store `sToP[str] = char`

7. **Success:**
   - If all iterations complete without conflicts, return `true`

**Data Structure Choice:**
- **Two HashMaps (Char → String, String → Char):**
  - Provides O(1) average lookup and update operations
  - First map tracks forward mapping (pattern → words)
  - Second map tracks reverse mapping (words → pattern)
  - Together they ensure bijection: each pattern character maps to unique word, and vice versa
  - This is a classic bijection validation pattern

**Why Two Maps?**
- Using a single map and checking `values.contains()` would be O(n) per check, making total time O(n²)
- Two maps ensure both directions are validated in O(1) time
- Memory trade-off is acceptable for the time complexity gain

### 4. Production-Ready Implementation

```kotlin
package wordPattern

class Solution {
    fun wordPattern(pattern: String, s: String): Boolean {
        // Split the string into words
        val words = s.split(" ")
        
        // Guard clause: pattern length must match number of words
        if (pattern.length != words.size) {
            return false
        }
        
        // Two maps to ensure bijection
        val pToS = HashMap<Char, String>()  // pattern -> word mapping
        val sToP = HashMap<String, Char>()  // word -> pattern mapping (reverse)
        
        for (i in pattern.indices) {
            val char = pattern[i]
            val word = words[i]
            
            // Check forward mapping consistency
            if (pToS.containsKey(char)) {
                if (pToS[char] != word) {
                    // Pattern character already maps to a different word
                    return false
                }
            }
            
            // Check reverse mapping consistency
            if (sToP.containsKey(word)) {
                if (sToP[word] != char) {
                    // Word already maps from a different pattern character
                    return false
                }
            }
            
            // Establish bidirectional mappings
            pToS[char] = word
            sToP[word] = char
        }
        
        // All mappings are consistent - bijection exists
        return true
    }
}
```

**Code Improvements and Notes:**
- Clear variable names (`pToS`, `sToP`, `word` instead of `str`)
- Guard clause at the beginning for early termination
- Comments explain the bijection logic
- Separate checks for forward and reverse mappings for clarity
- Both mappings are updated after validation

**Alternative Implementation (Optimized Checks):**

```kotlin
package wordPattern

class Solution {
    fun wordPattern(pattern: String, s: String): Boolean {
        val words = s.split(" ")
        
        // Guard clause: lengths must match
        if (pattern.length != words.size) return false
        
        val pToS = HashMap<Char, String>()
        val sToP = HashMap<String, Char>()
        
        for (i in pattern.indices) {
            val char = pattern[i]
            val word = words[i]
            
            // Get existing mappings (null if not present)
            val mappedWord = pToS[char]
            val mappedChar = sToP[word]
            
            // If either mapping exists, both must exist and match current pair
            if (mappedWord != null || mappedChar != null) {
                if (mappedWord != word || mappedChar != char) {
                    return false
                }
            }
            
            // Establish new mappings (or overwrite with same values)
            pToS[char] = word
            sToP[word] = char
        }
        
        return true
    }
}
```

### 5. Verification & Complexity Finalization

#### Dry Run Example:
**Input:** `pattern = "abba"`, `s = "dog cat cat dog"`

**Step 1: Split and Validate**
```
words = ["dog", "cat", "cat", "dog"]
pattern.length = 4, words.size = 4 ✓
Continue...
```

**Step 2: Initialize**
```
pToS = {}
sToP = {}
```

**Step 3: Iterate Through Pattern**

```
Iteration 0: char = 'a', word = "dog"
  - pToS['a'] not in map → no conflict
  - sToP["dog"] not in map → no conflict
  - Store: pToS['a'] = "dog", sToP["dog"] = 'a'
  State: pToS = {'a': "dog"}, sToP = {"dog": 'a'}

Iteration 1: char = 'b', word = "cat"
  - pToS['b'] not in map → no conflict
  - sToP["cat"] not in map → no conflict
  - Store: pToS['b'] = "cat", sToP["cat"] = 'b'
  State: pToS = {'a': "dog", 'b': "cat"}, sToP = {"dog": 'a', "cat": 'b'}

Iteration 2: char = 'b', word = "cat"
  - pToS['b'] = "cat" ✓ (matches current word)
  - sToP["cat"] = 'b' ✓ (matches current char)
  - Store: (same values, no change)
  State: unchanged

Iteration 3: char = 'a', word = "dog"
  - pToS['a'] = "dog" ✓ (matches current word)
  - sToP["dog"] = 'a' ✓ (matches current char)
  - Store: (same values, no change)
  State: unchanged

All iterations complete successfully!
Return: true ✓
```

**Negative Example:** `pattern = "abba"`, `s = "dog cat cat fish"`
```
words = ["dog", "cat", "cat", "fish"]
pattern.length = 4, words.size = 4 ✓

Iteration 0: 'a' -> "dog"
  - pToS = {'a': "dog"}, sToP = {"dog": 'a'}

Iteration 1: 'b' -> "cat"
  - pToS = {'a': "dog", 'b': "cat"}, sToP = {"dog": 'a', "cat": 'b'}

Iteration 2: 'b' -> "cat"
  - pToS['b'] = "cat" ✓, sToP["cat"] = 'b' ✓
  - All good

Iteration 3: 'a' -> "fish"
  - pToS['a'] = "dog" but current word = "fish" ✗
  - Return: false ✓
```

**Bijection Violation Example:** `pattern = "abba"`, `s = "dog dog dog dog"`
```
words = ["dog", "dog", "dog", "dog"]
pattern.length = 4, words.size = 4 ✓

Iteration 0: 'a' -> "dog"
  - pToS = {'a': "dog"}, sToP = {"dog": 'a'}

Iteration 1: 'b' -> "dog"
  - pToS['b'] not in map → no conflict
  - sToP["dog"] = 'a' but current char = 'b' ✗
  - Return: false ✓
  
This correctly catches the bijection violation!
```

#### Final Complexity Analysis:

**Time Complexity: O(n + m)**
- **Splitting string:** O(m) where m = s.length
  - Must traverse entire string to split by spaces
- **Iteration through pattern:** O(n) where n = pattern.length
  - Each iteration performs O(1) HashMap operations
  - String comparison in HashMap is O(w) where w = average word length
  - Since total characters = m, and we do n comparisons, average case is O(m/n) per comparison
  - Total: O(n × m/n) = O(m)
- **Overall:** O(m + n)
- Since m ≥ n (string must be at least as long as pattern), this is O(m)

**Space Complexity: O(n + k)**
- **Words array:** O(n) elements (n = number of words = pattern.length)
  - Total characters stored: O(m) where m = s.length
- **pToS HashMap:** O(u) where u = unique characters in pattern (at most 26, but could be up to n)
- **sToP HashMap:** O(k) where k = unique words (at most n)
- **Total:** O(n + k) which is O(n) since k ≤ n
- More precisely: O(m) considering the actual string content

**Optimizations:**
- Early termination with guard clause saves time when lengths don't match
- Two HashMaps ensure O(1) lookup instead of O(n) for values.contains()
- Single pass through the pattern after splitting
- No need to store additional arrays or perform multiple passes

**Trade-offs:**
- **Space for time:** Using two HashMaps (2n space) for O(1) lookups vs one HashMap with O(n) checks
- The implementation is optimal for this problem class
- Further optimization would require problem-specific constraints (e.g., limited alphabet size)
