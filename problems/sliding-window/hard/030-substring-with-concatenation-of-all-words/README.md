# 30 - Substring with Concatenation of All Words

**Difficulty**: 🔴 Hard  
**LeetCode**: https://leetcode.com/problems/substring-with-concatenation-of-all-words/

---

## Problem Description

You are given a string `s` and an array of strings `words`. All strings in `words` are of the **same length**.

A **concatenated substring** is a substring that exactly contains all the strings of any permutation of `words` concatenated.

Return an array of the **starting indices** of all concatenated substrings in `s`. You can return the answer in any order.

**Example 1:**
```
Input:  s = "barfoothefoobarman", words = ["foo","bar"]
Output: [0,9]
```

**Example 2:**
```
Input:  s = "wordgoodgoodgoodbestword", words = ["word","good","best","word"]
Output: []
```

**Example 3:**
```
Input:  s = "barfoofoobarthefoobarman", words = ["bar","foo","the"]
Output: [6,9,12]
```

**Constraints:**
- `1 <= s.length <= 10^4`
- `1 <= words.length <= 5000`
- `1 <= words[i].length <= 30`
- `s` and `words[i]` consist of lowercase English letters.

---

## 1. Clarification & Edge Cases

**Key Constraints:**
- All words are the **same length** (`L`) — this is the critical insight enabling an efficient sliding window.
- Words may contain **duplicates** (e.g., `words = ["word","good","best","word"]`), so a frequency map is needed, not a set.
- `s` can be up to `10^4` characters; `words` can have up to `5000` entries of up to `30` characters each.

**Edge Cases:**
| Scenario | Handling |
|---|---|
| `s` shorter than `total length` of all words | Return `[]` immediately |
| `words` contains duplicates | Use a frequency map (`unordered_map<string,int>`) |
| No valid concatenation exists | Sliding window naturally produces an empty result |
| All characters in `s` are the same word repeated | Multiple valid windows detected correctly |
| Single word in `words` | Works as a regular substring search |

---

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force
For every index `i` in `s` (from `0` to `n - totalLen`), extract the substring of length `totalLen = L * m`. Split it into `m` chunks of size `L` and check if the frequency map of those chunks matches `wordFreq`.

- **Time:** $O(n \cdot m \cdot L)$ — iterating `n` positions, splitting `m` words, comparing strings of length `L`.
- **Space:** $O(m \cdot L)$ — for the frequency maps.

### Optimized Approach — Multi-Offset Sliding Window
Since every word has the same length `L`, there are only `L` distinct alignments for any valid window. Run **`L` independent sliding windows**, one per alignment offset `[0, L)`. Within each pass, slide by one word at a time (step `= L`), maintaining a live frequency map and a `matched` counter. Shrink from the left when a word is over-used or the window is complete.

- **Time:** $O(n \cdot L)$ — `L` passes, each inspecting $\frac{n}{L}$ words with $O(L)$ substring/hash ops per word.
- **Space:** $O(m \cdot L)$ — two frequency maps of at most `m` entries.

### Why Optimized Is Better
The brute force re-checks every overlapping substring from scratch. The sliding window **reuses** the running frequency state across positions, replacing $O(m)$ redundant re-checks with $O(1)$ amortized pointer movements. The `m` factor is eliminated from the time complexity.

| | Time | Space |
|---|---|---|
| Brute Force | $O(n \cdot m \cdot L)$ | $O(m \cdot L)$ |
| Sliding Window | $O(n \cdot L)$ | $O(m \cdot L)$ |

---

## 3. Algorithm Design

**Data Structures:**
- `unordered_map<string, int> wordFreq` — target frequency of each word.
- `unordered_map<string, int> windowFreq` — frequency of words currently inside the sliding window.
- `int matched` — count of word slots correctly filled (i.e., `windowFreq[w] <= wordFreq[w]` contribution).

