package `121-best-time-to-buy-and-sell-stock`

import kotlin.math.max

class Solution {
    fun maxProfit(prices: IntArray): Int {
        var profit = 0
        var lowestPrice = prices[0]
        var next = 0
        while (next < prices.size) {
            if (prices[next] < lowestPrice) {
                lowestPrice = prices[next]
            } else {
                profit = max(profit, prices[next] - lowestPrice)
            }
            next++
        }
        return profit
    }
}
