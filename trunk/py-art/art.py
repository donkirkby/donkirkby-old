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

def display_3x2(size, display, curve):
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

def display_3x2_with_border(size, display, curve):
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

filename = 'input/bwR.jpg'
filename = '/home/don/Dropbox/Art/balcony.jpg'
filename = 'input/balcony-cropped-levels.jpg'
filename = 'input/balcony-cropped-3x2.jpg'
filename = 'input/Kirkby-trimmed.jpg'
filename = 'input/lena-cropped.jpg'
image = Image.open(filename)

size = 256
display = SvgDisplay('output/lena.svg', 3*size + 22, 2*size + 22)
display.offset = (11,11)
curve = Curve(display, image)
curve.scale = (1, 1)
curve.calculate_levels(4, 0, 0, 3*size, 2*size)

print "Starting."

display_3x2_with_border(size, display, curve)

display.close()
print "Success."
