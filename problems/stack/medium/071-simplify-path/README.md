# 71 - Simplify Path

**Difficulty**: Medium
**LeetCode**: https://leetcode.com/problems/simplify-path/

---

## 1. Clarification & Edge Cases

### Key Constraints
- The input `path` is a valid absolute Unix path starting with `'/'`.
- `1 <= path.length <= 3000`
- `path` consists only of English letters, digits, `'.'`, `'/'`, and `'_'`.
- The path is guaranteed to be a valid absolute path.

### Edge Cases
| Case | Input | Expected Output |
|---|---|---|
| Root only | `"/"` | `"/"` |
| Multiple consecutive slashes | `"//home//foo/"` | `"/home/foo"` |
| Single dot (current dir) | `"/./home/./foo"` | `"/home/foo"` |
| Double dot (parent dir) | `"/home/foo/../bar"` | `"/home/bar"` |
| Double dot at root | `"/../"` | `"/"` |
| Trailing slash | `"/home/foo/"` | `"/home/foo"` |
| Triple dot (valid dir name) | `"/home/..."` | `"/home/..."` |
| Complex mix | `"/a/./b/../../c/"` | `"/c"` |

---

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force
Split the path by `'/'`, then iterate through the resulting tokens and simulate navigation:
- Skip empty strings and `"."`.
- On `".."`, remove the last directory if one exists.
- Otherwise, append the directory name to a result list.
- Join with `'/'` and prepend `'/'`.

This is essentially the same as the optimized approach — the key design decision is **which data structure** to use for the directory stack.

### Optimized Approach
Use a **`Deque<String>`** (specifically `ArrayDeque`) instead of `java.util.Stack`:
- `ArrayDeque` is faster than `Stack` (which is `synchronized` and backed by `Vector`).
- Iterate character-by-character through the path to extract directory tokens **without allocating an intermediate `List<String>`** from `split("/")`.
- This avoids creating a throwaway array and offers better cache locality.

### Comparison

| | Brute Force (`split`) | Optimized (char-by-char + `ArrayDeque`) |
|---|---|---|
| **Time** | O(n) | O(n) |
| **Space** | O(n) — extra array from `split` | O(n) — only the stack |
| **Why better** | Simple but allocates extra array | Avoids intermediate array; `ArrayDeque` is faster than `Stack` |

Both are O(n) time, but the optimized approach has **lower constant factors** due to avoiding `split()` allocation overhead and using the more performant `ArrayDeque`.

---

## 3. Algorithm Design

### Data Structure: `ArrayDeque<String>` (used as a Stack)
- Chosen because it provides O(1) `push`/`pop` (via `addLast`/`removeLast`) without synchronization overhead, unlike `java.util.Stack`.

### Step-by-Step Logic
1. Initialize an `ArrayDeque<String>` to act as a stack of directory components.
2. Track `startIndex = -1` to mark the beginning of a directory token.
3. Iterate over every character in `path`:
   - If the current character is **not** `'/'` and `startIndex == -1`, mark `startIndex = i` (start of a new token).
   - If the current character **is** `'/'` (or we've reached the last index) and `startIndex != -1`:
     - Extract the substring from `startIndex` to the current position as `dir`.
     - Reset `startIndex = -1`.
     - Apply the rule:
       - `"."` or `""` → skip (no-op, current directory).
       - `".."` → pop from the stack if it's not empty (go up one level).
       - anything else → push onto the stack (enter a directory).
4. After processing, if the stack is empty, return `"/"`.
5. Otherwise, build the result using a `StringBuilder`: for each directory in the stack (bottom to top), prepend `"/"` + directory name.
6. Return the built string.

---

## 4. Production-Ready Implementation

```kotlin
import java.util.ArrayDeque

class Solution {
    fun simplifyPath(path: String): String {
        val stack = ArrayDeque<String>()
        var startIndex = -1

        for (i in path.indices) {
            val char = path[i]

            // Mark the start of a new directory token
            if (startIndex == -1 && char != '/') {
                startIndex = i
            }

            // End of a token: hit a '/' or reached the end of the string
            if ((char == '/' || i == path.lastIndex) && startIndex != -1) {
                val dir = if (i == path.lastIndex && char != '/') {
                    path.substring(startIndex, i + 1) // include last char
                } else {
                    path.substring(startIndex, i)
                }
                startIndex = -1

                when (dir) {
                    ".", "" -> { /* current directory — no-op */ }
                    ".."    -> if (stack.isNotEmpty()) stack.removeLast() // go up one level
                    else    -> stack.addLast(dir) // enter directory
                }
            }
        }

        // Build canonical path
        if (stack.isEmpty()) return "/"
        val result = StringBuilder()
        for (dir in stack) {
            result.append('/').append(dir)
        }
        return result.toString()
    }
}
```

---

## 5. Verification & Complexity Finalization

### Dry Run: `"/a/./b/../../c/"`

| i | char | startIndex | dir extracted | stack after |
|---|---|---|---|---|
| 0 | `/` | -1 | — | `[]` |
| 1 | `a` | 1 | — | `[]` |
| 2 | `/` | 1 | `"a"` → push | `["a"]` |
| 3 | `.` | 3 | — | `["a"]` |
| 4 | `/` | 3 | `"."` → skip | `["a"]` |
| 5 | `b` | 5 | — | `["a"]` |
| 6 | `/` | 5 | `"b"` → push | `["a","b"]` |
| 7 | `.` | 7 | — | `["a","b"]` |
| 8 | `.` | — | — | `["a","b"]` |
| 9 | `/` | 7 | `".."` → pop | `["a"]` |
| 10 | `.` | 10 | — | `["a"]` |
| 11 | `.` | — | — | `["a"]` |
| 12 | `/` | 10 | `".."` → pop | `[]` |
| 13 | `c` | 13 | — | `[]` |
| 14 | `/` | 13 | `"c"` → push | `["c"]` |

Stack is `["c"]` → result = `"/c"` ✅

### Final Complexity

| | Complexity |
|---|---|
| **Time** | **O(n)** — single pass over the path string |
| **Space** | **O(n)** — stack holds at most n/2 directory components |
