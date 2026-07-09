
class Solution {
public:
    ListNode* removeNthFromEnd(ListNode* head, int n) {
        if(head->next == nullptr) {
            return nullptr;
        }
        ListNode* current = head;
        int count = 0;
        while (current != nullptr){
            count++;
            current = current->next;
        }
        int removedIndex = count - n + 1;
        if(removedIndex == 1) {
            return head->next;   
        }
        current = head;
        count = 0;
        while (current != nullptr)
        {
            count++;
            if(count == removedIndex - 1) {
                ListNode* nextNext = current->next->next;
                current->next = nextNext;
                break;
            }
            
            current = current->next;
        }
        return head;
    }
};

 struct ListNode {
     int val;
     ListNode *next;
     ListNode() : val(0), next(nullptr) {}
     ListNode(int x) : val(x), next(nullptr) {}
     ListNode(int x, ListNode *next) : val(x), next(next) {}
 };