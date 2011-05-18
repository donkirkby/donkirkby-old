using System;
using System.Diagnostics;
using System.Collections.Generic;
using System.Windows.Forms;
using System.Drawing;

namespace WindowSettings
{
	/* Author: Don Kirkby http://donkirkby.googlecode.com/
	 * Released under the MIT licence http://www.opensource.org/licenses/mit-license.php
	 * Installation:
	 * - Copy this file into your project.
	 * - Change the namespace above to match your project's namespace.
	 * - Compile your project.
	 * - Edit the project properties using the Project Properties... item in 
	 * the project menu.
	 * - Go to the settings tab.
	 * - Add a new setting for each form whose position you want to save, and 
	 * type a name for it like MainFormPosition.
	 * - In the type column, select Browse... from the bottom of the list.
	 * - You won't see WindowSettings in the list, but you can just type the
	 * namespace and class name, and click OK. For example, if you changed this
	 * class's namespace to UltimateApp, then you would type 
	 * UltimateApp.WindowSettings and click OK.
	 * - Add Load and FormClosing event handlers to any forms you want to save.
	 * See the forms in this project for example code.
	 * - Add a call to Settings.Default.Save() somewhere in your shutdown code.
	 * The FormClosed event of your main form is a good spot. If you have 
	 * subforms open, you may have to explicitly call their FormClosing events 
	 * when shutting down the app, because they're not called by default.
	 */
    /// <summary>
    /// Serializes the location, size, and other state, of several controls to 
    /// a single application setting. Then you can just create a 
    /// setting of this type for each form in the application, save on close, 
    /// and restore on load.
    /// </summary>
	[Serializable()]
	public class WindowSettings
	{
		public ControlSetting[] settings { get; set; }

        /// <summary>
        /// Record the position and state of several controls.
        /// </summary>
        /// <param name="windowSettings">Where the settings should be recorded,
        /// or null.</param>
        /// <param name="controls">The controls to record. You can change 
        /// some entries to null if you no longer use those positions in the 
        /// list.</param>
        /// <returns>The windowSettings parameter, or a new WindowSettings 
        /// object if that was null.</returns>
        public static WindowSettings Record(
            WindowSettings windowSettings,
            params Control[] controls)
        {
            if (windowSettings == null)
            {
                windowSettings = new WindowSettings();
            }
            windowSettings.Record(controls);
            return windowSettings;
        }

        /// <summary>
        /// Record the position and state of several controls.
        /// </summary>
        /// <param name="controls">The controls to record. You can change 
        /// some entries to null if you no longer use those positions in the 
        /// list.</param>
        public void Record(params Control[] controls)
		{
			settings = new ControlSetting[controls.Length];

			int i = 0;
			foreach (Control c in controls)
			{
				ControlSetting s = ControlSetting.Create(c);
				if (s != null)
				{
					settings[i] = s;
					s.Save(c);
				}
				++i;
			}
		}

        /// <summary>
        /// Restore the position and state of several controls.
        /// </summary>
        /// <param name="windowSettings">Holds the settings to restore.</param>
        /// <param name="controls">The controls to restore. You can change 
        /// some entries to null if you no longer use those positions in the 
        /// list.</param>
        public static void Restore(
            WindowSettings windowSettings,
            params Control[] controls)
        {
            if (windowSettings != null)
            {
                windowSettings.Restore(controls);
            }
        }

        /// <summary>
        /// Restore the position and state of several controls.
        /// </summary>
        /// <param name="controls">The controls to restore. You can change 
        /// some entries to null if you no longer use those positions in the 
        /// list.</param>
        public void Restore(params Control[] controls)
		{
			if (settings != null)
			{
				for (int i = 0; i < controls.Length && i < settings.Length; i++)
				{
					Control control = controls[i];
					ControlSetting setting = settings[i];
					if (control != null &&
						setting != null &&
						setting.IsOf(control))
					{
						setting.Restore(control);
					}
				}
			}
		}

