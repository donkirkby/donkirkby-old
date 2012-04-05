from turtle import Turtle
from Tkinter import Frame, Label, StringVar, IntVar, DoubleVar, Entry, Text, \
    E, W, Checkbutton, Button
    
version = "1.0"

class Application(Frame):

    def __init__(self, master=None):
        Frame.__init__(self, master)
        self.grid()
        self.createWidgets()
        self.turtle = Turtle()

    def createWidgets(self):       
        self.EntryFrame = Frame(self,bd=5)
        self.EntryFrame.grid(row=0, column=1)
        
        i = 0
        Label(self.EntryFrame, text='Type some Python code\n').grid(row=i, column=0, columnspan=2)

        i += 1
        Label(self.EntryFrame, text='The Code').grid(row=i, column=0, sticky=E)

        self.text = Text(self.EntryFrame, width=50, height=10)
        self.text.grid(row=i, column=1)
        
        self.EntryFrame.after(1000, self.runCode)
    
    def runCode(self):
        t = self.turtle
        script = self.text.get("1.0", "end-1c")
        try:
            t.tracer(100000)
            t.reset()
            exec script in globals(), locals()
            t.tracer(1)
        except:
            pass
        
        self.EntryFrame.after(1000, self.runCode)

app = Application()
app.master.title("live-py-"+version+".py by Don Kirkby ")
app.mainloop()

turtle = Turtle()
turtle.fd(100)

print "Press enter to continue."
raw_input()