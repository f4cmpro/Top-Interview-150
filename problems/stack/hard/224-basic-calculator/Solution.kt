package `224-basic-calculator`

import java.util.Stack

class Solution {
    fun calculate(s: String): Int {
        val stack1 = Stack<Char>()
        val strBuilder = StringBuilder()
        for (i in s.indices) {
            val char = s[i]
            if (char == '+' || char == '-') {
                if (char == '-' && (i == 0 || s[i - 1] == '(')) {
                    strBuilder.append(0)
                }
                if (stack1.isEmpty()) {
                    stack1.push(char)
                } else {
                    if (stack1.peek() == '+' || stack1.peek() == '-') {
                        strBuilder.append(stack1.pop())
                    }
                    stack1.push(char)
                }
            } else if (char == '(') {
                stack1.push(char)
            } else if (char == ')') {
                var operation = stack1.pop()
                while (operation != '(') {
                    strBuilder.append(operation)
                    operation = stack1.pop()
                }
            } else if (char == ' ') {
                continue
            } else {
                strBuilder.append(char)
            }
        }
        while (stack1.isNotEmpty()) {
            strBuilder.append(stack1.pop())
        }

        if (!strBuilder.contains('+') && !strBuilder.contains('-')) {
            return strBuilder.toString().toInt()
        }

        val stack = Stack<Int>()

        for (char in strBuilder) {
            when (val str = char.toString()) {
                "+" -> {
                    val operand1 = stack.pop()
                    val operand2 = stack.pop()
                    stack.push(operand2 + operand1)
                }

                "-" -> {
                    val operand1 = stack.pop()
                    val operand2 = stack.pop()
                    stack.push(operand2 - operand1)
                }

                else -> {
                    stack.push(str.toInt())
                }
            }
        }

        return stack.toString().toInt()
    }

    fun calculate2(s: String): Int {
        var sum = 0
        var operation = '+'
        var i = 0
        while (i < s.length) {
            when (s[i]) {
                '(' -> {
                    val start = i
                    while (s[i] != ')') {
                        i++
                    }
                    when (operation) {
                        '+' -> sum += calculate2(s.substring(start + 1, i))
                        '-' -> sum -= calculate2(s.substring(start + 1, i))
                    }
                }

                ' ' -> {
                    i++
                    continue
                }
                '+' -> operation = '+'
                '-' -> operation = '-'
                else -> {
                    when (operation) {
                        '+' -> sum += s[i].toString().toInt()
                        '-' -> sum -= s[i].toString().toInt()
                    }
                }
            }
            i++
        }
        return sum
    }

    fun calExpression(sub: String): Int {
        var sum = 0
        var operation = '+'
        var i = 0
        while (i < sub.length) {
            when (sub[i]) {
                '(' -> {
                    when (operation) {
                        '+' -> sum += calExpression(sub.substring(i + 1))
                        '-' -> sum -= calExpression(sub.substring(i + 1))
                    }
                }

                ')' -> return sum
                ' ' -> continue
                '+' -> operation = '+'
                '-' -> operation = '-'
                else -> {
                    when (operation) {
                        '+' -> sum += sub[i].toString().toInt()
                        '-' -> sum -= sub[i].toString().toInt()
                    }
                }
            }
        }
        return sum
    }
}