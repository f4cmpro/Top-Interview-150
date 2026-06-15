# 28 - Find the Index of the First Occurrence in a String

**Difficulty**: 🟢 Easy  
**LeetCode**: https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/

---

## 1. Clarification & Edge Cases

- Goal: return the starting index of the first occurrence of `needle` in `haystack`; return `-1` if not found.
- Input type: two strings.
- Typical constraints for this problem are up to around `10^4` characters, so both brute force and optimized methods are feasible, but optimized scales better.
- Matching must be contiguous and case-sensitive.

Edge cases to handle:

- `needle` is empty -> return `0`.
- `haystack` is empty and `needle` is non-empty -> return `-1`.
- `needle.length > haystack.length` -> return `-1`.
- Exact full-string match -> return `0`.
- Overlapping repeated patterns (for correctness and efficiency), e.g. `haystack = "aaaaa"`, `needle = "aaa"`.

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force

- Try every possible starting index in `haystack`.
- Compare characters one by one with `needle`.
- If all characters match, return that starting index.
- Worst-case time complexity: `O((n - m + 1) * m)` which is approximately `O(n * m)`.
- Space complexity: `O(1)`.

### Optimized Approach (Best Solution): KMP (Knuth-Morris-Pratt)

- Precompute an `lps` (Longest Prefix Suffix) array for `needle`.
- During mismatches, use `lps` to skip unnecessary re-comparisons.
- Time complexity: `O(n + m)`.
- Space complexity: `O(m)`.

Why KMP is better:

- Brute force may re-check many characters repeatedly.
- KMP reuses previous match information to avoid redundant work, giving linear-time matching.

## 3. Algorithm Design

Optimized KMP steps:

1. Guard clauses:
   - If `needle` is empty, return `0`.
   - If `haystack` is empty or `needle` is longer, return `-1`.

2. Build `lps` for `needle`:
   - `lps[i]` stores the length of the longest proper prefix of `needle[0..i]` that is also a suffix of it.
   - Use two pointers:
	 - `length` for the current best prefix-suffix length.
	 - `i` to scan through `needle`.
   - On match, increase both and assign `lps[i]`.
   - On mismatch, fallback with `length = lps[length - 1]` until a match or zero.

3. Search in `haystack`:
   - Use `i` for `haystack`, `j` for `needle`.
   - If characters match, increment both pointers.
   - If `j` reaches `needle.length`, return `i - j`.
   - On mismatch:
	 - If `j > 0`, set `j = lps[j - 1]`.
	 - Else increment `i`.

4. If no full match is found, return `-1`.

Data structure used:

- `IntArray` for `lps` because it stores reusable prefix-suffix information needed for efficient fallback.

## 4. Production-Ready Implementation (Kotlin)

```kotlin
class Solution {
	fun strStr(haystack: String, needle: String): Int {
		// Guard clauses for important edge cases.
		if (needle.isEmpty()) return 0
		if (haystack.isEmpty()) return -1
		if (needle.length > haystack.length) return -1

		val lps = buildLps(needle)

		var i = 0 // Pointer for haystack
		var j = 0 // Pointer for needle

		while (i < haystack.length) {
			if (haystack[i] == needle[j]) {
				i++
				j++

				if (j == needle.length) {
					return i - j // Start index of first match
				}
			} else {
				if (j > 0) {
					// Jump using LPS to avoid re-checking matched characters.
					j = lps[j - 1]
				} else {
					i++
				}
			}
		}

		return -1
	}

	private fun buildLps(pattern: String): IntArray {
		val lps = IntArray(pattern.length)
		var length = 0 // Current longest prefix-suffix length
		var i = 1

		while (i < pattern.length) {
			if (pattern[i] == pattern[length]) {
				length++
				lps[i] = length
				i++
			} else {
				if (length > 0) {
					length = lps[length - 1]
				} else {
					lps[i] = 0
					i++
				}
			}
		}

		return lps
	}
}
```

## 5. Verification & Complexity Finalization

Dry run:

- Input: `haystack = "mississippi"`, `needle = "issip"`
- `lps("issip") = [0, 0, 0, 1, 0]`
- Search progression finds the first full match starting at index `4`.
- Output: `4`

Final complexity:

- Time: `O(n + m)`
- Space: `O(m)`
