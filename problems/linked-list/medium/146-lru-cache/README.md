# 146 - LRU Cache

**Difficulty**: 🟠 Medium  
**LeetCode**: https://leetcode.com/problems/lru-cache/

---

## 1. Clarification & Edge Cases

**Constraints:**
- `1 <= capacity <= 3000`
- `0 <= key <= 10^4`
- `0 <= value <= 10^5`
- Both `get` and `put` must run in **O(1) average time**.
- Up to `2 * 10^5` calls total to `get` and `put`.

**Edge Cases:**
- `capacity = 1`: every `put` of a new key evicts the only existing entry.
- `put` on an existing key: update the value **and** mark it as MRU (do not insert a duplicate).
- `get` on a missing key: return `-1` without modifying the cache.
- `get` on an existing key: must promote it to MRU before returning.
- Eviction should only happen when `size == capacity` and a **new** key is being inserted.

---

## 2. High-Level Approach Analysis (Trade-offs)

### Brute Force
Use only an `unordered_map<int,int>` plus a separate `vector` that records insertion/access order. On every `get`/`put`, scan the vector to find and update positions.

| | Time | Space |
|---|---|---|
| `get` | O(n) | O(n) |
| `put` | O(n) | O(n) |

The linear scan makes it too slow for the 2×10⁵ call limit.

### Optimized Approach — HashMap + Doubly-Linked List
Combine:
- `std::list<pair<int,int>>` (doubly-linked list) to maintain access order in O(1) via `splice`.
- `unordered_map<int, list::iterator>` to jump directly to any node in O(1).

| | Time | Space |
|---|---|---|
| `get` | O(1) avg | O(n) |
| `put` | O(1) avg | O(n) |

**Why better:** `std::list::splice` re-links pointers without copying data, giving true O(1) reordering. The hash map gives O(1) lookup by key. Neither approach is possible alone — the map provides fast lookup, the list provides fast reordering.

---

## 3. Algorithm Design

**Data structures:**
- `list<pair<int,int>> mList` — ordered from **MRU (front)** to **LRU (back)**.
- `unordered_map<int, list<pair<int,int>>::iterator> mMap` — maps each key to its iterator in the list.

**`get(key)`:**
1. Look up `key` in `mMap`. If not found → return `-1`.
2. Use `splice` to move the node to the **front** of the list (MRU position).
3. Return the value.

**`put(key, value)`:**
1. If `key` already exists:
   - Update the value in-place via the stored iterator.
   - Use `splice` to move the node to the **front** (MRU).
2. If `key` is new:
   - If `size == capacity`: erase the **back** node (LRU) from both the list and the map.
   - Push `{key, value}` to the **front** of the list.
   - Store the new iterator in `mMap[key]`.

---

## 4. Production-Ready Implementation

```cpp
#include <unordered_map>
#include <list>
using namespace std;

class LRUCache {
    int mCapacity;
    // front = MRU, back = LRU
    list<pair<int,int>> mList;
    unordered_map<int, list<pair<int,int>>::iterator> mMap;

public:
    LRUCache(int capacity) : mCapacity(capacity) {}

    int get(int key) {
        auto it = mMap.find(key);
        if (it == mMap.end()) return -1;

        // Promote to MRU by moving node to front
        mList.splice(mList.begin(), mList, it->second);
        return it->second->second;
    }

    void put(int key, int value) {
        auto it = mMap.find(key);
        if (it != mMap.end()) {
            // Key exists: update value and promote to MRU
            it->second->second = value;
            mList.splice(mList.begin(), mList, it->second);
        } else {
            // Evict LRU if at capacity
            if ((int)mList.size() == mCapacity) {
                mMap.erase(mList.back().first);
                mList.pop_back();
            }
            // Insert new entry at MRU position
            mList.push_front({key, value});
            mMap[key] = mList.begin();
        }
    }
};
```

---

## 5. Verification & Complexity Finalization

**Dry Run** with `capacity=3`, operations: `put(1,1)→put(2,2)→put(3,3)→put(4,4)→get(4)→get(3)→get(2)→get(1)→put(5,5)→get(3)`:

| Operation | List (front→back) | Map keys | Return |
|---|---|---|---|
| `put(1,1)` | `[1]` | {1} | — |
| `put(2,2)` | `[2,1]` | {1,2} | — |
| `put(3,3)` | `[3,2,1]` | {1,2,3} | — |
| `put(4,4)` | `[4,3,2]` | {2,3,4} ← evicts 1 | — |
| `get(4)` | `[4,3,2]` | {2,3,4} | **4** |
| `get(3)` | `[3,4,2]` | {2,3,4} | **3** |
| `get(2)` | `[2,3,4]` | {2,3,4} | **2** |
| `get(1)` | `[2,3,4]` | {2,3,4} | **-1** |
| `put(5,5)` | `[5,2,3]` | {2,3,5} ← evicts 4 | — |
| `get(3)` | `[3,5,2]` | {2,3,5} | **3** ✓ |

Output matches expected: `[null,null,null,null,null,4,3,2,-1,null,-1,2,3,-1,5]` ✓

**Final Complexity:**

| | Time | Space |
|---|---|---|
| `get` | **O(1)** average | — |
| `put` | **O(1)** average | — |
| Overall | **O(1)** per operation | **O(capacity)** |