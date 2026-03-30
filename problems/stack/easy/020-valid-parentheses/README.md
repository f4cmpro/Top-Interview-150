# 20 - Valid Parentheses

**Difficulty**: Easy
**LeetCode**: https://leetcode.com/problems/valid-parentheses/

---

## 1. Clarification & Edge Cases

### Problem restatement
Given a string `s` containing only the characters `'('`, `')'`, `'{'`, `'}'`, `'['`, `']'`, determine if the input string is **valid**.

A string is valid if:
1. Open brackets are closed by the **same type** of brackets.
2. Open brackets are closed in the **correct order**.
3. Every close bracket has a corresponding open bracket.

### Key constraints
* `0 <= s.length <= 10^4` (typical LeetCode constraint)
* Input is **not** sorted; ordering matters.
* Characters are limited to the 6 bracket characters.

### Edge cases to handle
* Empty string `""` → valid (`true`).
* Odd length strings (e.g., `"("`, `"()]"`) → immediately invalid.
* Starts with a closing bracket (e.g., `")("`, `"]"`) → invalid.
* Ends with an opening bracket (e.g., `"([]{"`) → invalid.
* Crossed / mis-nested pairs (e.g., `"([)]"`) → invalid.
* Large inputs: ensure O(n) time, avoid quadratic rescans.

---

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force
Repeatedly remove occurrences of the valid pairs `"()"`, `"{}"`, `"[]"` from the string until:
* no more removals are possible, then check whether the string is empty.

**Why it’s not great:** Each removal creates new strings and can scan the string multiple times.

* **Time:** Up to **O(n^2)** in worst-case (many passes + string copies)
* **Space:** Often **O(n)** due to creating intermediate strings

### Optimized Approach (Best Solution): Stack
Scan the string once. Push opening brackets onto a stack. When encountering a closing bracket, check whether it matches the most recent opening bracket (the top of the stack).

* **Time:** **O(n)** — each character is pushed/popped at most once
* **Space:** **O(n)** worst-case — all openings stored (e.g., `"((((..."`)

### Comparison (why stack is better)
The stack approach avoids repeated rescans and string allocations. Validity is inherently a **nesting** problem, and stacks are designed for “last opened must be first closed” (LIFO) behavior.

---

## 3. Algorithm Design

### Core idea
Use a stack to track **unmatched opening brackets**.

### Step-by-step logic
1. **Guard clause:** If `s.length` is odd, return `false`.
2. Initialize an empty stack.
3. For each character `c` in `s` (left-to-right):
   * If `c` is an opening bracket `(`, `{`, `[`: push it onto the stack.
   * Otherwise `c` is a closing bracket:
	 1. If the stack is empty, there is nothing to match → return `false`.
	 2. Pop the top opening bracket and verify it is the correct pair for `c`.
	 3. If it doesn’t match, return `false`.
4. After scanning all characters, the string is valid only if the stack is empty.

### Data structure choice
* **Stack** (in Kotlin, `ArrayDeque<Char>` is preferred):
  * Efficient push/pop from the end
  * Models nested structure correctly via LIFO

---

## 4. Production-Ready Implementation (Kotlin)

```kotlin
class Solution {
	fun isValid(s: String): Boolean {
		// Guard clause: odd-length strings can never be fully paired.
		if (s.length % 2 != 0) return false

		val stack = ArrayDeque<Char>()

		for (c in s) {
			when (c) {
				'(', '{', '[' -> stack.addLast(c)
				')', '}', ']' -> {
					if (stack.isEmpty()) return false

					val open = stack.removeLast()
					val matches = (open == '(' && c == ')') ||
							(open == '{' && c == '}') ||
							(open == '[' && c == ']')

					if (!matches) return false
				}
				else -> {
					// Not required on LeetCode (input is guaranteed), but safe for production.
					return false
				}
			}
		}

		return stack.isEmpty()
	}
}
```

Notes:
* `ArrayDeque` is generally preferred over `java.util.Stack` in Kotlin/Java for performance and modern style.
* The `else` branch is defensive; LeetCode inputs exclude it.

---

## 5. Verification & Complexity Finalization

### Dry Run
Input: `s = "{[()]}"`

* stack = []
* '{' → push → ['{']
* '[' → push → ['{', '[']
* '(' → push → ['{', '[', '(']
* ')' → pop '(' matches ')'
* ']' → pop '[' matches ']'
* '}' → pop '{' matches '}'
* end → stack empty → return `true`

Counterexample: `s = "([)]"`
* '(' push, '[' push
* ')' encountered: top is '[' which does **not** match ')' → return `false`

### Final Complexity
* **Time:** **O(n)**
* **Space:** **O(n)** (worst-case stack size)
