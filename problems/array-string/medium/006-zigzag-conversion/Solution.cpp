#include <string>
#include <vector>
using namespace std;

class Solution
{
public:
    string convert(string s, int numRows)
    {
        if (numRows == 1)
        {
            return s;
        }
        vector<string> zigzag(numRows, "");
        bool isBackward = false;
        int rowIndex = 0;
        for (int i = 0; i < s.length(); i++)
        {
            zigzag[rowIndex] += s[i];
            if (isBackward)
            {
                rowIndex--;
            }
            else
            {
                rowIndex++;
            }
            if (rowIndex == 0)
            {
                isBackward = false;
            }
            else if (rowIndex == numRows - 1)
            {
                isBackward = true;
            }
        }
        string result;
        for (const string &row : zigzag)
        {
            result += row;
        }
        return result;
    }
};
