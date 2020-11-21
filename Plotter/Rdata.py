## Copyright (C) 2010 S.Box
## Rdata.py

## Author: S.Box
## Created: 2011-10-27

#from matplotlib.mathtext import __init__
import xml.dom.minidom as md
import numpy as np


class Rdata:
    """this class handles the trajectory data, different dimensions and flight stats (if available)
    """

    def __init__(self,FN):
        """initalises Rdata structure based on a filename

        input arguments:
        FN -- filename location
        """


        self.FileName = FN
        self.mod = md.parse(self.FileName)
        self.FlightStats = self.mod.getElementsByTagName("FlightStats")[0]
        self.FlightData = self.mod.getElementsByTagName("FlightData")[0]

        self.X_TR = self.Read3VectorFromXML(self.FlightData, "Position")


    def ReadScalarFromXML(self,DOM,Name):
        """returns a scalar based on XML node

        arg:
        DOM -- XML tree
        Name -- name of element to find
        """
        Snode = DOM.getElementsByTagName(Name)[0]
        return float(Snode.firstChild.data.split(';')[0])

    def ReadVectorFromXML(self,DOM,Name):
        """returns a vector based on XML node

        arg:
        DOM -- XML tree
        Name -- name of element to find
        """
        Vnode = DOM.getElementsByTagName(Name)[0]
        VsArray = Vnode.firstChild.data.split(';')[0].split(',')
        VnArray = [float(s) for s in VsArray]
        return np.array(VnArray)

    def Read3VectorFromXML(self,DOM,Name):
        """returns a triple vector based on XML node (used for x,y,z position)

        arg:
        DOM -- XML tree
        Name -- name of element to find
        """
        Vnode = DOM.getElementsByTagName(Name)[0]
        VsArrayX = Vnode.firstChild.data.split(';')[0].split(',')
        VsArrayY = Vnode.firstChild.data.split(';')[1].split(',')
        VsArrayZ = Vnode.firstChild.data.split(';')[2].split(',')
        VnArrayX = [float(s) for s in VsArrayX]
        VnArrayY = [float(s) for s in VsArrayY]
        VnArrayZ = [float(s) for s in VsArrayZ]
        VG = VectorGroup(np.array(VnArrayX),np.array(VnArrayY),np.array(VnArrayZ))
        return(VG)

    def GetStatsArray(self,DOM,Name):
        """returns an array based on XML node

        arg:
        DOM -- XML tree
        Name -- name of element to find
        """
        Arr = np.array([0,0,0])
        for Stat in DOM:
            VG = self.ReadVectorFromXML(Stat,Name)
            Arr = np.vstack((Arr,VG))

        Arr.transpose()
        VG = VectorGroup(Arr[1:,0],Arr[1:,1],Arr[1:,2])
        return(VG)

    def getText(self,nodelist):
        """returns text of all specified nodes

        arg:
        nodelist -- returns the text of these nodes
        """
        rc = ""
        for node in nodelist:
            if node.nodeType == node.TEXT_NODE:
                rc = rc + node.data

        return(rc)

    def UserPlotData(self,AxisXS,AxisYS):
        """returns the axis limits in a dictionary

        args:
        AxisXS -- axis label for x axis
        AxisYS -- axis label for y axis
        """

        AxisN=[]
        AxisS=[AxisXS,AxisYS]

        for Axis in AxisS:
            Sarray = Axis.split(':')

            RootNodeList = {
            "Total": self.mod.getElementsByTagName("Run"),
            "Lower": self.mod.getElementsByTagName("LowerStage"),
            "Upper": self.mod.getElementsByTagName("UpperStage")
            }[Sarray[1]]

            Node = RootNodeList[int(Sarray[2])-1]

            Larray = Sarray[0].split('_')
            if Larray[1]== '0':
                AxisN.append(self.ReadVectorFromXML(Node, Larray[0]))

            else:
                VG = self.Read3VectorFromXML(Node, Larray[0])

                dict = {'X':VG.X, 'Y':VG.Y, 'Z':VG.Z, 'Mag':VG.Mag}

                AxisN.append( dict[Larray[1]])

        return AxisN


class RdataOSF(Rdata):
    """class to store data for one stage rocket, nominal flight"""

    def __init__(self,Filename):
        """initalise based on filename

        args:
        Filename -- points to XML file
        """
        Rdata.__init__(self,Filename)

class RdataTSF(Rdata):
    """class to store data for two stage rocket, nominal flight"""

    def __init__(self,Filename):
        """initialise class based on filename

        args:
        Filename -- points to XML file
        """
        Rdata.__init__(self,Filename)
        self.FlightStatsU = self.mod.getElementsByTagName("FlightStats")[1]
        self.FlightDataU = self.mod.getElementsByTagName("FlightData")[1]

        self.X_US = self.Read3VectorFromXML(self.FlightDataU, "Position")

class RdataOSM(RdataOSF):
    """class to store data for a one stage rocket, monte carlo"""

    def __init__(self, Filename):
        """initialise class based on filename

        args:
        Filename -- points to XML file
        """
        RdataOSF.__init__(self,Filename)

        ListStats = self.mod.getElementsByTagName("FlightStats")

        self.ApoVg = self.GetStatsArray(ListStats,"Apogee")
        self.LandVg = self.GetStatsArray(ListStats,"Landing")

        ListData = self.mod.getElementsByTagName("FlightData")

        self.FpathList = []
        for Data in ListData:
            self.FpathList.append(self.Read3VectorFromXML(Data,"Position"))

class RdataTSM(RdataTSF):
    """class to store data for a two stage rocket, monte carlo"""

    def __init__(self,Filename):
        """initialise class based on filename

        args:
        Filename -- points to XML file
        """
        RdataTSF.__init__(self,Filename)

        ListLowerStages = self.mod.getElementsByTagName("LowerStage")
        ListUpperStages = self.mod.getElementsByTagName("UpperStage")

        LStat = md.Element("TempNode")

        for Lnode in ListLowerStages:
            LStat.appendChild(Lnode)

        LStatList = LStat.getElementsByTagName("FlightStats")
        LDataList = LStat.getElementsByTagName("FlightData")

        self.LApoVg = self.GetStatsArray(LStatList,"Apogee")
        self.LLandVg = self.GetStatsArray(LStatList,"Landing")

        self.LFpathList = []
        for LData in LDataList:
            self.LFpathList.append(self.Read3VectorFromXML(LData, "Position"))


        UStat = md.Element("TempNode")

        for Unode in ListUpperStages:
            UStat.appendChild(Unode)

        UStatList = UStat.getElementsByTagName("FlightStats")
        UDataList = UStat.getElementsByTagName("FlightData")

        self.UApoVg = self.GetStatsArray(UStatList,"Apogee")
        self.ULandVg = self.GetStatsArray(UStatList,"Landing")

        self.UFpathList = []
        for UData in UDataList:
            self.UFpathList.append(self.Read3VectorFromXML(UData,"Position"))

class VectorGroup:
    """this class is specifically here for x, y, z coordinates, with the ability to calculate the magnitude."""

    X=[]
    Y=[]
    Z=[]
    Mag=[]

    def __init__(self,x,y,z):
        """initialises the class and calculates the magnitude

        args:
        x -- coords for x axis
        y -- coords for y axis
        z -- coords for z axis
        """
        self.X = x
        self.Y = y
        self.Z = z

        self.Mag = np.sqrt(x**2+y**2+z**2)
