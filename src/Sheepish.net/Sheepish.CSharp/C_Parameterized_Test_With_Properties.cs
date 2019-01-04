using System.Collections.Generic;
using NUnit.Framework;

namespace Sheepish.CSharp
{
    [TestFixture]
    [TestFixtureSource(nameof(SheepishExamples))]
    public class C_Parameterized_Test_With_Properties
    {
        readonly SheepishTestCase testCase;
        readonly bool isSheepBleat;

        string Text => testCase.Text;

        public C_Parameterized_Test_With_Properties(SheepishTestCase testCase)
        {
            this.testCase = testCase;
            isSheepBleat = Sheepish.IsSheepBleat(testCase.Text);
        }

        [Test]
        public void SheepishIsAtLeast3CharactersLong()
        {
            if (isSheepBleat)
                Assert.GreaterOrEqual(testCase.Text.Length, 3,
                    "Sheep bleat is at least 3 characters");
        }

        [Test]
        public void SheepishStartsWith_b()
        {
            if (isSheepBleat)
                StringAssert.StartsWith("b", Text);
        }

        [Test]
        public void SheepishEndsWith_aa()
        {
            if (isSheepBleat)
                StringAssert.EndsWith("aa", Text);
        }

        [Test]
        public void SheepishOnlyContains_ab()
        {
            var chars = new HashSet<char>(Text);
            var ab = new HashSet<char>("ab");
            if (isSheepBleat)
                CollectionAssert.AreEquivalent(ab, chars);
            if (!ab.SetEquals(chars))
                Assert.IsFalse(isSheepBleat, "isSheepBleat");
        }

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
            public override string ToString() => Text + "<-" + Reason;
        }
    }
}
