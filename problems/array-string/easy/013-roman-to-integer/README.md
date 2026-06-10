# 13 - Roman to Integer

**Difficulty**: 🟢 Easy  
**LeetCode**: https://leetcode.com/problems/roman-to-integer/

---

## 1. Clarification & Edge Cases:

### Key Constraints
- Input is a Roman numeral string `s`.
- In LeetCode constraints, `1 <= s.length <= 15`.
- Characters are from `{I, V, X, L, C, D, M}`.
- Input is guaranteed to be a valid Roman numeral.

### Important Clarifications
- Roman symbols normally add their values.
- If a smaller value appears before a larger one, it is subtractive (e.g., `IV = 4`, `IX = 9`).
- Valid subtractive pairs are:
  - `I` before `V` or `X`
  - `X` before `L` or `C`
  - `C` before `D` or `M`

### Edge Cases
- Single-character numeral (`"I"`, `"V"`, `"M"`).
- Numerals with multiple subtractive pairs (`"MCMXCIV"`).
- Numerals with only additive pattern (`"LVIII"`).
- Minimum/empty-like defensive case (if an empty string is passed unexpectedly).

---

## 2. High-Level Approach Analysis (Trade-offs):

### Brute Force Approach
Generate integer values by repeatedly checking two-character substrings first (for subtractive pairs), otherwise one character, potentially using many conditional branches and string slicing.

- Time Complexity: $O(n)$ for typical scanning, but can degrade in practical implementations if substring operations/copies are used repeatedly.
- Space Complexity: $O(1)$ auxiliary (ignoring temporary substring allocations).

### Optimized Approach (Best Solution)
Traverse from right to left:
- Keep `prevValue` (value of previous symbol to the right).
- If current value is smaller than `prevValue`, subtract it.
- Otherwise, add it.

Why this works:
- Subtractive notation is exactly captured by the comparison with the symbol on the right.

- Time Complexity: $O(n)$
- Space Complexity: $O(1)$

### Why Optimized Is Better
- Cleaner and less error-prone than checking all pair combinations manually.
- No substring handling.
- Very compact logic with direct Roman numeral rule encoding.

---

## 3. Algorithm Design:

### Step-by-Step Logic
1. Build a map from Roman character to integer value.
2. Add a guard clause: if input is empty, return `0`.
3. Initialize:
   - `total = 0`
   - `prevValue = 0`
4. Iterate from the last character to the first:
   - `currentValue = value of current Roman symbol`
   - If `currentValue < prevValue`, subtract it from `total`.
   - Else, add it to `total`.
   - Update `prevValue = currentValue`.
5. Return `total`.

### Data Structures Used
- `HashMap<Char, Int>` (or `mapOf` in Kotlin):
  - Provides quick symbol-to-value conversion.
  - Keeps the implementation readable and maintainable.

---

## 4. Production-Ready Implementation:

```kotlin
class Solution {
	fun romanToInt(s: String): Int {
		// Guard clause for defensive programming.
		if (s.isEmpty()) return 0

		val romanValues = mapOf(
			'I' to 1,
			'V' to 5,
			'X' to 10,
			'L' to 50,
			'C' to 100,
			'D' to 500,
			'M' to 1000
		)

		var total = 0
		var prevValue = 0

		for (index in s.lastIndex downTo 0) {
			val currentValue = romanValues[s[index]] ?: 0

			// If a smaller symbol appears before a larger one, subtract.
			if (currentValue < prevValue) {
				total -= currentValue
			} else {
				total += currentValue
			}

			prevValue = currentValue
		}

		return total
	}
}
```

---

## 5. Verification & Complexity Finalization:

### Dry Run (Example: `"MCMXCIV"`)

Values: `M=1000, C=100, M=1000, X=10, C=100, I=1, V=5`

Traverse right to left:
1. `V(5)`: `5 >= 0` -> `total = 5`, `prev = 5`
2. `I(1)`: `1 < 5` -> `total = 4`, `prev = 1`
3. `C(100)`: `100 >= 1` -> `total = 104`, `prev = 100`
4. `X(10)`: `10 < 100` -> `total = 94`, `prev = 10`
5. `M(1000)`: `1000 >= 10` -> `total = 1094`, `prev = 1000`
6. `C(100)`: `100 < 1000` -> `total = 994`, `prev = 100`
7. `M(1000)`: `1000 >= 100` -> `total = 1994`, `prev = 1000`

Final result: `1994`

### Final Complexity
- Time Complexity: $O(n)$, where $n$ is the length of the string.
- Space Complexity: $O(1)$ auxiliary space (Roman symbol set size is constant).