        [Serializable()]
        [System.Xml.Serialization.XmlInclude(typeof(FormSetting))]
        [System.Xml.Serialization.XmlInclude(typeof(SplitterSetting))]
        public abstract class ControlSetting
        {
            public abstract void Save(Control control);
            public abstract void Restore(Control control);
            public abstract bool IsOf(Control control);

            // Little factory - add new types here
            public static ControlSetting Create(Control c)
            {
                if (c is Form) return new FormSetting();
                else if (c is SplitContainer) return new SplitterSetting();
                else return null;
            }
        }

        [Serializable()]
        public class FormSetting : ControlSetting
        {
            public Point Location { get; set; }
            public Size Size { get; set; }
            public FormWindowState WindowState { get; set; }

            public override void Save(Control control)
            {
                Form form = control as Form;
                if (form != null)
                {
                    switch (form.WindowState)
                    {
                        case FormWindowState.Maximized:
                            RecordWindowPosition(form.RestoreBounds);
                            break;
                        case FormWindowState.Normal:
                            RecordWindowPosition(form.Bounds);
                            break;
                        default:
                            // Don't record anything when closing while minimized.
                            return;
                    }
                    WindowState = form.WindowState;
                }
            }

            public override void Restore(Control control)
            {
                Form form = control as Form;
                if (form != null)
                {
                    form.WindowState = WindowState;
                    form.Location = Location;
                    form.Size = Size;
                    Debug.WriteLine(String.Format(
                        "Restore: Location: {0}, Size: {1}", 
                        Location, 
                        Size));
                }
            }

            public override bool IsOf(Control control)
            {
                return control is Form;
            }

            private bool RecordWindowPosition(Rectangle bounds)
            {
                bool isOnScreen = IsOnScreen(bounds.Location, bounds.Size);
                if (isOnScreen)
                {
                    Location = bounds.Location;
                    Size = bounds.Size;
                    Debug.WriteLine(String.Format(
                        "Save: Location: {0}, Size: {1}", 
                        Location, 
                        Size));
                }
                else
                {
                    Debug.WriteLine("Save: Not on screen!");
                }
                return isOnScreen;
            }

            private bool IsOnScreen(Point location, Size size)
            {
                return IsOnScreen(location) && IsOnScreen(location + size);
            }

            private bool IsOnScreen(Point location)
            {
                foreach (var screen in Screen.AllScreens)
                {
                    Rectangle workingArea = new Rectangle(
                        screen.WorkingArea.Location,
                        screen.WorkingArea.Size);
                    workingArea.Inflate(1, 1);
                    if (workingArea.Contains(location))
                    {
                        return true;
                    }
                }
                return false;
            }
        }

        [Serializable()]
        public class SplitterSetting : ControlSetting
        {
            public int distance { get; set; }
            public int size { get; set; }

            public override void Save(Control control)
            {
                SplitContainer splitContainer = control as SplitContainer;
                if (splitContainer != null)
                {
                    distance = splitContainer.SplitterDistance;
                    size = GetSplitterSize(splitContainer);
                }
            }

            public override void Restore(Control control)
            {
                SplitContainer splitter = control as SplitContainer;
                if (splitter != null)
                {
                    int curSplitterSize = GetSplitterSize(splitter);
                    int splitterDistance = distance * curSplitterSize / size;
                    if (splitter.Panel1MinSize <= splitterDistance &&
                        splitterDistance <= curSplitterSize - splitter.Panel2MinSize)
                    {
                        splitter.SplitterDistance = splitterDistance;
                    }
                }
            }

            public override bool IsOf(Control control)
            {
                return control is SplitContainer;
            }

            private static int GetSplitterSize(SplitContainer splitter)
            {
                int splitterSize =
                    splitter.Orientation == Orientation.Vertical
                    ? splitter.Width
                    : splitter.Height;
                return splitterSize;
            }
        }
    }
}