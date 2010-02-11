using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Threading;

namespace BlockingIO
{
    public class BlockingStringReader : TextReader
    {
        private object locker = new object();
        private StringReader activeReader = new StringReader("");
        private StringWriter pendingWriter = new StringWriter();
        private ManualResetEvent textAvailable = new ManualResetEvent(false);
        private int activeLength = 0;
        private bool isEnded = false;

        public void WriteLine(String value)
        {
            lock (locker)
            {
                pendingWriter.WriteLine(value);
                textAvailable.Set();
            }
        }

        public void End()
        {
            lock (locker)
            {
                isEnded = true;
                textAvailable.Set();
            }
        }

        public override void Close()
        {
            lock (locker)
            {
                activeReader.Close();
                pendingWriter.Close();
                base.Close();
            }
        }

        protected override void Dispose(bool disposing)
        {
            lock (locker)
            {
                if (disposing)
                {
                    activeReader.Dispose();
                    pendingWriter.Dispose();
                }
            }
            base.Dispose(disposing);
        }

        private void CheckActiveText()
        {
            lock (locker)
            {
                if (activeLength <= 0)
                {
                    String pendingText = pendingWriter.ToString();
                    if (pendingText.Length > 0)
                    {
                        activeReader = new StringReader(pendingText);
                        activeLength = pendingText.Length;
                        pendingWriter = new StringWriter();
                    }
                }
            }
        }

        public override int Peek()
        {
            lock (locker)
            {
                CheckActiveText();
                return activeReader.Peek();
            }
        }

        public override int Read()
        {
            lock (locker)
            {
                WaitForText(1);

                CheckActiveText();
                int result = activeReader.Read();
                if (result >= 0)
                {
                    activeLength--;
                }
                return result;
            }
        }

        private void WaitForText(int requiredLength)
        {
            bool isReady;
            do
            {
                isReady = IsTextReady(requiredLength);
                if (!isReady)
                {
                    textAvailable.Reset();
                    textAvailable.WaitOne(Timeout.Infinite, true);
                }
            } while (!isReady);
        }

        private bool IsTextReady(int requiredLength)
        {
            bool isReady;
            lock (locker)
            {
                int pendingLength = pendingWriter.ToString().Length;
                isReady =
                    isEnded ||
                    (pendingLength + activeLength >= requiredLength);
            }
            return isReady;
        }

        public override int Read(char[] buffer, int index, int count)
        {
            int bytesRead = 0;
            bool isReady;
            do
            {
                WaitForText(count);
                lock (locker)
                {
                    isReady = IsTextReady(count);
                    if (isReady || isEnded)
                    {
                        int oldBytesRead = activeReader.Read(buffer, index, count);
                        activeLength -= oldBytesRead;
                        int newBytesRead = 0;
                        if (oldBytesRead < count)
                        {
                            CheckActiveText();
                            newBytesRead = activeReader.Read(
                                buffer,
                                index + oldBytesRead,
                                count - oldBytesRead);
                            activeLength -= oldBytesRead;
                        }
                        bytesRead = oldBytesRead + newBytesRead;
                    }
                }
            } while (!isReady && !isEnded);
            return bytesRead;
        }

        public override string ReadLine()
        {
            string result = null;
            bool isReady;
            do
            {
                WaitForText(1);
                lock (locker)
                {
                    isReady = IsTextReady(1);
                    if (isReady || isEnded)
                    {
                        CheckActiveText();
                        result = activeReader.ReadLine();
                        if (result != null)
                        {
                            activeLength -= (result.Length + pendingWriter.NewLine.Length);
                        }
                    }
                }
            } while (!isReady && !isEnded);
            return result;
        }

        public override string ReadToEnd()
        {
            lock (locker)
            {
                End();
                string pendingText = pendingWriter.ToString();
                string result = activeReader.ReadToEnd() + pendingText;
                if (pendingText.Length > 0)
                {
                    pendingWriter = new StringWriter();
                }
                return result;
            }
        }
    }
}
