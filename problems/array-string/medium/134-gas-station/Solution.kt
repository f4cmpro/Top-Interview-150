package `134-gas-station`

class Solution {
    fun canCompleteCircuit(gas: IntArray, cost: IntArray): Int {
        var startIndex = 0
        var sumCost = 0
        var remaining = 0
        for (i in 0 until gas.size) {
            val currentTank = gas[i] - cost[i]
            sumCost += currentTank
            remaining += currentTank
            if (remaining < 0) {
                startIndex = i + 1
                remaining = 0
            }
        }
        return if (sumCost >= 0) startIndex else -1
    }
}