class GCodeDisplay:
    __started = False

    def __init__(self):    
        self.scale = (1, 1)
        self.offset = (0, 0)
        self.preamble = "g21 g64 p0.01"
        self.feed_rate = 50
        self.plunge_feed_rate = 50
        self.spindle_speed = 4500
        self.zsafe = 1.0
        self.ztop = 100.0
        self.depth = 0.1

    def add_point(self, point):
        x, y = self.translate(point)
            
        if self.__started:
            print "g1 x%f y%f" % (x, y)
        else:
            print self.preamble
            print "f%f" % self.plunge_feed_rate
            print "m3 s%d" % self.spindle_speed
            print "g4 p4" # pause 4 seconds
            self.__started = True
            print "g0 z%f" % self.zsafe
            print "g0 x%f y%f" % (x, y)
            print "g1 z%f" % -self.depth
            print "f%f" % self.feed_rate
            self.__xstart = x
            self.__ystart = y

    def close_curve(self):
        print "g1 x%f y%f" % (self.__xstart, self.__ystart)
        print "g0 z%f" % self.zsafe
        self.__started = False
        
    def close(self):
        print "g0 z%f" % self.ztop
        print "m30"
    
    def translate(self, point):
        x, y = point
        xscale, yscale = self.scale
        xoff, yoff = self.offset
        return (xscale*x + xoff, yscale*y + yoff)
