"""
simple python script to plot result
"""


import matplotlib as mpl
from mpl_toolkits.mplot3d import Axes3D
import numpy as np
import matplotlib.pyplot as plt

listY = []


with open('output.csv') as csvFile:
	for thisLine in csvFile.readlines():
		Y = eval(thisLine)
		listY.append(Y)


fig = plt.figure()
ax = Axes3D(fig)
# ax.set_aspect("equal")

i = 1

for Y in listY:
	ax.plot(Y[0],Y[1],Y[2],label=str(i))
	i += 1

plt.show()
