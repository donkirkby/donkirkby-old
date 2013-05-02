using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using NUnit.Framework;
using System.Threading;
using System.IO;
using TickingTest;
using SampleApp;

namespace SampleTest
{
    [TestFixture]
    public class WaiterTest
    {
        [Test]
        public void RequestFirst()
        {
            TestFramework.RunOnce(new RequestFirstThreads());
        }

        private class RequestFirstThreads : WaiterTestThreads
        {
            [TestThread]
            public void RequesterThread()
            {
                int response = waiter.Request(27);
                Assert.AreEqual(
                    54,
                    response,
                    "response");
                AssertTick(1);
            }

            [TestThread]
            public void ResponderThread()
            {
                WaitForTick(1);
                waiter.ReceiveResponse(54);
                AssertTick(1);
            }
        }

        [Test]
        public void RespondFirst()
        {
            TestFramework.RunOnce(new RespondFirstThreads());
        }

        private class RespondFirstThreads : WaiterTestThreads
        {
            [TestThread]
            public void RequesterThread()
            {
                WaitForTick(1);
                int response = waiter.Request(27);
                Assert.AreEqual(
                    54,
                    response,
                    "response");
                AssertTick(1);
            }

            [TestThread]
            public void ResponderThread()
            {
                waiter.ReceiveResponse(54);
                AssertTick(0);
            }
        }

        [Test]
        public void AlternatingRequestResponse()
        {
            TestFramework.RunOnce(new AlternatingRequestResponseThreads());
        }

        private class AlternatingRequestResponseThreads : WaiterTestThreads
        {
            [TestThread]
            public void RequesterThread()
            {
                int response1 = waiter.Request(27);
                AssertTick(1);
                int response2 = waiter.Request(28);
                AssertTick(2);
                Assert.AreEqual(
                    54,
                    response1,
                    "response 1");
                Assert.AreEqual(
                    55,
                    response2,
                    "response 2");
            }

            [TestThread]
            public void ResponderThread()
            {
                WaitForTick(1);
                waiter.ReceiveResponse(54);
                WaitForTick(2);
                waiter.ReceiveResponse(55);
                AssertTick(2);
            }
        }

        [Test]
        public void ResponsesFirst()
        {
            TestFramework.RunOnce(new ResponsesFirstThreads());
        }

        private class ResponsesFirstThreads : WaiterTestThreads
        {
            [TestThread]
            public void RequesterThread()
            {
                WaitForTick(1);
                int response1 = waiter.Request(27);
                int response2 = waiter.Request(28);
                Assert.AreEqual(
                    54,
                    response1,
                    "response 1");
                Assert.AreEqual(
                    55,
                    response2,
                    "response 2");
                AssertTick(1);
            }

            [TestThread]
            public void ResponderThread()
            {
                waiter.ReceiveResponse(54);
                waiter.ReceiveResponse(55);
                AssertTick(0);
            }
        }

        /// <summary>
        /// Add a new thread to the set during the test. Useful for receiving
        /// multithreaded events. See the ResponderThread() method for details
        /// on what you must do in your new thread to register with the ticker.
        /// </summary>
        [Test]
        public void SeparateThread()
        {
            TestFramework.RunOnce(new SeparateThreads());
        }

        private class SeparateThreads : WaiterTestThreads
        {
            [TestThread]
            public void RequesterThread()
            {
                // Don't time out while we're waiting for the background
                // thread to start. The background thread will unfreeze it.
                IsTickerFrozen = true;

                // fire a background thread for the responder.
                Action responder = ResponderThread;
                var result = responder.BeginInvoke(null, null);

                WaitForTick(1);
                int response1 = waiter.Request(27);
                int response2 = waiter.Request(28);
                Assert.AreEqual(
                    54,
                    response1,
                    "response 1");
                Assert.AreEqual(
                    55,
                    response2,
                    "response 2");
                AssertTick(1);

                // Have to call EndInvoke to receive any exceptions that the
                // background thread threw.
                responder.EndInvoke(result);
                AssertTick(2);
            }

            // Not a test thread!
            private void ResponderThread()
            {
                // IMPORTANT! The first thing you have to do is unfreeze the 
                // ticker. This also lets the framework know about this thread so
                // it will wait for it to block before advancing the tick.
                IsTickerFrozen = false;

                try
                {
                    waiter.ReceiveResponse(54);
                    waiter.ReceiveResponse(55);
                    AssertTick(0);

                    WaitForTick(2);
                }
                finally
                {
                    // IMPORTANT! The last thing you have to do is call
                    // ReleaseTicker(). This lets the framework know that it 
                    // doesn't have to wait for this thread.
                    ReleaseTicker();
                }
            }
        }

        private class WaiterTestThreads : MultithreadedTestCase
        {
            protected Waiter waiter;

            [Initialize]
            public void Initialize()
            {
                waiter = new Waiter();
            }
        }


    }
}
