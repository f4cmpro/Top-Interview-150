
class Solution {
public:
    ListNode* partition(ListNode* head, int x) {
        if(!head || !head->next) {
            return head;
        }
        ListNode* leftNode = new ListNode();
        ListNode* rightNode = new ListNode();
        ListNode* current = head;
        ListNode* currentLeft = leftNode;
        ListNode* currentRight = rightNode;
        while (current)
        {
            if(current->val < x) {
                currentLeft->next = current;
                currentLeft = currentLeft->next;
            } else {
                currentRight->next = current;
                currentRight = currentRight->next;
            }
            current = current->next;
        }
        currentRight->next = nullptr;
        if(!leftNode->next) {
            return rightNode->next;
        } else if(!rightNode->next) {
            return leftNode->next;
        } else {
            currentLeft->next = rightNode->next;
            return leftNode->next;
        }
    }
};

 struct ListNode {
     int val;
     ListNode *next;
     ListNode() : val(0), next(nullptr) {}
     ListNode(int x) : val(x), next(nullptr) {}
     ListNode(int x, ListNode *next) : val(x), next(next) {}
 };