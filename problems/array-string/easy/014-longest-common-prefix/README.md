# 14 - Longest Common Prefix

**Difficulty**: Easy  
**LeetCode**: https://leetcode.com/problems/longest-common-prefix/

---

## 1. Clarification & Edge Cases

### Key Constraints
- Input: an array of strings `strs`.
- The array can contain multiple strings with different lengths.
- Characters are lowercase English letters on LeetCode for this problem, but the logic works for any characters.
- Need the longest prefix shared by every string.
- If no common prefix exists, return an empty string.

### Edge Cases to Handle
- Empty array: `[]` -> return `""`.
- Array with one string: `["flower"]` -> return `"flower"`.
- Any empty string in input: `["", "abc"]` -> return `""`.
- No common prefix at all: `["dog", "racecar", "car"]` -> return `""`.
- All strings identical: `["test", "test"]` -> return `"test"`.

---

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force Approach
- Compare characters column by column across all strings.
- Start at index `0`, check if every string has the same character at that index.
- Stop at first mismatch or when one string ends.

This is already efficient enough and effectively near-optimal for this problem.

### Optimized Approach (Best Solution)
- Use the first string as the current prefix candidate.
- For each next string, shrink the candidate prefix until that string starts with it.
- If the prefix becomes empty, return `""` immediately.

This is often called horizontal scanning.

### Complexity Comparison
- Brute Force (column-wise scan):
	- Time: `O(S)`, where `S` is total number of characters across all strings (in worst case).
	- Space: `O(1)` extra (excluding output).
- Optimized (horizontal scanning with shrinking prefix):
	- Time: `O(S)` worst case.
	- Space: `O(1)` extra (excluding output).

Why optimized is better in practice:
- It is very readable and production-friendly.
- It supports early termination naturally when prefix becomes empty.
- It avoids unnecessary checks once prefix shrinks significantly.

---

## 3. Algorithm Design

### Step-by-Step Logic
1. If `strs` is empty, return `""`.
2. Initialize `prefix` as the first string.
3. Iterate over each remaining string `current`:
	 - While `current` does not start with `prefix`:
		 - Remove the last character from `prefix`.
		 - If `prefix` becomes empty, return `""`.
4. After processing all strings, `prefix` is the longest common prefix.
5. Return `prefix`.

### Data Structures Used
- String variables only (`prefix`, `current`).
- No additional complex data structures are needed.
- Chosen because the operation is direct string prefix matching and trimming.

---

## 4. Production-Ready Implementation

```kotlin
class Solution {
		fun longestCommonPrefix(strs: Array<String>): String {
				// Guard clause: no strings provided.
				if (strs.isEmpty()) return ""

				var prefix = strs[0]

				for (i in 1 until strs.size) {
						val current = strs[i]

						// Shrink prefix until current starts with it.
						while (!current.startsWith(prefix)) {
								prefix = prefix.dropLast(1)

								// Guard clause: no common prefix remains.
								if (prefix.isEmpty()) return ""
						}
				}

				return prefix
		}
}
```

---

## 5. Verification & Complexity Finalization

### Dry Run
Input: `strs = ["flower", "flow", "flight"]`

- Start: `prefix = "flower"`
- Compare with `"flow"`:
	- `"flow"` does not start with `"flower"` -> shrink to `"flowe"`
	- no -> `"flow"`
	- yes -> keep `prefix = "flow"`
- Compare with `"flight"`:
	- does not start with `"flow"` -> `"flo"`
	- does not start with `"flo"` -> `"fl"`
	- starts with `"fl"` -> keep `prefix = "fl"`

Final answer: `"fl"`

### Final Complexity
- Time Complexity: `O(S)` where `S` is the total number of characters processed across all strings.
- Space Complexity: `O(1)` extra space (excluding the returned string).
