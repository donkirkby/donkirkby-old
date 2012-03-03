class Curve(object):
    def __init__(self, display, image):
        self.__display = display
        self.__image = image
        self.scale = (1, 1)
        
    def draw_cell(self, entryPoint, exitPoint, sign):
        if entryPoint == exitPoint:
            return
        xin, yin = entryPoint
        xout, yout = exitPoint
        dx = xout-xin
        dy = yout-yin
        x2 = xin + dy*sign
        y2 = yin - dx*sign
        x3 = xout + dy*sign
        y3 = yout - dx*sign
        
        size = max(abs(dx), abs(dy)) + 1
        xmin = min(xin, x2, x3, xout)
        xmax = xmin+size
        ymin = min(yin, y2, y3, yout)
        ymax = ymin+size
        
        xscale, yscale = self.scale
        total = 0
        for x in range(xmin, xmax):
            for y in range(ymin, ymax):
                total += self.__image.getpixel((xscale*x,yscale*y))
        area = size*size
        intensity = total/area/255.0
        if (1 - intensity) < (1.0 / size):
            self.__display.add_point(exitPoint)
        else:
            half_size = size / 2
            step_dx = cmp(dx, 0) # get sign
            step_dy = cmp(dy, 0) # get sign
            half_dx = (half_size-1) * step_dx
            half_dy = (half_size-1) * step_dy
            entry1 = entryPoint
            exit1 = (xin + half_dy*sign, yin - half_dx*sign)
            entry2 = (exit1[0] + step_dy*sign, exit1[1] - step_dx*sign)
            exit2 = (entry2[0] + half_dx, entry2[1] + half_dy)
            entry3 = (exit2[0] + step_dx, exit2[1] + step_dy)
            exit3 = (entry3[0] + half_dx, entry3[1] + half_dy)
            entry4 = (exit3[0] - step_dy*sign, exit3[1] + step_dx*sign)
            exit4 = exitPoint
            self.draw_cell(entry1, exit1, -sign)
            self.__display.add_point(entry2)
            self.draw_cell(entry2, exit2, sign)
            self.__display.add_point(entry3)
            self.draw_cell(entry3, exit3, sign)
            self.__display.add_point(entry4)
            self.draw_cell(entry4, exit4, -sign)
        
