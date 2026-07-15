
class Solution {
public:
    ListNode* rotateRight(ListNode* head, int k) {
        if(!head || !head->next || k == 0) {
            return head;
        }

        ListNode* current = head;
        int nodeTotal = 0;
        while(current) {
            nodeTotal++;
            current = current->next;
        }

        int realRotateCount = k % nodeTotal;
        if(realRotateCount == 0) {
            return head;
        }

        int startRotatedNodeInd = nodeTotal - realRotateCount + 1;
        current = head;
        int currentInd = 0;
        ListNode* dummyNode = head;
        ListNode* previous = nullptr;
        while(current) {
            currentInd++;
            if(currentInd == startRotatedNodeInd) {
                dummyNode = current;
                if(previous) {
                    previous->next = nullptr;
                }
                break;
            }
            previous = current;
            current = current->next;
        }

        current = dummyNode;
        while(current && current->next) {
            current = current->next;
        }
        current->next = head;

        return dummyNode;
    }
};

 struct ListNode {
     int val;
     ListNode *next;
     ListNode() : val(0), next(nullptr) {}
     ListNode(int x) : val(x), next(nullptr) {}
     ListNode(int x, ListNode *next) : val(x), next(next) {}
 };