using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using WindowSettings.Properties;

namespace WindowSettings
{
    public partial class CustomSplitterForm : Form
    {
        public CustomSplitterForm()
        {
            InitializeComponent();
        }

        private void CustomSplitterForm_Load(object sender, EventArgs e)
        {
            WindowSettings.Restore(
                Settings.Default.CustomWindowSettings, 
                this, 
                splitContainer1);
        }

        private void CustomSplitterForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            Settings.Default.CustomWindowSettings = WindowSettings.Record(
                Settings.Default.CustomWindowSettings,
                this, 
                splitContainer1);
        }
    }
}
