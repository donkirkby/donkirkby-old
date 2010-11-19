namespace CountDown
{
    partial class CountDownForm
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
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(CountDownForm));
            this.timeRemaining = new System.Windows.Forms.TextBox();
            this.startStop = new System.Windows.Forms.Button();
            this.timer = new System.Windows.Forms.Timer(this.components);
            this.SuspendLayout();
            // 
            // timeRemaining
            // 
            this.timeRemaining.Location = new System.Drawing.Point(12, 12);
            this.timeRemaining.Name = "timeRemaining";
            this.timeRemaining.Size = new System.Drawing.Size(100, 20);
            this.timeRemaining.TabIndex = 0;
            this.timeRemaining.Text = "00:20";
            this.timeRemaining.TextChanged += new System.EventHandler(this.timeRemaining_TextChanged);
            // 
            // startStop
            // 
            this.startStop.Location = new System.Drawing.Point(118, 10);
            this.startStop.Name = "startStop";
            this.startStop.Size = new System.Drawing.Size(75, 23);
            this.startStop.TabIndex = 1;
            this.startStop.Text = "Start";
            this.startStop.UseVisualStyleBackColor = true;
            this.startStop.Click += new System.EventHandler(this.startStop_Click);
            // 
            // timer
            // 
            this.timer.Tick += new System.EventHandler(this.timer_Tick);
            // 
            // CountDownForm
            // 
            this.AcceptButton = this.startStop;
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(206, 43);
            this.Controls.Add(this.startStop);
            this.Controls.Add(this.timeRemaining);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "CountDownForm";
            this.Text = "20 minutes";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox timeRemaining;
        private System.Windows.Forms.Button startStop;
        private System.Windows.Forms.Timer timer;
    }
}

