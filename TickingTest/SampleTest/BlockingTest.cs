using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using NUnit.Framework;
using TickingTest;
using System.Threading;
using System.Reflection;

namespace SampleTest
{
    [TestFixture]
    public class BlockingTest
    {
        /// <summary>
        /// Test that RunOnce() will fail when all threads are blocked and no one is 
        /// waiting for a tick.
        /// </summary>
        [Test]
        public void Blocking()
        {
            // SETUP
            String message = null;

            // EXEC
            try
            {
                TestFramework.RunOnce(new BlockingThreads());

                Assert.Fail("should have thrown.");
            }
            catch (TargetInvocationException ex)
            {
                message = ex.InnerException.Message;
            }

            // VERIFY
            Assert.AreEqual(
                "All test threads are blocked. Did you start new threads that " +
                "the test doesn't know about?",
                message,
                "error message");
        }

        private class BlockingThreads : MultithreadedTestCase
        {
            [TestThread]
            public void Sleeper1()
            {
                Thread.Sleep(10000);
            }

            [TestThread]
            public void Sleeper2()
            {
                WaitForTick(2);
                Thread.Sleep(10000);
            }
        }
    }
}
