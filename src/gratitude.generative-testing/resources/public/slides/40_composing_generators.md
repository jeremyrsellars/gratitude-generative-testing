-----------#Transforming-and-Composing-Generators.jumbo

# Transforming and Composing Generators

<style class='before-speaker-note'></style>

In order generate data useful for complicated applications, we'll need to assemble these generators to do much more powerful tasks.

--------------#Transforming-Generators

# Transforming Generators

<style class='before-speaker-note'></style>

We know sheepish has only `a` and `b` characters, so let's take a list of random choices of these and convert them to a string.  In both examples, we'll build a generator that applies a function to the output of another generator.

1. Gen1: Choose between `a` and `b`.
2. Gen2: Make a list of outputs of Gen1.
    * `.NonEmptyListOf()`, or just `.ListOf()` – Generates a collection of values from a generator.
    * `(s/coll-of some-spec)` – Generates a collection of of values from a generator.
3. Gen3: Apply a function to the output of Gen2 (convert to string).
    * `Gen.Select` – Apply a function to transform generated values.
    * `(gen/fmap your-fn your-gen)` – Apply a function to transform generated values.
4. Take a sample: `["bb" "aaaaaba" "bbbbbaabaaabbbaab" "baaabbab" "abbabaabbbbababbabba" "aaaaabbbbbbbb" "abbaaabababbabb"]`

<style class='before-speaker-note'></style>

With FsCheck, the Gen class offers extension methods, much like LINQ's `.ToList()`, like `.NonEmptyListOf()`, or just `.ListOf()` which return a generator that can generate a list.  It is important to note that this returns a generator, not a list.  `.Sample()` returns a list.  `.ListOf` returns a `Gen<List>`. Also, FsCheck provides its own `Gen.Select` extension method that creates a generator that applies a function to the output of another generator.

```csharp
        static IEnumerable<string> SheepishAlphabetExamples =>
            Gen.OneOf(Gen.Constant('a'),
                      Gen.Constant('b'))    // Choose beetween a and b
                .ListOf()                   // Make a list like [a, b, a]
                .Select(chars => new string(chars.ToArray())) // To string
                .Sample(4, 7);              // take 7 examples
```

<style class='before-speaker-note'></style>

In Clojure, to specify a list of a spec, use `(s/coll-of some-spec)`.  To apply a function to the output of a generator, use `(gen/fmap your-fn your-gen)`.

```clojure
(gen/sample
  (gen/fmap string/join
  	(s/gen (s/coll-of #{"a" "b"})))
  7)
; produces something like:
; ("bb" "aaaaaba" "bbbbbaabaaabbbaab" "baaabbab" "abbabaabbbbababbabba" "aaaaabbbbbbbb" "abbaaabababbabb")
```

---------------#Composing-Generators

# Combining Generators (Trivial example)

```csharp
            Gen.OneOf(Gen.Constant('a'),
                      Gen.Constant('b'))    // Choose beetween a and b
```

<style class='before-speaker-note'></style>

In order to generate composite data for non-trivial applications, we'll need to use more powerful generators.  These are built up from single-responsibility functions provided by the libraries.

---------------#Generators-table

<div class="speaker-note label">Abridge</div>

# Combining Generators

<style class='before-speaker-note'></style>

* Here are some generators for testing the card game of "Hearts," which uses a standard 52-card deck of playing cards.
* Some of the generators listed below are built from random card generator, `Gen<Card> cardGen` in C# or `(s/gen ::card)`.
* Let's go through this list in order.

<style id='combinators' class='before-alternating-table'></style>

