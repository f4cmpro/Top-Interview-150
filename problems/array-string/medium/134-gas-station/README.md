# 134 - Gas Station

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/gas-station/

---

### 1. Clarification & Edge Cases:
*   **Input:** Two integer arrays, `gas` and `cost`, of the same length `n`.
*   **Output:** The index of the starting gas station if a full circuit is possible, otherwise -1.
*   **Constraints:**
    *   `gas.length == cost.length`
    *   `1 <= gas.length <= 10^5`
    *   `0 <= gas[i], cost[i] <= 10^4`
*   **Edge Cases:**
    *   **No Solution:** The total amount of gas is less than the total cost to travel, making a full circuit impossible.
    *   **Single Station:** If there's only one station, a solution exists only if `gas[0] >= cost[0]`.
    *   **Guaranteed Unique Solution:** The problem statement guarantees that if a solution exists, it is unique.

### 2. High-Level Approach Analysis (Trade-offs):
*   **Brute Force Approach:**
    *   Iterate through each gas station from `i = 0` to `n-1` and treat it as a potential starting point.
    *   For each starting point `i`, simulate the journey around the circular route. Keep track of the current gas in the tank.
    *   If the simulation from `i` successfully completes a full circle back to `i`, then `i` is the answer.
    *   If the tank becomes negative at any point during the simulation, that starting point `i` is invalid, so we break and try the next starting point `i + 1`.
    *   **Time Complexity:** O(n²) because for each of the `n` starting points, we might traverse up to `n` stations.
    *   **Space Complexity:** O(1).

*   **Optimized Approach (Greedy):**
    *   The core idea is that if the total gas is less than the total cost, no solution exists.
    *   We can solve this in a single pass. We maintain two variables: `total_tank` (to check if a solution is possible at all) and `current_tank` (to find the starting position).
    *   We iterate through the stations, updating both `total_tank` and `current_tank` with the net gas (`gas[i] - cost[i]`).
    *   If `current_tank` ever drops below zero, it means we cannot reach the next station from the current `start_station`. Therefore, the `start_station` must be at least `i + 1`. We reset `current_tank` to zero and update our candidate `start_station` to `i + 1`.
    *   After the loop, if `total_tank` is non-negative, a solution exists, and the answer is `start_station`. Otherwise, no solution exists.

*   **Comparison:**
    *   The **Optimized Approach** is significantly better with a **Time Complexity of O(n)**, as it only requires a single pass through the arrays. The Brute Force approach is much slower at O(n²).
    *   Both approaches have a **Space Complexity of O(1)**, making the optimized approach superior in terms of time without any space penalty.

### 3. Algorithm Design:
1.  Initialize three integer variables:
    *   `total_tank` to 0: This will accumulate the net gas balance for the entire trip (`sum(gas) - sum(cost)`).
    *   `current_tank` to 0: This tracks the gas in the tank from the current candidate starting station.
    *   `start_station` to 0: This holds the index of the candidate starting station.
2.  Iterate through the `gas` and `cost` arrays from `i = 0` to `n-1`.
3.  In each iteration, calculate the net gas for the current station: `gas[i] - cost[i]`.
4.  Add this net value to both `total_tank` and `current_tank`.
5.  Check if `current_tank` has become negative.
    *   If it is, it means we cannot complete the journey starting from the current `start_station`.
    *   Reset `current_tank` to 0.
    *   Update the candidate `start_station` to the next station, i.e., `i + 1`.
6.  After the loop finishes, check the value of `total_tank`.
    *   If `total_tank` is greater than or equal to 0, it means a solution exists, and the valid starting point is `start_station`. Return `start_station`.
    *   If `total_tank` is negative, it's impossible to complete the circuit. Return -1.

### 4. Production-Ready Implementation:
```kotlin
class Solution {
    fun canCompleteCircuit(gas: IntArray, cost: IntArray): Int {
        // Guard Clause: If total gas is less than total cost, no solution is possible.
        // This check also implicitly handles the empty array case if constraints allowed it.
        if (gas.sum() < cost.sum()) {
            return -1
        }

        var currentTank = 0
        var startStation = 0

        for (i in gas.indices) {
            // Net gain/loss at the current station
            currentTank += gas[i] - cost[i]

            // If we run out of gas, it means we can't start from the current 'startStation'.
            // The new potential start must be after the current station 'i'.
            if (currentTank < 0) {
                // Reset the tank for the new journey segment
                currentTank = 0
                // Set the new candidate start station
                startStation = i + 1
            }
        }

        // The problem guarantees a unique solution if one exists.
        // The initial check (sum of gas vs cost) ensures that if we reach here, a solution is guaranteed.
        return startStation
    }
}
```

### 5. Verification & Complexity Finalization:
*   **Dry Run:**
    *   Let `gas = [1, 2, 3, 4, 5]` and `cost = [3, 4, 5, 1, 2]`.
    *   `sum(gas)` = 15, `sum(cost)` = 15. `15 >= 15`, so a solution exists.
    *   `i = 0`: `currentTank` = 1 - 3 = -2. `currentTank` < 0. Reset `currentTank` = 0, `startStation` = 1.
    *   `i = 1`: `currentTank` = 2 - 4 = -2. `currentTank` < 0. Reset `currentTank` = 0, `startStation` = 2.
    *   `i = 2`: `currentTank` = 3 - 5 = -2. `currentTank` < 0. Reset `currentTank` = 0, `startStation` = 3.
    *   `i = 3`: `currentTank` = 4 - 1 = 3. `currentTank` >= 0.
    *   `i = 4`: `currentTank` = 3 + (5 - 2) = 6. `currentTank` >= 0.
    *   Loop ends. Return `startStation` which is **3**.
    *   Let's verify: Start at 3 with tank=0.
        *   Station 3: Tank = 0 + 4 = 4. Travel to 4. Cost=1. Tank = 3.
        *   Station 4: Tank = 3 + 5 = 8. Travel to 0. Cost=2. Tank = 6.
        *   Station 0: Tank = 6 + 1 = 7. Travel to 1. Cost=3. Tank = 4.
        *   Station 1: Tank = 4 + 2 = 6. Travel to 2. Cost=4. Tank = 2.
        *   Station 2: Tank = 2 + 3 = 5. Travel to 3. Cost=5. Tank = 0.
    *   The dry run confirms the logic is correct.

*   **Final Complexity:**
    *   **Time Complexity:** **O(n)**, where `n` is the number of gas stations. We perform a single pass through the arrays.
    *   **Space Complexity:** **O(1)**. We only use a few constant extra space variables.

