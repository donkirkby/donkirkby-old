using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using SampleApp;
using System.Threading;

namespace SampleTest
{
    class MockTimeoutTimer : TimeoutTimer
    {
        private EventWaitHandle timerHandle = new AutoResetEvent(false);

        public void TimeoutNow()
        {
            timerHandle.Set();
        }

        public override void Sleep()
        {
            timerHandle.WaitOne();
        }

        public override bool WaitOne(WaitHandle waitHandle)
        {
            int satisfiedIndex =
                WaitHandle.WaitAny(new WaitHandle[] { timerHandle, waitHandle });
            return satisfiedIndex == 1;
        }
    }
}
