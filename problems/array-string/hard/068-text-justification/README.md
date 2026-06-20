# 68 - Text Justification

**Difficulty**: 🔴 Hard  
**LeetCode**: https://leetcode.com/problems/text-justification/

---

### Problem Description
Given an array of strings `words` and a width `maxWidth`, format the text such that each line has exactly `maxWidth` characters and is **fully (left and right) justified**.

- Pack words greedily: fit as many words as possible per line.
- Distribute extra spaces as evenly as possible; when uneven, left slots get more spaces.
- The **last line** must be **left-justified** with single spaces between words, padded with trailing spaces.

**Constraints:**
- `1 <= words.length <= 300`
- `1 <= words[i].length <= 20`
- `words[i]` consists of only English letters and symbols.
- `1 <= maxWidth <= 100`
- `words[i].length <= maxWidth`

---

### 1. Clarification & Edge Cases:
- **Input:** `words: Array<String>`, `maxWidth: Int`
- **Output:** `List<String>` — each string is exactly `maxWidth` characters long.
- **Constraints to note:**
  - Each word fits within `maxWidth`, so a single word will never overflow a line on its own.
  - `words.length >= 1`, so input is never empty.
- **Edge Cases:**
  - **Single word on a line** (including the last line): left-justify with trailing spaces — no inter-word spacing to distribute.
  - **Last line:** always left-justified — words separated by exactly one space, remaining characters filled with spaces.
  - **Line with exactly one word** (non-last): same as left-justify — pad right with spaces.
  - **Words perfectly filling a line** (zero extra spaces): valid; no extra distribution needed.
  - **Single word input** (`words.length == 1`): forms one line, left-justified.

---

### 2. High-Level Approach Analysis (Trade-offs):

- **Brute Force:**
  For each starting word index, compute all possible line configurations, then pick the one that fits within `maxWidth`. Format each chosen line naively with a loop.
  - **Time Complexity:** O(n²) — nested scanning for line breaks.
  - **Space Complexity:** O(n) — for storing grouped lines.

- **Optimized Approach (Greedy Single Pass):**
  Use a greedy scan to pack words into lines: keep a running character count, add words as long as they fit (accounting for at least one space between words), and close a line when the next word would overflow. Then format each line in a second pass.
  - **Time Complexity:** O(n · W) — where n is the number of words and W is `maxWidth`; the string building per line is bounded by `maxWidth`.
  - **Space Complexity:** O(n) — for storing the grouped lines before formatting.

- **Comparison:**
  The greedy approach is optimal. It determines line boundaries in a single linear scan (O(n)), and string building per line is O(maxWidth) at most. The brute force wastes time re-evaluating invalid combinations. There is no better approach since we must at least read every word once.

---

### 3. Algorithm Design:

**Phase 1 — Greedy Word Grouping:**
1. Maintain a `currentLine` word list and `currentLength` (total chars used, counting one minimum space between adjacent words).
2. For each word: if `currentLength + 1 + word.length <= maxWidth` (or the line is empty), add it to `currentLine`; otherwise, save `currentLine` to `lines` and start a new `currentLine` with this word.
3. After processing all words, push the final `currentLine` into `lines`.

**Phase 2 — Line Formatting:**
For each line group:
- **Last line or single-word line → left-justify:**
  - Join words with one space, then append trailing spaces until length equals `maxWidth`.
- **All other lines → full justify:**
  - `totalWordChars = sum of word lengths`
  - `totalSpaces = maxWidth - totalWordChars`
  - `gaps = numWords - 1`
  - `baseSpaces = totalSpaces / gaps` (minimum spaces per gap)
  - `extraSpaces = totalSpaces % gaps` (first `extraSpaces` gaps get one additional space)
  - Build the string by interleaving words with their computed gap widths.

**Data Structures:** `MutableList<String>` for word groups per line; `StringBuilder` for efficient string construction.

---

### 4. Production-Ready Implementation:

