# 80 - Remove Duplicates from Sorted Array II

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/

## Solution Design

### Approach: Two Pointers
Sử dụng kỹ thuật two pointers để duyệt mảng và loại bỏ các phần tử duplicate xuất hiện quá 2 lần.

### Key Insights
1. **Mảng đã được sắp xếp**: Các phần tử giống nhau sẽ nằm cạnh nhau
2. **Tối đa 2 lần**: Mỗi phần tử có thể xuất hiện tối đa 2 lần trong kết quả
3. **So sánh với phần tử cách 2 vị trí**: Nếu `nums[right] != nums[k-2]`, ta có thể thêm `nums[right]` vào vị trí `k`

### Algorithm
1. Khởi tạo hai pointers:
   - `k = 2`: vị trí để ghi phần tử hợp lệ tiếp theo
   - `right = 2`: vị trí đang xét trong mảng
2. Duyệt từ vị trí thứ 2 đến hết mảng:
   - Nếu `nums[right] != nums[k-2]`: swap `nums[k]` với `nums[right]` và tăng `k`
   - Di chuyển `right` sang phải
3. Return `min(nums.size, k)`

### Why It Works
- Bằng cách so sánh `nums[right]` với `nums[k-2]`, ta đảm bảo mỗi phần tử xuất hiện tối đa 2 lần
- Nếu phần tử thứ 3 cùng giá trị xuất hiện, nó sẽ bằng `nums[k-2]` và không được thêm vào

### Complexity Analysis
- **Time Complexity**: O(n) - duyệt mảng một lần
- **Space Complexity**: O(1) - chỉ sử dụng biến constant

### Example Walkthrough
```
Input: nums = [1,1,1,2,2,3]

Initial: k=2, right=2
[1,1,1,2,2,3]
     k
     r

Step 1: nums[2]=1 == nums[0]=1 → skip
[1,1,1,2,2,3]
     k
       r

Step 2: nums[3]=2 != nums[1]=1 → swap and k++
[1,1,2,1,2,3]
       k
         r

Step 3: nums[4]=2 != nums[2]=2 → false, skip
[1,1,2,1,2,3]
       k
           r

Step 4: nums[5]=3 != nums[3]=1 → swap and k++
[1,1,2,2,3,1]
         k

Result: k=5
```
