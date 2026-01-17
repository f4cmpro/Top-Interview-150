package sudoku

class Solution {
    fun isValidSudoku(board: Array<CharArray>): Boolean {
        val rows = Array(board.size) { BooleanArray(board.size) { false } }
        val cols = Array(board.size) { BooleanArray(board.size) { false } }
        val boxes = Array(board.size) { BooleanArray(board.size) { false } }
        for (i in 0..board.lastIndex) {
            for (j in 0..board[i].lastIndex) {
                val item = board[i][j].digitToIntOrNull() ?: continue
                if (rows[i][item - 1] || cols[j][item - 1]) return false
                val subX = i / 3
                val subY = j / 3
                val boxInd = 3 * subX + subY
                if (boxes[boxInd][item - 1]) return false
                rows[i][item - 1] = true
                cols[j][item - 1] = true
                boxes[boxInd][item - 1] = true
            }
        }
        return true
    }
}

fun main() {
    val board: Array<CharArray> = arrayOf(
        charArrayOf('8', '3', '.', '.', '7', '.', '.', '.', '.'),
        charArrayOf('6', '.', '.', '1', '9', '5', '.', '.', '.'),
        charArrayOf('.', '9', '1', '.', '.', '.', '.', '6', '.'),
        charArrayOf('5', '.', '.', '.', '6', '.', '.', '.', '3'),
        charArrayOf('4', '.', '.', '8', '.', '3', '.', '.', '1'),
        charArrayOf('7', '.', '.', '.', '2', '.', '.', '.', '6'),
        charArrayOf('.', '6', '.', '.', '.', '.', '2', '8', '.'),
        charArrayOf('.', '.', '.', '4', '1', '9', '.', '.', '5'),
        charArrayOf('.', '.', '.', '.', '8', '.', '.', '7', '9')
    )
    val ans = Solution().isValidSudoku(board)
    println("Sudoku is valid = $ans")
}