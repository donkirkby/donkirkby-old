using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Reflection;
using System.Threading;
using NUnit.Framework;
using log4net;

namespace TickingTest
{
    public class MultithreadedTestCase
    {
        private static ILog log = LogManager.GetLogger(typeof(MultithreadedTestCase));
        private const String BLOCKED_MESSAGE = 
            "All test threads are blocked. Did you start new threads that " +
            "the test doesn't know about?";

        private Dictionary<Thread, int> threadTickRequests =
            new Dictionary<Thread, int>();
        private int currentTick = 0;
        private bool isBlocked = false;
        private bool isTickerFrozen = false;

        public void WaitForTick(int tick)
        {
            lock (this)
            {
                log.DebugFormat(
                    "{0} waiting for tick {1}",
                    Thread.CurrentThread.Name,
                    tick);
                threadTickRequests[Thread.CurrentThread] = tick;
                while (currentTick < tick && !isBlocked)
                {
                    Monitor.Wait(this);
                }
                if (isBlocked)
                {
                    throw new InvalidOperationException(BLOCKED_MESSAGE);
                }
            }
        }

        public bool IsTickerFrozen 
        {
            get
            {
                lock (this)
                {
                    return isTickerFrozen;
                }
            }
            set
            {
                lock (this)
                {
                    isTickerFrozen = value;
                }
            }
        }

        public void AssertTick(int expectedTick)
        {
            lock (this)
            {
                Assert.AreEqual(
                    expectedTick,
                    currentTick,
                    "current tick");
            }
        }

        internal void AddCurrentThread()
        {
            lock (this)
            {
                threadTickRequests[Thread.CurrentThread] = 0;
            }
        }

        internal void RunTicker()
        {
            lock (this)
            {
                threadTickRequests.Remove(Thread.CurrentThread);
            }
            int blockedIterationCount = 0;
            bool isDone = false;
            while (!isDone)
            {
                Thread.Sleep(10);
                lock (this)
                {
                    bool isRunning = false;
                    foreach (var thread in threadTickRequests.Keys)
                    {
                        ThreadState nonRunningStates =
                            ThreadState.Stopped | 
                            ThreadState.Unstarted | 
                            ThreadState.WaitSleepJoin |
                            ThreadState.Suspended;
                        isRunning = (thread.ThreadState & nonRunningStates) == 0;
                        if (isRunning)
                        {
                            break;
                        }
                    }
                    if (isRunning || isTickerFrozen)
                    {
                        blockedIterationCount = 0;
                    }
                    else
                    {
                        int countUnfinished = 0;
                        int nextTick = int.MaxValue;
                        foreach (var tickRequest in threadTickRequests.Values)
                        {
                            if (tickRequest < int.MaxValue)
                            {
                                countUnfinished++;
                            }
                            if (tickRequest < nextTick && tickRequest > currentTick)
                            {
                                nextTick = tickRequest;
                            }
                        }
                        if (nextTick < int.MaxValue || countUnfinished == 0)
                        {
                            log.DebugFormat(
                                "Advancing clock from {0} to {1}.",
                                currentTick,
                                nextTick);
                            currentTick = nextTick;
                            isDone = currentTick == int.MaxValue;
                            Monitor.PulseAll(this);
                        }
                        else
                        {
                            if (++blockedIterationCount > 10)
                            {
                                isBlocked = true;
                                Monitor.PulseAll(this);
                                foreach (var thread in threadTickRequests.Keys)
                                {
                                    thread.Interrupt();
                                }
                                throw new InvalidOperationException(BLOCKED_MESSAGE);
                            }
                        }
                    }
                }
            }
        }
    }
}
