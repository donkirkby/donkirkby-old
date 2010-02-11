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
    public partial class MultiSplitterForm : Form
    {
        public MultiSplitterForm()
        {
            InitializeComponent();
        }

        private void CustomSplitterForm_Load(object sender, EventArgs e)
        {
            if (Settings.Default.MultiSplitterSettings != null)
            {
                Settings.Default.MultiSplitterSettings.Restore(this, splitContainer1, splitContainer2);
            }
        }

        private void CustomSplitterForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (Settings.Default.MultiSplitterSettings == null)
            {
                Settings.Default.MultiSplitterSettings = new WindowSettings();
            }
            Settings.Default.MultiSplitterSettings.Record(this, splitContainer1, splitContainer2);
        }
    }
}
