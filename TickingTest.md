---
title: TickingTest
subtitle: A unit testing library for multi-threaded code in .NET
---

This library ports the [multithreadedtc library][origin] from Java to .NET. You can download the Ticking Test source code from [the repository][repo], and you will find an example multi-threaded class with some test cases.

The library lets you declare a method for each thread that you want to run, and then coordinate the timing of the thread execution with checkpoints called ticks.

[origin]: https://code.google.com/p/multithreadedtc/
[repo]: https://github.com/donkirkby/donkirkby-old/tree/master/TickingTest