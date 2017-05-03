# blackjacksimulator

## Synopsis

Blackjack simulator with a dealer (configurable to HIT or STAND on soft 17) and basic implementations for basic strategy player.  The simulation can be run with an arbitrary number of shoes which will be shuffled in the beginning of the simulation, and will end at a random point near the end of the shoe.
Output will show for each game what the dealer's upcard was, dealer's total and each players' hand totals with a W, D, L next to each hand.

## To Run

Install Intellij IDEA with Kotlin plugin, open project and run app.kt
TODO: build a server that will do the running of the code, and maybe a web UI to configure different configurations.

## Motivation

While on a trip to Las Vegas for a friend's bachelor party April 2017, I realized how fascinating blackjack is as a probability/numbers game.
I had never really gambled before, but the strategies behind each action, coupled with more knowledge/edge if cards are counted and bets are varied depending on the count seemed fascinating to me.
So I started this endeavor to see for myself if counting cards actually does give me an edge over the house or not.


## License

MIT License

Copyright (c) 2016 Ben Mann

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
