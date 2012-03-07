from svg_display import SvgDisplay
from gcode_display import GCodeDisplay
from curve import Curve
from Tkinter import *
import Image
import os
import ConfigParser

version = '1'
IN_AXIS = os.environ.has_key("AXIS_PROGRESS_BAR")

class Application(Frame):

    def __init__(self, master=None):
        Frame.__init__(self, master)
        self.DEFAULT_SECTION = 'DEFAULT'
        
        self.grid()
        self.createWidgets()

    def createWidgets(self):
        self.__config = ConfigParser.ConfigParser()
        self.__config.set(self.DEFAULT_SECTION, 'input', '~/input.jpg')
        self.__config.set(self.DEFAULT_SECTION, 'inverted', '0')
        self.__config.set(self.DEFAULT_SECTION, 'output', '~/output.svg')
        self.__config.set(self.DEFAULT_SECTION, 'preamble', 'G17 G21 G90 G64 P0.1 S4500 M3 M8')
        self.__config.set(self.DEFAULT_SECTION, 'size', '256')
        self.__config.set(self.DEFAULT_SECTION, 'tool_diameter', '3.175')
        self.__config.read(os.path.expanduser('~/.py-art.cfg'))
        
        self.EntryFrame = Frame(self,bd=5)
        self.EntryFrame.grid(row=0, column=1)
        
        i = 0
        Label(self.EntryFrame, text='Engrave an image\n').grid(row=i, column=0, columnspan=2)

        i += 1
        Label(self.EntryFrame, text='Input file').grid(row=i, column=0, sticky=E)

        self.InputVar = StringVar()
        self.InputVar.set(self.__config.get(self.DEFAULT_SECTION, 'input'))
        Entry(self.EntryFrame, textvariable=self.InputVar ,width=50).grid(row=i, column=1)

        i += 1
        Label(self.EntryFrame, text='Size').grid(row=i, column=0, sticky=E)

        self.SizeVar = IntVar()
        self.SizeVar.set(self.__config.get(self.DEFAULT_SECTION, 'size'))
        Entry(self.EntryFrame, textvariable=self.SizeVar ,width=50).grid(row=i, column=1)

        i += 1
        Label(self.EntryFrame, text='Image effects').grid(row=i, column=0, sticky=E)
        self.InvertedVar = IntVar()
        self.InvertedVar.set(self.__config.getint(self.DEFAULT_SECTION, 'inverted'))
        Checkbutton(self.EntryFrame, text='Inverted', variable=self.InvertedVar).grid(row=i, column=1,sticky=W)

        if IN_AXIS:
            i += 1
            Label(self.EntryFrame, text='Preamble').grid(row=i, column=0, sticky=E)

            self.PreambleVar = StringVar()
            self.PreambleVar.set(self.__config.get(self.DEFAULT_SECTION, 'preamble'))
            Entry(self.EntryFrame, textvariable=self.PreambleVar ,width=50).grid(row=i, column=1)

            i += 1
            Label(self.EntryFrame, text='Tool diameter').grid(row=i, column=0, sticky=E)

            self.ToolDiameterVar = DoubleVar()
            self.ToolDiameterVar.set(self.__config.get(self.DEFAULT_SECTION, 'tool_diameter'))
            Entry(self.EntryFrame, textvariable=self.ToolDiameterVar ,width=50).grid(row=i, column=1)

            i += 1
            Button(self.EntryFrame, text='Trace boundary in AXIS and Quit',command=self.WriteBoundaryTraceToAxis).grid(row=i, column=1, sticky=E)
            
            i += 1
            Button(self.EntryFrame, text='Engrave in AXIS and Quit',command=self.WriteEngravingToAxis).grid(row=i, column=1, sticky=E)

            i += 1
            Button(self.EntryFrame, text='Cut boundary in AXIS and Quit',command=self.WriteBoundaryCutToAxis).grid(row=i, column=1, sticky=E)
        else:
            i += 1
            Label(self.EntryFrame, text='Output file').grid(row=i, column=0, sticky=E)
    
            self.OutputVar = StringVar()
            self.OutputVar.set(self.__config.get(self.DEFAULT_SECTION, 'output'))
            Entry(self.EntryFrame, textvariable=self.OutputVar ,width=50).grid(row=i, column=1)
    
            i += 1
            Button(self.EntryFrame, text='Write to SVG and Quit', command=self.WriteToSvg).grid(row=i, column=1, sticky=E)
        
        
    def WriteConfig(self):
        with open(os.path.expanduser('~/.py-art.cfg'), 'wb') as configfile:
            self.__config.set(self.DEFAULT_SECTION, 'input', self.InputVar.get())
            self.__config.set(self.DEFAULT_SECTION, 'inverted', self.InvertedVar.get())
            self.__config.set(self.DEFAULT_SECTION, 'size', self.SizeVar.get())
            if IN_AXIS:
                self.__config.set(self.DEFAULT_SECTION, 'preamble', self.PreambleVar.get())
                self.__config.set(self.DEFAULT_SECTION, 'tool_diameter', self.ToolDiameterVar.get())
            else:
                self.__config.set(self.DEFAULT_SECTION, 'output', self.OutputVar.get())
                
            self.__config.write(configfile)
        

    def DisplayAndQuit(self, display, size):
        image = Image.open(os.path.expanduser(self.InputVar.get()))
        curve = Curve(display, image)
        curve.scale = 1, 1
        curve.is_inverted = self.InvertedVar.get()
        curve.calculate_levels(5, 0, 0, 3 * size, 2 * size)
        self.display_3x2_with_border(size, display, curve)
        display.close()
        self.WriteConfig()
        self.quit()

    def WriteToSvg(self):
        size = int(self.SizeVar.get())
        display = SvgDisplay(os.path.expanduser(self.OutputVar.get()), 3*size + 22, 2*size + 22)
        display.offset = (11,11)
        
        self.DisplayAndQuit(display, size)
        
    def WriteBoundaryTraceToAxis(self):
        size = int(self.SizeVar.get())
        tool_diameter = self.ToolDiameterVar.get()
        display = self.CreateGCodeDisplay()
        xscale, yscale = display.scale
        xscale = abs(xscale)
        yscale = abs(yscale)
        xoff, yoff = display.offset
        margin = 10.0 # mm
        
        leftmargin = (-margin - tool_diameter*0.5)/xscale
        rightmargin = 3 * size + (margin + tool_diameter*0.5)/xscale
        topmargin = (-margin - tool_diameter*0.5)/yscale
        bottommargin = 2 * size + (margin + tool_diameter*0.5)/yscale
        display.add_point((leftmargin, 0))
        display.add_point((leftmargin, 2 * size))
        display.add_point((0, bottommargin))
        display.add_point((3*size, bottommargin))
        display.add_point((rightmargin, 2*size))
        display.add_point((rightmargin, 0))
        display.add_point((3*size, topmargin))
        display.add_point((0, topmargin))
        display.close_curve()
        display.close()

        self.WriteConfig()
        self.quit()
        
    def WriteBoundaryCutToAxis(self):
        size = int(self.SizeVar.get())
        display = self.CreateGCodeDisplay()
        
        display.add_point((0, 0))
        display.add_point((size, 0))
        display.add_point((size, size))
        display.close_curve()
        display.close()

        self.WriteConfig()
        self.quit()
        

    def CreateGCodeDisplay(self):
        display = GCodeDisplay()
        display.preamble = self.PreambleVar.get()
        display.scale = 0.3, -0.3
        display.offset = (-100, 100)
        display.feed_rate = 1200
        return display

    def WriteEngravingToAxis(self):
        size = int(self.SizeVar.get())
        display = self.CreateGCodeDisplay()
        
        self.DisplayAndQuit(display, size)

    def display_rectangle(self, size, display, curve):
        display.add_point((-1, -1))
        display.add_point((-1, size))
        display.add_point((size * 2, size))
        display.add_point((size * 2, -1))
        display.close_curve()
        
        curve.draw_cell((size - 1, 0), (size - 1, size - 1), -1)
        display.add_point((size, size - 1))
        curve.draw_cell((size, size - 1), (size, 0), -1)
        display.add_point((size - 1, 0))
        display.close_curve()
    
    def display_square(self, size, display, curve):
        display.add_point((-1, -1))
        display.add_point((-1, size))
        display.add_point((size, size))
        display.add_point((size, -1))
        display.close_curve()
        
        curve.draw_cell((size/2 - 1, 0), (size/2 - 1, size/2 - 1), -1)
        display.add_point((size/2 - 1, size/2))
        curve.draw_cell((size/2 - 1, size/2), (size/2 - 1, size-1), -1)
        display.add_point((size/2, size - 1))
        curve.draw_cell((size/2, size - 1), (size/2, size/2), -1)
        display.add_point((size/2, size/2 - 1))
        curve.draw_cell((size/2, size/2 - 1), (size/2, 0), -1)
        display.add_point((size/2 - 1, 0))
        display.close_curve()
    
    def display_3x2(self, size, display, curve):
        display.add_point((-1, -1))
        display.add_point((-1, 2*size))
        display.add_point((3*size, 2*size))
        display.add_point((3*size, -1))
        display.close_curve()
        
        curve.draw_cell((0, size - 1), (size - 1, size - 1), 1)
        display.add_point((size, size-1))
        curve.draw_cell((size, size-1), (2*size - 1, size - 1), 1)
        display.add_point((2*size, size - 1))
        curve.draw_cell((2*size, size - 1), (3*size - 1, size - 1), 1)
        display.add_point((3*size - 1, size))
        curve.draw_cell((3*size - 1, size), (2*size, size), 1)
        display.add_point((2*size - 1, size))
        curve.draw_cell((2*size - 1, size), (size, size), 1)
        display.add_point((size - 1, size))
        curve.draw_cell((size - 1, size), (0, size), 1)
        display.add_point((0, size - 1))
        display.close_curve()
    
    def display_3x2_with_border(self, size, display, curve):
        display.add_point((-1, size))
        display.add_point((-1, 2*size))
        display.add_point((3*size, 2*size))
        display.add_point((3*size, -1))
        display.add_point((-1, -1))
        display.add_point((-1, size - 1))
        
        display.add_point((0, size - 1))
        curve.draw_cell((0, size - 1), (size - 1, size - 1), 1)
        display.add_point((size, size-1))
        curve.draw_cell((size, size-1), (2*size - 1, size - 1), 1)
        display.add_point((2*size, size - 1))
        curve.draw_cell((2*size, size - 1), (3*size - 1, size - 1), 1)
        display.add_point((3*size - 1, size))
        curve.draw_cell((3*size - 1, size), (2*size, size), 1)
        display.add_point((2*size - 1, size))
        curve.draw_cell((2*size - 1, size), (size, size), 1)
        display.add_point((size - 1, size))
        curve.draw_cell((size - 1, size), (0, size), 1)
        display.close_curve()

app = Application()
app.master.title("hilbert-art-"+version+".py by Don Kirkby ")
#app.LoadPartNumbers(part_numbers)
#app.DoIt()
app.mainloop()
