import cairo

class SvgDisplay:
    __started = False

    def __init__(self, filename, width, height):    
        fo = file(filename, 'w')

        ## Prepare a destination surface -> out to an SVG file!
        self.__surface = cairo.SVGSurface (fo, width, height)
        self.__ctx = cairo.Context(self.__surface)
        self.scale = (1, 1)
        self.offset = (0, 0)
        self.length = 0
        
    

    def add_length(self, x, y):
        self.length += abs(x - self.__xprev) + abs(y - self.__yprev)

    def add_point(self, point):
        x, y = self.translate(point)
            
        if self.__started:
            self.__ctx.line_to (x, y)
            self.add_length(x, y)
        else:
            self.__started = True
            self.__ctx.move_to(x, y)
            self.__xstart = x
            self.__ystart = y
        self.__xprev = x
        self.__yprev = y

    def close_curve(self):
        self.__ctx.close_path()
        self.__ctx.set_source_rgb(0, 0, 0)
        self.__ctx.set_line_width (0.25)
        self.__ctx.stroke ()
        self.add_length(self.__xstart, self.__ystart)
        print "Total length is %d" % self.length
        self.__started = False
        
    def close(self):
        self.__surface.finish()
    
    def translate(self, point):
        x, y = point
        xscale, yscale = self.scale
        xoff, yoff = self.offset
        return (xscale*x + xoff, yscale*y + yoff)
