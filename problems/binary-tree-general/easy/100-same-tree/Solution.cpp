using namespace std;
class Solution {
public:
    bool isSameTree(TreeNode* p, TreeNode* q) {
        if(!p && !q) {
            return true;
        }
        if((!p && q) || (p && !q) || p->val != q->val) {
            return false;
        }
        bool isSameLeft = false;
        bool isSameRight = false;
        if(p->left && q->left) {
            isSameLeft = isSameTree(p->left, q->left);
        } else if (!p->left && !q->left) {
            isSameLeft = true;
        }
        if(p->right && q->right) {
            isSameRight = isSameTree(p->right, q->right);
        } else if (!p->right && !q->right) {
            isSameRight = true;
        }
        return isSameLeft && isSameRight;
    }
};

struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};