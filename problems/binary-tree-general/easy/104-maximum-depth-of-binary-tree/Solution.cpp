
#include <algorithm>
using namespace std;
class Solution {
public:
    int maxDepth(TreeNode* root) {
        if(!root) return 0;
        int maxDepthLeft = 1;
        int maxDepthRight = 1;
        if(root->left) {
            maxDepthLeft += maxDepth(root->left);
        }
        if(root->right) {
            maxDepthRight += maxDepth(root->right);
        }
        return max(maxDepthLeft, maxDepthRight);    
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