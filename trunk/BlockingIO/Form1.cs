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
    public partial class Form1 : Form
    {
        private object locker = new object();
        private StringWriter outputWriter = new StringWriter();
        private MemoryStream stream;
        private StreamReader streamReader;
        private StreamWriter streamWriter;

        public Form1()
        {
            InitializeComponent();
            stream = new MemoryStream();
            streamReader = new StreamReader(stream);
            streamWriter = new StreamWriter(stream);
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            lock (locker)
            {
                output.Text = outputWriter.ToString();
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            lock (locker)
            {
                streamWriter.WriteLine(input.Text);
                streamWriter.Flush();
                input.Text = "";
                input.Focus();
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            backgroundWorker1.RunWorkerAsync();
            button2.Enabled = false;
        }

        private void backgroundWorker1_DoWork(object sender, DoWorkEventArgs e)
        {
            int numRead;
            do
            {
                char[] buffer = new char[5];
                numRead = streamReader.ReadBlock(buffer, 0, 5);
                lock (locker)
                {
                    outputWriter.WriteLine("---");
                    outputWriter.Write(buffer);
                }
            } while (numRead > 0);
        }

        private void backgroundWorker1_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            button2.Enabled = true;
        }
    }
}
