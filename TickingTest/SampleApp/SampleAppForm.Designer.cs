namespace SampleApp
{
    partial class SampleAppForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.waitForSignal = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.timeoutText = new System.Windows.Forms.TextBox();
            this.statusStrip = new System.Windows.Forms.StatusStrip();
            this.statusLabel = new System.Windows.Forms.ToolStripStatusLabel();
            this.sendSignal = new System.Windows.Forms.Button();
            this.backgroundWaiter = new System.ComponentModel.BackgroundWorker();
            this.sleep = new System.Windows.Forms.Button();
            this.backgroundSleeper = new System.ComponentModel.BackgroundWorker();
            this.statusStrip.SuspendLayout();
            this.SuspendLayout();
            // 
            // waitForSignal
            // 
            this.waitForSignal.Location = new System.Drawing.Point(12, 39);
            this.waitForSignal.Name = "waitForSignal";
            this.waitForSignal.Size = new System.Drawing.Size(94, 23);
            this.waitForSignal.TabIndex = 0;
            this.waitForSignal.Text = "Wait for signal";
            this.waitForSignal.UseVisualStyleBackColor = true;
            this.waitForSignal.Click += new System.EventHandler(this.waitForSignal_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(12, 16);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(62, 13);
            this.label1.TabIndex = 1;
            this.label1.Text = "Timeout (s):";
            // 
            // timeoutText
            // 
            this.timeoutText.Location = new System.Drawing.Point(80, 13);
            this.timeoutText.Name = "timeoutText";
            this.timeoutText.Size = new System.Drawing.Size(51, 20);
            this.timeoutText.TabIndex = 2;
            this.timeoutText.Text = "5";
            // 
            // statusStrip
            // 
            this.statusStrip.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.statusLabel});
            this.statusStrip.Location = new System.Drawing.Point(0, 232);
            this.statusStrip.Name = "statusStrip";
            this.statusStrip.Size = new System.Drawing.Size(292, 24);
            this.statusStrip.TabIndex = 3;
            this.statusStrip.Text = "statusStrip1";
            // 
            // statusLabel
            // 
            this.statusLabel.Name = "statusLabel";
            this.statusLabel.Size = new System.Drawing.Size(52, 19);
            this.statusLabel.Text = "Ready";
            // 
            // sendSignal
            // 
            this.sendSignal.Location = new System.Drawing.Point(13, 69);
            this.sendSignal.Name = "sendSignal";
            this.sendSignal.Size = new System.Drawing.Size(93, 23);
            this.sendSignal.TabIndex = 4;
            this.sendSignal.Text = "Send Signal";
            this.sendSignal.UseVisualStyleBackColor = true;
            this.sendSignal.Click += new System.EventHandler(this.sendSignal_Click);
            // 
            // backgroundWaiter
            // 
            this.backgroundWaiter.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWaiter_DoWork);
            this.backgroundWaiter.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWaiter_RunWorkerCompleted);
            // 
            // sleep
            // 
            this.sleep.Location = new System.Drawing.Point(13, 99);
            this.sleep.Name = "sleep";
            this.sleep.Size = new System.Drawing.Size(93, 23);
            this.sleep.TabIndex = 5;
            this.sleep.Text = "Sleep";
            this.sleep.UseVisualStyleBackColor = true;
            this.sleep.Click += new System.EventHandler(this.sleep_Click);
            // 
            // backgroundSleeper
            // 
            this.backgroundSleeper.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundSleeper_DoWork);
            this.backgroundSleeper.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundSleeper_RunWorkerCompleted);
            // 
            // SampleAppForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(292, 256);
            this.Controls.Add(this.sleep);
            this.Controls.Add(this.sendSignal);
            this.Controls.Add(this.statusStrip);
            this.Controls.Add(this.timeoutText);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.waitForSignal);
            this.Name = "SampleAppForm";
            this.Text = "Sample App";
            this.statusStrip.ResumeLayout(false);
            this.statusStrip.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button waitForSignal;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox timeoutText;
        private System.Windows.Forms.StatusStrip statusStrip;
        private System.Windows.Forms.ToolStripStatusLabel statusLabel;
        private System.Windows.Forms.Button sendSignal;
        private System.ComponentModel.BackgroundWorker backgroundWaiter;
        private System.Windows.Forms.Button sleep;
        private System.ComponentModel.BackgroundWorker backgroundSleeper;
    }
}

