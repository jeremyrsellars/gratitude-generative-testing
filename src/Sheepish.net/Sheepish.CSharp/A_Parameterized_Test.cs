using System.Collections.Generic;
using NUnit.Framework;

namespace Sheepish.CSharp
{
    [TestFixture]
    public class A_Parameterized_Test
    {
        [TestCaseSource(nameof(SheepishExamples))]
        public void SheepBleatDetection(SheepishTestCase testCase) =>
            Assert.AreEqual(
                testCase.IsSheepBleat,
                Sheepish.IsSheepBleat(testCase.Text), testCase.Reason);

        static IEnumerable<SheepishTestCase> SheepishExamples =>
            new[] {
                new SheepishTestCase("b",         false,    "Too short"),
                new SheepishTestCase("ba",        false,    "Too short"),
                new SheepishTestCase("baa",       true,     "2+ 'a' s"),
                new SheepishTestCase("baaa",      true,     "2+ 'a' s"),
                new SheepishTestCase("baaad",     false,    "invalid ' d'"),
                new SheepishTestCase(" baa",      false,    "leading space"),
                new SheepishTestCase("baa " ,     false,    "trailing space"),
            };

        public class SheepishTestCase
        {
            public SheepishTestCase(string text, bool isSheepBleat, string reason)
            {
                Text = text;
                IsSheepBleat = isSheepBleat;
                Reason = reason;
            }
            public string Text;
            public bool IsSheepBleat;
            public string Reason;
            // nUnit requires a unique ToString() for a name.
            public override string ToString() => Text;
        }
    }
}
