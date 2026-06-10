#include <string>
#include <iostream>
using namespace std;

class Solution
{
public:
    int romanToInt(string s)
    {
        int sum = 0;
        size_t i = 0;
        while (i < s.size())
        {
            char c1 = s[i];
            if (i + 1 < s.size() && isSubtraction(c1, s[i + 1]))
            {
                char c2 = s[i + 1];
                int value = convertToInt(c2) - convertToInt(c1);
                sum += value;
                i += 2;
            }
            else
            {
                sum += convertToInt(c1);
                i += 1;
            }
        }
        return sum;
    }

private:
    bool isSubtraction(char c1, char c2)
    {
        return (c1 == 'I' && (c2 == 'V' || c2 == 'X')) || (c1 == 'X' && (c2 == 'L' || c2 == 'C')) || (c1 == 'C' && (c2 == 'D' || c2 == 'M'));
    }

private:
    int convertToInt(char c)
    {
        int num = 0;
        switch (c)
        {
        case 'I':
            num = 1;
            break;

        case 'V':
            num = 5;
            break;

        case 'X':
            num = 10;
            break;

        case 'L':
            num = 50;
            break;

        case 'C':
            num = 100;
            break;

        case 'D':
            num = 500;
            break;

        case 'M':
            num = 1000;
            break;

        default:
            break;
        }
        return num;
    }
};

int main()
{
    Solution solution;

    // Quick fixed test cases
    string tests[] = {"III", "LVIII", "MCMXCIV", "IX", "XLII"};
    for (const string &s : tests)
    {
        cout << s << " -> " << solution.romanToInt(s) << endl;
    }

    return 0;
}