import java.util.Stack

class Solution {
    fun simplifyPath(path: String): String {
        val stack = Stack<String>()
        var startStrIndex = -1
        for (i in path.indices) {
            val char = path[i]
            if (startStrIndex == -1 && path[i] != '/') {
                startStrIndex = i
            }
            if ((char == '/' || i == path.lastIndex) && startStrIndex != -1) {
                val dir = if (i == path.lastIndex && char != '/') {
                    path.substring(startStrIndex, i + 1)
                } else {
                    path.substring(startStrIndex, i)
                }
                startStrIndex = -1
                when (dir) {
                    "", "." -> continue
                    ".." -> if (!stack.empty()) stack.pop()
                    else -> stack.push(dir)
                }
            }
        }
//        return stack.joinToString(separator = "/", prefix = "/") //slower than StringBuilder

        if (stack.isEmpty()) return "/"
        val result = StringBuilder()
        for (dir in stack) {
            result.append("/").append(dir)
        }

        return result.toString()
    }
}
