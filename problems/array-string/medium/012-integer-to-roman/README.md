# 12 - Integer to Roman

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/integer-to-roman/

---

### 1. Clarification & Edge Cases:

- Input is a single integer `num`.
- LeetCode constraint for this problem is typically `1 <= num <= 3999`.
- Output must be the Roman numeral representation using standard subtractive notation:
	- `IV` (4), `IX` (9), `XL` (40), `XC` (90), `CD` (400), `CM` (900).
- Roman numeral symbols are uppercase and combined from largest value to smallest.

Potential edge cases to handle:

- Smallest valid number: `1` -> `I`
- Largest valid number: `3999` -> `MMMCMXCIX`
- Values that require subtractive pairs:
	- `4`, `9`, `40`, `90`, `400`, `900`
- Repeated symbols:
	- `3` -> `III`, `30` -> `XXX`, `3000` -> `MMM`
- Out-of-range defensive handling (if used outside LeetCode constraints):
	- `num <= 0` or `num > 3999`

### 2. High-Level Approach Analysis (Trade-offs):

Brute Force approach:

- Build Roman numeral by processing each digit position separately (thousands, hundreds, tens, ones) with many conditional branches.
- Example: for hundreds digit, manually map 1..9 to `C`, `CC`, ..., `CM`.

Optimized approach (Best Solution):

- Use two parallel arrays for Roman values and symbols ordered descending:
	- Values: `[1000, 900, 500, 400, ..., 1]`
	- Symbols: `['M', 'CM', 'D', 'CD', ..., 'I']`
- Greedily subtract the largest possible value while appending its symbol.

Complexity comparison:

- Brute Force:
	- Time: `O(1)` (bounded digits), but logic-heavy and harder to maintain.
	- Space: `O(1)` excluding output.
- Optimized Greedy:
	- Time: `O(1)` because the value/symbol table has fixed size (13 entries).
	- Space: `O(1)` excluding output.

Why optimized is better:

- Much cleaner and less error-prone.
- Naturally supports subtractive notation by including entries like `900 -> CM`.
- Easier to extend or verify versus many digit-specific branches.

### 3. Algorithm Design:

Step-by-step logic:

1. If `num` is outside supported Roman numeral range (`1..3999`), return an empty string (or throw, depending on API contract).
2. Prepare descending arrays:
	 - `values = [1000, 900, 500, 400, ..., 1]`
	 - `symbols = ["M", "CM", "D", "CD", ..., "I"]`
3. Initialize:
	 - `remaining = num`
	 - `result = StringBuilder()`
4. Iterate through each index `i` in `values`:
	 - While `remaining >= values[i]`:
		 - Append `symbols[i]` to `result`
		 - Subtract `values[i]` from `remaining`
5. Return `result.toString()`.

Data structures used:

- `IntArray` for numeric Roman values: compact and fast for fixed integer table.
- `Array<String>` for Roman symbols: direct lookup by index.
- `StringBuilder` for efficient repeated string concatenation.

### 4. Production-Ready Implementation:

```kotlin
class Solution {
		fun intToRoman(num: Int): String {
				// Guard clause for defensive usage outside the original LeetCode constraints.
				if (num !in 1..3999) return ""

				val values = intArrayOf(
						1000, 900, 500, 400,
						100, 90, 50, 40,
						10, 9, 5, 4, 1
				)

				val symbols = arrayOf(
						"M", "CM", "D", "CD",
						"C", "XC", "L", "XL",
						"X", "IX", "V", "IV", "I"
				)

				var remaining = num
				val roman = StringBuilder()

				for (i in values.indices) {
						while (remaining >= values[i]) {
								roman.append(symbols[i])
								remaining -= values[i]
						}
				}

				return roman.toString()
		}
}
```

### 5. Verification & Complexity Finalization:

Dry run with input `num = 1994`:

- Start: `remaining = 1994`, `roman = ""`
- Use `1000 (M)` once -> `remaining = 994`, `roman = "M"`
- Use `900 (CM)` once -> `remaining = 94`, `roman = "MCM"`
- Skip `500, 400, 100`
- Use `90 (XC)` once -> `remaining = 4`, `roman = "MCMXC"`
- Skip `50, 40, 10, 9, 5`
- Use `4 (IV)` once -> `remaining = 0`, `roman = "MCMXCIV"`
- End result: `"MCMXCIV"`

Final complexity:

- Time: `O(1)` (fixed-size table of 13 symbols; bounded iterations)
- Space: `O(1)` auxiliary (excluding output string)