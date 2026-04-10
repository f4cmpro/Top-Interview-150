package `155-min-stack`

class Solution {
    val stack = ArrayList<Int>()
    var mins = ArrayList<Int>()
    fun push(`val`: Int) {
        stack.add(`val`)
        if (mins.isEmpty()) {
            mins.add(`val`)
        } else {
            if (`val` <= mins.last()) {
                mins.add(`val`)
            }
        }
    }

    fun pop() {
        if (stack.isEmpty() || mins.isEmpty()) {
            return
        }
        if (stack.last() == mins.last()) {
            mins.removeAt(mins.lastIndex)
        }
        stack.removeAt(stack.lastIndex)
    }

    fun top(): Int {
        return stack.last()
    }

    fun getMin(): Int? {
        return if (mins.isEmpty()) {
            null
        } else {
            mins.last()
        }
    }
}