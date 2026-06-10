#include <string>
#include <iostream>
#include <vector>
using namespace std;

class Solution
{
public:
    string intToRoman(int num)
    {
        string roman = "";
        int temp = num;
        int count = 1;
        while(temp > 0) {
            int num1 =  (temp % 10) * count;
            roman = convertToRoman(num1) + roman;
            temp = temp / 10;
            count *= 10;
        }
        return roman;
    }

private:
    string convertToRoman(int num)
    {
        if (num < 4)
        {
            string roman = "";
            for (int i = 0; i < num; i++)
            {
                roman += "I";
            }
            return roman;
        }
        else if (num == 4)
        {
            return "IV";
        }
        else if (num < 9)
        {
            string roman = "V";
            for (int i = 0; i < num - 5; i++)
            {
                roman += "I";
            }
            return roman;
        }
        else if (num == 9)
        {
            return "IX";
        }
        else if (num < 40)
        { // 10, 20, 30
            string roman = "";
            for (int i = 0; i < (num / 10); i++)
            {
                roman += "X";
            }
            return roman;
        }
        else if (num == 40)
        {
            return "XL";
        }
        else if (num < 90)
        { // 50, 60, 70, 80
            string roman = "L";
            for (int i = 0; i < (num / 10) - 5; i++)
            {
                roman += "X";
            }
            return roman;
        }
        else if (num == 90)
        {
            return "XC";
        }
        else if (num < 400)
        { // 100, 200, 300
            string roman = "";
            for (int i = 0; i < (num / 100); i++)
            {
                roman += "C";
            }
            return roman;
        }
        else if (num == 400)
        {
            return "CD";
        }
        else if (num < 900)
        { // 500, 600, 700, 800
            string roman = "D";
            for (int i = 0; i < (num / 100) - 5; i++)
            {
                roman += "C";
            }
            return roman;
        }
        else if (num == 900)
        {
            return "CM";
        }
        else if (num < 4000)
        { // 1000, 2000, 3000
            string roman = "";
            for (int i = 0; i < (num / 1000); i++)
            {
                roman += "M";
            }
            return roman;
        }
        else
        {
            return "";
        }
    }
};

int main()
{
    Solution solution;

    vector<pair<int, string>> testCases = {
        {1, "I"},
        {3, "III"},
        {4, "IV"},
        {9, "IX"},
        {58, "LVIII"},
        {1994, "MCMXCIV"},
        {3749, "MMMDCCXLIX"},
        {3999, "MMMCMXCIX"},
    };

    int passed = 0;
    for (const auto &testCase : testCases)
    {
        const int input = testCase.first;
        const string expected = testCase.second;
        const string actual = solution.intToRoman(input);
        const bool ok = (actual == expected);

        cout << (ok ? "PASS" : "FAIL")
             << " | input=" << input
             << " | expected=" << expected
             << " | actual=" << actual
             << '\n';

        if (ok)
        {
            passed++;
        }
    }

    cout << "\nSummary: " << passed << "/" << testCases.size() << " tests passed.\n";

    return 0;
}