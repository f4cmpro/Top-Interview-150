import java.util.Stack

class Solution {
    fun isValid(s: String): Boolean {
        if (s.length % 2 != 0) return false
        val stack = Stack<Char>()
        for (i in s.lastIndex downTo 0) {
            if (stack.empty()) {
                stack.push(s[i])
            } else {
                val char = s[i]
                val stackChar = stack.peek()
                if (
                    ((char == '{' || char == '[') && stackChar == ')')
                    || ((char == '(' || char == '[') && stackChar == '}')
                    || ((char == '{' || char == '(') && stackChar == ']')
                ) {
                    return false
                }
                if ((char == '(' && stackChar == ')')
                    || (char == '{' && stackChar == '}')
                    || (char == '[' && stackChar == ']')
                ) {
                    stack.pop()
                } else {
                    stack.push(char)
                }
            }
        }
        return stack.empty()
    }
}