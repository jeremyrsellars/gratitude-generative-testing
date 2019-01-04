Unit tests validate one example at a time, but what if you didn't have to think up dozens of unique examples and write them by hand?

* 100 monkeys to drive your web application
* 10 million example transactions for load testing
* gain confidence in your software without writing hundreds of example scenarios.

* "thought tool" to identify useful properties to validate,
* ideas for testing some "tricky" properties,
* an intuition for when to use generative testing vs. example-based unit tests.

* team-based gratitude tracker
	* design
	* validation of examples of generating "synthetic thank you notes" for testing, property testing the implementation,
	* model-based testing the awards system.

# Outline

* Introduction
    * Topic
    * Gratitude definition – the quality of being thankful; readiness to show appreciation for and to return kindness.
    * Sponsors
    * Me

    * Craftsmanship Manifesto
        * "Well-crafted software"
            * Open to extension
        * "Steadily adding value"
            * Predictable timelines for update features
        * "A community of professionals"
            * Here we are at a conference
        * "Productive partnerships"
            * Providing examples for a spec

    * Thought Expiriments
        *

* Slide visuals
    * Interview slides
        * Money, checks -- http://localhost:3470/presenter.html#!/gratitude.generative_testing.section_10_introduction/Interview-1
        * Hand shake
        * Data
    * Abstraction-principal -- http://localhost:3470/presenter.html#!/gratitude.generative_testing.section_10_introduction/
    * Shoulders of giants -- http://localhost:3470/presenter.html#!/gratitude.generative_testing.section_10_introduction/Media
    * Generate -> Test -- http://localhost:8080/slides.html#!/gratitude.generative_testing.section_20_property_testing/Generative-Testing

* Generative Testing
    * Property Testing
        * Example-based vs. Property-based (parameterized tests & DRY)
        * Oracles vs. Properties
        * "Thought tool" to identify useful properties to validate,
    * Generative
        * John Hughes (QuickCheck ~1999) – https://en.wikipedia.org/wiki/John_Hughes_(computer_scientist)
            * ["2018 ACM Fellows Honored for Pivotal Achievements that Underpin the Digital Age"](https://www.acm.org/media-center/2018/december/fellows-2018)
        * Simple Generators (pseudo-code)
        * Languages and libraries in this talk
            * Promise not to evangelize Clojure too much. ;-)
            * FsCheck (C#)
            * Test.Check/Clojure.Spec.Alpha (Clojure)
                * How to read Clojure
        * Examples (in pseudo-code w/diagrams, C#, Clojure)
            * Generate an integer
            * Always (Constant)
            * Random element from a vector
            * An integer or nothing
            * Common or rare (frequency)
        * Transforming Generators
            * Change a generators (fmap)
            * Guess and check (SuchThat/.Where)
        * Combining Generators
            * Alternatives
            * Lists & Tuples
            * Random Permutations (Shuffle)
            * Building complex data
        * ideas for testing some "tricky" properties
    * Shrink?
        * An intuition is enough for now

* Application
    * Gratitude tracker
* Conclusion
    * An intuition for when to use generative testing vs. example-based unit tests.
    * Craftsmanship block (revisited)
        * Well-crafted
            * Reusable test-data generation
            * Open to extension
            * Example Transactions for load testing, stress testing
            * Drive a web application with Selenium
        * "Steadily adding value"
            * Predictable timelines for update features
            * Reusable test-data generation
        * "A community of professionals"
            * Here we are at a conference
        * "Productive partnerships"
            * Providing examples for a spec
            * Example messages for a contract/protocol/specification (help validate a draft of a specification to shake out problems)
            * Validate a simpler "working model" of a system. (Without actual database/threads/)




# Possible Questions for Mentors

* What about stage fright?
* How small is readable?
* How to practice?
* How much content?  Is this outline too big?


# Big to-dos

* Grattitude Tracker – A web application
    * Selenium
    * Model
* Talk Outline – structure in 4s
* `Size` blog – https://github.com/clojure/test.check/blob/master/doc/growth-and-shrinking.md

# Tips

* Diagrams "sweat bullets"
* Speak to the 45th percentile of the group


# Gratitude app

* Thank-you notes
    * From :user
    * To [:user]
    * Message string
    * Tacos int
    * When #inst
* CRUD Implementation
    * FS/Event-sourcing-based
    * Notifications?
* Awards system
    * Monthly report
* "Murphy" user.  Anything that can go wrong will go wrong.

## Examples in C# and Clojure.

* [x] Code formatting in devcards
    * [x] Clojure
    * [x] C#

C#:

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

Clojure:

```clojure
(s/gen int?)
(gen/generate (s/gen int?))
(take 3 (repeatedly #(gen/sample (s/gen int?) 3)))
(gen/sample (s/gen int?) 3))
```

Clojure Repl:

```clojure
user=> (s/gen int?)
#clojure.test.check.generators.Generator{:gen #object[clojure.test.check.generators$such_that$fn__1825 0x633837ae "clojure.test.check.generators$such_that$fn__1825@633837ae"]}
user=> (gen/generate (s/gen int?))
123
user=> (take 3 (repeatedly #(gen/sample (s/gen int?) 3)))
(60273 -94 -3)
user=> (gen/sample (s/gen int?) 3))
(0 -1 1)
```

One line:

```csharp
Gen.Constant<string>(null);
```

In-line: `Gen.Constant<string>(null);`
