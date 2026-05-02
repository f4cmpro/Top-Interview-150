package `122-best-time-to-buy-and-sell-stock-ii`

class Solution {
    fun maxProfit(prices: IntArray): Int {
        var maxProfit = 0
        var next = 0
        while (next < prices.size - 1) {
            val profit = prices[next + 1] - prices[next]
            if (profit > 0) {
                maxProfit += profit
            }
            next++
        }
        return maxProfit
    }
}