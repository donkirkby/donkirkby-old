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
            if (Settings.Default.CustomWindowSettings != null)
            {
                Settings.Default.CustomWindowSettings.Restore(this, splitContainer1);
            }
        }

        private void CustomSplitterForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (Settings.Default.CustomWindowSettings == null)
            {
                Settings.Default.CustomWindowSettings = new WindowSettings();
            }
            Settings.Default.CustomWindowSettings.Record(this, splitContainer1);
        }
    }
}
