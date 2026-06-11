#include <string>
#include <iostream>
#include <vector>
using namespace std;
class Solution {
public:
    string longestCommonPrefix(vector<string>& strs) {
        sort(strs.begin(), strs.end(), [](const string& a, const string& b) {
            return a.size() < b.size();
        });
        string longestPrefix = strs[0];
        while (true)
        {
            bool isLongest = true;
            for(int i = 1; i < strs.size(); i++) {
                if(strs[i].compare(0, longestPrefix.size(), longestPrefix) != 0) {
                    isLongest = false;
                    break;
                }
            }
            if(isLongest || longestPrefix.empty()) {
                break;
            } else {
                longestPrefix = longestPrefix.substr(0, longestPrefix.size() - 1);
            }
        }

        return longestPrefix;
    }
};