namespace WindowSettings
{
    partial class WindowSettingsForm
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
            this.databoundSplitterButton = new System.Windows.Forms.Button();
            this.plainSplitterButton = new System.Windows.Forms.Button();
            this.resetButton = new System.Windows.Forms.Button();
            this.customSplitterButton = new System.Windows.Forms.Button();
            this.multiSplitterButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // databoundSplitterButton
            // 
            this.databoundSplitterButton.Location = new System.Drawing.Point(12, 12);
            this.databoundSplitterButton.Name = "databoundSplitterButton";
            this.databoundSplitterButton.Size = new System.Drawing.Size(107, 23);
            this.databoundSplitterButton.TabIndex = 0;
            this.databoundSplitterButton.Text = "Databound Splitter";
            this.databoundSplitterButton.UseVisualStyleBackColor = true;
            this.databoundSplitterButton.Click += new System.EventHandler(this.databoundSplitterButton_Click);
            // 
            // plainSplitterButton
            // 
            this.plainSplitterButton.Location = new System.Drawing.Point(125, 12);
            this.plainSplitterButton.Name = "plainSplitterButton";
            this.plainSplitterButton.Size = new System.Drawing.Size(107, 23);
            this.plainSplitterButton.TabIndex = 1;
            this.plainSplitterButton.Text = "Plain Splitter";
            this.plainSplitterButton.UseVisualStyleBackColor = true;
            this.plainSplitterButton.Click += new System.EventHandler(this.plainSplitterButton_Click);
            // 
            // resetButton
            // 
            this.resetButton.Location = new System.Drawing.Point(12, 70);
            this.resetButton.Name = "resetButton";
            this.resetButton.Size = new System.Drawing.Size(107, 23);
            this.resetButton.TabIndex = 3;
            this.resetButton.Text = "Reset";
            this.resetButton.UseVisualStyleBackColor = true;
            this.resetButton.Click += new System.EventHandler(this.resetButton_Click);
            // 
            // customSplitterButton
            // 
            this.customSplitterButton.Location = new System.Drawing.Point(12, 41);
            this.customSplitterButton.Name = "customSplitterButton";
            this.customSplitterButton.Size = new System.Drawing.Size(107, 23);
            this.customSplitterButton.TabIndex = 2;
            this.customSplitterButton.Text = "Custom Splitter";
            this.customSplitterButton.UseVisualStyleBackColor = true;
            this.customSplitterButton.Click += new System.EventHandler(this.customSplitterButton_Click);
            // 
            // multiSplitterButton
            // 
            this.multiSplitterButton.Location = new System.Drawing.Point(125, 41);
            this.multiSplitterButton.Name = "multiSplitterButton";
            this.multiSplitterButton.Size = new System.Drawing.Size(107, 23);
            this.multiSplitterButton.TabIndex = 4;
            this.multiSplitterButton.Text = "Multiple Splitter";
            this.multiSplitterButton.UseVisualStyleBackColor = true;
            this.multiSplitterButton.Click += new System.EventHandler(this.multiSplitterButton_Click);
            // 
            // WindowSettingsForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(292, 266);
            this.Controls.Add(this.multiSplitterButton);
            this.Controls.Add(this.customSplitterButton);
            this.Controls.Add(this.resetButton);
            this.Controls.Add(this.plainSplitterButton);
            this.Controls.Add(this.databoundSplitterButton);
            this.Name = "WindowSettingsForm";
            this.Text = "Window Settings";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.WindowSettingsForm_FormClosed);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button databoundSplitterButton;
        private System.Windows.Forms.Button plainSplitterButton;
        private System.Windows.Forms.Button resetButton;
        private System.Windows.Forms.Button customSplitterButton;
        private System.Windows.Forms.Button multiSplitterButton;
    }
}

