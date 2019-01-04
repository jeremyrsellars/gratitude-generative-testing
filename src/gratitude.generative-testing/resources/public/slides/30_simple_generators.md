-----------#_Generative_-testing.jumbo-left

<style class='before-speaker-note'></style>

* Earlier, in our discussion of "generative testing", we skipped the first word, "generative", and went straight to "testing."
* ~~Now, let's turn to automatic generation of examples for testing – test-case generation.~~
* Unlike example-based unit tests, we may not already know the "correct" answer for any given input, especially if the data are generated randomly.
* If we have an oracle, we can use it to compute the correct answer.
* Without the oracle, we can only test facts (properties) that should be true in every case.

# *Generative* Testing

1. Generate some test scenarios.
    * Generate some strings from combinations of "a", "b", and "c".
2. Run the program or parameterized tests with that data.
    * See if the program produces the same answer as the oracle, or that other properties hold.

--------------#QuickCheck

# [QuickCheck ~1999](http://www.cse.chalmers.se/~rjmh/QuickCheck/)

```speaker-note
The canonical example of a generative testing library is QuickCheck, a famous library from the Haskell development community.
```

> **QuickCheck is a tool for testing Haskell programs automatically. The programmer provides a specification of the program, in the form of properties which functions should satisfy, and QuickCheck then tests that the properties hold in a large number of randomly generated cases.** Specifications are expressed in Haskell, using combinators defined in the QuickCheck library. QuickCheck provides combinators to define properties, observe the distribution of test data, and define test data generators.
– [John Hughes](https://en.wikipedia.org/wiki/John_Hughes_(computer_scientist%29)

<style class='before-speaker-note'></style>

* ["2018 ACM Fellows Honored for Pivotal Achievements that Underpin the Digital Age"](https://www.acm.org/media-center/2018/december/fellows-2018)

![John Hughes](http://www.cse.chalmers.se/~rjmh/Me%20prisma.jpg)

----------------#Generators.jumbo

# Generators

```speaker-note
What is a generator?
```

Generates reproducible, pseudo-random values

Shrink (produce less "interesting" values)

```speaker-note
* Example
    * Giant string. Email body.  Fancy unicode characters.
```

---------#Libraries.jumbo

# Libraries

```speaker-note
~~Less talk, more code!~~

For comparison's sake, we'll try libraries in 2 different language ecosystems: Clojure (for JVM or the Browser) and C# for .Net.
```

*Clojure*: [Clojure.Spec](https://clojure.org/guides/spec) powered by [Test.Check](https://github.com/clojure/test.check)

*C#/F#/.Net*: [FsCheck](https://fscheck.github.io/FsCheck)


<style class='before-speaker-note'></style>

* Haskell: [QuickCheck](https://wiki.haskell.org/Introduction_to_QuickCheck1)
* Clojure: [Clojure.Spec](https://clojure.org/guides/spec), which generates data according to a specification using a QuickCheck-inspired property-based testing library called [Test.Check](https://github.com/clojure/test.check).
* C#/.Net: [FsCheck](https://fscheck.github.io/FsCheck) is a QuickCheck-inspired property based testing framework implemented in F#.  It has good support for C#, as well.

```speaker-note
Let's start with Clojure.
```
-------------#REPL-1

<div class="speaker-note label">Omit</div>

## Clojure

<style class='before-speaker-note'></style>

* We will be using a relatively new part of Clojure, called clojure.spec.
    * Suffice it to say that Clojure.spec is useful for describing data without resorting to strong types.
    * The property-based testing capabilities are powered by a library named [test.check](https://github.com/clojure/test.check).
    * Since there is so much going on, I will only call out the distinction between `spec` and `test.check` when necessary.
    * Most anything that is a description of the data is spec-related, while most things data-generation-related are test.check.

* Clojure's interactive REPL program makes it easy to demonstrate small examples.

```speaker-note
Let's demonstrate an integer generator in each library to get a feel for what's involved.
```

### [clojure.spec.alpha](https://clojure.org/guides/spec)

* [clojure.spec.alpha](https://clojure.org/guides/spec)
    * New in Clojure 1.9.
    * `...alpha` – Powerful ideas.  Production ready, but a forked project may eventually replace the "alpha" version to prevent breaking changes.)
* Generative testing is powered by [test.check](https://github.com/clojure/test.check).

### [Clojure test.check](https://github.com/clojure/test.check)

> test.check is a Clojure property-based testing tool inspired by QuickCheck. The core idea of test.check is that instead of enumerating expected input and output for unit tests, you write properties about your function that should hold true for all inputs. This lets you write concise, powerful tests.
- [test.check](https://github.com/clojure/test.check)

### [REPL](https://clojure.org/guides/repl/introduction) (Clojure interactive)

* **`R`**ead – Reads an expression that you type after the prompt (like `user=>`)
* **`E`**valuate – Evaluates the expression, computing the result
* **`P`**rint – Prints it to the screen
* **`L`**oop – Loop back to the beginning to read again

------------#Clojure-Generator

# Clojure Example

<style class='before-speaker-note'></style>

0. After we bring in the required namespaces, we can start using the generators.
1. In this case, we'll ask clojure.spec for an int generator.
2. We creates an instance of a Java class called `Generator` in the `clojure.test.check.generators` package, then we generate some examples with it.
3. Notice:
    * `int?` is a `fn` from Clojure's standard library that asks if a value is an integer.  Clojure.spec is able to look up an appropriate generator from this function.  It's a generator that is capable of generating integers.
    * `(gen/generate (s/gen int?))` yields an integer (and not necessarilly a simple one)
    * `(gen/sample (s/gen int?) 3)` yields 3 integers, usually simpler ones

<style class='before-speaker-note'></style>

* This generates a new value, or list of values, each time, which already sounds more interesting than example-based unit tests.
* So, you may notice that the result from `gen/generate` seems more surprising than that of `gen/sample`.
  * test.check usually starts by generating smaller, more "boring" values.


```clojure
user=> (require '[clojure.spec.alpha :as s]
'[clojure.spec.gen.alpha :as gen] '[clojure.test.check.generators])
user=> (s/gen int?)
#clojure.test.check.generators.Generator{:gen #object[clojure.test.check.generators$such_that$fn__1825 0x633837ae "clojure.test.check.generators$such_that$fn__1825@633837ae"]}
user=> (gen/generate (s/gen int?))
123
user=> (take 3 (repeatedly #(gen/generate (s/gen int?))))
(60273 -94 -3)
user=> (gen/sample (s/gen int?) 3))
(0 -1 1)
```

* `int?` is a `fn` from Clojure's standard library
* `(s/gen int?)` yields a `clojure.test.check.generators.Generator`
* `(gen/generate (s/gen int?))` yields an integer
* `(gen/sample (s/gen int?) 3)` yields 3 integers
* If you're following along in the REPL, you'll need to include the `test.check` library before you start the REPL.  Add `:dependencies` to project.clj:
    ```clojure
    [org.clojure/test.check "0.9.0"]
    ```
    * See the `:require` section of [sheepish.d-parameterized-test-with-properties](https://github.com/jeremyrsellars/no-new-legacy/blob/master/src/sheepish/test/sheepish/d_parameterized_test_with_generators.cljc#L2) or the [spec Guide](https://clojure.org/guides/spec).

-------------#nUnit-Generator

# FsCheck Generators

<style class='before-speaker-note'></style>

* Let's see how to create and use a generator in FsCheck.
    * Again, we'll ask for an int generator.
    * It creates an instance of a .Net class called `Gen` in the `FsCheck` namespace.
* FsCheck generators also have a `size` parameter that can help control the iterestingness of data generated, to prevent examples from being too simple to expose errors or too complex to execute quickly.  A larger "size" in a list generator may produce longer lists, while a larger "size" in a string generator may produce more of the unusual characters, for example.
    * The precise effect of generating different 'sizes', changes from type to type.  Different generators use it differently, but the key point is this: **larger sizes produce more interesting values.**
    * FsCheck has a good writeup of this in the [documentation](https://fscheck.github.io/FsCheck/TestData.html#The-size-of-test-data) linked in the last slide.
    * The libraries usually offer a way to opt-out of using size in some parts of your code.  This could be used to let you generate many small examples.
* Some ways of generating examples:
    1. So, one way to make a generator for integer values is `var gen = Gen.Choose(low, hi)`.  This returns an instance of `Gen<int>`.
    2. To generate 3 examples, we use `gen.Sample(someSize, 3)` to use that generator 3 times and put the generated values in a List.

```csharp
var size = 42;            // interestingness
var exampleCount = 3;     // Generate 3 random integers.
Gen<int> generator = Gen.Choose(int.MinValue, int.MaxValue);  // Gen.Choose(low, hi);
IEnumerable<int> examples = generator.Sample(size, exampleCount);
```

1. `var gen = Gen.Choose(low, hi)` – returns an instance of `Gen<int>`.
2. `var examples = gen.Sample(interestingnessSize, 3)` – Generates 3 integers, with some complexity "size".

### Larger sizes produce more interesting values.

------------#returns

# More example generators

## Generate null/nil

```clojure
(s/gen nil?)
```

## Generator returns a value

Or, more literally, you can make a constant generator that always returns the same value that is passed in.
```clojure
(gen/return nil)
(gen/return "Some String")
```

```csharp
Gen.Constant<string>(null);
Gen.Constant<string>("Some String");
```

------------#element-of-collection

## Choose an element from a collection

<style class='before-speaker-note'></style>

* Imagine you have a few values and you want a generator to make a random choice from just those already realized values. You want to select a random element from a list.
* clojure.spec:
    * The default generator for a hash set `#{}` in clojure.spec is a choice of the members.

```clojure
(s/gen #{"bears" "beets" "Battlestar Galactica"})
(gen/elements ["bears" "beets" "Battlestar Galactica"])
```

```csharp
Gen<string> gen = Gen.Elements(new [] {"bears", "beets", "Battlestar Galactica"});
```

------------#alternatives

## Choose between alternative generators

<style class='before-speaker-note'></style>

* Now, what if you want to pick a random generators and have that generator generate the value.
* FsCheck
    * With FsCheck, making the choice between options can be done with a combination of generators, with `Gen.OneOf`.
    * In this case, it chooses between "constant" generators which always return the same value.
* Weighted vs. unweighted
    * FsCheck has an easy way for each.
    * Clojure.spec doesn't have use for weighted generation, but this is possible with test.check. It is very similar to the FsCheck method.

```clojure
; Equal probability, using a hash set literal value in `#{}`
(s/gen #{"bears" "beets" "Battlestar Galactica"})
(gen/elements #{"bears" "beets" "Battlestar Galactica"})
(gen/one-of [(gen/return "bears")
             (gen/return "beets")
             (gen/return "Battlestar Galactica")])
; Weighted probability
(gen/frequency
  [[2 (gen/return "bears")]
   [1 (gen/return "beets")]
   [1 (gen/return "Battlestar Galactica")]])
```

```csharp
// Equal probability
Gen<string> gen = Gen.Elements(new [] {"bears", "beets", "Battlestar Galactica"});
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

------------------#source-code

# Source code (generators in context)

<style class='before-speaker-note'></style>

A lot of the data generation capabilities of these libraries comes from combining simpler generators to create more capable generators.

* Clojure: https://github.com/jeremyrsellars/no-new-legacy/blob/master/src/sheepish/test/sheepish/d_parameterized_test_with_generators.cljc
* C#: https://github.com/jeremyrsellars/no-new-legacy/blob/master/src/Sheepish.net/Sheepish.CSharp/D_Parameterized_Test_With_Generators.cs
