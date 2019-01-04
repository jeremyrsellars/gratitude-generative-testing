using System.Collections.Generic;
using System.Text.RegularExpressions;
using NUnit.Framework;

namespace Sheepish.CSharp
{
    [TestFixture]
    public class B_Parameterized_Test_With_Oracle
    {
        [TestCaseSource(nameof(SheepishExamples))]
        public void SheepBleatDetection(SheepishTestCase testCase) =>
            Assert.AreEqual(
                Regex.IsMatch(testCase.Text, @"^baa+$"),
                Sheepish.IsSheepBleat(testCase.Text), testCase.Reason);

        static IEnumerable<SheepishTestCase> SheepishExamples =>
            new[] {
                new SheepishTestCase("b",         "Too short"),
                new SheepishTestCase("ba",        "Too short"),
                new SheepishTestCase("baa",       "2+ 'a' s"),
                new SheepishTestCase("baaa",      "2+ 'a' s"),
                new SheepishTestCase("baaad",     "invalid ' d'"),
                new SheepishTestCase(" baa",      "leading space"),
                new SheepishTestCase("baa " ,     "trailing space"),
            };

        public class SheepishTestCase
        {
            public SheepishTestCase(string text, string reason)
            {
                Text = text;
                Reason = reason;
            }
            public string Text;
            public string Reason;
            // nUnit requires a unique ToString() for a name.
            public override string ToString() => Text;
        }
    }
}
