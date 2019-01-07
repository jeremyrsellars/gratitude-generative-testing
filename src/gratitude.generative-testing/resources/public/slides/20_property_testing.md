-----------#Generative-Testing.jumbo

# "Generative Testing"

```speaker-note
From the plain English root verbs of "generative testing", we can surmise there are at least two actions to explore: 'generate' and 'test'.  At its core, the actions are
```

1. Generate some random test cases.
2. Check that the software works.

```speaker-note
Let's start with the more familiar part: _testing_.
```

-----------#Generative__Testing_.jumbo

# Testing

```speaker-note
If you've been around programming for any length of time, it will come as no surprise that 'testing' means checking that the program works as expected.

Let's start with testing so we can connect with concepts you may already be familiar with, like unit testing.  Many of the foundational concepts of generative testing can be applied in testing frameworks like nUnit and jUnit.

Most generative testing software is part of a testing framework that does a lot of things for you, like selecting which tests to run, executing the tests, reporting the results of the tests, perhaps including a reason for the failure, etc..
```

---------#Sheepish-0

## Sheepish

Sheepish spec: `b` followed by 2 or more `a` characters.

### Sheep bleat examples

* Yes: `baa`, `baaa`, `baaaaaaaaaaaaaaaaaaaaaaaaaa`, `baaaaa`
* No: `baad`, `ba`, `baaaaaa9aaaaaaaaaaaaaaaaaaaa`, `abaa`

```speaker-note
Let's start with a simple test about the contents of a string: does a string look like sheep bleat?  For our purposes, let's define sheep bleat as any string that starts with a `b` and is followed by at least 2 `a`s, making a string like "baa" or "baaaaaaaaaaaaaaaaa".  Further, let's say that only `b` and `a`s are allowed in sheep bleat, so `baad` isn't valid bleat.
```

