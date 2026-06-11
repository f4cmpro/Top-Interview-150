#include <string>
using namespace std;
class Solution
{
public:
    int lengthOfLastWord(string s)
    {
        int count = 0;
        int i = s.length() - 1;
        while (i >= 0)
        {
            if(s[i] == ' ') {
                if(count == 0) {
                    i--;
                    continue;
                } else {
                    break;
                }
            } else {
                count++;
            }
            i--;
        }
        return count;
    }
};