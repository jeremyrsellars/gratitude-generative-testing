using System;
using System.Collections.Generic;
using System.Linq;
using NUnit.Framework;
using FsCheck;

namespace Sheepish.CSharp
{
    [TestFixture]
    [TestFixtureSource(nameof(SheepishExamples))]
    [TestFixtureSource(nameof(SheepishExamples2))]
    public class F_Parameterized_Test_With_Better_Generators
    {
        readonly SheepishTestCase testCase;
        readonly bool isSheepBleat;

        string Text => testCase.Text;

        public F_Parameterized_Test_With_Better_Generators(SheepishTestCase testCase)
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
                    isSheepBleat,
                    correctSheepishForStringLength + "<->" + Text);
            }
        }

        static IEnumerable<int> ExampleOfGenerators(int exampleCount)
        {
            var size = 42;
            Gen<int> generator = Gen.Choose(int.MinValue, int.MaxValue);
            IEnumerable<int> examples = generator.Sample(size, exampleCount);
            return examples;
        }

        static Gen<string> UsuallyEmptyString =>
            Gen.Frequency(
                Tuple.Create(9, Gen.Constant("")),
                Tuple.Create(1, Arb.Default.NonEmptyString().Generator.Select(s => s.Item)));

        static Gen<string> StringOfA =>
            Gen.Choose(-3, 3).Select(n => new string('a', Math.Max(n, 0)));
        static Gen<string> StringOfB =>
            Gen.Choose(0, 3).Select(n => new string('b', Math.Max(n, 0)));

        static IEnumerable<SheepishTestCase> SheepishExamples =>
            Gen.zip(
                Gen.zip3(UsuallyEmptyString,
                         StringOfB,
                         UsuallyEmptyString),
                Gen.zip3(StringOfA,
                         UsuallyEmptyString,
                         StringOfA))
            .Select(t => t.Item1.Item1 + t.Item1.Item2 + t.Item1.Item3
                       + t.Item2.Item1 + t.Item2.Item2 + t.Item2.Item3)  // That's confusing!
            .Select(text => new SheepishTestCase(text, "random")) // To test case
            .Sample(4, 100);                                      // take 100 examples

        /// <summary>
        /// A class to represent a "better" sheepish generation.
        /// </summary>
        public class SheepishParts
        {
            // FsCheck can use reflection to find such a constructor,
            // https://github.com/fscheck/FsCheck/blob/8fd6dd7/src/FsCheck/Reflect.fs#L21-L27
            public SheepishParts(UsuallyEmptyString S0, StringOfBs B1, UsuallyEmptyString S2, StringOfAs A3, UsuallyEmptyString S4, StringOfAs A5)
            {
                this.S0 = S0;
                this.B1 = B1;
                this.S2 = S2;
                this.A3 = A3;
                this.S4 = S4;
                this.A5 = A5;
            }
            public UsuallyEmptyString S0 { get; }
            public StringOfBs B1 { get; }
            public UsuallyEmptyString S2 { get; }
            public StringOfAs A3 { get; }
            public UsuallyEmptyString S4 { get; }
            public StringOfAs A5 { get; }

            public override string ToString() => "" + S0 + B1 + S2 + A3 + S4 + A5;

            public class UsuallyEmptyString : Wrapper<string> { }
            public class StringOfAs : Wrapper<string> { }
            public class StringOfBs : Wrapper<string> { }
        }

        public class SheepishPartsGenerators
        {
            public static Arbitrary<T> FromStringGen<T>(Gen<string> gen)
                where T : Wrapper<string>, new() =>
                gen.Select(s => new T{ Value = s }).ToArbitrary();
            public static Arbitrary<SheepishParts.UsuallyEmptyString> UsuallyEmptyString() => FromStringGen<SheepishParts.UsuallyEmptyString>(F_Parameterized_Test_With_Better_Generators.UsuallyEmptyString);
            public static Arbitrary<SheepishParts.StringOfAs> StringOfAs() => FromStringGen<SheepishParts.StringOfAs>(F_Parameterized_Test_With_Better_Generators.StringOfA);
            public static Arbitrary<SheepishParts.StringOfBs> StringOfBs() => FromStringGen<SheepishParts.StringOfBs>(F_Parameterized_Test_With_Better_Generators.StringOfB);
        }

        public class Wrapper<T>
        {
            public Wrapper() => Value = default(T);
            public Wrapper(T value) => Value = value;
            public T Value;
            public override string ToString() => Value?.ToString();
        }

        static F_Parameterized_Test_With_Better_Generators()
        {
            try
            {
                Arb.Register<SheepishPartsGenerators>();
            }
            catch(Exception e)
            {
                e.ToString();
            }
        }

        static IEnumerable<SheepishTestCase> SheepishExamples2 =>
            Arb.Default.Derive<SheepishParts>().Generator
            .Select(parts => parts.ToString())
            .Select(text => new SheepishTestCase(text, "random")) // To test case
            .Sample(4, 100);                                      // take 100 examples

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
