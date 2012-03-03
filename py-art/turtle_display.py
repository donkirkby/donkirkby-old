import turtle

class TurtleDisplay:
    __start_point = None
    __scale = (2, -2)
    __offset = (-250, 250)
    
    def add_point(self, point):
        x, y = self.translate(point)
        if self.__start_point:
            turtle.goto(x, y) #@UndefinedVariable
        else:
            turtle.speed(0) #@UndefinedVariable
            self.__start_point = point
            turtle.penup() #@UndefinedVariable
            turtle.goto(x, y) #@UndefinedVariable
            turtle.pendown() #@UndefinedVariable

    def close_curve(self):
        x, y = self.translate(self.__start_point)
        turtle.goto(x, y) #@UndefinedVariable
        self.__start_point = None
        
    def close(self):
        print "Press enter to continue."
        raw_input()
    
    def translate(self, point):
        x, y = point
        xscale, yscale = self.__scale
        xoff, yoff = self.__offset
        return (xscale*x + xoff, yscale*y + yoff)