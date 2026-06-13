class Solution {
	fun convert(s: String, numRows: Int): String {
		if (numRows == 1 || s.length <= numRows) {
			return s
		}

		val zigzag = MutableList(numRows) { StringBuilder() }
		var isBackward = false
		var rowIndex = 0

		for (ch in s) {
			zigzag[rowIndex].append(ch)

			if (isBackward) {
				rowIndex--
			} else {
				rowIndex++
			}

			if (rowIndex == 0) {
				isBackward = false
			} else if (rowIndex == numRows - 1) {
				isBackward = true
			}
		}

		val result = StringBuilder()
		for (row in zigzag) {
			result.append(row)
		}
		return result.toString()
	}
}