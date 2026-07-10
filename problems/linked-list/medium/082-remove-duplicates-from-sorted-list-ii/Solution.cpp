
class Solution {
public:
    ListNode* deleteDuplicates(ListNode* head) {
        if(head == nullptr || head->next == nullptr) {
            return head;
        }
        ListNode* current = head;
        while (current != nullptr)
        {
            ListNode* next = current->next;
            ListNode* nextNext = next->next;
            if(next->val != nextNext->val) {
                current = current->next;
            } else {
                while (next->val == nextNext->val)
                {
                    current->next = nextNext->next;
                    next = next->next;
                    nextNext = nextNext->next;
                }
            }
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