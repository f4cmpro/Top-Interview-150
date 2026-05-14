class Solution {
    fun canCompleteCircuit(gas: IntArray, cost: IntArray): Int {
        var startIndex = -1
        for (i in gas.indices) {
            if (gas[i] - cost[i] >= 0) {
                startIndex = i
                break
            }
        }
        if (startIndex == -1) {
            return -1
        }
        var restTank = gas[startIndex] - cost[startIndex]
        var nextIndex = startIndex + 1
        while (nextIndex != startIndex) {
            restTank += gas[nextIndex] - cost[nextIndex]
            if (restTank < 0) {
                return -1
            }
            nextIndex =
        }
        return startIndex
    }
}