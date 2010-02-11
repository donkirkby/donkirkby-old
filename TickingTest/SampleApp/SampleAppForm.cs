using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Threading;

namespace SampleApp
{
    public partial class SampleAppForm : Form
    {
        private SampleController controller = new SampleController();

        public SampleAppForm()
        {
            InitializeComponent();
            controller.TimeoutTimer = new TimeoutTimer();
        }

        private void waitForSignal_Click(object sender, EventArgs e)
        {
            SetTimer();
            statusLabel.Text = "Waiting...";
            backgroundWaiter.RunWorkerAsync();
        }

        private void SetTimer()
        {
            controller.Timeout = int.Parse(timeoutText.Text) * 1000;
        }

        private void backgroundWaiter_DoWork(object sender, DoWorkEventArgs e)
        {
            bool isSignaled = controller.WaitForSignal();
            e.Result = isSignaled;
        }

        private void backgroundWaiter_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            bool isSignaled = (bool)e.Result;
            if (isSignaled)
            {
                statusLabel.Text = "Signal received.";
            }
            else
            {
                statusLabel.Text = "Timed out.";
            }
        }

        private void sendSignal_Click(object sender, EventArgs e)
        {
            controller.SendSignal();
        }

        private void sleep_Click(object sender, EventArgs e)
        {
            SetTimer();
            statusLabel.Text = "Sleeping...";
            backgroundSleeper.RunWorkerAsync();
        }

        private void backgroundSleeper_DoWork(object sender, DoWorkEventArgs e)
        {
            controller.Sleep();
        }

        private void backgroundSleeper_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            statusLabel.Text = "Woke up.";
        }
    }
}
