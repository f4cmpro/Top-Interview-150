#include <string>
#include <vector>
using namespace std;

class Solution
{
public:
    int strStr(string haystack, string needle)
    {
        if (needle.empty())
        {
            return 0;
        }

        int n = static_cast<int>(haystack.length());
        int m = static_cast<int>(needle.length());
        if (m > n)
        {
            return -1;
        }

        vector<int> lps(m, 0);
        int len = 0;
        for (int i = 1; i < m;)
        {
            if (needle[i] == needle[len])
            {
                len++;
                lps[i] = len;
                i++;
            }
            else if (len > 0)
            {
                len = lps[len - 1];
            }
            else
            {
                lps[i] = 0;
                i++;
            }
        }

        int i = 0;
        int j = 0;
        while (i < n)
        {
            if (haystack[i] == needle[j])
            {
                i++;
                j++;
                if (j == m)
                {
                    return i - m;
                }
            }
            else if (j > 0)
            {
                j = lps[j - 1];
            }
            else
            {
                i++;
            }
        }

        return -1;
    }
};