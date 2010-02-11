using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using NUnit.Framework;
using TickingTest;
using SampleApp;

namespace SampleTest
{
    /* TODO: remaining tests
     * Sleep when timeout is infinite
     */
    [TestFixture]
    public class SampleControllerTest
    {
        [Test]
        public void Sleep()
        {
            TestFramework.RunOnce(new SleepThreads());
        }

        private class SleepThreads : MultithreadedTestCase
        {
            private MockTimeoutTimer mockTimer;

            [Initialize]
            public void Initialize()
            {
                mockTimer = new MockTimeoutTimer();
            }

            [TestThread]
            public void Client()
            {
                // SETUP
                var controller = new SampleController();
                controller.TimeoutTimer = mockTimer;

                // EXEC
                controller.Sleep();

                // VERIFY
                AssertTick(1);
            }

            [TestThread]
            public void Timer()
            {
                // EXEC
                WaitForTick(1);

                mockTimer.TimeoutNow();
            }
        }

        [Test]
        public void WaitSuccessful()
        {
            TestFramework.RunOnce(new WaitSuccessfulThreads());
        }

        private class WaitSuccessfulThreads : MultithreadedTestCase
        {
            private SampleController controller;

            [Initialize]
            public void Initialize()
            {
                controller = new SampleController();
                controller.TimeoutTimer = new MockTimeoutTimer();
            }

            [TestThread]
            public void Waiter()
            {
                // EXEC
                bool isReceived = controller.WaitForSignal();

                // VERIFY
                AssertTick(1);
                Assert.AreEqual(
                    true,
                    isReceived,
                    "is received");
            }

            [TestThread]
            public void Signaller()
            {
                // EXEC
                WaitForTick(1);

                controller.SendSignal();
            }
        }

        [Test]
        public void WaitUnsuccessful()
        {
            TestFramework.RunOnce(new WaitUnsuccessfulThreads());
        }

        private class WaitUnsuccessfulThreads : MultithreadedTestCase
        {
            private MockTimeoutTimer mockTimer;

            [Initialize]
            public void Initialize()
            {
                mockTimer = new MockTimeoutTimer();
            }

            [TestThread]
            public void Client()
            {
                // SETUP
                var controller = new SampleController();
                controller.TimeoutTimer = mockTimer;

                // EXEC
                bool isReceived = controller.WaitForSignal();

                // VERIFY
                AssertTick(1);
                Assert.AreEqual(
                    false,
                    isReceived,
                    "is received");
            }

            [TestThread]
            public void Timer()
            {
                // EXEC
                WaitForTick(1);

                mockTimer.TimeoutNow();
            }
        }

        [Test]
        public void SignalBeforeWait()
        {
            TestFramework.RunOnce(new SignalBeforeWaitThreads());
        }

        private class SignalBeforeWaitThreads : MultithreadedTestCase
        {
            private SampleController controller;

            [Initialize]
            public void Initialize()
            {
                controller = new SampleController();
                controller.TimeoutTimer = new MockTimeoutTimer();
            }

            [TestThread]
            public void Waiter()
            {
                // EXEC
                WaitForTick(1);
                bool isReceived = controller.WaitForSignal();

                // VERIFY
                AssertTick(1);
                Assert.AreEqual(
                    true,
                    isReceived,
                    "is received");
            }

            [TestThread]
            public void Signaller()
            {
                // EXEC
                controller.SendSignal();
            }
        }

        [Test]
        public void SetTimeout()
        {
            // SETUP
            var controller = new SampleController();
            int expectedTimerTimeout = 100;
            int expectedControllerTimeout = 200;

            // EXEC
            controller.Timeout = expectedTimerTimeout;
            int actualTimerTimeout = controller.TimeoutTimer.Timeout;

            controller.TimeoutTimer.Timeout = expectedControllerTimeout;
            int actualControllerTimeout = controller.Timeout;

            // VERIFY
            Assert.AreEqual(
                expectedControllerTimeout,
                actualControllerTimeout,
                "controller timeout");
            Assert.AreEqual(
                expectedTimerTimeout,
                actualTimerTimeout,
                "timer timeout");
        }
    }
}
