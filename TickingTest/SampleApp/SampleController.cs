using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace SampleApp
{
    public class SampleController
    {
        private EventWaitHandle waitHandle = new AutoResetEvent(false);

        public SampleController()
        {
            TimeoutTimer = new TimeoutTimer();
        }

        public TimeoutTimer TimeoutTimer { get; set; }

        /// <summary>
        /// Convenience property for setting the Timeout value on the timer.
        /// </summary>
        public int Timeout 
        {
            get { return TimeoutTimer.Timeout; }
            set { TimeoutTimer.Timeout = value; } 
        }

        public void Sleep()
        {
            TimeoutTimer.Sleep();
        }

        public bool WaitForSignal()
        {
            return TimeoutTimer.WaitOne(waitHandle);
        }

        public void SendSignal()
        {
            waitHandle.Set();
        }
    }
}
