using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using WindowSettings.Properties;
using System.Diagnostics;

namespace WindowSettings
{
    public partial class WindowSettingsForm : Form
    {
        public WindowSettingsForm()
        {
            InitializeComponent();
        }

        private void databoundSplitterButton_Click(object sender, EventArgs e)
        {
            new DataboundSplitterForm().Show();
        }

        private void plainSplitterButton_Click(object sender, EventArgs e)
        {
            new PlainSplitterForm().Show();
        }

        private void resetButton_Click(object sender, EventArgs e)
        {
            Settings.Default.Reset();
        }

        private void customSplitterButton_Click(object sender, EventArgs e)
        {
            new CustomSplitterForm().Show();
        }

        private void WindowSettingsForm_FormClosed(object sender, FormClosedEventArgs e)
        {
            Settings.Default.Save();
        }

        private void multiSplitterButton_Click(object sender, EventArgs e)
        {
            new MultiSplitterForm().Show();
        }
    }
}
