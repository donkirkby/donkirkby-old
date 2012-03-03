import Image
from turtle_display import TurtleDisplay
from curve import Curve

image = Image.open('/home/don/Dropbox/Art/balcony.jpg')
#image.show()

display = TurtleDisplay()
curve = Curve(display, image)

print "Starting."
curve.draw_cell((0, 0), (255, 0), -1)
#display.close()

print "Success."
raw_input()