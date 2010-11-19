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
            timeRemaining.Text = String.Format(
                "{0:00}:{1:00}:{2:00}", 
                diff.TotalHours,
                diff.Minutes,
                diff.Seconds);
            if (diff.TotalSeconds > 0)
            {
                Text = FormatRemainingTime(diff);
            }
            else
            {
                timeRemaining.Text = "0";
                Text = "Finished";
                StopCounter();
            }
        }

        private string FormatRemainingTime(TimeSpan diff)
        {
            if (diff.Days > 0)
            {
                return String.Format("{0} days", diff.Days);
            }
            if (diff.Hours > 0)
            {
                return String.Format("{0} hours", diff.Hours);
            }
            if (diff.Minutes > 0)
            {
                return String.Format("{0} minutes", diff.Minutes);
            }
            return String.Format("{0:0} seconds", diff.TotalSeconds);
        }

        private void timeRemaining_TextChanged(object sender, EventArgs e)
        {
            TimeSpan diff;
            bool isValid = TimeSpan.TryParse(timeRemaining.Text, out diff);
            if (isValid)
            {
                Text = FormatRemainingTime(diff);
            }
            else
            {
                Text = "Count Down";
            }
        }
    }
}
