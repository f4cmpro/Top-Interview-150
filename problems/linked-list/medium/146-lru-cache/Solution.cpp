#include <vector>
#include <unordered_map>
#include <optional>
#include <iostream>
using namespace std;
class LRUCache {
    int mCapacity = 0;
    int count = 0;
    unordered_map<int, std::optional<int>> mCache;
    ListNode* head = nullptr;
    ListNode* current = nullptr;
public:
    LRUCache(int capacity) {
        mCapacity = capacity;
    }
    
    int get(int key) {
        if(mCache.find(key) == mCache.end()){
            return -1;
        }
        if(key == head->val) {
            current->next = head;
            current = current->next;
            head = head->next;
        }
        cout << "GET - mCache: { ";
        for (auto& [k, v] : mCache) {
            cout << k << ": " << (v.has_value() ? to_string(v.value()) : "null") << ", ";
        }
        cout << "}" << endl;
        return mCache[key].value();
    }
    
    void put(int key, int value) {
        if(mCache[key] != nullopt) {
            mCache[key] = value;
            if(key == head->val) {
                current->next = head;
                current = current->next;
                head = head->next;
            }
        } else if(count < mCapacity) {
            mCache[key] = value;
            count++;
            ListNode* newNode = new ListNode(key);
            if(!head) {
                head = newNode;
                current = head;
            } else {
                current->next = newNode;
                current = current->next;
            }
        } else {
            mCache.erase(head->val);
            mCache[key] = value;
            ListNode* newNode = new ListNode(key);
            current->next = newNode;
            current = current->next;
            head = head->next;
        }
        cout << "PUT - mCache: { ";
        for (auto& [k, v] : mCache) {
            cout << k << ": " << (v.has_value() ? to_string(v.value()) : "null") << ", ";
        }
        cout << "}" << endl;
    }
};

struct ListNode {
     int val;
     ListNode *next;
     ListNode() : val(0), next(nullptr) {}
     ListNode(int x) : val(x), next(nullptr) {}
     ListNode(int x, ListNode *next) : val(x), next(next) {}
};

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache* obj = new LRUCache(capacity);
 * int param_1 = obj->get(key);
 * obj->put(key,value);
 */