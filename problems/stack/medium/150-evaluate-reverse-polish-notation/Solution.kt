package `150-evaluate-reverse-polish-notation`

import java.util.Stack

class Solution {
    fun evalRPN(tokens: Array<String>): Int {
        val stack = Stack<Int>()
        for (str in tokens) {
            when (str) {
                "+" -> {
                    val operand1 = stack.pop().toInt()
                    val operand2 = stack.pop().toInt()
                    stack.push(operand2 + operand1)
                }

                "-" -> {
                    val operand1 = stack.pop().toInt()
                    val operand2 = stack.pop().toInt()
                    stack.push(operand2 - operand1)
                }

                "*" -> {
                    val operand1 = stack.pop().toInt()
                    val operand2 = stack.pop().toInt()
                    stack.push(operand2 * operand1)
                }

                "/" -> {
                    val operand1 = stack.pop().toInt()
                    val operand2 = stack.pop().toInt()
                    stack.push(operand2 / operand1)
                }

                else -> {
                    stack.push(str.toInt())
                }
            }
        }
        return stack.pop()
    }
}