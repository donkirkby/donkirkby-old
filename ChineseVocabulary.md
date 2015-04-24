---
title: Chinese Vocabulary
subtitle: A project to generate Chinese vocabulary flashcards to use with Mnemosyne
---

# Introduction #

This is a project to generate computer flashcards for reading and writing all the Chinese vocabulary I'll need for the foreseeable future. There are currently 32614 cards with 4166 characters and 18385 words. They are divided into separate files, each with about 100 cards in it. You can download the flashcards for the first 700 characters from the downloads tab. I'll publish the remaining characters after I'm happy with the current set.

This project was only possible because other people put in tons of work and made it freely available. For a list of sources, see the ChineseSources page.

# The flashcards #

The flashcard files are for importing into the [Mnemosyne project's](http://www.mnemosyne-proj.org/) flashcard software. Here's a screen shot of the card for 不:

![screen shot](https://donkirkby.github.com/donkirkby/images/chvocab_screenshot.PNG)

There are several types of flashcards:

  * English -> stroke order, pinyin, and sample words
  * Simplified character -> pinyin, English, and sample words
  * Pinyin -> stroke order, English, and sample words
  * Traditional character (if different) -> pinyin, English, and sample words
  * Chinese word -> pinyin and English

Some characters have more than one pronunciation, so you'll get a definition, pinyin, and sample word for each pronunciation.


# Installation #
To use the flashcards on your computer, follow these steps:

  1. Download and install [Mnemosyne](http://mnemosyne-proj.org/download-mnemosyne.php).
  1. Start the Mnemosyne program by choosing it from the Start menu. It should come up empty with no flash cards loaded.
  1. Download the Chinese vocabulary library from the [releases page](https://github.com/donkirkby/donkirkby/releases).
  1. Unzip the library to a folder where you can find it later as you need to import the next card file whenever you've memorized most of your current cards.
  1. Move the stroke-images folder to your .mnemosyne folder. Under Microsoft Windows, it will usually be at C:\Documents and Settings\ _yourname_ \.mnemosyne. I think Windows Vista users will find it at C:\Users\ _yourname_ \.mnemosyne, but I haven't tried it. Linux users should find it at ~/.mnemosyne. For more details and an FAQ list, see the [Mnemosyne site](http://www.mnemosyne-proj.org/) and their [documentation](http://www.mnemosyne-proj.org/help/index.php).
  1. Import the first card file (rank0020.xml) into Mnemosyne.

# Usage #
Now that you've loaded your first batch of cards you can go through and memorize them. Press space bar to show the answer and a number key to rank how well you remembered. 0 or 1 mean you haven't memorized it and you'll be asked again during today's session.

When you've memorized most of your current cards, you can import the next card file. I usually check the number of scheduled cards at the start of a day. If it's less than 20, then I'm ready to import the next card file. You can decide for yourself how often to add new cards, but don't overdo it. I burned out after six weeks of learning about 30 new characters per week.

# Problems or enhancements #
If you have enhancements you'd like to see or a suggestion for a different card format, please [create an issue](https://github.com/donkirkby/donkirkby/issues).
Some definitions and stroke order diagrams have errors, so create an issue if you find any problems. However, there seem to be inconsistencies between different stroke order authorities, so don't panic if the stroke order disagrees with what you've been taught. If you really think that the stroke order is wrong on a character, please include a link to a stroke order reference that I can verify. There are several stroke order references at my [wikia page](http://scratchpad.wikia.com/wiki/Learning_Chinese_-_Tools).