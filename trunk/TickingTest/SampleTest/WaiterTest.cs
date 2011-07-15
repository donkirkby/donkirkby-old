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
                // fire a background thread for the responder.
                Action responder = ResponderThread;
                responder.BeginInvoke(null, null);

                WaitForTick(2);
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
                AssertTick(2);
            }

            // Not a test thread!
            private void ResponderThread()
            {
                // IMPORTANT! The first thing you have to do is call 
                // WaitForTick(). Tick zero is acceptable. If you don't do 
                // this, the framework doesn't know about this thread and
                // won't wait for it to block before advancing the tick.
                WaitForTick(1);

                waiter.ReceiveResponse(54);
                waiter.ReceiveResponse(55);
                AssertTick(1);

                // IMPORTANT! The last thing you have to do is call
                // WaitForTick() with a int.MaxValue. This keeps
                // your thread alive until the end of the test run.
                WaitForTick(int.MaxValue);
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
