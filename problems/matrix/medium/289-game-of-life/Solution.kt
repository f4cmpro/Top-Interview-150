package gameoflife

class Solution {
    fun gameOfLife(board: Array<IntArray>) {
        // Guard clause: empty board
        if (board.isEmpty() || board[0].isEmpty()) return

        val m = board.size
        val n = board[0].size

        // Define 8 direction vectors for all neighbors
        val directions = arrayOf(
            Pair(-1, -1), // top-left
            Pair(-1, 0),  // top
            Pair(-1, 1),  // top-right
            Pair(0, -1),  // left
            Pair(0, 1),   // right
            Pair(1, -1),  // bottom-left
            Pair(1, 0),   // bottom
            Pair(1, 1)    // bottom-right
        )

        // Phase 1: Encode next state into current cell values
        for (i in 0 until m) {
            for (j in 0 until n) {
                val currentState = board[i][j]
                var liveNeighbors = 0

                // Count live neighbors
                for (direction in directions) {
                    val newRow = i + direction.first
                    val newCol = j + direction.second

                    // Check bounds and count original state using bit mask
                    if (newRow in 0 until m && newCol in 0 until n) {
                        liveNeighbors += board[newRow][newCol] and 1
                    }
                }

                // Apply Conway's Game of Life rules with encoding
                when {
                    // Rule 1 & 3: Live cell dies (underpopulation or overpopulation)
                    currentState == 1 && (liveNeighbors < 2 || liveNeighbors > 3) -> {
                        board[i][j] = 1 // encode: 1 -> 0 (dies)
                    }
                    // Rule 2: Live cell survives
                    currentState == 1 -> {
                        board[i][j] = 3 // encode: 1 -> 1 (lives on)
                    }
                    // Rule 4: Dead cell becomes alive (reproduction)
                    currentState == 0 && liveNeighbors == 3 -> {
                        board[i][j] = 2 // encode: 0 -> 1 (becomes alive)
                    }
                    // Dead cell stays dead
                    else -> {
                        board[i][j] = 0 // encode: 0 -> 0 (stays dead)
                    }
                }
            }
        }

        // Phase 2: Decode to get final next state
        for (i in 0 until m) {
            for (j in 0 until n) {
                board[i][j] = board[i][j] shr 1 // right shift to extract next state
            }
        }
    }
}