# 122 - Best Time to Buy and Sell Stock II

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/

---

### 1. Clarification & Edge Cases:
*   **Input:** An array of integers `prices`, where `prices[i]` is the price of a stock on day `i`.
*   **Output:** The maximum profit that can be achieved by buying and selling multiple times.
*   **Constraints:**
    *   You can complete as many transactions as you like (i.e., buy one and sell one share of the stock multiple times).
    *   You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy again).
    *   `1 <= prices.length <= 3 * 10^4`
    *   `0 <= prices[i] <= 10^4`
*   **Edge Cases:**
    *   **Empty or Single-element array:** No transactions are possible, so the profit is 0.
    *   **Prices are always decreasing:** No profit can be made, so the result is 0.
    *   **All prices are the same:** No profit can be made.

### 2. High-Level Approach Analysis (Trade-offs):
*   **Brute Force (Recursive):**
    *   A recursive approach would explore every possible transaction. For each day, we can either buy, sell, or do nothing. This creates a decision tree of all possible sequences of actions.
    *   **Time Complexity:** O(2^n) - For each day, we have two choices (buy/sell or hold), leading to an exponential number of paths to explore. This is computationally infeasible for the given constraints.
    *   **Space Complexity:** O(n) - Due to the recursion depth.

*   **Optimized Approach (Peak-Valley Single Pass):**
    *   The key insight is that we can accumulate profit from every consecutive rise in price. Instead of trying to find the single best buy and sell time, we can treat every day-to-day increase as a separate, valid transaction.
    *   For example, if prices are `[1, 5, 8]`, buying at 1 and selling at 8 (profit = 7) is the same as buying at 1, selling at 5 (profit = 4), then buying at 5 and selling at 8 (profit = 3). The total profit is `4 + 3 = 7`.
    *   We can iterate through the array once, and whenever `prices[i]` is greater than `prices[i-1]`, we add the difference to our `totalProfit`.
    *   **Time Complexity:** O(n) - We iterate through the array only once.
    *   **Space Complexity:** O(1) - We only use a few variables to store the total profit.

*   **Comparison:**
    The single-pass approach is vastly superior. Its O(n) linear time complexity is extremely efficient and easily handles the input size constraints, whereas the O(2^n) brute-force approach would time out on all but the smallest inputs.

### 3. Algorithm Design:
The algorithm is based on the idea of accumulating profits from all upward price movements.

1.  Initialize a variable `totalProfit` to `0`. This will accumulate the profit from all transactions.
2.  Handle the edge case where the array has fewer than two elements. If so, return `0` as no transaction is possible.
3.  Iterate through the `prices` array starting from the second day (`i = 1`).
4.  For each day, compare its price (`prices[i]`) with the previous day's price (`prices[i-1]`).
5.  If `prices[i]` is greater than `prices[i-1]`, it means there was a price increase, and we can make a profit. Add the difference (`prices[i] - prices[i-1]`) to `totalProfit`.
6.  If the price is not greater, we do nothing, as it represents either a loss or no change, and we are only interested in profitable transactions.
7.  After the loop completes, `totalProfit` will hold the sum of all possible profits.

**Data Structures:** No special data structures are needed. We only use a single variable to accumulate the profit.

### 4. Production-Ready Implementation:
```kotlin
class Solution {
    fun maxProfit(prices: IntArray): Int {
        // Guard Clause: If there are fewer than 2 days, no profit can be made.
        if (prices.size < 2) {
            return 0
        }

        var totalProfit = 0

        // We can accumulate profit by capturing all upward price movements.
        // This is equivalent to buying low and selling high for every consecutive profitable transaction.
        for (i in 1 until prices.size) {
            // If today's price is higher than yesterday's, it's a profitable transaction.
            if (prices[i] > prices[i - 1]) {
                totalProfit += prices[i] - prices[i - 1]
            }
        }

        return totalProfit
    }
}
```

### 5. Verification & Complexity Finalization:
*   **Dry Run Example:** `prices = [7, 1, 5, 3, 6, 4]`
    1.  `totalProfit` = 0.
    2.  `i = 1`: `prices[1]` (1) is not > `prices[0]` (7). No profit.
    3.  `i = 2`: `prices[2]` (5) > `prices[1]` (1). `totalProfit += 5 - 1 = 4`. `totalProfit` is 4.
    4.  `i = 3`: `prices[3]` (3) is not > `prices[2]` (5). No profit.
    5.  `i = 4`: `prices[4]` (6) > `prices[3]` (3). `totalProfit += 6 - 3 = 3`. `totalProfit` is `4 + 3 = 7`.
    6.  `i = 5`: `prices[5]` (4) is not > `prices[4]` (6). No profit.
    *   **Final Result:** 7. This corresponds to buying at 1, selling at 5 (profit 4), then buying at 3, selling at 6 (profit 3). The code is correct.

*   **Final Complexity:**
    *   **Time Complexity:** O(n) - We perform a single pass through the input array.
    *   **Space Complexity:** O(1) - We use a constant amount of extra space.
