package spiralmatrix

class Solution {
    fun spiralOrder(matrix: Array<IntArray>): List<Int> {
        val spiralOrder = arrayListOf<Int>()
        var top = 0
        var right = matrix[0].lastIndex
        var bottom = matrix.lastIndex
        var left = 0
        while (top <= bottom && left <= right) {

            //process entire top row elements
            //element range from the top-left corner -> top-right
            for (t in left..right) {
                spiralOrder.add(matrix[top][t])
            }

            //process entire right column elements
            //element range from top-right + 1 to bottom-right
            for (r in top + 1..bottom) {
                spiralOrder.add(matrix[r][right])
            }

            //process entire bottom row elements
            //element range from bottom-right - 1 to bottom-left
            if (top < bottom) {
                for (b in right - 1 downTo left) {
                    spiralOrder.add(matrix[bottom][b])
                }
            }

            //process entire left column elements
            //element range from bottom-left - 1 to top-left + 1
            if (left < right) {
                for (l in bottom - 1 downTo top + 1) {
                    spiralOrder.add(matrix[l][left])
                }
            }

            top++
            right--
            bottom--
            left++
        }
        return spiralOrder
    }
}