|Description|FsCheck (C#)|Clojure `(s/gen …)`|
|-----------|------------|-------------------|
|alternative generators| `Gen.OneOf(heartGen, queenSpadeGen, cardGen)`| `(s/or :heart ::heart-card`<br>`      :queen-spade ::queen-of-spades`<br>`      :zero-point-card ::card)`|
|exact-length list| `cardGen.ListOf(5)`| `(s/coll-of ::card :count 5)`|
|homogeneous pair tuple| `cardGen.Two()`| `(s/coll-of ::card :count 2)`<br>`(s/coll-of ::card :count 2 :into [])`|
|homogeneous triple tuple| `cardGen.Three()`| `(s/coll-of ::card :count 3)`|
|homogeneous quadruple tuple| `cardGen.Four()`| `(s/coll-of ::card :count 4)`|
|heterogeneous tuple| `Gen.zip(suitGen, rankGen)`|`(s/cat :suit ::suit, :rank ::rank)`<br>`(gen/tuple (s/gen int?) (s/gen string?))`|
|element from list| `Gen.Elements(new Card[]{exampleCard1,exampleCard2})`| `(gen/elements [example-card-1 example-card-2])`|
|size-driven single| `Gen.GrowingElements(new Card[]{exampleCard1,exampleCard2})`| |
|size-driven list| `cardGen.ListOf()`| `(s/coll-of ::card)`<br>`(s/* ::card)`|
|size-driven list, non-empty| `cardGen.NonEmptyListOf(size)`| `(s/coll-of ::card :min-count 1)`<br>`(s/+ ::card)`|
|constant| `Gen.Constant(queenOfSpades)`| `#{some-value}`|
|satisfying constraint| `cardGen.Where(c => c.Suit == Suit.Hearts)`| `(s/and ::card is-heart?)`|
|satisfying constraint<br>(without throwing)| `cardGen.TryWhere(c => c.Suit == Suit.Hearts`<br>`                  && c.Suit == Suit.Clubs)`<br>`// impossible or improbable`| `Use a custom generator`|
|random permutations| `Gen.Shuffle(new Card[]{exampleCard1,exampleCard2})`| `(gen/shuffle xs)`|

<style class='before-speaker-note'></style>

This list is a good place to start along the path of learning to transforming and combining generators.  Further description of the above, from the FsCheck perspective, can be found in [FsCheck Test Data: Useful-Generator-Combinators](https://fscheck.github.io/FsCheck/TestData.html#Useful-Generator-Combinators).  The [Test.Check Cheat Sheet](https://github.com/clojure/test.check/blob/master/doc/cheatsheet.md) is also helpful.


--------#Alternative_generators.jumbo-left

# Alternative generators

```csharp
Gen<Card> gen = Gen.OneOf(heartGen, queenSpadeGen, cardGen)
```

```clojure
;; clojure.spec
(s/or     :heart               ::heart-card
          :queen-spade         ::queen-of-spades
          :zero-point-card     ::card)
; note:   ^ data-label         ^ spec              ; Data label is useful for conformance

;; test.check
(gen/one-of [(s/gen ::heart-card)
             (s/gen ::queen-of-spades)
             (s/gen ::card)])
```

A little more about Clojure.spec's conformance for re-labeling data from the [spec guide](https://clojure.org/guides/spec#_composing_predicates).

--------#Exact-length_list.jumbo-left

# Exact-length list

```csharp
Gen<IList<Card>> gen = cardGen.ListOf(5)   ; List of 5 cards
```

```clojure
(s/coll-of ::card :count 5)                ; vector of 5 cards
```

--------#Homogeneous_pair_tuple.jumbo-left

<div class="speaker-note label">Omit</div>

# Homogeneous tuple

```csharp
Gen<Tuple<Card,Card>>            gen2 = cardGen.Two()
Gen<Tuple<Card,Card,Card>>       gen3 = cardGen.Three()
Gen<Tuple<Card,Card,Card,Card>>  gen4 = cardGen.Four()
```

```clojure
(s/coll-of ::card :count 2)      ; vector of 2 cards
(s/coll-of ::card :count 3)      ; vector of 3 cards
(s/coll-of ::card :count 4)      ; vector of 4 cards
```

--------#Heterogeneous_tuple.jumbo-left

# Heterogeneous tuple

```csharp
Gen<Tuple<Suit,Rank>> gen = Gen.zip(suitGen, rankGen)
Gen<Tuple<float,float>> gen = Gen.zip(heightGen, weightGen)
```

```clojure
(gen/tuple (s/gen ::suit) (s/gen ::rank))    ; => [:spade  :ten]
(gen/tuple (s/gen int?)   (s/gen string?))   ; => [1       "ab"]
              /--- specs ---\
             v               v
(s/cat :suit ::suit,   :rank ::rank)         ; => [:spade  :ten]
       ^- data labels -^                     ; Data label is useful for conformance
;; conformance demo
(s/def ::card-tuple
  (s/cat :suit ::suit, :rank ::rank))        ; yields [:hearts :ten]
;; Reattach the data labels with `conform`
(s/conform ::card-tuple [:hearts :ten])      ; yields {:suit :hearts, :rank :ten}
```

A little more about Clojure.spec's conformance for re-labeling data from the [spec guide](https://clojure.org/guides/spec#_composing_predicates).

--------#Element_from_list.jumbo-left

# Element from list

```csharp
Gen<Card> gen = Gen.Elements(new Card[]{exampleCard1, exampleCard2})
```

```clojure
(gen/elements [example-card-1 example-card-2])
```

--------#Size-driven_single.jumbo-left

<div class="speaker-note label">Omit</div>

# Size-driven single

```csharp
Gen<Card> gen = Gen.GrowingElements(new Card[]{exampleCard1, exampleCard2})
```

--------#Size-driven_list.jumbo-left

# Size-driven list

```csharp
Gen<IList<Card>> gen = cardGen.ListOf()
```

```clojure
(s/coll-of ::card)
(s/* ::card)
```

--------#Size-driven_list_non-empty.jumbo-left

<div class="speaker-note label">Abridge</div>

# Size-driven list, non-empty

```csharp
Gen<IList<Card>> gen = cardGen.NonEmptyListOf(size)
```

```clojure
(s/coll-of ::card :min-count 1)
(s/+ ::card)
```

--------#Constant.jumbo-left

<div class="speaker-note label">Omit</div>

# Constant

```csharp
Gen<Card> gen = Gen.Constant(queenOfSpades)
```

```clojure
#{:some-value}
(gen/return :some-value)
```

--------#Satisfying_constraint.jumbo-left

# Satisfying constraint (Such-that)

```csharp
cardGen.Where(c => c.Suit == Suit.Hearts)
cardGen.TryWhere(c => c.Suit == Suit.Hearts
                   && c.Suit == Suit.Clubs)    // impossible or improbable, but don't throw
```

```clojure
(s/and ::card is-heart?)
(s/and ::card is-heart? is-club?) ; will throw
;; To avoid exceptions in Use a custom generator that *will* satisfy the constraint.
```

Note: Use a custom generator that *will* satisfy the constraint so it will be fast.

--------#Random_permutations.jumbo-left

<div class="speaker-note label">Omit</div>

# Random permutations

```csharp
Gen.Shuffle(new Card[]{exampleCard1, exampleCard2})
```

```clojure
(gen/shuffle xs)
```

-------#List-types

<div class="speaker-note label">Abridge</div>

# List types

<style class='before-speaker-note'></style>

* You may note several similarities and differences between the .Net and Clojure versions.
* One difference is around the types of list returned from different generators.
* The FsCheck functions sometimes use .Net's `System.Tuple` type for short, fixed-size lists, which provide **constant time access**. It uses `IList<T>` for variable-length & longer lists.  The type of the data structure is encoded into the type signature of the extension methods like `ListOf`, `Two`, `Three`, `Four`.
* In contrast, Clojure specs may specify the type of container, with different characteristics (like vectors, lists, and sets).  Clojure usually mimics Tuples with vectors, which **also offer constant time access in these use cases.**  In the first clojure poker example, a vector is generated: `(s/coll-of int? :into [])`.  In fact, the default collection type for `s/coll-of` is a vector, so `:into []` is optional.  But, it could just as easily be generated as a sorted-set with `:into (sorted-set)`.  In FsCheck, this would require two steps: generating a collection, then transforming the result type with `.Select`, as in `intGen.ListOf(5).Select(lst => new SortedSet<int>(lst))`.  For more, optional parameters of [`s/coll-of`](https://clojure.github.io/spec.alpha/clojure.spec.alpha-api.html#clojure.spec.alpha/coll-of), see [`s/every`](https://clojure.github.io/spec.alpha/clojure.spec.alpha-api.html#clojure.spec.alpha/every) for a detailed description of the options for how to customize collection-data generation.

```csharp
Gen<IList<Card>>      pokerHandGen     = cardGen.ListOf(5);    // IList`T
Gen<Tuple<Card,Card>> blackjackHandGen = cardGen.Two();        // Tuple<T1,T2>
```

```clojure
(let [^Gen poker-hand-gen    (s/gen (s/coll-of ::card, :count 5, :into []))    ; vector
      ^Gen another-poker-gen (s/gen (s/coll-of ::card, :count 5))              ; also vector
      ^Gen blackack-hand-gen (s/gen (s/coll-of ::card, :count 2, :into #{}))]  ; hash set
  ...)
```

# Custom List types

```csharp
intGen.ListOf(5).Select(lst => new SortedSet<int>(lst))
```

```clojure
(s/coll-of int?, :count 5, :into (sorted-set))
```

-------#Labeled-data

<div class="speaker-note label">Abridge</div>

# Anonymous vs. Labeled data

<style class='before-speaker-note'></style>

* Another difference is in whether or not list contents are labeled.
* To generate a random value from 2 generators, FsCheck's `Gen.OneOf` takes 2 parameters, while Clojure spec `s/or` takes 4 – twice as many parameters.  This is also true of homogeneous tuples with `s/cat`.  Both produce similar unlabeled data, but in Clojure the values can be run 'backwards' through a spec to produce labeled data.  This is called conformance.
* The Clojure "alternative" generator, the card classification example, produces a single card that matches one of the specs. The "extra" parameters provide labels for "conforming" a value, a process similar to destructuring.  This is out of scope for data generation (and this talk), but it can sure come in handy to switch behavior based on which spec matches.
* For the tuple example, a vector is returned containing a value matching each spec in order; the "extra" parameters also provide labels when conforming a value for destructuring or runtime introspection.

### Alternative generators

```csharp
Tuple<Card> gen = Gen.OneOf(heartGen, queenSpadeGen, cardGen)
```

```clojure
(s/def ::card-classification
    (s/or :heart ::heart-card
          :queen-spade ::queen-of-spades
          :zero-point-card ::card))                ; yields a card generator
```

### Heterogeneous tuple

```csharp
Tuple<Suit,Rank> gen = Gen.zip(suitGen, rankGen)
```

```clojure
(s/def ::card-tuple
  (s/cat :suit ::suit, :rank ::rank))              ; yields [:hearts :ten]

(s/conform ::card-tuple [:hearts :ten])            ; yields {:suit :hearts, :rank :ten}
```
-------------#Un-labeled-data

<div class="speaker-note label">Abridge</div>

# Staying safe with un-labeled data

<style class='before-speaker-note'></style>

* Since FsCheck uses `System.Tuple`, which doesn't support "naming" the ordinals, it uses anonymous labels like `tuple.Item1`, `.Item2`, etc. so the semantic meaning of `Item2` may not be obvious in the code.  Often the generic type serves to label the data, but when a tuple is used to model both height and weight with a float, this can be confusing.  If you want to embed the intended use in the type, consider avoiding a primitive like float, and using a custom type.

```csharp
    float CalculateBMI(float height, float weight) // confusing
```

```csharp
    public class Length {
      public static Length FromInches(float inches) => new Length(inches * 2.54);
      public static Length FromCentimeters(int cm) => new Length(cm);
      private Length(float cm)// private constructor saves dimension
    }
```

```csharp
    float CalculateBMI(Length height, Weight weight) // better!
```

```csharp
    var weight = Weight.FromPounds(100);
    var height = Length.FromInches(100);
    var bmi = CalculateBMI(weight, height); // doesn't compile! Yeah!
```

For more about this idea, see my blog: http://jeremyrsellars.github.io/no-new-legacy/posts/2017-08-08-domain-identifiers-instead-of-primitive-obsession/


----------#arbitrary-data-types-and-specs

<div class="speaker-note label">Abridge</div>

# Registering generators and shrinkers for arbitrary data types/specs

```csharp
public class LengthGenerators
{
    public static Arbitrary<T> Arbitrary<Length> arbLen =
        Gen.Choose(0, 1000).Select(s => Length.FromInches(s)).ToArbitrary();
}
// Then register all the Arbitrary generators with
// Arb.Register<LengthGenerators>();
```

```clojure
(s/def ::rank keyword?)
(s/def ::suit keyword?)
(s/def ::card (s/keys :req-un [::suit ::rank]))
(gen/generate
      (s/gen (s/coll-of ::card :count 2)
        {::card #(gen/return {:rank :ace, :suit :spades})}))  ; provide custom card generator
(gen/generate
      (s/gen (s/coll-of ::card :count 2)
        {::suit #(gen/return :spades)
         ::rank #(gen/return :ace)}))  ; default card generator can use custom rank/suit generators
; both always generate a hand of 2 "ace of spade" cards
[{:suit :spades, :rank :ace}
 {:suit :spades, :rank :ace}]
```

-------------#Card-types

<div class="speaker-note label">Abridge</div>

# Card types in `C#`

<style class='before-speaker-note'></style>

Now, let's build a card generator.  A card will be modeled with a type and enums in C#.

`new Card{Suit=Suit.Diamonds, Rank=Rank.Ten})`

```csharp
    public struct Card
    {
        public Suit Suit;
        public Rank Rank;
    }
    public enum Suit
    {
        Clubs, Hearts, Spades, Diamonds
    }
    public enum Rank
    {
        King, Queen, Jack, Ten, Nine, Eight, Seven, Six, Five, Four, Three, Two, Ace
    }
```

---------#Card-generator-FsCheck

# Card Generator in `C#`

<style class='before-speaker-note'></style>

Generate example cards by defining and combining some generators with some of the functions described above.  `.Elements` chooses from a list of suits or ranks.  `Gen.zip` and `.Select` are used to create a Tuple, and then map a Tuple to a card.

```csharp
    class E_Hearts_Card_Generators
    {
        static Gen<Suit> suitGen =
            Gen.Elements((Suit[])Enum.GetValues(typeof(Suit)));
        static Gen<Rank> rankGen =
            Gen.Elements((Rank[])Enum.GetValues(typeof(Rank)));

        static Gen<Card> heartGen =
            rankGen
            .Select(rank => new Card { Rank = rank, Suit = Suit.Hearts });

        static Gen<Card> cardGen =
            Gen.zip(rankGen, suitGen)
            .Select(c => new Card { Rank = c.Item1, Suit = c.Item2 });

        static IReadOnlyList<Card> CreateRandomCards(int count) =>
            cardGen.Sample(100, count);
    }
```

-----------#Card-generator-clojure

# Card Generator in Clojure

<style class='before-speaker-note'></style>

* In Clojure, we might model a card as map and keywords in Clojure, like `{:suit :diamonds, :rank :ten}`.
* These specs might similarly describe card data.

```clojure
(s/def ::suit #{:clubs :hearts :spades :diamonds})
(s/def ::rank #{:king :queen :jack :ten :nine :eight :seven :six :five :four :three :two :ace})
(s/def ::card (s/keys :req-un [::suit ::rank]))
```

<style class='before-speaker-note'></style>

* Let's see them in action:

```clojure
(defn generate-examples
  "Simple function to generate some example values that satisfy a spec."
  [spec example-count]
  (let [generator (s/gen spec)
        generate-example #(gen/generate generator)
        examples (repeatedly generate-example)]
    (take example-count examples)))

; user=> (generate-examples ::suit 8)   ; Generate 8 suits
(:clubs :diamonds :hearts :clubs :clubs :diamonds :diamonds :hearts)
; user=> (generate-examples ::rank 8)   ; 8 ranks
(:five :nine :six :three :seven :nine :ace :six)
; user=> (generate-examples ::card 3)   ; 3 cards
({:suit :diamonds, :rank :ten}
 {:suit :hearts,   :rank :three}
 {:suit :diamonds, :rank :queen})
```

-----------#Sheepish-gen-1

# Back to the sheep pen

<style class='before-speaker-note'></style>

* Here are some ways we can use the generator-combining and transforming functions to generate more-tricky sheepish and sheepish-like data.


```clojure
(ns sheepish.f-better-sheepish-examples
  (:require [clojure.spec.alpha :as s] [clojure.spec.gen.alpha :as gen] [clojure.string :as string]
            [clojure.pprint :as pprint] clojure.test.check.generators))

(defn generate-examples
  [spec example-count]
  (let [generator (s/gen spec)
        generate-example #(gen/generate generator)
        examples (repeatedly generate-example)]
    (take example-count examples)))

;; Let's say we want to decompose the test string into:
;;    preamble + `b` + some intermediate characters
;;    + some `a` characters, + some "after" characters.

(s/def ::usually-empty-string
  (s/with-gen string?
   #(gen/frequency [[9 (gen/return "")]
                    [1 (s/gen string?)]])))
; generates strings like ( "", "", "ON865d3", "", "", "", "", "", "")

(pprint/pprint (generate-examples ::usually-empty-string 40))
```

-----------#Sheepish-gen-2

# Back to the sheep pen (continued)

```clojure
(s/def ::string-of-a
  (s/with-gen string?
    #(gen/fmap
      (fn [n] (string/join (repeat (max n 0) \a)))
      (s/gen (s/int-in -3 4)))))
; generates strings like ("aaa", "", "aa")
(pprint/pprint (generate-examples ::string-of-a 40))

(s/def ::string-of-b
  (s/with-gen string?
    #(gen/fmap
      (fn [n] (string/join (repeat (max n 0) \b)))
      (s/gen (s/int-in 0 3)))))

(pprint/pprint (generate-examples ::string-of-b 40))
```

-----------#Sheepish-gen-3

# Back to the sheep pen (continued)

```clojure
(s/def ::sheepish-like-substrings
  (s/with-gen (s/coll-of string?)
    #(gen/tuple (s/gen ::usually-empty-string)
                (s/gen ::string-of-b)
                (s/gen ::usually-empty-string)
                (s/gen ::string-of-a)
                (s/gen ::usually-empty-string)
                (s/gen ::string-of-a))))
; generates vectors of substrings like
; (["A9hrfNg56618b066y07nb" "bb" "" "" "Q" "aa"]
;  ["" "b" "" "" "" ""] ...)

(pprint/pprint (generate-examples ::sheepish-like-substrings 10))

(s/def ::sheepish-like-string
  (s/with-gen string?
    #(gen/fmap string/join (s/gen ::sheepish-like-substrings))))
; generates strings like ("baaaaa" "bBey1gsp0" "bbCDUmyMzt4Kaaa8l70Eaaa" "b" "bbaa" "baaa" "baaa" "baaaaa" "baaa" "bbaaa")

(pprint/pprint (generate-examples ::sheepish-like-string 10))
```

------------#generator-source

<div class="speaker-note label">Omit</div>

# Source code

* Clojure
    * Hearts: https://github.com/jeremyrsellars/no-new-legacy/blob/master/src/sheepish/test/sheepish/e_hearts_card_generators.cljc
    * Sheepish: https://github.com/jeremyrsellars/no-new-legacy/blob/master/src/sheepish/test/sheepish/f_better_sheepish_examples.cljc
* C#
    * Hearts: https://github.com/jeremyrsellars/no-new-legacy/blob/master/src/Sheepish.net/Sheepish.CSharp/E_Example_Generators.cs
    * Sheepish: https://github.com/jeremyrsellars/no-new-legacy/blob/master/src/Sheepish.net/Sheepish.CSharp/F_Parameterized_Test_With_Better_Generators.cs
