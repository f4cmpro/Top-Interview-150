# 121 - Best Time to Buy and Sell Stock

**Difficulty**: 🟢 Easy  
**LeetCode**: https://leetcode.com/problems/best-time-to-buy-and-sell-stock/

---

### 1. Clarification & Edge Cases:
*   **Input:** An array of integers `prices`, where `prices[i]` is the price of a given stock on the `i`-th day.
*   **Output:** The maximum profit that can be achieved. If no profit is possible, return 0.
*   **Constraints:**
    *   `1 <= prices.length <= 10^5`
    *   `0 <= prices[i] <= 10^4`
*   **Edge Cases:**
    *   **Empty or Single-element array:** If the array has 0 or 1 elements, no transaction is possible, so the profit is 0.
    *   **No profitable transaction:** If the prices are always decreasing, it's impossible to make a profit. The max profit is 0.
    *   **All prices are the same:** No profit can be made.

### 2. High-Level Approach Analysis (Trade-offs):
*   **Brute Force Approach:**
    *   Use nested loops. The outer loop iterates through each day as a potential `buy` day. The inner loop iterates through the subsequent days as potential `sell` days.
    *   Calculate the profit for each `buy-sell` pair and keep track of the maximum profit found.
    *   **Time Complexity:** O(n²) - For each element, we iterate through the rest of the array.
    *   **Space Complexity:** O(1) - No extra space is used.

*   **Optimized Approach (Single Pass):**
    *   Iterate through the `prices` array just once.
    *   Maintain two variables: `minPrice` (the lowest stock price found so far) and `maxProfit` (the maximum profit seen).
    *   On each day, update `minPrice` if the current price is lower. Then, calculate the potential profit if we were to sell on the current day (`currentPrice - minPrice`) and update `maxProfit` if this potential profit is higher than the current `maxProfit`.
    *   **Time Complexity:** O(n) - We only need to iterate through the array once.
    *   **Space Complexity:** O(1) - We only use a few variables to store state.

*   **Comparison:**
    The optimized single-pass approach is significantly better because its O(n) time complexity is far more efficient for large inputs than the O(n²) of the brute-force method. Given the constraint of `10^5` elements, an O(n²) solution would be too slow and likely time out.

### 3. Algorithm Design:
The optimized algorithm works by simulating the process of finding the best time to buy and sell in a single pass.

1.  Initialize a variable `minPrice` to a very large value (or the price on the first day) to track the lowest buy price encountered.
2.  Initialize a variable `maxProfit` to `0` to store the maximum profit found.
3.  Iterate through the `prices` array from the first day to the last.
4.  For each `price`:
    a.  Compare the current `price` with `minPrice`. If the current `price` is lower, it becomes the new `minPrice`. This represents finding a better (cheaper) day to buy.
    b.  If the current `price` is not lower, calculate the potential profit by subtracting `minPrice` from the current `price`.
    c.  Compare this potential profit with `maxProfit`. If it's greater, update `maxProfit`.
5.  After the loop finishes, `maxProfit` will hold the highest possible profit.

**Data Structures:** We only use simple variables (pointers/trackers), making this a very space-efficient approach.

### 4. Production-Ready Implementation:
```kotlin
import kotlin.math.max

class Solution {
    fun maxProfit(prices: IntArray): Int {
        // Guard Clause: If there are fewer than 2 days, no profit can be made.
        if (prices.size < 2) {
            return 0
        }

        var maxProfit = 0
        var minPrice = prices[0]

        // Start from the second day as we need a day to sell.
        for (i in 1 until prices.size) {
            val currentPrice = prices[i]

            // Is this a new, lower price to buy at?
            if (currentPrice < minPrice) {
                minPrice = currentPrice
            } else {
                // Otherwise, calculate potential profit if we sell today.
                val potentialProfit = currentPrice - minPrice
                maxProfit = max(maxProfit, potentialProfit)
            }
        }
        return maxProfit
    }
}
```

### 5. Verification & Complexity Finalization:
*   **Dry Run Example:** `prices = [7, 1, 5, 3, 6, 4]`
    1.  `minPrice` = 7, `maxProfit` = 0.
    2.  `i = 1`: `price` is 1. `1 < 7`, so `minPrice` becomes 1.
    3.  `i = 2`: `price` is 5. `5 > 1`. `profit = 5 - 1 = 4`. `maxProfit` becomes 4.
    4.  `i = 3`: `price` is 3. `3 > 1`. `profit = 3 - 1 = 2`. `maxProfit` remains 4.
    5.  `i = 4`: `price` is 6. `6 > 1`. `profit = 6 - 1 = 5`. `maxProfit` becomes 5.
    6.  `i = 5`: `price` is 4. `4 > 1`. `profit = 4 - 1 = 3`. `maxProfit` remains 5.
    *   **Final Result:** 5. The code is correct.

*   **Final Complexity:**
    *   **Time Complexity:** O(n) - Single pass through the input array.
    *   **Space Complexity:** O(1) - Constant extra space for two variables.
