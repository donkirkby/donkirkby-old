from turtle import Turtle

class TurtleDisplay:
    __start_point = None
    __scale = (2, -2)
    __offset = (-250, 250)
    
    def __init__(self):
        self.__turtle = Turtle()
    
    def add_point(self, point):
        x, y = self.translate(point)
        if self.__start_point:
            self.__turtle.goto(x, y)
        else:
#            self.__turtle.speed(0)
            self.__turtle.tracer(100)
            self.__start_point = point
            self.__turtle.penup()
            self.__turtle.goto(x, y)
            self.__turtle.pendown()

    def close_curve(self):
        x, y = self.translate(self.__start_point)
        self.__turtle.tracer(1)
        self.__turtle.goto(x, y)
        self.__start_point = None
        
    def close(self):
        print "Press enter to continue."
        raw_input()
    
    def translate(self, point):
        x, y = point
        xscale, yscale = self.__scale
        xoff, yoff = self.__offset
        return (xscale*x + xoff, yscale*y + yoff)