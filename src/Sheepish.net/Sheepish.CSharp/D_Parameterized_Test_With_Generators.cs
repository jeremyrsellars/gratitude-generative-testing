using System.Collections.Generic;
using System.Linq;
using NUnit.Framework;
using FsCheck;

namespace Sheepish.CSharp
{
    [TestFixture]
    [TestFixtureSource(nameof(SheepishExamples))]
    public class D_Parameterized_Test_With_Generators
    {
        readonly SheepishTestCase testCase;
        readonly bool isSheepBleat;

        string Text => testCase.Text;

        public D_Parameterized_Test_With_Generators(SheepishTestCase testCase)
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

        [Test]
        public void Sheepish_HasCorrectStructure()
        {
            if (Text.Length < 3)
                Assert.IsFalse(isSheepBleat, "Too short");
            else
            {
                var correctSheepishForStringLength =
                    "b" + new string('a', Text.Length - 1);

                Assert.AreEqual(
                    correctSheepishForStringLength == Text,
                    isSheepBleat);
            }
        }

        static IEnumerable<int> ExampleOfGenerators(int exampleCount)
        {
            var size = 42;
            Gen<int> generator = Gen.Choose(int.MinValue, int.MaxValue);
            IEnumerable<int> examples = generator.Sample(size, exampleCount);
            return examples;
        }

        static IEnumerable<SheepishTestCase> SheepishExamples =>
            Gen.OneOf(Gen.Constant('a'), Gen.Constant('b'))    // Choose beetween a and b
                .ListOf()                                      // Make a list like [a, b, a]
                .Select(chars => new string(chars.ToArray()))  // Convert to string;
                .Select(text => new SheepishTestCase(text, "random")) // To test case
                .Sample(4, 100);                               // take 100 examples

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
