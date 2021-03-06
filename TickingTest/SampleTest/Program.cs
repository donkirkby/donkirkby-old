﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using log4net;

namespace SampleTest
{
    class Program
    {
        static void Main(string[] args)
        {
            // need to do this to initialize log4net
            ILog log = LogManager.GetLogger(typeof(Program));
            log.Info("Starting test.");

            try
            {
                var test = new WaiterTest();
                test.SeparateThread();
                log.Info("Success.");
            }
            catch (Exception ex)
            {
                log.Error("Failed.", ex);
            }
        }
    }
}
