# Project 3: Markov Part 2, Spring 2022

This is the directions document for Project P2 Markov Part 1 in CompSci 201 at Duke University, Spring 2022. Please follow the directions carefully while you complete the project. Please refer to the directions at https://coursework.cs.duke.edu/201spring22/p3-markov-part-2 rather than any forks or local copies in the event that any changes are made to the document.


## Outline

- [Introduction](#introduction)
- [Running and Understanding the Starter Code](#running-and-understanding-the-starter-code)
- [Developing and Testing EfficientMarkov](#developing-and-testing-efficientmarkov)
- [Developing and Testing EfficientWordMarkov](#developing-and-testing-efficientwordmarkov)
- [Benchmarking and Analysis](#benchmarking-and-analysis)
- [Submitting, Reflect, Grading](#submitting-reflect-grading)
- [Appendix](#appendix)


## Introduction

This is the second part of [Project P2, Markov Part 1](https://coursework.cs.duke.edu/201spring22/p2-markov-part-1). In part 1, you developed a `WordGram` class to represent an immutable sequence of a given number of words (2-grams, 3-grams, etc.). In part 2 (this part) you will use a generative model to create realistic looking text in a data-driven algorithmic way using a **Markov Model**. The first version of this generative model you develop uses Strings of characters as its basic object; the second version uses `WordGram`s as its basic object. You do **not** need to use your own `WordGram` implementation for this part, we have provided a complete implementation in the starter code for this project.

The starter code includes correct but inefficient implementations of Markov Models for both Strings of characters (`BaseMarkov`) and `WordGram`s (`BaseWordMarkov`). Unlike previous projects, your goal here is not just to write code that works, but to `extend` these models and make them more efficient using a `HashMap` data structure. You will implement `EfficientMarkov` (an outline of which is included in the starter code) and `EfficientWordMarkov` (which you will create on your own), verify that they work the same as the inefficient `BaseMarkov` and `BaseWordMarkov`, and analyze the performance using a benchmarking program.


### What is a Markov Model?

An order-k Markov model uses strings of k characters to predict text: we call these *k-grams*. An order-2 Markov model uses two-character strings or *bigrams* to calculate probabilities in generating random letters. A string called the *training text* is used to calculate these probabilities. We walk through an example calculating the probabilities in the expandable section below. One can also use k-grams that are composed of words rather than letters. We use `WordGram` objects to represent these, but the logic is the same as that shown in the expandable section below, just for words instead of characters.

<details>
<summary>Expand for example calculation of probabilities</summary>

For example, suppose that in the text we're using for generating random letters, the so-called training text, using an order-2 Markov model, the bigram `"th"` is followed 50 times by the letter `"e"`, 20 times by the letter `"a"`, and 30 times by the letter `"o"`, because the sequences `"the"`, `"tha"`, and `"tho"` occur 50, 20, and 30 times, respectively while there are no other occurrences of `"th"` in the text we're modeling. This suggests that random text that *looks similar to the training text* should most often have an `e` after `th`, and should have an `a` or `o` following with somewhat lower frequency.

Concretely, while generating random text using an order-2 Markov process suppose we generate the bigram `"th"` --- then based on this bigram we must generate the next random character using the order-2 model. The next letter will be an 'e' with a probability of 0.5 (50/100); will be an 'a' with probability 0.2 (20/100); and will be an 'o' with probability 0.3 (30/100). If 'e' is chosen, then the next bigram used to calculate random letters will be `"he"` since the last part of the old bigram is combined with the new letter to create the next bigram used in the Markov process.

Rather than using probabilities explicitly, your code will use them implicitly. You'll store 50 occurrences of `"e"`, 20 occurrences of `"a"` and 30 occurrences of `"o"` in an `ArrayList`. You'll then choose one of these at random. This will replicate the probabilities, e.g., of 0.3 for `"o"` since there will be 30 `"o"` strings in the 100-element `ArrayList`.

</details>

### Starter Code and Using Git
You must have installed all software (Java, Git, VS Code) before you can complete the project.You can find the [directions for installation here](https://coursework.cs.duke.edu/201-public-documentation/resources-201/-/blob/main/installingSoftware.md).

We'll be using Git and the installation of GitLab at [coursework.cs.duke.edu](https://coursework.cs.duke.edu). All code for classwork will be kept here. Git is software used for version control, and GitLab is an online repository to store code in the cloud using Git.

**[This document details the workflow](https://coursework.cs.duke.edu/201-public-documentation/resources-201/-/blob/main/projectWorkflow.md) for downloading the starter code for the project, updating your code on coursework using Git, and ultimately submitting to Gradescope for autograding.** We recommend that you read and follow the directions carefully while working on a project! While coding, we recommend that you periodically (perhaps when completing a method or small section) push your changes as explained in Section 5.


## Running and Understanding the Starter Code

All Markov Models in this project must implement the `MarkovInterface` provided. The starter code provides two correct but inefficient implementations: `BaseMarkov` and `BaseWordMarkov`. `BaseMarkov` provides a correct but inefficient implementation of the generative Markov model for creating text using k-grams, and `MarkovDriver` uses such a model trained on some data to print text that is random but looks similar to the data on which the model was trained.  

The starter code also provides `MarkovDriver` which includes a psvm method for generating text using one of the models. You should be able to run the psvm method in `MarkovDriver` immediately. By default, the starter code uses `BaseMarkov` to create random text using k-characters at a time to predict text based on President Biden's 2021 State of the Union Address. You should see five different output examples of 144 characters each, generated with k ranging from 1 to 5. Expand below to see the expected output. Note that you should see the same text, but the time at the end will be different on your particular machine.

<details>
<summary>Expand here for example default output</summary>

```
1 markov model with 144 chars
----------------------------------
r bus io vear mans anovine se futinybereagicorthences – min pl 
anginsethec. lid Imint a me e estoreon We alel the tho Wed orreshilthe- 
lrien tha 
----------------------------------
2 markov model with 144 chars
----------------------------------
com of rovestin thater the meris to becaust Chilly th. It’s itiand 
Amerear precouleassarms on Just it usell cour con 9/11.3 muld 
st an ton. Stax 
----------------------------------
3 markov model with 144 chars
----------------------------------
irst on pock of their a Medica’s demong Right ten andame failesses 
at I know – it we cans by bet why time to said by more imple. 
We ass the wron 
----------------------------------
4 markov model with 144 chars
----------------------------------
 life can will crising the faucet again. The world. Talk away. 
So how right — and get by negotiating than 400,000 pharmacism 
to be big tax brack 
----------------------------------
5 markov model with 144 chars
----------------------------------
permanent study shot an option during the productive diseases 
opens without our high-speed international to Americans can be 
first 100 Days of j 
----------------------------------
total time = 0.054
```

</details>

You can also modify the psvm method of `MarkovDriver` to use `BaseWordMarkov` (also provided to you in the starter code) with `WordGram` objects. To do so, simply uncomment the line of code `MarkovInterface<WordGram> wmm = new BaseWordMarkov();` and pass the `wmm` object to the call to the static `markovGenerate()` method rather than `standard`. Still using President Biden's 2021 State of the Union Address as the training text, you should see the output available in the expanded section below.

<details>
<summary>Expand here for example output using `WordGram`s</summary>

```
1 markov model with 807 chars
----------------------------------
the 21st Century. We cannot walk away from their net worth increase 
by man—made and save lives. And we will long endure is familiar, 
this podium, and I know the American households. We’ve done nothing 
– Democrats and billionaires who knows what it’s in that have 
to leave. Our grids are so their income tax loopholes and it 
will crack down on how do not flinch. At the extraordinary times 
as we save lives. And all the 21st Century. That’s estimated 
to their fair share – other countries beset by man—made and stern 
deterrence. And I’m introducing the future for a college degree. 
The American valor and be guided by proving that we can’t do 
with Congress should be done. Now, if Congress and the vicious 
hate crimes we’ve always been a child who have to deliver. We’re 
marshalling every shot dead. 250 shot 
----------------------------------
2 markov model with 807 chars
----------------------------------
survive. It did. But the struggle is far from over. The question 
of whether our democracy since the Great Depression. The worst 
economic crisis since the Great Depression. The worst pandemic 
in a direct and proportionate way to Russia’s interference in 
our mutual interests. As we gather here tonight, the images of 
the loopholes that allow Americans who still don’t have it. This 
will help millions of good paying jobs – jobs Americans can raise 
their families on. And all the crises of our power. And in my 
first day in office. And I need not tell anyone this, but gun 
violence is an epidemic in America. 100 days ago, America’s house 
was on fire. We had to act. We have stared into an abyss of insurrection 
and autocracy — of pandemic and pain — and revitalizing our democracy. 
And winning the future for 
----------------------------------
3 markov model with 806 chars
----------------------------------
we need to ensure greater equity and opportunity for women. Let’s 
get the Paycheck Fairness Act to my desk as soon as possible. 
I also hope Congress can get to my desk the Equality Act to protect 
Asian Americans and Pacific Islanders from the vicious hate crimes 
we’ve seen this past year – and for too long. I urge the House 
to do the same and send that legislation to my desk for equal 
pay. It’s long past time. Finally, the American Jobs Plan do 
not require a college degree. 75% do not require an associate’s 
degree. The American Jobs Plan creates jobs replacing 100% of
the nation’s lead pipes and service lines so every American, 
so every child – can turn on the faucet and be certain to drink 
clean water. It creates jobs to upgrade our transportation infrastructure. 
Jobs modernizing roads, bridges 
----------------------------------
4 markov model with 809 chars
----------------------------------
our tables. Immigrants have done so much for America during the 
pandemic – as they have throughout our history. The country supports 
immigration reform. Congress should act. We have a giant opportunity 
to bend to the arc of the moral universe toward justice. Real 
justice. And with the plans I outlined tonight, we have a real 
chance to root out systemic racism in our criminal justice system. 
And to enact police reform in George Floyd’s name that passed 
the House already. I know the Republicans have their own ideas 
and are engaged in productive discussions with Democrats. We 
need to work together to find a consensus. Let’s get it done 
this year. This is all about a simple premise: Health care should 
be a right, not a privilege in America. So how do we pay for 
my Jobs and Family Plans? I’ve made clear 
----------------------------------
5 markov model with 837 chars
----------------------------------
soul of America – we need to protect the sacred right to vote. 
More people voted in the last presidential election than ever 
before in our history – in the middle of one of the worst pandemics 
ever. That should be celebrated. Instead it’s being attacked. 
Congress should pass H.R. 1 and the John Lewis Voting Rights 
Act and send them to my desk right away. The country supports 
it. Congress should act. As we gather here tonight, the images 
of a violent mob assaulting this Capitol—desecrating our democracy—remain 
vivid in our minds. Lives were put at risk. Lives were lost. 
Extraordinary courage was summoned. The insurrection was an existential 
crisis—a test of whether our democracy could survive. It did. 
But the struggle is far from over. The question of whether our 
democracy will long endure is both ancient and urgent. As old 
as 
----------------------------------
total time = 0.080

```

</details>

You can also change the data source used for training the model. To do so, you just need to change the line `String filename = "data/biden-2021.txt";` inside the psvm method of `MarkovDriver` to use `data/X` where `X` is any of the file names inside of the `data` folder. For example, using `data/alice.txt` (the text of the book Alice in Wonderland) and `BaseWordMarkov` as described above should generate the following output.

<details>
<summary>Expand for example output using alice.txt</summary>

```
1 markov model with 785 chars
----------------------------------
a trademark license fee to the Lizard as you to her going, though 
she had gone. `Well! WHAT things?' said the other: the little 
chin in that you like,' said the party that SOMEBODY ought to 
do not, would feel it gloomily: then I'll take this as well enough; 
don't think,' Alice went up like then?' And they met in a little 
door that were perfectly quiet thing,' Alice said; but all sorts 
of what am now? That'll be two and in her head, and other equivalent 
proprietary form, including legal rights. INDEMNITY You MUST 
have this be jury," Said the real nose; also provide on the Knave 
was quite pleased so shiny?' Alice looked down and you got settled 
down `important,' and found herself `Suppose we had struck her 
head!' the end.' `If you're so she heard him two, it `arrum.') 
`An arm, 
----------------------------------
2 markov model with 744 chars
----------------------------------
Alice; `I daresay it's a set of verses.' `Are they in the distance, 
and she swam about, trying to touch her. `Poor little thing!' 
said Alice, `a great girl like you,' (she might well say this), 
`to go on with the Dutchess, it had made. `He took me for a few 
minutes to see a little worried. `Just about as it turned a corner, 
`Oh my ears and whiskers, how late it's getting!' She was close 
behind it was growing, and very neatly and simply arranged; the 
only one who had got its head to keep back the wandering hair 
that curled all over with fright. `Oh, I know!' exclaimed Alice, 
who always took a minute or two, and the little door: but, alas! 
either the locks were too large, or the key was too much frightened 
that she had to leave off this 
----------------------------------
3 markov model with 754 chars
----------------------------------
some `unimportant.' Alice could see it trying in a helpless sort 
of way to fly up into a tree. By the time she went on planning 
to herself how she would keep, through all her riper years, the 
simple and loving heart of her childhood: and how she would gather 
about her other little children, and make THEIR eyes bright and 
eager with many a strange tale, perhaps even with the dream of 
Wonderland of long ago: and how she would gather about her other 
little children, and make THEIR eyes bright and eager with many 
a strange tale, perhaps even with the dream of Wonderland of 
long ago: and how she would feel with all their simple sorrows, 
and find a pleasure in all their simple joys, remembering her 
own child-life, and the happy summer days. THE END  
----------------------------------
4 markov model with 827 chars
----------------------------------
of settling all difficulties, great or small. `Off with his head!' 
or `Off with her head!' Those whom she sentenced were taken into 
custody by the soldiers, who of course had to leave off being 
arches to do this, so that by the end of the bill, "French, music, 
AND WASHING--extra."' `You couldn't have wanted it much,' said 
Alice; `living at the bottom of a well?' The Dormouse again took 
a minute or two to think about it, and then said, `It was a treacle-well.' 
`There's no such thing!' Alice was beginning very angrily, but 
the Hatter and the March Hare and his friends shared their never-ending 
meal, and the shrill voice of the Queen ordering off her unfortunate 
guests to execution--once more the pig-baby was sneezing on the 
Duchess's knee, while plates and dishes crashed around it--once 
more the shriek of the Gryphon, 
----------------------------------
5 markov model with 727 chars
----------------------------------
to ME,' said Alice hastily; `but I'm not looking for eggs, as 
it happens; and if I was, I shouldn't want YOURS: I don't like 
them raw.' `Well, be off, then!' said the Pigeon in a tone of 
the deepest contempt. `I've seen a good many little girls in 
my time, but never ONE with such a neck as that! No, no! You're 
a serpent; and there's no use denying it. I suppose you'll be
telling me next that you never tasted an egg!' `I HAVE tasted 
eggs, certainly,' said Alice, who was a very truthful child; 
`but little girls eat eggs quite as much as serpents do, you 
know.' `I don't believe it,' said the Pigeon; `but if they do, 
why then they're a kind of serpent, that's all I can say.' This 
was such a new idea to Alice, that she was 
----------------------------------
total time = 0.269

```
</details>


### BaseMarkov

`BaseMarkov` provides simple implementations of the methods defined in the interface `MarkovInterface`. Before describing how to create the more efficient version, read the expandable sections below to understand how the `BaseMarkov` versions work.

<details>
<summary>Expand for description of `getRandomText()` in `BaseMarkov`</summary>

The method we want to optimize to be more efficient is method `getRandomText()`. At a high level, the method works as follows (the exact code for `BaseMarkov.getRandomText()` is provided below). 

- Choose a random substring of `k` characters `current` from the text of length *N*:
- Repeat the following sequence of steps (with a `for` loop) up to the desired length *T*:
    - Get a list of all the characters in the training text that follow `current`
    - Choose one of those characters at random
    - If this character is `PSEUDO_EOS`
        - stop generating new text and `break` out of the `for` loop.
    - Otherwise,
        - Take the last *k*-1 characters of `current` and append this character onto the end of `current`.

</details>

<details>
<summary>Expand for example of `getRandomText()` in `BaseMarkov`</summary>

Here is an example of how the algorithm works. Suppose we're using an order 3-gram (i.e., k=3) and the training text for generating characters is 

***"then five of these themes were themes were thematic in my theatre"***

Then the algorithm would proceed as follows:

- Choose a 3-letter substring from the text at random as the starting current 3-gram. This happens in method `BaseMarkov.getRandomText()` before the for-loop.
- Suppose this text referenced in `current` on line 56 is "the". These three characters are the start of the random text generated by the Markov model. In the loop above, the method `.getFollows(current)` is called on line 60. This method returns a list of all the characters that follow `current`, or "the", in the training text. This is `{"n", "s", "m", "m", “m", "a"}` in the text above -- look for each occurrence of "the" and see the character that follows to understand this returned list.
- One of these characters is chosen at random, is appended to `sb` as part of the randomly generated text, and then `current` is changed to drop the first character and add the last character. So if `"m"` is chosen at random (and it's more likely to be since there are two m's) the String `current` becomes `"hem"`.
- The loop iterates again and `"hem"` is passed to `getFollows()` -- the returned list will be `{"e", "e", "a"}`. The process continues until the designated number of random characters has been generated.
- In the example above if the string `"tre"` is chosen as the initial value of `current`, or becomes the value of `current` as text is generated, then there is no character that follows. In this case `getFollows("tre")` would return `{PSEUDO_EOS}` where the character `PSEUDO_EOS` is defined as the empty string. The loop in `getRandomText` breaks when `PSEUDO_EOS` is found -- ***this is an edge case and the designated number of random characters will not be generated.***

</details>

<details>
<summary>Expand for discussion of complexity of `getRandomText()` in `BaseMarkov`</summary>

Generating each random character requires scanning the entire training text to find the following characters when `getFollows` is called. Generating `T` random characters will call `getFollows` `T` times. Each time the entire text is scanned. If the text contains `N` characters, then generating `T` characters from a training text of size `N` is an O(`NT`) operation - meaning that the running time scales with the product of `N` and `T`.
</details>


## Developing and Testing EfficientMarkov

The starter code should already include `EfficientMarkov`. You will note that it `extends BaseMarkov`, meaning that every object of `EfficientMarkov` also has all of the instance variables and methods included in `BaseMarkov`, with the same implementation of those methods by default (this is called **inheritance** in object-oriented programming). To make `EfficientMarkov` more efficient, you will see that the starter code `@Override`s two methods: `setTraining` and `getFollows`. You need to complete the implementation of these two methods as described below.

<details>
<summary>Expand for Details on the constructor for EfficientMarkov</summary>

One constructor has the order of the markov model as a parameter and the other default constructor calls `this(3)` to set the order to three (calling this(3) invokes the other parameterized constructor). The parameterized constructor first calls `super(order)` to initialize inherited state (this call invokes the constructor of the "parent" class) --- then initializes the instance variable `myMap` to a `HashMap`. 

</details>

### Reasoning about Efficiency of `EfficientMarkov`

Calling `BaseMarkov.getFollows` requires looping over the training text of size *N*. In the class `EfficientMarkov`, you'll improve the efficiency of `getFollows` by making it a constant time operation. In order to accomplish this, you will use the `HashMap` instance variable called `myMap` in `getFollows`.

In `EfficientMarkov`, instead of rescanning the entire text of *N* characters each time `getFollows` is called (as in `BaseMarkov`), the `setTraining` method should instead scan through the training text of size *N* once and store each unique k-gram as a key in the instance variable `myMap`, with the characters/single-char strings that follow the k-gram in a list associated as the value of the key. Then, `getFollows(key)` should simply perform a lookup operation in `myMap` to return the list that follows `key`.

This should result in a complexity of O(*N*+*T*) for `EfficientMarkov`: *N* to run `setTraining` once, and then *T* calls to the now constant time `getFollows` method to generate *T* random characters. We call this linear complexity as opposed to the O(*NT*) quadratic complexity of `BaseMarkov`.


### Implementing setTraining

`myText` must be set to the parameter text as the first line in your new `setTraining` implementation. You can do this directly, or by calling `super.setTraining(text)`. Next, the instance variable `myMap` must be cleared, by writing `myMap.clear()`. These steps are included in the starter code for you.

Now implement the rest of the `setTraining` method. The discussion above about the efficiency of `EfficientMarkov` introduces how the method should work, and we also provide a detailed example in the expandable section below. **The example also highlights the PSEUDO_EOS character, an important special case.**

<details>
<summary>Expand for example of EfficientMarkov and PSEUDO_EOS</summary>

**The keys in `myMap` are k-grams in a k-order Markov model**. Suppose we're creating an order-3 Markov Model and the training text is the string `"bbbabbabbbbaba"`. Each different k-gram in the training text will be a key in the map (e.g. `"bbb"`). **The value associated with each k-gram key is a list of single-character strings that follow the key in the training text (e.g. {`"a"`, `"a"`, `"b"`})**.  *Your map will have Strings as keys and each key will have an `ArrayList<String>` as the corresponding value.*

Let’s consider other 3-grams in the training text. The 3-letter string `"bba"` occurs three times, each time followed by `'b'`. The 3-letter string `"bab"` occurs three times, followed twice by `'b'` and once by `'a'`. 

What about the 3-letter string `"aba"`? It only occurs once, and it occurs at the end of the string, and thus is not followed by any characters. So, if our 3-gram is ever `"aba"` we will always end the text with that 3-gram. Suppose instead, there is one instance of `"aba"` followed by a `'b'` and another instance at the end of the text, then if our current 3-gram was `"aba"` we would have a 50% chance of ending the generation of random text early.

To represent this special case in our structure, we say that `"aba"` here is followed by an end-of-string (EOS) character. This isn't a real character, but a special String/character we'll use to indicate that the process of generating text is over.***While generating text, if we randomly choose the end-of-string character to be our next character, then instead of actually adding a character to our text, we simply stop generating new text and return whatever text we currently have.*** For this assignment, to represent an end-of-string character you'll use the static constant `PSEUDO_EOS` – see `MarkovModel.getRandomText` method for how this constant is used when generating random text.

The map will be constructed in the parameterized constructor and keys/values added in this method. To continue with the previous example, suppose we're creating an order-3 Markov Model and the training text is the string `"bbbabbabbbbaba"`.

| Key | List of Values |
| ---- | ------        |
| `"bbb"` | `{"a", "b", "a"}` |
| `"bba"` | `{"b", "b", "b"}` |
| `"bab"` | `{"b", "b", "a"}` |
| `"abb"` | `{"a", "b"}` |
| `"aba"` | `{PSEUDO_EOS}` |


In processing the training text from left-to-right, e.g., in the method `setTraining`, we see the following 3-grams starting with the left-most 3-gram `“bbb”`. Your code will need to generate every 3-gram in the text as a possible key in the map you'll build. In this example the keys/Strings you'll generate are:

`bbb` -> `bba` -> `bab` -> `abb` -> `bba` -> `bab` -> `abb` -> `bbb` -> `bbb` -> `bba` -> `bab` -> `aba`

You can use the `String.substring()` method ([documentation here](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html#substring(int,int)) to create substrings of the appropriate length, i.e., `myOrder`. You will need to reason about how to loop over the training text and what values to pass for the `beginIndex` and `endIndex` (hint: they need to be related to the index you are using to loop over the training text!) of the `substring` method in order to create all of the possible substrings. Remember that each one should be of length `myOrder`, for example `substring(0, myOrder)` would be the first.

As you create these keys, you'll store them in `myMap` and add each of the following single-character strings to the ArrayList value associated with the key.

For example, you'd expect to see these keys and values for the string `"bbbabbabbbbaba"`. The order of the keys in the map isn't known, but for each key the order of the single-character strings should be as shown below -- the order in which they occur in the training text.

</details> 


### Implementing getFollows

This method simply looks up the key in `myMap` and returns the associated value: an `ArrayList` of single-character strings that was created when `setTraining` is called. If the key isn't in the map you should throw an exception as shown in the following code.

`throw new NoSuchElementException(key+" not in map");`

The code in this version of `getFollows` is constant time because if the key is in the map, the corresponding value is simply returned. The value for each key is set in the method `setTraining`.


### Running and Testing EfficientMarkov

To test that your code is still correct but more efficient, you can run `MarkovDriver` (see [Running and Understanding the Starter Code](#running-and-understanding-the-starter-code)) twice using with everything the same except once using your `EfficientMarkov` model and the other time using the `BaseMarkov` model. You change this by uncommenting the line `MarkovInterface<String> efficient = new EfficientMarkov();` and then passing the `efficient` object to the call to the static `markovGenerate` method within the psvm method of `MarkovDriver`.  You should get the same text, but the running time (shown at the bottom of the output) should be less for `EfficientMarkov`.

***Note on the random seed:*** You will see that `BaseMarkov` sets a `RANDOM_SEED` to `1234`. This is to ensure that running the same program on the same inputs twice generates the same output. We recommend you not change the seed while developing and testing your program so you can compare output. Also, if you change the seed later, be sure to set it back to `1234` for running JUnit tests or submitting to Gradescope.

In addition to running `MarkovDriver` using `EfficientMarkov` and comparing the results to using `BaseMarkov`, we also provide JUnit tests in the `MarkovTest` class. ***You will need to change the class being tested that's returned by the method `getModel`***. See the [section in the P2 Markov Part 1 directions](https://coursework.cs.duke.edu/201spring22/p2-markov-part-1/-/tree/main#junit-tests) on how to run a Java program that uses JUnit tests.


## Developing and Testing EfficientWordMarkov

As discussed above in [Running and Understanding the Starter Code](#running-and-understanding-the-starter-code), if you change the `MarkovDriver` to use `BaseWordMarkov` instead of `BaseMarkov` then words rather than characters will be used to generate a model. Here, a k-gram is a sequence of *k* words, e.g., a `WordGram` rather than a String of k-characters. A working `WordGram` class just like the one you completed for P2 Markov Part 1 is included in the starter code.

Just as you created `EfficientMarkov` by extending `BaseMarkov`, you'll create `EfficientWordMarkov` by extending `BaseWordMarkov`. You'll create two constructors and implement two methods similar to those in `EfficientMarkov`: `setTraining` and `getFollows`. The difference is that instead of String objects as keys in a map you'll be using `WordGram` objects as keys. 

You must create the `EfficientWordMarkov` class from scratch; you're not provided with starter code. Create the file `EfficientWordMarkov.java` and model the class on the `EfficientMarkov` class you have already implemented and tested. We provide tips for implementing `EfficientWordMarkov` in the expandable section below, but not a step by step walkthrough; you will need to reason about how to adapt the code from `EfficientMarkov` to work with `WordGram` objects.

<details>
<summary>Expand for tips on implementing EfficientWordMarkov</summary>

1. Use `WordGram` objects as keys in an instance variable map of type `HashMap<WordGram, ArrayList<String>>`.
2. The instance variable String `myText` from `BaseMarkov` becomes `String[] myWords` in `BaseWordMarkov`.
3. There should be two constructors, designed just like those in `EfficientMarkov` and using the same default order of 3. 
4. In creating an array of words in the `setTraining` method, you can use `text.split("\\s+")` to process the String passed to `setTraining` into an array of "words" separated by whitespace. You'll see this code in `setTraining` method of `BaseWordMarkov`.
5. Instead of using `substring()` to create each key, you'll create a new `WordGram` for every key in the map. Consider whether the the `shiftAdd` method might be convenient to use.
6. Instead of using a one-character `String` to follow each key, you'll use the appropriate `String` in the `myWords` array as the string that follows each key.
7. `getFollows` is essentially the same, just looking up in a different map.

</details>

To test your class, use it in the `MarkovDriver` program and compare the output to what's generated using `BaseWordMarkov` just as you did when [testing `EfficientMarkov`](#running-and-testing-efficientmarkov). Again, you should get the same results but in less time.


## Benchmarking and Analysis

The purpose of this assignment was to develop a more efficient implementation of a Markov model than the basic implementation provided in the starter code. In this section, you will analyze the runtime performance of these models, first by answering conceptual questions to form hypotheses, then by considering empirical runtime performance that supports or contradicts your hypotheses. This section will only require you to answer questions about the `BaseMarkov` and `EfficientMarkov`, so you can still complete the analysis even if you were not able to complete the implementation of `EfficientWordMarkov`.

Answer the following questions in your analysis. You'll submit your analysis as a separate PDF as a separate assignment ***to Gradescope***. 

<details>
<summary>Question 1: Base Markov hypotheses</summary>

If there are *N* characters in the training text and we want to generate *T* characters of random text, we claimed that the runtime complexity to do so using `BaseMarkov` should be O(*NT*), that is, the runtime should scale with the product of *N* and *T*. If that is true, what would you hypothesize about the empirical runtime of the program (that is, the actual time, measured in seconds, for the program to execute)? Specifically, answer the following under the O(*NT*) assumption:

- If we double *N* and we double *T*, how would you expect the runtime to change?
- If *N* is much larger than *T* and we double *T* but not *N*, how would you expect the runtime to change?
- If *N* is much larger than *T* and we double *N* but not *T*, how would you expect the runtime to change?

</details>

<details>
<summary>Question 2: Efficient Markov hypotheses</summary>

If there are *N* characters in the training text and we want to generate *T* characters of random text, we claimed that the runtime complexity to do so using `EfficientMarkov` should be O(*N*+*T*), that is, the runtime should scale with the sum of *N* and *T*. If that is true, what would you hypothesize about the empirical runtime of the program (that is, the actual time, measured in seconds, for the program to execute)? Specifically, answer the following (same questions as before) under the O(*N*+*T*) assumption:

- If we double *N* and we double *T*, how would you expect the runtime to change?
- If *N* is much larger than *T* and we double *T* but not *N*, how would you expect the runtime to change?
- If *N* is much larger than *T* and we double *N* but not *T*, how would you expect the runtime to change?

</details>

<details>
<summary>Question 3: BaseMarkov Benchmark</summary>

The starter code includes a class called `Benchmark`. Running the psvm method of that class runs several tests that allow you to compare the performance (in terms of the program runtime) of the default, brute-force `BaseMarkov` and the more efficient map-based `EfficientMarkov` code. The code you start with uses `data/hawthorne.txt`, which is the text of ***A Scarlet Letter***, a text of 487,614 characters (as you'll see in the output when running the benchmark tests). 

The table below shows the output of running the main method of `Benchmark` using `BaseMarkov` on staff laptop using the default file and an order 5 Markov Model. Note that the individual numbers may vary somewhat on your laptop, it is the pattern of growth that interests us.

source is *N*, the size of the training text, and #chars is *T*, the number of random characters being generated. First the `Benchmark` program holds *N* constant and increases *T*. 

|time |   source | #chars |
|------|--------|------|
|0.107   |487614  |1000 |
|0.204   |487614  |2000 |
|0.302   |487614  |8000 |
|1.440   |487614  |16000 |
|2.825   |487614  |32000 |
|5.674   |487614  |64000 |

Then the `Benchmark` program holds *T* constant and increases *N*.

|time |   source | #chars |
|------|--------|------|
|0.370   |487614  |4096 |
|0.720   |975228  |4096 |
|1.065  |1462842 |4096 |
|1.421   |1950456 |4096 |
|1.795   |2438070 |4096 |
|2.121   |2925684 |4096 |
|2.531   |3413298 |4096 |

Explain whether the timings presented in the example provide evidence supporting the characterization of the runtime complexity using `BaseMarkov` as O(*NT*). Reference the actual timings in the example, as well as the hypotheses you made in questions 1 and 2.

</details>

<details>
<summary>Question 4: EfficientMarkov Benchmark</summary>

The `Benchmark` class uses `BaseMarkov` by default, but you can change it to use your `EfficientMarkov` by changing the appropriate line in the `getMarkov` method called from the main method. Make that change and run the `Benchmark` program to determine how long it takes for `EfficientMarkov` to generate random characters using the default file and an order 5 Markov Model. ***Report your timings.*** Explain whether the timings you report provide evidence supporting the characterization of the runtime complexity using `EfficientMarkov` as O(*N*+*T*). Reference the actual timings you report, as well as the hypotheses you made in questions 1 and 2.

</details>

<details>
<summary>Question 5: Open source AI models</summary>

Markov models like the one you implemented in this project are one example of a larger research area in artificial intelligence (AI) and machine learning (ML) related called *generative models* for *natural language processing*. Currently, one of the state-of-the-art models is called GPT-3. It is not *open-source*, meaning that the underlying source code of the model is not freely available. Read this short article about open source code in artificial intelligence: *Can’t Access GPT-3? Here’s GPT-J — Its Open-Source Cousin* [accessible via this link](https://towardsdatascience.com/cant-access-gpt-3-here-s-gpt-j-its-open-source-cousin-8af86a638b11). Do you think new research code in AI/ML should be more open source? Why, or why not? There is no right or wrong answer to this question, we are looking for one or two paragraphs of thoughtful reflection.

</details>

***After completing the analysis questions you submit your answers in a PDF to Gradescope in the appropriate assignment.***


## Submitting, Reflect, Grading

Push your code to Git. Do this often. Once you have run and tested your completed program locally:

1. Submit your code on gradescope to the autograder.
2. Submit a PDF to Gradescope in the separate P3: Analysis assignment.
3. Complete the [reflect form linked here](https://forms.office.com/Pages/ResponsePage.aspx?id=TsVyyzFKnk2xSh6jbfrJTErNjWEU70pGg_ytfEVEPi5UQjRTUkpLWE1QTzE1QVhCR1BaS0RVM1NWWS4u).


For this program grading will be:

| Points | Grading Criteria |
| ------ | ------ |
| 16 | EfficientMarkov and EfficientWordMarkov code |
| 6 | Answers to analysis + reflect |


## Appendix

You don't need to read this section, but some of the information might be helpful.

<details>
<summary>Expand for FAQs about this project</summary>

**My unchanged `BaseMarkov` does not produce the same output as reported above or does not give the correct number of characters as described in `analysis.txt`.** 

Post to Ed. This is unlikely to happen for correct programs.

**When I run `Benchmark` using `EfficientMarkov`, it takes even longer than `BaseMarkov` and/or generates an `OutOfMemoryException`.**

Make sure you clear the map at the start of `setTraining` by calling `myMap.clear()`. Because the `Benchmark` class creates a single `EfficientMarkov` object and calls `setTraining()` several times, if you do not clear the map, new values will be added every time the method is called.

**How can I debug my `EfficientMarkov` implementation?**

You can test your implementation by providing a String like `“bbbabbabbbbaba”` as the training text and print the keys and values of the map you build to confirm that your map is constructed correctly. Working out simple cases by hand and confirming them with your code is a good way to test code in general.
In order to run your code, you can make some changes to the `main()` method in `MarkovDriver` or `Benchmark`.

**How do I deal with randomness?**

It’s hard enough to debug code without random effects making it even harder. In the `MarkovModel` class you’re provided, the Random object used for random-number generation is constructed as follows:

`myRandom = new Random(RANDOM_SEED);`

`RANDOM_SEED` is defined to be 1234. Using the same seed to initialize the random number stream ensures that the same random numbers are generated each time you run the program. Removing `RANDOM_SEED` and using `new Random()` will result in a different set of random numbers, and thus different text, being generated each time you run the program. This is more amusing, but harder to debug. If you use a seed of `RANDOM_SEED` in your `EfficientMarkov` model you should get the same random text as when the brute-force method is used. This will help you debug your program because you can check your results with those of the code you’re given which you can rely on as being correct. You'll get this behavior "for free" since the first line of your `EfficientMarkov` constructor will be `super(order)` -- which initializes the `myRandom` instance variable.

If you use the same `RANDOM_SEED` in constructing the random number generator used in your new implementation, you should get the same text, but your code should be faster. You can use `MarkovDriver` to test this. Do not change the given `RANDOM_SEED` random seed when testing and submitting the program, though you can change it when you'd like to see more humorous and different random text.

**The length of the Markov Model is way too small**
 
The code is encountering the EOS tag too soon and then exiting - look over where you’re adding the EOS tag.

</details>


<details>
<summary>More example output</summary>

This table shows more example output of different Markov Models generating characters. 

| k | President Trump State of the Union 2017 | President Biden State of the Union 2021 |
| - | ------                                  | ------                                 |
| 1 |  t bers, thatetiourotha atr Itry es tr l en thed mpparow eves, The l t. tos tive derke, t d, by. Ames ther ais, outyeneanonerod pisind Th. pr cres olinacrop a chencon borurenge ind on thers thest wat w | r bus io vear mans anovine se futinybereagicorthences – min pl anginsethec. lid Imint a me e estoreon We alel the tho Wed orreshilthe- lrien thaionveost NIDandend jon cempest Amaid ofit’r thnkege awis |
| 2 | ps of Yort sameriabonuchismany to reving theiregive nor toget ou, war. Shat ve vinno Nat thar deart ree.” Alatiall ar And on Natiens, to plese — and on, an eve imboyme shly offely, as penigh Kengrear| om of rovestin thater the meris to becaust Chilly th. It’s itiand Amerear precouleassarms on Just it usell cour con 9/11.3 muld st an ton. Stax owthat to come so puble coles divered magaild ouggle vi|
| 3 | f us the pracificity othe including. Pare workers of his Capitol, meets with in againspire food there need. Aftere in our strain. We regislast, homento uneminists, them, thinally are top prover our Go | irst on pock of their a Medica’s demong Right ten andame failesses at I know – it we cans by bet why time to said by more imple. We ass the wrong the nation a your drugs. Tonighternet, and in Famillio |
| 4 | arriorities why, this like Congressure the nearly 400, incoln an incredible to hardships are build our countain minor Otto join me it, but where break to than $4,000 — so Americans — manufacture of St | life can will crising the faucet again. The world. Talk away. So how right — and get by negotiating than 400,000 pharmacism to be big tax brack up. That I’m provided to act police President have act. |
| 5 | has taughters order Patrol Agents, and together, gaining our difficult — because of the authority and that we do. I will be a major plants will determined a tube to endured by criminals and minor chi | permanent study shot an option during the productive diseases opens without our high-speed international to Americans can be first 100 Days of joy, cried out the central challenges facing nothing up. |

This table shows more example output of Markov models using `WordGram`s.

| k | President Trump State of the Union 2017 | President Biden State of the Union 2021 |
| - | ------                                  | ------                                 |
| 1 | friends on Long Island. His tormentors wanted to reform is moving a train tracks, exhausted from Mexico to reopen an order directing Secretary Mattis to independence, and a mountain, we are dreamers too. Here tonight — and paramedics who will see their corrupt dictatorship, I am asking both parties as | the 21st Century. We cannot walk away from their net worth increase by man—made and save lives. And we will long endure is familiar, this podium, and I know the American households. We’ve done nothing – Democrats and billionaires who knows what it’s in that have to leave. Our grids|
| 2 | expected, and others we could never have imagined. We have faced challenges we expected, and others we could never have imagined.  We have faced challenges we expected, and others we could never have imagined. We have shared in the gallery with Melania. Ashlee was aboard one of our country. The | survive. It did. But the struggle is far from over. The questionof whether our democracy since the Great Depression. The worst economic crisis since the Great Depression. The worst pandemic in a direct and proportionate way to Russia’s interference in our mutual interests. As we gather here tonight, the|
| 3 | respect our country. The fourth and final pillar protects the nuclear family by ending chain migration. Under the current broken system, a single immigrant can bring in virtually unlimited numbers of distant relatives. Under our plan, those who meet education and work requirements, and show good moral character, will be | we need to ensure greater equity and opportunity for women. Let’s get the Paycheck Fairness Act to my desk as soon as possible. I also hope Congress can get to my desk the Equality Act to protect Asian Americans and Pacific Islanders from the vicious hate crimes we’ve seen this |
| 4 | we passed tax cuts, roughly 3 million workers have already gotten tax cut bonuses — many of them thousands of dollars per worker.  Apple has just announced it plans to invest a total of $350 billion in America, and hire another 20,000 workers. This is our new American moment. There | our tables. Immigrants have done so much for America during the pandemic – as they have throughout our history. The country supports immigration reform. Congress should act. We have a giant opportunity to bend to the arc of the moral universe toward justice. Real justice. And with the plans I|
| 5 | did not stay silent. America stands with the people of Iran in their courageous struggle for freedom. I am asking the Congress to end the dangerous defense sequester and fully fund our great military. As part of our defense, we must modernize and rebuild our nuclear arsenal, hopefully never having | soul of America – we need to protect the sacred right to vote. More people voted in the last presidential election than ever before in our history – in the middle of one of the worst pandemics ever. That should be celebrated. Instead it’s being attacked. Congress should pass H.R.|

</details>

