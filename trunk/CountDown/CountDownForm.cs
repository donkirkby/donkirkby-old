using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace CountDown
{
    public partial class CountDownForm : Form
    {
        private DateTime endTime;

        public CountDownForm()
        {
            InitializeComponent();
        }

        private void startStop_Click(object sender, EventArgs e)
        {
            if (timer.Enabled)
            {
                StopCounter();
            }
            else
            {
                StartCounter();
            }
        }

        private void StartCounter()
        {
            TimeSpan diff;
            bool isValid = TimeSpan.TryParse(timeRemaining.Text, out diff);
            if (isValid)
            {
                startStop.Text = "Stop";
                timer.Enabled = true;
                endTime = DateTime.Now + diff;
            }
        }

        private void StopCounter()
        {
            startStop.Text = "Start";
            timer.Enabled = false;
        }

        private void timer_Tick(object sender, EventArgs e)
        {
            var diff = endTime - DateTime.Now;
            timeRemaining.Text = diff.ToString();
            if (diff.Minutes > 0)
            {
                Text = String.Format("{0} minutes", diff.Minutes);
            }
            else if (diff.Seconds > 0)
            {
                Text = String.Format("{0} seconds", diff.Seconds);
            }
            else
            {
                Text = "Finished";
                timeRemaining.Text = "0";
                StopCounter();
            }
        }
    }
}
