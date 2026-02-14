package containDuplicateII

class Solution {
    /**
     * Determines if there are two distinct indices i and j in the array such that
     * nums[i] == nums[j] and abs(i - j) <= k.
     *
     * @param nums The input array of integers
     * @param k The maximum allowed distance between duplicate indices
     * @return true if a valid duplicate pair exists within distance k, false otherwise
     */
    fun containsNearbyDuplicate(nums: IntArray, k: Int): Boolean {
        // Guard clause: If k is 0, no two distinct indices can have distance <= 0
        if (k == 0) return false

        // Guard clause: Single element array cannot have two distinct indices
        if (nums.size == 1) return false

        // Map to store the most recent index of each number
        val indexMap = HashMap<Int, Int>()

        // Single pass through the array
        for (i in nums.indices) {
            val currentNum = nums[i]

            // Check if we've seen this number before
            if (indexMap.containsKey(currentNum)) {
                val previousIndex = indexMap[currentNum]!!
                val distance = i - previousIndex

                // If distance is within k, we found a valid pair
                if (distance <= k) {
                    return true
                }
            }

            // Update the map with current index (most recent occurrence)
            indexMap[currentNum] = i
        }

        // No valid duplicate pair found
        return false
    }
}

fun main() {
    val solution = Solution()

    // Test cases
    println("containsNearbyDuplicate([1,2,3,1], k=3) = ${solution.containsNearbyDuplicate(intArrayOf(1,2,3,1), 3)}")  // Expected: true
    println("containsNearbyDuplicate([1,0,1,1], k=1) = ${solution.containsNearbyDuplicate(intArrayOf(1,0,1,1), 1)}")  // Expected: true
    println("containsNearbyDuplicate([1,2,3,1,2,3], k=2) = ${solution.containsNearbyDuplicate(intArrayOf(1,2,3,1,2,3), 2)}")  // Expected: false
    println("containsNearbyDuplicate([1], k=1) = ${solution.containsNearbyDuplicate(intArrayOf(1), 1)}")  // Expected: false
    println("containsNearbyDuplicate([1,2,3,4], k=3) = ${solution.containsNearbyDuplicate(intArrayOf(1,2,3,4), 3)}")  // Expected: false
}