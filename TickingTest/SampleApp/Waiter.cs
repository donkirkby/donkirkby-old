using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace SampleApp
{
    public class Waiter
    {
        private Semaphore semaphore = new Semaphore(0, int.MaxValue);
        private Queue<int> responses = new Queue<int>();

        public int Request(int input)
        {
            semaphore.WaitOne();
            lock (this)
            {
                return responses.Dequeue();
            }
        }

        public void ReceiveResponse(int response)
        {
            lock (this)
            {
                responses.Enqueue(response);
            }
            semaphore.Release();
        }
    }
}
