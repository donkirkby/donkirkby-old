using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace BlockingIO
{
    public partial class Form2 : Form
    {
        private BlockingStringReader inputReader = new BlockingStringReader();
        private object outputLocker = new object();
        private StringWriter outputWriter = new StringWriter();

        public Form2()
        {
            InitializeComponent();
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            lock (outputLocker)
            {
                output.Text = outputWriter.ToString();
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            inputReader.WriteLine(input.Text);
            input.Text = "";
            input.Focus();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            backgroundWorker1.RunWorkerAsync();
            button2.Enabled = false;
        }

        private void backgroundWorker1_DoWork(object sender, DoWorkEventArgs e)
        {
            String line;
            do
            {
                line = inputReader.ReadLine();
                lock (outputLocker)
                {
                    outputWriter.WriteLine("---");
                    outputWriter.WriteLine(line);
                }
            } while (line != null);
        }

        private void backgroundWorker1_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            button2.Enabled = true;
        }
    }
}
