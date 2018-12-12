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
