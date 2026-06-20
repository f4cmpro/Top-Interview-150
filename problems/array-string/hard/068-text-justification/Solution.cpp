#include <string>
#include <vector>
using namespace std;
class Solution
{
public:
    vector<string> fullJustify(vector<string> &words, int maxWidth)
    {
        vector<string> output;
        int i = 0;
        while (i < words.size())
        {
            int lengthCount = words[i].length();
            int wordCount = 1;
            int j = i + 1;
            bool isLastLine = false;
            // collect line by greedy approach
            //  1 presents for space btw words
            while (j < words.size() && (lengthCount + 1 + words[j].length() <= maxWidth))
            {
                lengthCount += 1 + words[j].length();
                wordCount++;
                j++;
            }
            if (j >= words.size())
            {
                isLastLine = true;
            }
            string line = "";
            if (wordCount == 1 || isLastLine)
            {
                for (int k = i; k < j; k++)
                {
                    line.append(words[k]);
                    if (wordCount > 1 && k < j - 1)
                    {
                        line.append(" ");
                    }
                }
                int space = maxWidth - lengthCount;
                line.append(space, ' ');
            }
            else
            {
                int spaceCount = wordCount - 1;
                int realSpaceLength = maxWidth - lengthCount + spaceCount;
                int fixSpace = realSpaceLength / spaceCount;
                int expandSpace = realSpaceLength % spaceCount;
                for (int k = i; k < j; k++)
                {
                    line.append(words[k]);
                    if (k < j - 1)
                    {
                        line.append(fixSpace, ' ');
                        if (expandSpace > 0)
                        {
                            line.append(" ");
                            expandSpace--;
                        }
                    }
                }
            }
            output.push_back(line);
            i = j;
        }
        return output;
    }
};