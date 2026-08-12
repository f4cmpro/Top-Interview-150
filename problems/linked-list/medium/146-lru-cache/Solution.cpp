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
        // Move accessed node to front (MRU)
        mList.splice(mList.begin(), mList, it->second);
        return it->second->second;
    }

    void put(int key, int value) {
        auto it = mMap.find(key);
        if (it != mMap.end()) {
            it->second->second = value;
            mList.splice(mList.begin(), mList, it->second);
        } else {
            if ((int)mList.size() == mCapacity) {
                // Evict LRU (back)
                mMap.erase(mList.back().first);
                mList.pop_back();
            }
            mList.push_front({key, value});
            mMap[key] = mList.begin();
        }
    }
};

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache* obj = new LRUCache(capacity);
 * int param_1 = obj->get(key);
 * obj->put(key,value);
 */