using System;
using System.Collections.Generic;
using System.Reflection;
using System.Threading;
using NUnit.Framework;

namespace TickingTest
{
    public class TestFramework
    {
        private static Dictionary<object, MultithreadedTestCase> testRuns =
            new Dictionary<object, MultithreadedTestCase>();

        public static void RunOnce(MultithreadedTestCase testCase)
        {
            var type = testCase.GetType();
            MethodInfo[] methods =
                type.GetMethods(BindingFlags.Public | BindingFlags.Instance);
            var tasks = new List<TaskInfo>();
            MethodInfo initializeMethod = null;
            MethodInfo finishMethod = null;
            foreach (var method in methods)
            {
                if (method.IsDefined(typeof(TestThreadAttribute), true))
                {
                    tasks.Add(new TaskInfo(method, testCase));
                }
                if (method.IsDefined(typeof(InitializeAttribute), true))
                {
                    initializeMethod = method;
                }
                if (method.IsDefined(typeof(FinishAttribute), true))
                {
                    finishMethod = method;
                }
            }
            
            // Add one more task for the ticker thread
            tasks.Add(new TaskInfo(
                type.GetMethod("RunTicker", BindingFlags.NonPublic | BindingFlags.Instance), 
                testCase));

            if (initializeMethod != null)
            {
                initializeMethod.Invoke(testCase, null);
            }

            foreach (var task in tasks)
            {
                task.BeginInvoke();
            }

            TaskInfo.WaitAll(tasks, 5000);

            // we want to start with the ticker task to see if it timed out.
            for (int index = tasks.Count-1; index >= 0; index--)
            {
                tasks[index].EndInvoke();
            }

            if (finishMethod != null)
            {
                finishMethod.Invoke(testCase, null);
            }
        }

        private class TaskInfo
        {
            private MultithreadedTestCase testCase;
            private TaskDelegate taskDelegate;
            private MethodInfo taskMethod;
            private IAsyncResult result;

            public TaskInfo(MethodInfo taskMethod, MultithreadedTestCase testCase)
            {
                this.testCase = testCase;
                this.taskMethod = taskMethod;
            }

            private delegate void TaskDelegate();

            public void BeginInvoke()
            {
                taskDelegate = delegate()
                {
                    try
                    {
                        Thread.CurrentThread.Name =
                            taskMethod.Name;
                        testCase.AddCurrentThread();
                        taskMethod.Invoke(testCase, null);
                    }
                    catch (Exception)
                    {
                        // Something went wrong, but we still have to keep the thread
                        // alive until the ticker shuts down.
                        try
                        {
                            testCase.WaitForTick(int.MaxValue);
                        }
                        catch (Exception)
                        {
                            // swallow this exception, because we're already
                            // dealing with ex.
                        }
                        throw;
                    }
                    // The thread finished successfully, now we just keep the thread
                    // alive until the ticker shuts down.
                    testCase.WaitForTick(int.MaxValue);
                };
                result = taskDelegate.BeginInvoke(null, null);
            }

            public void EndInvoke()
            {
                taskDelegate.EndInvoke(result);
            }

            public static void WaitAll(ICollection<TaskInfo> tasks, int millisecondsTimeout)
            {
                WaitHandle[] waitHandles = new WaitHandle[tasks.Count];
                int i = 0;
                foreach (var task in tasks)
                {
                    waitHandles[i++] = task.result.AsyncWaitHandle;
                }
                Assert.IsTrue(
                    WaitHandle.WaitAll(waitHandles, millisecondsTimeout, false),
                    "Tasks timed out before completing. Later test results are suspect!");
            }
        }
    }
}
