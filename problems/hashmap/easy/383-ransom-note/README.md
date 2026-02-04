# 383 - Ransom Note

**Difficulty**: Easy
**LeetCode**: https://leetcode.com/problems/ransom-note/

---

## Problem Description

Given two strings `ransomNote` and `magazine`, return `true` if `ransomNote` can be constructed by using the letters from `magazine` and `false` otherwise.

Each letter in `magazine` can only be used once in `ransomNote`.

**Example 1:**
```
Input: ransomNote = "a", magazine = "b"
Output: false
```

**Example 2:**
```
Input: ransomNote = "aa", magazine = "ab"
Output: false
```

**Example 3:**
```
Input: ransomNote = "aa", magazine = "aab"
Output: true
```

**Constraints:**
- `1 <= ransomNote.length, magazine.length <= 10^5`
- `ransomNote` and `magazine` consist of lowercase English letters.

---

## Solution Design

### 1. Clarification & Edge Cases

**Key Constraints:**
- Input size: Both strings can be up to 10^5 characters
- Character set: Only lowercase English letters (26 possible characters)
- Each character in magazine can only be used once
- Order doesn't matter - we only care about character frequency

**Edge Cases to Handle:**
- Empty `ransomNote` → Should return `true` (need zero letters)
- Empty `magazine` with non-empty `ransomNote` → Should return `false`
- `ransomNote` longer than `magazine` → Could still be false
- Single character strings
- All same characters
- `ransomNote` requires more of a specific character than available in `magazine`

### 2. High-Level Approach Analysis (Trade-offs)

#### Brute Force Approach:
For each character in `ransomNote`, search through the entire `magazine` string to find a match, then remove that character from magazine (or mark it as used).

**Complexity:**
- **Time:** O(n × m) where n = ransomNote.length, m = magazine.length
  - For each character in ransomNote (n iterations), we scan through magazine (m operations)
- **Space:** O(m) if we modify magazine or create a copy

#### Optimized Approach (HashMap):
1. Count the frequency of each character in `magazine` using a HashMap
2. For each character in `ransomNote`, check if it exists in the HashMap with count > 0
3. Decrement the count for each used character

**Complexity:**
- **Time:** O(n + m) - single pass through both strings
- **Space:** O(1) or O(26) - at most 26 characters (constant space)

**Why Optimized is Better:**
- Reduces time from quadratic O(n × m) to linear O(n + m)
- HashMap lookup is O(1) average case vs O(m) for string scanning
- Space is effectively constant since we only store at most 26 characters

### 3. Algorithm Design

**Step-by-Step Logic:**

1. **Build Frequency Map:**
   - Create a HashMap to store character frequencies
   - Iterate through `magazine` string once
   - For each character, increment its count in the map (default to 0 if not present)

2. **Validate Ransom Note:**
   - Iterate through `ransomNote` string once
   - For each character:
     - Check if the character exists in map with count > 0
     - If not available (count <= 0), return `false` immediately
     - If available, decrement the count in the map

3. **Success:**
   - If we complete the iteration without returning false, return `true`

**Data Structure Choice:**
- **HashMap (Character → Integer):** 
  - Provides O(1) average lookup and update operations
  - Perfect for frequency counting problems
  - Automatically handles the "can only use each letter once" constraint by decrementing counts

### 4. Production-Ready Implementation

```kotlin
package problem.hashmap.easy.ransomnote

class Solution {
    fun canConstruct(ransomNote: String, magazine: String): Boolean {
        // Guard clause: If ransomNote is longer, it's impossible to construct
        // (though this is just an optimization, not strictly necessary)
        if (ransomNote.length > magazine.length) {
            // Note: This optimization isn't always correct if we consider
            // that magazine might have many duplicates, so we can skip it
            // and rely on the main logic
        }
        
        // Build frequency map for magazine
        val magazineFreq = HashMap<Char, Int>()
        for (char in magazine) {
            magazineFreq[char] = magazineFreq.getOrDefault(char, 0) + 1
        }
        
        // Validate if ransomNote can be constructed
        for (char in ransomNote) {
            val availableCount = magazineFreq.getOrDefault(char, 0)
            
            // Guard clause: character not available or already used up
            if (availableCount <= 0) {
                return false
            }
            
            // Use one instance of this character
            magazineFreq[char] = availableCount - 1
        }
        
        // All characters in ransomNote were successfully matched
        return true
    }
}
```

**Alternative Space-Optimized Implementation (using IntArray):**

```kotlin
package problem.hashmap.easy.ransomnote

class Solution {
    fun canConstruct(ransomNote: String, magazine: String): Boolean {
        // Use array instead of HashMap since we only have 26 lowercase letters
        val charCount = IntArray(26)
        
        // Build frequency array for magazine
        for (char in magazine) {
            charCount[char - 'a']++
        }
        
        // Validate if ransomNote can be constructed
        for (char in ransomNote) {
            val index = char - 'a'
            
            // Guard clause: character not available
            if (charCount[index] <= 0) {
                return false
            }
            
            // Use one instance of this character
            charCount[index]--
        }
        
        return true
    }
}
```

### 5. Verification & Complexity Finalization

#### Dry Run Example:
**Input:** `ransomNote = "aa"`, `magazine = "aab"`

**Step 1: Build Frequency Map**
```
magazine = "aab"
After iteration:
magazineFreq = {'a': 2, 'b': 1}
```

**Step 2: Validate Ransom Note**
```
ransomNote = "aa"

Iteration 1: char = 'a'
  - magazineFreq['a'] = 2 (> 0) ✓
  - Decrement: magazineFreq['a'] = 1

Iteration 2: char = 'a'
  - magazineFreq['a'] = 1 (> 0) ✓
  - Decrement: magazineFreq['a'] = 0

All characters processed successfully!
Return: true ✓
```

**Negative Example:** `ransomNote = "aa"`, `magazine = "ab"`
```
Step 1: magazineFreq = {'a': 1, 'b': 1}

Step 2:
Iteration 1: char = 'a'
  - magazineFreq['a'] = 1 (> 0) ✓
  - Decrement: magazineFreq['a'] = 0

Iteration 2: char = 'a'
  - magazineFreq['a'] = 0 (<= 0) ✗
  - Return: false ✓
```

#### Final Complexity Analysis:

**Time Complexity: O(n + m)**
- O(m) to build the frequency map from magazine
- O(n) to validate all characters in ransomNote
- Total: O(n + m) where n = ransomNote.length, m = magazine.length

**Space Complexity: O(1) or O(k)**
- HashMap stores at most 26 characters (lowercase English letters)
- O(26) = O(1) constant space
- For the array implementation: O(26) = O(1) fixed space
- If we consider k = size of character set, it's O(k), but since k = 26 (constant), it's O(1)

**Trade-offs:**
- HashMap approach: More intuitive, works with any character set
- Array approach: Slightly faster, more memory efficient, but limited to lowercase English letters

Both implementations are production-ready and handle all edge cases correctly.
