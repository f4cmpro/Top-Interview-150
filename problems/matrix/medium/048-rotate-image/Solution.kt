package rotateimage

class Solution {
    fun rotate(matrix: Array<IntArray>): Unit {
        val n = matrix.size
        //layers loop
        for (layer in 0 until n / 2) {
            //iterations
            for (i in 0 until n - 2 * layer - 1) {
                //Swap Cycle A -> D -> C -> B -> A
                val temp = matrix[layer][layer + i]
                matrix[layer][layer + i] = matrix[n - 1 - layer - i][layer]
                matrix[n - 1 - layer - i][layer] = matrix[n - 1 - layer][n - 1 - layer - i]
                matrix[n - 1 - layer][n - 1 - layer - i] = matrix[layer + i][n - 1 - layer]
                matrix[layer + i][n - 1 - layer] = temp
            }
        }

    }
}

fun main() {
    val matrix = arrayOf(
        intArrayOf(1, 2, 3),
        intArrayOf(4, 5, 6),
        intArrayOf(7, 8, 9)
    )
    println("Original matrix:")
    matrix.forEach { row ->
        println(row.joinToString(" "))
    }

    Solution().rotate(matrix)

    println("\nRotated matrix:")
    matrix.forEach { row ->
        println(row.joinToString(" "))
    }
}