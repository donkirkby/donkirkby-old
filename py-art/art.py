from svg_display import SvgDisplay
from gcode_display import GCodeDisplay
from curve import Curve
from Tkinter import Frame, Label, StringVar, IntVar, DoubleVar, Entry, \
    E, W, Checkbutton, Button
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
        self.__config.set(self.DEFAULT_SECTION, 'width', '152.4') #material can be 1/8" thick
        self.__config.set(self.DEFAULT_SECTION, 'height', '101.6')
        self.__config.set(self.DEFAULT_SECTION, 'margin', '10.0')
        self.__config.set(self.DEFAULT_SECTION, 'thickness', '6.35')
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
            Label(self.EntryFrame, text='Width').grid(row=i, column=0, sticky=E)

            self.WidthVar = DoubleVar()
            self.WidthVar.set(self.__config.get(self.DEFAULT_SECTION, 'width'))
            Entry(self.EntryFrame, textvariable=self.WidthVar ,width=50).grid(row=i, column=1)

            i += 1
            Label(self.EntryFrame, text='Height').grid(row=i, column=0, sticky=E)

            self.HeightVar = DoubleVar()
            self.HeightVar.set(self.__config.get(self.DEFAULT_SECTION, 'height'))
            Entry(self.EntryFrame, textvariable=self.HeightVar ,width=50).grid(row=i, column=1)

            i += 1
            Label(self.EntryFrame, text='Margin').grid(row=i, column=0, sticky=E)

            self.MarginVar = DoubleVar()
            self.MarginVar.set(self.__config.get(self.DEFAULT_SECTION, 'margin'))
            Entry(self.EntryFrame, textvariable=self.MarginVar ,width=50).grid(row=i, column=1)

            i += 1
            Label(self.EntryFrame, text='Tool diameter').grid(row=i, column=0, sticky=E)

            self.ToolDiameterVar = DoubleVar()
            self.ToolDiameterVar.set(self.__config.get(self.DEFAULT_SECTION, 'tool_diameter'))
            Entry(self.EntryFrame, textvariable=self.ToolDiameterVar ,width=50).grid(row=i, column=1)

            i += 1
            Label(self.EntryFrame, text='Thickness').grid(row=i, column=0, sticky=E)

            self.ThicknessVar = DoubleVar()
            self.ThicknessVar.set(self.__config.get(self.DEFAULT_SECTION, 'thickness'))
            Entry(self.EntryFrame, textvariable=self.ThicknessVar ,width=50).grid(row=i, column=1)

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
                self.__config.set(self.DEFAULT_SECTION, 'height', self.ToolDiameterVar.get())
                self.__config.set(self.DEFAULT_SECTION, 'margin', self.ToolDiameterVar.get())
                self.__config.set(self.DEFAULT_SECTION, 'preamble', self.PreambleVar.get())
                self.__config.set(self.DEFAULT_SECTION, 'tool_diameter', self.ToolDiameterVar.get())
                self.__config.set(self.DEFAULT_SECTION, 'thickness', self.ThicknessVar.get())
                self.__config.set(self.DEFAULT_SECTION, 'width', self.ToolDiameterVar.get())
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
        

    def WriteBoundaryMoves(self, display, cut_depth):
        width = self.WidthVar.get()
        height = self.HeightVar.get()
        tool_radius = self.ToolDiameterVar.get() / 2
        corner_radius = 1.0
        display.add_point((-tool_radius, -corner_radius))
        print "g1 y%f z%f" % (-height + corner_radius, -cut_depth)
        print "g3 x%f y%f i%f" % (
            corner_radius, 
            -height - tool_radius, 
            tool_radius + corner_radius)
        print "g1 x%f" % (width - corner_radius)
        print "g3 x%f y%f j%f" % (
            width + tool_radius, 
            -height + corner_radius, 
            tool_radius + corner_radius)
        print "g1 y%f" % (-corner_radius)
        print "g3 x%f y%f i%f" % (
            width - corner_radius, 
            tool_radius, 
            -tool_radius - corner_radius)
        print "g1 x%f" % (corner_radius)
        print "g3 x%f y%f j%f" % (
            -tool_radius, 
            -corner_radius, 
            -tool_radius - corner_radius)

    def WriteBoundaryTraceToAxis(self):
        display = self.CreateGCodeDisplay()
        self.WriteBoundaryMoves(display, cut_depth=0)
        display.close_curve()
        display.close()

        self.WriteConfig()
        self.quit()
        
    def WriteBoundaryCutToAxis(self):
        display = self.CreateGCodeDisplay()
        tool_radius = self.ToolDiameterVar.get() / 2
        thickness = self.ThicknessVar.get()
        zstart = 0.05
        zstep = -tool_radius / 2
        zstop = -thickness - 0.5
        display.depth = -zstart
        
        for cut_depth in range(zstart, zstop, zstep):
            self.WriteBoundaryMoves(display, cut_depth)
        
        display.close_curve()
        display.close()

        self.WriteConfig()
        self.quit()

    def CreateGCodeDisplay(self):
        display = GCodeDisplay()
        display.preamble = self.PreambleVar.get()
        display.feed_rate = 1200
        return display

    def WriteEngravingToAxis(self):
        display = self.CreateGCodeDisplay()
        width = self.WidthVar.get()
        height = self.HeightVar.get()
        margin = self.MarginVar.get()
        size = self.SizeVar.get()
        xscale = (width - 2*margin) / (3*size)
        yscale = (height - 2*margin) / (2*size)
        scale = min(xscale, yscale)
        xoff = width/2 + 1.5*size*scale
        yoff = -height/2 + size*scale
        display.scale = (-scale, -scale)
        display.offset = (xoff, yoff)
        
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
