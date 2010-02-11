using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace TickingTest
{
    [AttributeUsage(AttributeTargets.Method, AllowMultiple = false)]
    public class TestThreadAttribute : Attribute
    {
    }
}
