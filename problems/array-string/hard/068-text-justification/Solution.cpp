#include <string>
#include <vector>
using namespace std;
class Solution {
public:
    vector<string> fullJustify(vector<string>& words, int maxWidth) {
        vector<string> output();
        int lengthCount = 0;
        int wordCount = 0;
        int i = 0;
        while(i < words.size()) {
            if(lengthCount + words[i].length() <= maxWidth) {
                lengthCount += words[i].length();
                wordCount++;
                i++;
            } else {
                if(lengthCount < maxWidth) {

                }
            }
        }
        
    }
};