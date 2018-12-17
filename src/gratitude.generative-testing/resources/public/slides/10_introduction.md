# Jeremy Sellars

My name is Jeremy Sellars.  I help businesses by showing how difficult and time-consuming tasks can be made simple, tractable, and cost-effective, then I help implement them with software.

# Gratitude

gratitude (noun)

> A strong feeling of appreciation to someone or something for what the person has done to help you.
[Cambridge Academic Content Dictionary](https://dictionary.cambridge.org/us/dictionary/english/)

Thank you all for coming here and for doing your part to make the world a better place.

My nine-year-old daughter reminded me of Michael Jackson's song "Man in the Mirror".

If you don't know it, the chorus says "I'm starting with the man in the mirror; I'm asking him to change his ways..." and goes on to say "If you want to make the world a better place, take a look at yourself and then make a change."

I think most of us at this conference are here to improve our capabilities in technology skills, design skills, interpersonal skills, etc..

# Gratitude for our Sponsors

This conference wouldn't be possible without our sponsors.

# Introductions

I'd like to get to know each other a bit over the next 50 minutes.  So, a short informal interview which you should feel free to participate in as much or as little as you desire.

# How many of us are in business?

Let us propose for a moment that a business is a group of people oriented around the idea of providing products or services or ideas that clients would value... value enough to voluntarily return "certificates of gratitude" as compensation.  Perhaps these certificates are covered with images of famous people like Benjamin Franklin or perhaps they say "pay to the order of _________________".  I'm getting at the notion that when we engage in business, mutual benefit is a good goal, at least at some level.  Both sides of the transaction get something they want.

# How many of us are employees?

I don't think it is too much of a stretch to propose that we, as employees, provide our employer with products, services, research, labor, TPS reports, or ideas that they value, and that they compensate us for.

# How many of us are in "Information Technology?"

I used to kind of bristle if someone confused "programming" with "Information Technology."  Then I realized that much of programming is collecting facts, and writing an algorithm to interpret the facts to produce a desired result.  Sometimes the end-result is information (as in a database or a report).  Other times it is a systematic action taken on the information produced.

# Do you love data?

Since you all have come to a session about generating data and generative testing, I am going to assume many of you are involved in or are at least interested in pursuits of gathering data, interpreting data, creating data, using data to assess the capabilities of the software and systems that you design and build.

Ok, that's it for the interview. Thanks.

# The "Art" of sculpture

So, it's common knowledge that computers aren't magical devices; most computers do our bidding via CPUs that process binary instructions and data to follow our carefully choreographed script to its desired effect.

> Every block of stone has a statue inside it and it is the task of the sculptor to discover it.
[Michelangelo](https://www.michelangelo.org/michelangelo-quotes.jsp)

### Michael Angelo's statue of David

![David's Face](https://upload.wikimedia.org/wikipedia/commons/thumb/8/86/%27David%27_by_Michelangelo_Fir_JBU013.jpg/400px-%27David%27_by_Michelangelo_Fir_JBU013.jpg)


# The "art" of programming

Programming is a lot like that. ;-)

> Every CD-ROM has program inside it and it is the task of the programmer to discover it.
[Jeremy Sellars](https://jeremy.sellars.us)

I think that's why we derive so much pleasure as we watch the CD-ROM spinning on the lathe in the work station and carefully take a chisel in hand and move it slowly to the correct part of the surface of the disk and tap out the beautiful stream of the music in our minds.

# The media of programs

Of course this isn't how it works.  We actually use ~~carefully arranged vacuum tubes or transistors~~, ~~punch cards~~, symbolic instructions, assembly language, C-like languages to provide precise control over the hardware, Java-like languages to provide an abstraction over the hardware and a simpler memory model, SQL-like languages to provide declarative access to a simplified model of the data, etc..

Generally, each of these technologies "stand on the shoulders of giants" and endeavor to model the desired computation or algorithm in a way that is less specific about the hardware or the representation of bits, and more specific about the information of the system being modeled.

# The "art" of abstraction

> Being abstract is something profoundly different from being vague... The purpose of abstraction is not to be vague, but to create a new semantic level in which one can be absolutely precise.
[Edsger W. Dijkstra](http://wiki.c2.com/?EwDijkstraQuotes)

# The Single Level of Abstraction Principle

ab·stract (adjective)

> Existing in thought or as an idea but not having a physical or concrete existence
[Google](https://www.google.com/search?q=abstract&rlz=1C1GCEU_enUS826US826&oq=abstract&aqs=chrome..69i57j69i60j69i59j0l3.1199j1j1&sourceid=chrome&ie=UTF-8)

ab·stract (adjective)

> Thought of apart from concrete realities, specific objects, or actual instances
[Dictionary.com](https://www.dictionary.com/browse/abstract)

Appropriate use of abstraction – appropriate detail for the semantic level of communication – is a big part of having clean design and clean code.  If you follow Uncle Bob Martin for long, you'll have heard about keeping the high-level code with the high-level, and the specific details "further down".  Some call it "[The Single Level of Abstraction Principle](https://markhneedham.com/blog/2009/06/12/coding-single-level-of-abstraction-principle/)".  In languages that permit this, it can even resemble a newspaper where the editor can cut the paper at any point, so the writer puts the most important part of the code first.  This idea resembles progressive disclosure.

This idea of semantic level will also be helpful when understanding how to effectively test software.

# Craftsmanship Manifesto

<a href="#!/gratitude.callout.software_craftsmanship/Software_Craftsmanship_Manifesto" target="manifesto">Open slide</a>
<a href="http://manifesto.softwarecraftsmanship.org" target="manifesto">Open manifesto.softwarecraftsmanship.org</a>

* "Well-crafted software"
    * Open to extension
* "Steadily adding value"
    * Predictable timelines for update features
* "A community of professionals"
    * Here we are at a conference
* "Productive partnerships"
    * Providing examples for a spec

It is rare that a topic is smack dab in the middle of the Venn diagram of so many of my favorite topics: software quality, functional programming, and extensibility.  The topic of generative testing hits all three!  I am excited to bring this to your attention and shed light on it from my point of view.

Software quality, from the [Manifesto for Software Craftsmanship](http://manifesto.softwarecraftsmanship.org/) emphasizes 4 values, and I see each of them touched on in these topics surrounding generative testing:

* "Well-crafted software"
    * Yes, software needs to function correctly, and testing can help demonstrate this, but well-crafted software is open to extension, especially line-of-business software.  Consider the scenario that stakeholders ask for a new version of software with an additional data field to be displayed.  It probably shouldn't require a complete rewrite of the app, right?  As an ideal, software should be extendable.  Also, a team's prior investment in tests shouldn't prevent or delay adding this feature.  In other words, in well-crafted software, both the product and the tests need to be extensible.
* "Steadily adding value"
    * Similar to the previous point, if testing the updated feature (just some new fields, right?) requires a rewrite of the tests, that change to the tests will probably take longer than the feature itself.  (I don't know about you, but that mucks up my estimates).  Generative testing supplements traditional example-based unit tests by making its own examples; a new field and generator can be declared once and then the generator takes over to "fill in" the new field.
* "A community of professionals"
    * With your help, this topic meets this point, too.  You, the reader, are participating and are hopefully benefitting from this community of professionals.  I am excited to see if this content adds to your excitement about these topics and helps you accomplish your goals.  Feel free to let me know on twitter, etc..
* "Productive partnerships"
    * One of the great helps from all things "generative" is that generative models can build examples.  Sheep say "Baa!", right?  Or is it "Baaaaaa"?  Consider a regular expression that specifies what "sheep-bleat" looks like in text: `baa+`.  Does "baaaaa" match the sheep-bleet specification? yes.  Howabout "bowow"? nope.  This ability to evaluate content against a specification is useful (in a reductive sense), but what if you had a way to derive many mega-bleats (I mean megabytes) of example text?  That could help you talk with the stakeholders and show examples to make sure everyone is bleating the same language.  As the saying goes, a picture is worth a thousand words.  Think about software requirements/specification documents.  Rather than go back and forth with stakeholders about some line of text in a spec doc, it may be far more productive to "show" examples that are possible based on a translation of the document into a generative specification.

I don't mean to say that craftsmanship demands using generative testing.  I am suggesting that this is another useful tool for your workshop.

Let's get started!

