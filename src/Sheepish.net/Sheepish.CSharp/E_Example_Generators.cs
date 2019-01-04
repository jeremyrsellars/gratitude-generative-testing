using System;
using System.Collections.Generic;
using System.Linq;
using FsCheck;
using NUnit.Framework;

namespace Sheepish.CSharp
{
    [TestFixture]
    [TestFixtureSource(nameof(ExampleCards))]
    public class E_Hearts_Card_Generators
    {
        public E_Hearts_Card_Generators(Card card) => this.card = card;

        Card card;

        [Test]
        public void ToStringContainsSuite() =>
            StringAssert.Contains(card.Suit.ToString(), card.ToString());

        [Test]
        public void ToStringContainsRank() =>
            StringAssert.Contains(card.Rank.ToString(), card.ToString());

        static IEnumerable<Card> ExampleCards => CreateRandomCards(5);

        void Examples()
        {
            int size = 12;
            Card exampleCard1 = new Card();
            Card exampleCard2 = new Card { Rank = Rank.Two };
            Gen<Card> queenSpadeGen = Gen.Constant(Cards.QueenOfSpades);
            Gen<Card> trickyCardGen = Gen.OneOf(heartGen, queenSpadeGen, cardGen);
            Gen<IList<Card>> handGen = cardGen.ListOf(5);
            Gen<Tuple<Card, Card>> twoGen = cardGen.Two();
            Gen<Tuple<Card, Card, Card>> threeGen = cardGen.Three();
            Gen<Tuple<Card, Card, Card, Card>> fourGen = cardGen.Four();
            Gen<Card> exampleCardGen = Gen.Elements(new Card[] { exampleCard1, exampleCard2 });
            Gen<Card> exampleCardGen2 = Gen.GrowingElements(new Card[] { exampleCard1, exampleCard2 });
            Gen<IList<Card>> cardsGen = cardGen.ListOf(size);
            Gen<IList<Card>> wildGen = cardGen.NonEmptyListOf();
            Gen<Card> heartFilterGen = cardGen.Where(c => c.Suit == Suit.Hearts);
            Gen<Card> twoSuitedCardGen = cardGen.Where(c => c.Suit == Suit.Hearts && c.Suit == Suit.Clubs); // impossible or improbable
            Gen<Card[]> shuffledExampleCards = Gen.Shuffle(new Card[] { exampleCard1, exampleCard2 });
        }

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

        //public class CardGenerators {
        //    public static Arbitrary<Suit> Suit() => Arb.From(suitGen);
        //    public static Arbitrary<Rank> Rank() => Arb.From(rankGen);
        //    public static Arbitrary<Card> Card() => Arb.From(cardGen);
        //}

        //// This can be registered for FsCheck with:
        //static Array registrations = new[] { Arb.Register<CardGenerators>() };
    }

    public struct Card
    {
        public Suit Suit;
        public Rank Rank;
        public override string ToString() => $"{Rank} of {Suit}";
    }
    public static class Cards
    {
        public static readonly Card QueenOfSpades = new Card { Rank = Rank.Queen, Suit = Suit.Spades };
    }
    public enum Suit
    {
        Clubs, Hearts, Spades, Diamonds
    }
    public enum Rank
    {
        King, Queen, Jack, Ten, Nine, Eight, Seven, Six, Five, Four, Three, Two, Ace
    }
}
