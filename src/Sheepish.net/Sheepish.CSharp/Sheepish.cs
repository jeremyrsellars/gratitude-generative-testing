using System;
using System.Linq;

namespace Sheepish.CSharp
{
    public class Sheepish
    {
        public static bool IsSheepBleat(string text) =>
            text[0] == 'b' && text.Substring(1).All('a'.Equals);
    }
}
