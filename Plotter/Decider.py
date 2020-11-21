## Copyright (C) 2010 S.Box
## Decider.py

## Author: S.Box
## Created: 2011-10-27


# To change this template, choose Tools | Templates
# and open the template in the editor.

__author__="SIMON"
__date__ ="$18-Jan-2010 13:10:01$"

import xml.dom.minidom as md
import Rdata as Rd
import PlotBase as Pb

class Decider:
    """This class decides which subfunctions to use, based on Function element
    """

    def __init__(self,Filename):
        """Initialise the class with a Filename and Function found in xml

        Keyword arguments:
        Filename -- name of the XML file
        """
        self.Filename = Filename
        DOM = md.parse(Filename)
        Fnode = DOM.getElementsByTagName("Function")[0]
        self.Function = Fnode.firstChild.data


    def FlightPlots(self):
        """This function links the name of the Function (set at init), to the relevant function.
        """
        {
        "OneStageFlight":self.OSFProcess,
        "TwoStageFlight":self.TSFProcess,
        "OneStageMonte":self.OSMProcess,
        "TwoStageMonte":self.TSMProcess
        }[self.Function]()



    def OSFProcess(self):
        """Produces figure for one stage flight, nominal trajectory.
        """
        Data = Rd.RdataOSF(self.Filename)
        Plotter = Pb.PlotBase()
        Fig = Plotter.NewFigWin()
        Plotter.OSFlightPlot(Fig,Data)
        Plotter.LaunchFigs()

    def TSFProcess(self):
        """Produces figure for two stage flight, nominal trajectory.
        """
        Data = Rd.RdataTSF(self.Filename)
        Plotter = Pb.PlotBase()
        Fig = Plotter.NewFigWin()
        Plotter.TSFlightPlot(Fig,Data)
        Plotter.LaunchFigs()

    def OSMProcess(self):
        """Produces figure for one stage flights, based on monte carlo runs.
        """
        Data = Rd.RdataOSM(self.Filename)
        Plotter = Pb.PlotBase()

        Fig1 = Plotter.NewFigWin()
        Plotter.OSMFlightPlot(Fig1,Data)

        Fig2 = Plotter.NewFigWin()
        Plotter.OSSplashPlot(Fig2,Data)

        Fig3 = Plotter.NewFigWin()
        Plotter.OSMCluster(Fig3,Data)
        Plotter.LaunchFigs()


    def TSMProcess(self):
        """Produces figure for two stage flights, based on monte carlo runs.
        """
        Data = Rd.RdataTSM(self.Filename)
        Plotter = Pb.PlotBase()

        Fig1 = Plotter.NewFigWin()
        Plotter.TSMFlightPlot(Fig1,Data)

        Fig2 = Plotter.NewFigWin()
        Plotter.TSSplashPlot(Fig2,Data)

        Fig3 = Plotter.NewFigWin()
        Plotter.TSMCluster(Fig3,Data)
        Plotter.LaunchFigs()


class LaunchUDataFig:
    """This class handles all the trajectory data
    """

    def __init__(self,FileName,Label1,Label2):
        """Initialise the class

        Keyword arguments:
        Filename -- XML file to read
        Label1 --
        Label2 --
        """
        RD = Rd.Rdata(FileName)
        LabArr1 = Label1.split(',')
        LabArr2 = Label2.split(',')

        PB = Pb.PlotBase()
        Fig = PB.NewFigWin()

        i=0
        for Lab1 in LabArr1:
            Lab2 = LabArr2[i]
            if Lab2 != "":
                AxD = RD.UserPlotData(Lab1,Lab2)
                PB.DataPlot(Fig,AxD[0],Lab1,AxD[1],Lab2)
                i=i+1
        PB.LaunchFigs()
