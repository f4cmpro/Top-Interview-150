# 49 - Group Anagrams

**Difficulty**: Medium
**LeetCode**: https://leetcode.com/problems/group-anagrams/

---

## Solution Design

### 1. Clarification & Edge Cases

**Constraints:**
- `1 <= strs.length <= 10^4`
- `0 <= strs[i].length <= 100`
- `strs[i]` consists of lowercase English letters only

**Edge Cases:**
- Empty string `""` — valid input; all empty strings are anagrams of each other and should be grouped together.
- Single-character strings — each unique character forms its own group unless duplicates exist.
- Array with a single element — return it as a single group.
- All strings are anagrams of each other — return one group containing all strings.
- No two strings are anagrams — return each string in its own group.

---

### 2. High-Level Approach Analysis (Trade-offs)

#### Brute Force
- For each string, sort its characters to produce a canonical key, then compare with all others.
- Group strings that share the same sorted key.
- **Time:** O(n * k log k) where `n` = number of strings, `k` = max string length.
- **Space:** O(n * k) for storing all keys and groups.

#### Optimized Approach ✅
- Instead of sorting, build a **frequency-count key** for each string using a fixed-size array of 26 integers (one per letter).
- Encode the frequency array into a unique string key (e.g., `"a2b1"`) and use it in a **HashMap**.
- All strings with the same frequency signature are anagrams → group them under the same key.
- **Time:** O(n * k) — counting characters is O(k), done for each of the `n` strings.
- **Space:** O(n * k) — storing all strings in the map.

**Why optimized is better:**
Sorting takes O(k log k) per string; counting characters takes only O(k). For large inputs with long strings, this is a meaningful improvement. The frequency-key approach avoids the overhead of sorting entirely.

---

### 3. Algorithm Design

1. Create a `HashMap<String, ArrayList<String>>` to map each frequency-key to its group of anagrams.
2. Iterate over every string in the input array:
   a. Create an integer array of size 26, initialized to 0.
   b. For each character in the string, increment `letters[char - 'a']`.
   c. Build a compact string key by encoding only non-zero counts (e.g., `"a2b1c3"`).
3. Use `getOrPut` to insert a new list if the key is new, then append the current string.
4. Collect all values from the map and return them as the result.

**Data Structures:**
- **HashMap** — O(1) average lookup/insert; perfect for grouping by a derived key.
- **Array(26)** — fixed-size frequency counter; O(1) space per string, more efficient than sorting.
- **StringBuilder** — efficient string concatenation when building the key.

---

### 4. Production-Ready Implementation

```kotlin
package groupanagram

class Solution {
    fun groupAnagrams(strs: Array<String>): List<List<String>> {
        // Map from frequency-based key → list of anagram strings
        val anagramMap = HashMap<String, ArrayList<String>>()

        for (str in strs) {
            val key = buildFrequencyKey(str)
            anagramMap.getOrPut(key) { arrayListOf() }.add(str)
        }

        return ArrayList(anagramMap.values)
    }

    /**
     * Builds a unique key based on character frequency.
     * Example: "eat" → "a1e1t1", "tea" → "a1e1t1"
     * Only non-zero counts are included to keep keys compact.
     */
    private fun buildFrequencyKey(s: String): String {
        val frequency = IntArray(26)
        for (char in s) {
            frequency[char - 'a']++
        }

        val keyBuilder = StringBuilder()
        for (i in frequency.indices) {
            if (frequency[i] > 0) {
                keyBuilder.append('a' + i).append(frequency[i])
            }
        }
        return keyBuilder.toString()
    }
}
```

---

### 5. Verification & Complexity Finalization

#### Dry Run

**Input:** `["eat", "tea", "tan", "ate", "nat", "bat"]`

| String | Frequency Key | Group        |
|--------|--------------|--------------|
| "eat"  | `a1e1t1`     | ["eat"]      |
| "tea"  | `a1e1t1`     | ["eat","tea"]|
| "tan"  | `a1n1t1`     | ["tan"]      |
| "ate"  | `a1e1t1`     | ["eat","tea","ate"] |
| "nat"  | `a1n1t1`     | ["tan","nat"]|
| "bat"  | `a1b1t1`     | ["bat"]      |

**Output:** `[["eat","tea","ate"], ["tan","nat"], ["bat"]]` ✅

#### Final Complexity

| | Complexity |
|---|---|
| **Time** | O(n * k) — `n` strings, each processed in O(k) |
| **Space** | O(n * k) — storing all strings and keys in the HashMap |
