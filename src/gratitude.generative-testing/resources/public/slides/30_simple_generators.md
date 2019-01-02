-----------.jumbo

# *Generative* Testing

1. Generate some test scenarios (data for parameterized tests).
    * Generate some strings from combinations of "a", "b", and "c".
2. Run the program with that data, and test that some assumptions (properties) prove true for each scenario.
    * See if the program produces the same answer as the oracle, or that other properties hold.

<style class='before-speaker-note'></style>

* Earlier, in our discussion of "generative testing", we skipped the first word, "generative", and went straight to "testing."
* Now, let's turn to automatic generation of examples for testing – test-case generation.
* Unlike example-based unit tests, we may not already know the "correct" answer for any given input, especially if the data are generated randomly.
* If we have an oracle, we can use it to compute the correct answer.
* Without the oracle, we can only test facts (properties) that should be true in every case.

--------------

# [QuickCheck ~1999](http://www.cse.chalmers.se/~rjmh/QuickCheck/)

> QuickCheck is a tool for testing Haskell programs automatically. The programmer provides a specification of the program, in the form of properties which functions should satisfy, and QuickCheck then tests that the properties hold in a large number of randomly generated cases. Specifications are expressed in Haskell, using combinators defined in the QuickCheck library. QuickCheck provides combinators to define properties, observe the distribution of test data, and define test data generators.
– [John Hughes](https://en.wikipedia.org/wiki/John_Hughes_(computer_scientist%29)

<style class='before-speaker-note'></style>

* ["2018 ACM Fellows Honored for Pivotal Achievements that Underpin the Digital Age"](https://www.acm.org/media-center/2018/december/fellows-2018)

![John Hughes](http://www.cse.chalmers.se/~rjmh/Me%20prisma.jpg)

----------------.jumbo

# First Generators

* What is a generator?
    * Generates reproducible, pseudo-random values
    * Shrink (produce less "interesting" values)

```speaker-note
Should I talk about John Hughes/ACM here?

Less talk, more code!

For comparison's sake, we'll try 2 libraries in different language ecosystems.
```

* *Clojure*: [Clojure.Spec](https://clojure.org/guides/spec) via [Test.Check](https://github.com/clojure/test.check).
* *C#/F#/.Net*: [FsCheck](https://fscheck.github.io/FsCheck)
* Haskell: [QuickCheck](https://wiki.haskell.org/Introduction_to_QuickCheck1)


<style class='before-speaker-note'></style>

* Haskell: [QuickCheck](https://wiki.haskell.org/Introduction_to_QuickCheck1)
* Clojure: [Clojure.Spec](https://clojure.org/guides/spec), which generates data according to a specification using a QuickCheck-inspired property-based testing library called [Test.Check](https://github.com/clojure/test.check).
* C#/.Net: [FsCheck](https://fscheck.github.io/FsCheck) is a QuickCheck-inspired property based testing framework implemented in F#.  It has good support for C#, as well.

```speaker-note
Let's demonstrate an integer generator in each library to get a feel for what's involved.
```
-------------

## Clojure Test.Check Generators

### Clojure's [REPL](https://clojure.org/guides/repl/introduction)

* **`R`**ead – Reads an expression that you type after the prompt (like `user=>`)
* **`E`**valuate – Evaluates the expression, computing the result
* **`P`**rint – Prints it to the screen
* **`L`**oop – Loop back to the beginning to read again

### [clojure.spec.alpha](https://clojure.org/guides/spec)

* [clojure.spec.alpha](https://clojure.org/guides/spec) – New in Clojure 1.9.  Alpha.  Powerful ideas, but subject to replacement.
* Generative testing is powered by [test.check](https://github.com/clojure/test.check).

<style class='before-speaker-note'></style>

* We will be using a relatively new part of Clojure, called clojure.spec.
    * The property-based testing capabilities are powered by a library named [test.check](https://github.com/clojure/test.check).
    * Since there is so much going on, I will only call out the distinction between `spec` and `test.check` when necessary.
    * Suffice it to say that Clojure.spec is useful for describing data without resorting to strong types.
    * Most anything that is a description of the data is spec-related, while most things data-generation-related are test.check.

* Let's see how to create and use a generator.
    * In this case, we'll ask clojure.spec for an integer generator.
    * It creates an instance of `clojure.test.check.generators.Generator`, then generate some examples with it.

* Clojure's REPL program makes it easy to demonstrate small examples.
    * Reads an expression that you type after the prompt (like `user=>`)
    * Evaluates the expression, computing the result
    * Prints it to the screen
    * Loop back to the beginning to read again

* So, let's evaluate a few expressions at the [REPL](https://clojure.org/guides/repl/introduction).
    * If you want to follow along, you'll want to `require` the right namespaces.
    * See the `:require` section of [sheepish.d-parameterized-test-with-properties](https://github.com/jeremyrsellars/no-new-legacy/blob/master/src/sheepish/test/sheepish/d_parameterized_test_with_generators.cljc#L2) or the [spec Guide](https://clojure.org/guides/spec).

------------

# Clojure Example

```clojure
user=> (s/gen int?)
#clojure.test.check.generators.Generator{:gen #object[clojure.test.check.generators$such_that$fn__1825 0x633837ae "clojure.test.check.generators$such_that$fn__1825@633837ae"]}
user=> (gen/generate (s/gen int?) 42)
123
user=> (take 3 (repeatedly #(gen/generate (s/gen int?) 42)))
(60273 -94 -3)
user=> (gen/sample (s/gen int?) 3))
(0 -1 1)
```

Notice:

* `int?` is a `fn` from Clojure's standard library
* `(s/gen int?)` yields a `clojure.test.check.generators.Generator`
* `(gen/generate (s/gen int?))` yields an integer
* `(gen/generate (s/gen int?) optional-size)` yields an integer, and can yield more-interesting results
* `(gen/sample (s/gen int?) 3)` yields 3 integers

### `gen/generate` vs. `gen/sample`

<style class='before-speaker-note'></style>

* In this case, we'll ask clojure.spec for an int generator.
* It creates an instance of a Java class called `Generator` in the `clojure.test.check.generators` package, then generate some examples with it.
* Notice:
    * `int?` is a `fn` from Clojure's standard library that asks if a value/class is an integer.  Clojure.spec is able to look up an appropriate generator from this function.
    * `(s/gen int?)` yields a `clojure.test.check.generators.Generator` capable of generating integers
    * `(gen/generate (s/gen int?))` yields an integer (and not necessarilly a simple one)
    * `(gen/sample (s/gen int?) 3)` yields 3 integers, usually simpler ones

<style class='before-speaker-note'></style>

* This generates a new value, or list of values, each time, which already sounds more interesting than example-based unit tests.
* So, you may notice that the result from `gen/generate` seems more surprising than that of `gen/sample`.
  * test.check usually starts by generating smaller, more "boring" values.  We'll come back to this concept of `size` later.

<style class='before-speaker-note'></style>

1. So, one way to make a generator for integer values is `(s/gen int?)`.  This returns a test.check generator.
2. To use that generator, we use `(gen/generate (s/gen int?))` to generate a value.
3. To generate several, we can lazily take a few "bigger" examples from an infinite lazy sequence, or a few simpler examples with `gen/sample`.

-------------

## FsCheck Generators

<style class='before-speaker-note'></style>

* Let's see how to create and use a generator in FsCheck.
    * Again, we'll ask for an int generator.
    * It creates an instance of a .Net class called `Gen` in the `FsCheck` namespace.
* C#'s type system may help describe what is going on for our type-oriented friends.

```csharp
var size = 42; // ignore for now
var exampleCount = 3; // Generate 3 random integers.
Gen<int> generator = Gen.Choose(int.MinValue, int.MaxValue);  // Gen.Choose(low, hi);
IEnumerable<int> examples = generator.Sample(size, exampleCount);
```

1. `var gen = Gen.Choose(low, hi)` – returns an instance of `Gen<int>`.
2. `var examples = gen.Sample(someSize, 3)` – Generates 3 integers, with some complexity "size".

<style class='before-speaker-note'></style>

* FsCheck generators have a `size` parameter that can help control the iterestingness of data generated, to prevent examples from being too simple to expose errors or too complex to execute quickly, like the size of lists, producing unusual characters in strings, among other things.
    * We'll come back to that later because it's very dependent on the type of generator being used.
    * FsCheck has a good writeup of this in the [documentation](https://fscheck.github.io/FsCheck/TestData.html#The-size-of-test-data) linked in the last slide.
* Some ways of generating examples:
    1. So, one way to make a generator for integer values is `var gen = Gen.Choose(low, hi)`.  This returns an instance of `Gen<int>`.
    2. To generate 3 examples, we use `gen.Sample(someSize, 3)`.

------------

# More example generators

## Generate null/nil

```clojure
(s/gen nil?)
```

Or, more literally, you can make a constant generator that always returns the same value that is passed in.
```clojure
(gen/return nil)
```

```csharp
Gen.Constant<string>(null);
```

------------

## Choose between alternatives

```clojure
; Equal probability, using a hash set literal value in `#{}`
(s/gen #{"bears" "beets" "Battlestar Galactica"})
; Weighted probability
(sgen/frequency
  [[2 (gen/return "bears")]
   [1 (gen/return "beets")]
   [1 (gen/return "Battlestar Galactica")]])
```

<style class='before-speaker-note'></style>

* Imagine you have a few values and you want to generate a random choice between those.
* clojure.spec:
    * The default generator for a hash set `#{}` in clojure.spec is a choice of the members.
* FsCheck
    * With FsCheck, making the choice between options can be done with a combination of generators, with `Gen.OneOf`.
    * In this case, it chooses between "constant" generators which always return the same value.
* Weighted vs. unweighted
    * FsCheck has an easy way for each.
    * Clojure.spec doesn't have use for weighted generation, but this is possible with test.check. It is very similar to the FsCheck method.

```csharp
// Equal probability
Gen<string> gen = Gen.OneOf(
  Gen.Constant("bears"),
  Gen.Constant("beets"),
  Gen.Constant("Battlestar Galactica"));
// Weighted probability
Gen<string> wgen = Gen.Frequency(
  Tuple.Create(2, Gen.Constant("bears")),
  Tuple.Create(1, Gen.Constant("beets")),
  Tuple.Create(1, Gen.Constant("Battlestar Galactica")));
```

------------------

# Source code (generators in context)

<style class='before-speaker-note'></style>

A lot of the data generation capabilities of these libraries comes from combining simpler generators to create more capable generators.

* Clojure: https://github.com/jeremyrsellars/no-new-legacy/blob/master/src/sheepish/test/sheepish/d_parameterized_test_with_generators.cljc
* C#: https://github.com/jeremyrsellars/no-new-legacy/blob/master/src/Sheepish.net/Sheepish.CSharp/D_Parameterized_Test_With_Generators.cs