![Sheep](https://i.imgflip.com/1q73q8.jpg)

https://imgflip.com/i/1q73q8

----------#Sheepish-examples

## Function signature and examples

```csharp
bool isSheepBleat(string s)
{
    return false;
}
```

```clojure
(defn sheep-bleat?
    [s]
    false) ; obvious bug here!
```

<style id='sheep-bleat' class='before-alternating-table'></style>

|Text      |Sheep Bleat?|Reason        |
|----------|------------|--------------|
|`b`       |false       |Too short     |
|`ba`      |false       |Too short     |
|`baa`     |**true**    |2+ `a`s       |
|`baaa`    |**true**    |2+ `a`s       |
|`baaad`   |false       |invalid `d`   |
|`·baa`    |false       |leading space |
|`baa·`    |false       |trailing space|

-----------#Example-based.jumbo-left

# Example-based Tests

```speaker-note
You can probably imagine these tests in your unit testing framework of choice.  Perhaps each of these tests would be represented as a function asserting the expected true/false value.  Some frameworks let you specify the test code once, and then the test data can be delivered as a parameter to the tests.  That increases the signal-to-noise ratio, and helps you keep your eye on the data rather than the redundant function execution code.  If you haven't yet taken the opportunity to try parameterized tests in your framework, I believe it would be well-worth your time.
```

* Unit tests
* Scenario tests

------------#Clojure

# Clojure – A dynamically typed LISP dialect

* Clojure – Hosted on Java Virtual Machine
* ClojureScript – Compiled to JavaScript
* ClojureCLR – Hosted on the .NET Framework

```clojure
(defn sheep-bleat?                        ; define a function named "sheep-bleat?"
    [s]                                   ; argument list
    (if (string/starts-with? s "b")      ; (if test-expression evaluated-when-true evaluated-when-false)
        (string/ends-with? s "aa")       ; (function argument-1 argument-2)
        false))

;; Literal data structures, etc.         ; Comments are proceeded by ;
(list 1 2 3)                             ; List (quick access to front of list)
[1 2 3 "go!"]                            ; Vector (quick access to any index)
{:name "Jeremy", "role" "speaker"}       ; Hash map (quick access by key)
#{"set" "without" "duplicates"}          ; Hash set (quick membership test)
#(if (= % ".clj") "Clojure" "Other")     ; Anonymous function
(fn infer-language [ext]                 ; Locally-scoped function (first-class!)
    (if (= % ".clj") "Clojure" "Other")) ; Everything is an expression
:keyword  :com.example/kw  ::alias/kw    ; Keywords often describe data
```
------------#Clojure-example-based-tests

# Clojure example-based tests

```speaker-note
The parameterized test might look like this, which executes an assertion function for each example:
```

```clojure
(defn sheep-bleat?
  [s]
  false) ; obvious bug here!

(defn assert-sheep-bleat
  [text expected-answer reason]
  (is (= expected-answer
         (sheep-bleat? text))
    reason))

(def examples
 [["b"         false    "Too short"]
  ["ba"        false    "Too short"]
  ["baa"       true     "2+ 'a' s"]
  ["baaa"      true     "2+ 'a' s"]
  ["baaad"     false    "invalid ' d'"]
  [" baa"      false    "leading space"]
  ["baa "      false    "trailing space"]])

(deftest Testing_sheep-bleat?
  ; Run the test for each of the examples
  (doseq [[text expected-answer reason] examples]
     (assert-sheep-bleat text expected-answer reason)))
```

------------#Csharp

# C# – An Object-Oriented language for .Net

Perhaps you've heard of it.  ;-)

```csharp
public class AnimalLanguageDetection         // Class definition
{
    public bool IsSheepBleat(string s)       // Function definition
    {                                        // Insignificant parenthesis ;-)
        return                               // Imperative statement
               s != null && s[0] == 'b';     // Expression with inline operators
    }

    public bool IsDogBark(string s) =>       // Function definition
        false;                               // Expression
}
```

------------#nUnit-example-based-tests

# C# with nUnit example-based tests

```csharp
[TestFixture] public class A_Parameterized_Test
{
    [TestCaseSource(nameof(SheepishExamples))]
    public void SheepBleatDetection(SheepishTestCase testCase) =>
        Assert.AreEqual(testCase.IsSheepBleat, Sheepish.IsSheepBleat(testCase.Text), testCase.Reason);

    static IEnumerable<SheepishTestCase> SheepishExamples => new[] {
        new SheepishTestCase("b",         false,    "Too short"),
        new SheepishTestCase("ba",        false,    "Too short"),
        new SheepishTestCase("baa",       true,     "2+ 'a' s"),
        new SheepishTestCase("baaa",      true,     "2+ 'a' s"),
        new SheepishTestCase("baaad",     false,    "invalid ' d'"),
        new SheepishTestCase(" baa",      false,    "leading space"),
        new SheepishTestCase("baa " ,     false,    "trailing space")};

    public class SheepishTestCase {
        public SheepishTestCase(string text, bool isSheepBleat, string reason) {
            Text = text; IsSheepBleat = isSheepBleat; Reason = reason;
        }
        public string Text;
        public bool IsSheepBleat;
        public string Reason;
        public override string ToString() => Text;  // nUnit requires a unique ToString() for a name.
    }
}
```

--------------#Oracle

# Parameterized Test (with an oracle)

```speaker-note
With unit tests, we may wish to come up with specific examples and test that against a known-correct answer, but with generative testing we don't have the opportunity to think through the correct answer (examples are generated randomly, after all).  We will want a way to compute the expected answer.  This is sometimes called an "oracle" (think the deity that heroes from mythology consult to answer a question).  For this contrived example, we have a ready-made oracle that would provide the correct answer for any string: the regular expression `^baa+$`
```

### Regular expression as an oracle

```
baa+

/^baa+$/
```

Matches strings like:

```
"baaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
"baaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
"baaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
"baaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
"baaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
"baaaaaa"
```

Source: [onlinestringtools.com/generate-string-from-regex?&regex=^baa+$](https://onlinestringtools.com/generate-string-from-regex?&regex=^baa+$)


--------------#Clojure-oracle

# Clojure parameterized test (with an Oracle)

```speaker-note
Same examples, but with the answers no-longer known beforehand.
```

```clojure
(defn assert-sheep-bleat
  [text reason]
     ; ^ Remove the correct answer; we have to compute it somehow.
  (is (= (some? (re-find #"^baa+$" text))  ; true when a match is found, false when nil is returned.
         (sheep-bleat? text))
    reason))

(def examples
 [["b"         "Too short"]
  ["ba"        "Too short"]
  ["baa"       "2+ 'a' s"]
  ["baaa"      "2+ 'a' s"]
  ["baaad"     "invalid 'd'"]
  [" baa"      "leading space"]
  ["baa "      "trailing space"]])

(deftest Testing_sheep-bleat?_with_oracle
  ; Run the test for each of the examples
  (doseq [[text reason] examples]
    (assert-sheep-bleat text reason)))
```

--------------#nUnit-oracle

# C# parameterized test (with an Oracle)


```csharp
    [TestFixture] public class B_Parameterized_Test_With_Oracle {
        [TestCaseSource(nameof(SheepishExamples))]
        public void SheepBleatDetection(SheepishTestCase testCase) =>
            Assert.AreEqual(
                Regex.IsMatch(testCase.Text, @"^baa+$"),
                Sheepish.IsSheepBleat(testCase.Text), testCase.Reason);

        static IEnumerable<SheepishTestCase> SheepishExamples => new[] {
                new SheepishTestCase("b",         "Too short"),
                new SheepishTestCase("ba",        "Too short"),
                new SheepishTestCase("baa",       "2+ 'a' s"),
                new SheepishTestCase("baaa",      "2+ 'a' s"),
                new SheepishTestCase("baaad",     "invalid ' d'"),
                new SheepishTestCase(" baa",      "leading space"),
                new SheepishTestCase("baa " ,     "trailing space"),
            };

        public class SheepishTestCase {
            public SheepishTestCase(string text, string reason) {
                Text = text;
                Reason = reason;
            }
            public string Text;
            public string Reason;
            public override string ToString() => Text;  // nUnit requires a unique ToString() for a name.
        }
    }
```

---------#Clojure-properties

#### Parameterized Test (with properties, without an oracle)

```speaker-note
Let's imagine we don't have an oracle to give us the right answers, but we do know some things that should always be true.  We know sheepish always begins with `b`, for example, which is easy to test.  We know it needs at least 2 `a`s (and since it starts with `b`, the length must be at least 3).  Let's look at a few such examples from the Sheepish specification.
```

```clojure
(defn sheep-bleat?
  [s]
  true) ; obvious bug here!

(defn assert-sheep-bleat
  [text reason]
  (testing (str `("sheep-bleat?" ~text) ": " reason)
    (when (< (count text) 3)
      (is (false? (sheep-bleat? text))
        "Bleat is always at least 3 characters."))
    (when (sheep-bleat? text)
      (is (string/starts-with? text "b")
        "Bleat always starts with `b`")
      (is (string/ends-with? text "aa")
        "Bleat always ends with `aa`")
      (is (= #{\a \b} (into #{} text))
        "Bleat only contains `a` and `b` characters"))
    (when-not (= #{\a \b} (into #{} text))
      (is (false? (sheep-bleat? text))
        "Bleat only contains `a` and `b` characters"))))
```

```speaker-note
These facts should hold true for any test case applied to the system being tested.  Some only apply in certain circumstances.

**Note:** Since we didn't use an oracle, we may not have covered all the test cases.  In fact, the above tests won't fail for the function that always returns `false`!  The tests fail for the `(constantly true)` function, but not the `(constantly false)` function.

This is one reason I recommend starting with example-based unit tests before moving on generating additional test cases.  But, just because you supply specific examples and results, that doesn't mean you can't benefit from the property tests above in the parameterized tests.  The properties should hold true for the specific examples and the new, generated examples.
```
---------#When-properties-1.jumbo

# Should I use examples or properties?

---------#When-properties-2

# Should I use examples or properties?

## Why not both?

![Why not both?](https://i.kym-cdn.com/photos/images/newsfeed/000/538/731/0fc.gif)

---------#When-properties-3

# Thought tool: Should I use examples or properties?

## Use examples...

* When you already have enumerated examples as a means of communication
* When the examples are easy to change
    * Count the cost (maintenance)
* When particular test cases have been problematic in the past

## Test with properties...

<style class='before-speaker-note'></style>

* In many cases *if* you already have an algorithm that can create the right answer for a test case, there wouldn't be a reason to write new code.
* Often we don't start out with an algorithm known to work, but stakeholders have identified some things they expected to be true of the system.  If we're lucky, it is nicely packaged in a specification document.
* Sometimes a specification may be incomplete or not internally consistent.
    * There was a specification I was working with recently.  It explicitly forbade the number 1, but 0, 2, 3, 4, etc. were fine.  The problem came when another part of the document was revised such that the number 1 could actually occur, and then the behavior was undefined.
* Sometimes an algorithm believed to work doesn't actually behave the way it is supposed to – the properties don't hold true in some corner cases.  Maybe the algorithm is right and the spec is wrong, or vice versa.  Either way, it is helpful to identify this, and the earlier, the better.
* The oracle may be "expensive" (measured computationally, dollar-cost, network, or some other resource) to use the oracle.
    * Is it a legacy system running on virtual hardware in the cloud?
    * Is it a third-party payment system that will only tell you that the request was issued correctly by issuing the request?

<style type="text/css"></style>

* When there are traits that should hold true for any condition
* When you want to test for exceptions

## Test with an oracle...

* When the answer can be expressed as a value
* When you already have an algorithm or reference specification
    * A rewrite in another language/platform
    * Count the cost (time, money, availability)

---------#Awesome.jumbo

# Random Examples

<style class='before-speaker-note'></style>

* Now that we have a way of testing arbitrary test cases, we can turn to the fun part.
* We use these great concepts together to make them more powerful: parameterized tests, random data, property-based tests.
* The generative testing library/framework can pull these together for us:
    * generate examples for us.
    * Then, it can supply those generated examples to our parameterized tests.


# + Parameterized Tests
# = Awesome
