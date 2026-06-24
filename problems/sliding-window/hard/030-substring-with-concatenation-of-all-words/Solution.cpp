#include <string>
#include <vector>
#include <unordered_map>
using namespace std;

class Solution {
public:
    vector<int> findSubstring(string s, vector<string>& words) {
        vector<int> result;
        if (s.empty() || words.empty()) return result;

        int L = words[0].size();
        int m = words.size();
        int n = s.size();
        if (n < L * m) return result;

        unordered_map<string, int> wordCount;
        for (const string& w : words) wordCount[w]++;

        for (int offset = 0; offset < L; offset++) {
            unordered_map<string, int> windowCount;
            int left = offset;
            int matched = 0;  // valid word slots filled

            for (int right = offset; right + L <= n; right += L) {
                string word = s.substr(right, L);

                if (wordCount.count(word)) {
                    windowCount[word]++;
                    matched++;

                    // Over-used word: shrink window from left until balanced
                    while (windowCount[word] > wordCount[word]) {
                        string leftWord = s.substr(left, L);
                        windowCount[leftWord]--;
                        matched--;
                        left += L;
                    }

                    if (matched == m) {
                        result.push_back(left);
                        // Slide window forward by one word
                        string leftWord = s.substr(left, L);
                        windowCount[leftWord]--;
                        matched--;
                        left += L;
                    }
                } else {
                    // Invalid word: reset window entirely
                    windowCount.clear();
                    matched = 0;
                    left = right + L;
                }
            }
        }

        return result;
    }
};