```kotlin
class Solution {
    fun fullJustify(words: Array<String>, maxWidth: Int): List<String> {
        val lines = mutableListOf<List<String>>()
        var currentLine = mutableListOf<String>()
        var currentLength = 0

        // Phase 1: Greedily pack words into lines.
        // currentLength counts characters assuming one space between each adjacent word pair.
        for (word in words) {
            val requiredLength = if (currentLine.isEmpty()) word.length else currentLength + 1 + word.length
            if (requiredLength <= maxWidth) {
                currentLine.add(word)
                currentLength = requiredLength
            } else {
                lines.add(currentLine)
                currentLine = mutableListOf(word)
                currentLength = word.length
            }
        }
        lines.add(currentLine) // Flush the last line

        // Phase 2: Format each line.
        return lines.mapIndexed { index, line ->
            val isLastLine = index == lines.size - 1
            formatLine(line, maxWidth, isLastLine)
        }
    }

    private fun formatLine(words: List<String>, maxWidth: Int, isLastLine: Boolean): String {
        val sb = StringBuilder()
        val numWords = words.size

        if (isLastLine || numWords == 1) {
            // Left-justify: single space between words, pad trailing spaces.
            sb.append(words.joinToString(" "))
            repeat(maxWidth - sb.length) { sb.append(' ') }
        } else {
            // Full justify: distribute spaces evenly across gaps, extra spaces go left-to-right.
            val totalWordChars = words.sumOf { it.length }
            val totalSpaces = maxWidth - totalWordChars
            val gaps = numWords - 1
            val baseSpaces = totalSpaces / gaps
            val extraSpaces = totalSpaces % gaps // First `extraSpaces` gaps get one extra space

            for (i in words.indices) {
                sb.append(words[i])
                if (i < gaps) {
                    val spacesForThisGap = baseSpaces + if (i < extraSpaces) 1 else 0
                    repeat(spacesForThisGap) { sb.append(' ') }
                }
            }
        }

        return sb.toString()
    }
}
```

---

### 5. Verification & Complexity Finalization:

**Dry Run** with `words = ["This","is","an","example","of","text","justification."]`, `maxWidth = 16`:

**Phase 1 — Greedy Grouping:**

| Word            | requiredLength | Fits? | currentLine after                    |
|-----------------|----------------|-------|---------------------------------------|
| "This"          | 4              | ✅    | ["This"]                              |
| "is"            | 4+1+2 = 7      | ✅    | ["This","is"]                         |
| "an"            | 7+1+2 = 10     | ✅    | ["This","is","an"]                    |
| "example"       | 10+1+7 = 18    | ❌    | push ["This","is","an"]; start ["example"] |
| "of"            | 7+1+2 = 10     | ✅    | ["example","of"]                      |
| "text"          | 10+1+4 = 15    | ✅    | ["example","of","text"]               |
| "justification."| 15+1+14 = 30   | ❌    | push ["example","of","text"]; start ["justification."] |
| (end)           | —              | —     | push ["justification."]               |

Lines: `[["This","is","an"], ["example","of","text"], ["justification."]]`

**Phase 2 — Formatting:**

Line 0: `["This","is","an"]`, not last, 3 words
- `totalWordChars = 4+2+2 = 8`, `totalSpaces = 16−8 = 8`, `gaps = 2`
- `baseSpaces = 4`, `extraSpaces = 0`
- Result: `"This    is    an"` ✓

Line 1: `["example","of","text"]`, not last, 3 words
- `totalWordChars = 7+2+4 = 13`, `totalSpaces = 16−13 = 3`, `gaps = 2`
- `baseSpaces = 1`, `extraSpaces = 1` → gap 0 gets 2 spaces, gap 1 gets 1 space
- Result: `"example  of text"` ✓

Line 2: `["justification."]`, last line → left-justify
- `"justification." + "  "` (2 trailing spaces)
- Result: `"justification.  "` ✓

**Final Complexity:**
- **Time Complexity:** O(n · W) — one linear pass to group words (O(n)), one pass to format lines where each line build is O(maxWidth). In practice this is essentially O(n) since W is bounded by 100.
- **Space Complexity:** O(n) — for the grouped line lists; the output list itself is O(total output characters) = O(n · W).