**Steps:**
1. Build `wordFreq` from the `words` array.
2. For each `offset` in `[0, L)`:
   - Reset `windowFreq`, `matched = 0`, `left = offset`.
   - Advance `right` from `offset` to `n - L` in steps of `L`:
     - Extract `word = s[right .. right+L)`.
     - **If `word` is in `wordFreq`:**
       - Increment `windowFreq[word]` and `matched` (only if `windowFreq[word] <= wordFreq[word]`).
       - **Shrink from left** while `windowFreq[word] > wordFreq[word]` (over-used):
         - Remove `leftWord`; decrement `matched` if it was a valid contribution; advance `left`.
       - **If `matched == m`**: record `left` as a valid start, then shrink one word from the left and advance `left`.
     - **If `word` is NOT in `wordFreq`:** reset `windowFreq`, `matched = 0`, move `left` past `right`.

---

## 4. Production-Ready Implementation

```cpp
#include <string>
#include <vector>
#include <unordered_map>
using namespace std;

class Solution {
public:
    vector<int> findSubstring(string s, vector<string>& words) {
        vector<int> result;
        if (s.empty() || words.empty()) return result;

        int L = words[0].size();   // length of each word
        int m = words.size();      // number of words
        int n = s.size();
        if (n < L * m) return result;

        // Build target frequency map
        unordered_map<string, int> wordFreq;
        for (const string& w : words) wordFreq[w]++;

        // Run one sliding window per alignment offset
        for (int offset = 0; offset < L; offset++) {
            unordered_map<string, int> windowFreq;
            int left = offset;
            int matched = 0;  // valid word slots filled

            for (int right = offset; right + L <= n; right += L) {
                string word = s.substr(right, L);

                if (wordFreq.count(word)) {
                    windowFreq[word]++;
                    if (windowFreq[word] <= wordFreq[word]) {
                        matched++;
                    }

                    // Over-used word: shrink window from left until balanced
                    while (windowFreq[word] > wordFreq[word]) {
                        string leftWord = s.substr(left, L);
                        windowFreq[leftWord]--;
                        if (windowFreq[leftWord] < wordFreq[leftWord]) {
                            matched--;
                        }
                        left += L;
                    }

                    // All words matched — record result and slide forward
                    if (matched == m) {
                        result.push_back(left);
                        string leftWord = s.substr(left, L);
                        windowFreq[leftWord]--;
                        matched--;
                        left += L;
                    }
                } else {
                    // Invalid word: reset window entirely
                    windowFreq.clear();
                    matched = 0;
                    left = right + L;
                }
            }
        }

        return result;
    }
};
```

---

## 5. Verification & Complexity Finalization

### Dry Run — Example 1
```
s = "barfoothefoobarman",  words = ["foo","bar"]
L = 3,  m = 2,  wordFreq = {foo:1, bar:1}
```

**offset = 0:**
| right | word  | windowFreq          | matched | left | result |
|-------|-------|---------------------|---------|------|--------|
| 0     | "bar" | {bar:1}             | 1       | 0    |        |
| 3     | "foo" | {bar:1, foo:1}      | 2 == m  | 0    | [0]    |
|       | shrink left → left=3 | {bar:0, foo:1} | 1 |   |        |
| 6     | "the" | reset               | 0       | 9    |        |
| 9     | "foo" | {foo:1}             | 1       | 9    |        |
| 12    | "bar" | {foo:1, bar:1}      | 2 == m  | 9    | [0,9]  |
|       | shrink left → left=12 | {foo:0,bar:1} | 1 |  |        |
| 15    | "man" | reset               | 0       | 18   |        |

**offsets 1 & 2:** no words from `wordFreq` align → no additions.

**Output: [0, 9]** ✓

### Final Complexity

| | Complexity |
|---|---|
| **Time** | $O(n \cdot L)$ — `L` passes of $O(n)$ each (substring/hash ops are $O(L)$ per word, $\frac{n}{L}$ words per pass) |
| **Space** | $O(m \cdot L)$ — two frequency maps with at most `m` entries of strings of length `L` |