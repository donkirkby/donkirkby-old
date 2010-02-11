using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace SampleApp
{
    /// <summary>
    /// This class is simply a wrapper around standard timing utilities.
    /// </summary>
    /// <remarks>
    /// This class allows
    /// you to write unit tests that replace the TimeoutTimer with a MockTimeoutTimer
    /// and then the unit test can make the timer time out on demand. You should
    /// copy the TimeoutTimer to your application and the MockTimeoutTimer to your
    /// test application.
    /// </remarks>
    public class TimeoutTimer
    {
        /// <summary>
        /// The length of time in milliseconds before a timeout occurs, or
        /// or Timeout.Infinite (-1) to wait indefinitely.
        /// </summary>
        public int Timeout { get; set; }

        /// <summary>
        /// Sleep for the configured length of time. 
        /// </summary>
        /// <remarks>
        /// This is equivalent to
        /// Thread.Sleep(Timeout), but it allows you to write unit tests that make
        /// the timer sleep until the unit test tells it to wake up.
        /// </remarks>
        public virtual void Sleep()
        {
            Thread.Sleep(Timeout);
        }

        /// <summary>
        /// Blocks the current thread until the WaitHandle receives a signal or the
        /// timer times out.
        /// </summary>
        /// <param name="waitHandle">The wait handle to block on.</param>
        /// <returns>true if the wait handle receives a signal; otherwise false.</returns>
        /// <remarks>
        /// This is equivalent to WaitHandle.WaitOne(Timeout), but it allows you
        /// to write unit tests that make the timer wait until the unit test
        /// tells it to time out.
        /// </remarks>
        public virtual bool WaitOne(WaitHandle waitHandle)
        {
            return waitHandle.WaitOne(Timeout);
        }
    }
}
