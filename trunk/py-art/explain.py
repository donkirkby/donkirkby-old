from svg_display import SvgDisplay
from math import cos, sin, pi

size = 8
display = SvgDisplay('output/explain.svg', 3 * size + 20, 10*size + 40)
display.offset = (10,10)

x = 0
y = 0
heading = 0

def hilbert(level, angle):
    global x, y, heading, display
    if level == 0:
        return

    display.add_point((x, y)) 
    heading -= angle
    hilbert(level - 1, -angle)
    x += cos(heading) * size
    y += sin(heading) * size
    display.add_point((x, y))
    heading += angle
    hilbert(level - 1, angle)
    x += cos(heading) * size
    y += sin(heading) * size
    display.add_point((x, y))
    hilbert(level - 1, angle)
    heading += angle
    x += cos(heading) * size
    y += sin(heading) * size
    display.add_point((x, y))
    hilbert(level - 1, -angle)
    heading -= angle

hilbert(2, -pi/2)
display.end_curve()
display.close()