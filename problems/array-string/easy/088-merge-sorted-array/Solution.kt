/**
 * Problem: 88 - Merge Sorted Array
 * Difficulty: Easy
 * Topic: Array / String
 * 
 * LeetCode: https://leetcode.com/problems/merge-sorted-array/
 * 
 * Description:
 * You are given two integer arrays nums1 and nums2, sorted in non-decreasing order,
 * and two integers m and n, representing the number of elements in nums1 and nums2 respectively.
 * Merge nums1 and nums2 into a single array sorted in non-decreasing order.
 * 
 * Example 1:
 * Input: nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
 * Output: [1,2,2,3,5,6]
 * 
 * Constraints:
 * - nums1.length == m + n
 * - nums2.length == n
 * - 0 <= m, n <= 200
 */

class Solution {
    /**
     * Approach: Two pointers from end to beginning
     * 
     * Time Complexity: O(m + n)
     * Space Complexity: O(1)
     * 
     * @param nums1 First sorted array with extra space
     * @param m Number of elements in nums1
     * @param nums2 Second sorted array
     * @param n Number of elements in nums2
     */
    fun merge(nums1: IntArray, m: Int, nums2: IntArray, n: Int): Unit {
        // TODO: Implement solution
        
    }
}

/**
 * Test cases
 */
fun main() {
    val solution = Solution()
    
    // Test case 1
    val nums1 = intArrayOf(1, 2, 3, 0, 0, 0)
    solution.merge(nums1, 3, intArrayOf(2, 5, 6), 3)
    println("Test 1: ${nums1.contentToString()}")
    
    // Test case 2
    val nums2 = intArrayOf(1)
    solution.merge(nums2, 1, intArrayOf(), 0)
    println("Test 2: ${nums2.contentToString()}")
}
