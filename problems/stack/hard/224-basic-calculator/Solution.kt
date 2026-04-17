package `224-basic-calculator`

import java.util.Stack

class Solution {
    fun calculate(s: String): Int {
        val stack1 = Stack<Char>()
        val tokens = ArrayList<String>()
        var i = 0
        while (i < s.length) {
            when (s[i]) {
                '(' -> {
                    stack1.push(s[i])
                    i++
                }

                ')' -> {
                    var operation = stack1.pop()
                    while (operation != '(') {
                        tokens.add(operation.toString())
                        operation = stack1.pop()
                    }
                    i++
                }

                '+' -> {
                    if (stack1.isNotEmpty() && (stack1.peek() == '+' || stack1.peek() == '-')) {
                        tokens.add(stack1.pop().toString())
                    }
                    stack1.push(s[i])
                    i++
                }

                ' ' -> {
                    i++
                    continue
                }

                '-' -> {
                    var k = i - 1
                    while (k >= 0 && s[k] == ' ') {
                        k--
                    }
                    if (k < 0 || s[k] == '(') {
                        tokens.add("0")
                    }
                    i++
                }

                else -> {
                    val numStr = StringBuilder()
                    while (i < s.length && s[i] != '(' && s[i] != ')' && s[i] != '+' && s[i] != '-' && s[i] != ' ') {
                        numStr.append(s[i])
                        i++
                    }
                    tokens.add(numStr.toString())
                }
            }
        }

        while (stack1.isNotEmpty()) {
            tokens.add(stack1.pop().toString())
        }

        val stack = Stack<Int>()

        for (str in tokens) {
            when (str) {
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

        return stack.pop()
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