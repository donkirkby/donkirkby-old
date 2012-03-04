import Image
from svg_display import SvgDisplay
from curve import Curve


def display_rectangle(size, display, curve):
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

def display_square(size, display, curve):
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

filename = 'input/bwR.jpg'
filename = '/home/don/Dropbox/Art/balcony.jpg'
filename = 'input/balcony-cropped-levels.jpg'
image = Image.open(filename)

size = 512
display = SvgDisplay('output/balcony.svg', 2*size + 2, size + 2)
display.offset = (1,1)
curve = Curve(display, image)
curve.scale = (1, 1)
curve.calculate_levels(6, 0, 0, 2*size, size)

print "Starting."

display_rectangle(size, display, curve)

display.close()
print "Success."
