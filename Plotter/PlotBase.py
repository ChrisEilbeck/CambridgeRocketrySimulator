## Copyright (C) 2010 S.Box
## PlotBase.py

## Author: S.Box
## Created: 2011-10-27

import matplotlib as mpl
from mpl_toolkits.mplot3d import Axes3D
import numpy as np
import matplotlib.pyplot as plt
import Rdata
# import scipy.linalg as la

class PlotBase:
    """This class produces the plots using matplotlib
    """
    def NewFigWin(self):
        """Produces a new window.
        """
        fig = plt.figure()
        return(fig)

    def OSFlightPlot(self, fig, data):
        """Produces trajectories for a one stage, nominal flight

        Keyword arguments:
        fig -- figure handle
        data -- trajectory data, see Rdata
        """
        ax = Axes3D(fig)
        #ax.view_init(0,0)
        ax.plot(data.X_TR.X,data.X_TR.Y,data.X_TR.Z,label='Flight Path')
        Limit = np.concatenate((data.X_TR.Z/2,data.X_TR.X,data.X_TR.Y))
        ax = self.ThreeDLimLabels(ax,Limit)


    def TSFlightPlot(self,fig,data):
        """Produces trajectories for a one stage, nominal flight

        Keyword arguments:
        fig -- figure handle
        data -- trajectory data, see Rdata
        """
        ax=Axes3D(fig)
        #ax.view_init(0,0)
        ax.plot(data.X_TR.X,data.X_TR.Y,data.X_TR.Z,label='LowerStage')
        ax.plot(data.X_US.X,data.X_US.Y,data.X_US.Z,label='UpperStage')
        Limit = np.concatenate((data.X_TR.Z/2,data.X_TR.X,data.X_TR.Y,data.X_US.X,data.X_US.Y,data.X_US.Z/2))
        ax = self.ThreeDLimLabels(ax,Limit)

    def OSApScatter(self,fig,data):
        """Produces scatter plot for apogee, single stage rocket

        Keyword arguments:
        fig -- figure handle
        data -- trajectory data, see Rdata
        """
        ax=Axes3D(fig)
        ax.scatter3D(data.ApoVg.X,data.ApoVg.Y, data.ApoVg.Z,label='ApogeeScatter')
        Limit = np.concatenate((data.ApoVg.X,data.ApoVg.Y,data.ApoVg.Z/2))
        ax = self.ThreeDLimLabels(ax,Limit)

    def TSApScatter(self,fig,data):
        """Produces scatter plot for apogee, two stage rocket

        Keyword arguments:
        fig -- figure handle
        data -- trajectory data, see Rdata
        """
        ax=Axes3D(fig)
        ax.scatter3D(data.LApoVg.X,data.LApoVg.Y, data.LApoVg.Z,label='LowerApogeeScatter')
        ax.scatter3D(data.UApoVg.X,data.UApoVg.Y, data.UApoVg.Z,label='UpperApogeeScatter')
        Limit = np.concatenate((data.LApoVg.X,data.LApoVg.Y,data.LApoVg.Z/2,data.UApoVg.X,data.UApoVg.Y, data.UApoVg.Z/2))
        ax = self.ThreeDLimLabels(ax,Limit)

    def OSSplashPlot(self,fig,data):
        """Produces a landing scatter plot, single stage rocket

        Keyword arguments:
        fig -- figure handle
        data -- trajectory data, see Rdata
        """
        ax = fig.add_subplot(111, aspect="equal")
        ax.scatter(data.LandVg.X,data.LandVg.Y,label='LandingScatter')
        ax, Z = self.GaussEllipse(ax,data.LandVg.X,data.LandVg.Y,1)
        ax, Z = self.GaussEllipse(ax,data.LandVg.X,data.LandVg.Y,2)
        ax.set_xlabel("Eastings (m)")
        ax.set_ylabel("Northings (m)")

    def TSSplashPlot(self,fig,data):
        """Produces a landing scatter plot, two stage rocket

        Keyword arguments:
        fig -- figure handle
        data -- trajectory data, see Rdata
        """
        ax = fig.add_subplot(111, aspect="equal")
        ax.scatter(data.LLandVg.X,data.LLandVg.Y,label='LowerStageLandingScatter')
        ax.scatter(data.ULandVg.X,data.ULandVg.Y,label='UpperStageLandingScatter')
        ax, Z = self.GaussEllipse(ax,data.LLandVg.X,data.LLandVg.Y,1)
        ax, Z = self.GaussEllipse(ax,data.ULandVg.X,data.ULandVg.Y,1)
        ax, Z = self.GaussEllipse(ax,data.LLandVg.X,data.LLandVg.Y,2)
        ax, Z = self.GaussEllipse(ax,data.ULandVg.X,data.ULandVg.Y,2)
        ax.set_xlabel("Eastings (m)")
        ax.set_ylabel("Northings (m)")

    def OSMFlightPlot(self,fig,data):
        """Produces trajectories for a one stage, monte carlo

        Keyword arguments:
        fig -- figure handle
        data -- trajectory data, see Rdata
        """

        ax = Axes3D(fig)
        #ax.view_init(0,0)
        ax.plot(data.X_TR.X,data.X_TR.Y,data.X_TR.Z,label='Flight Path')
        ax, Z1 = self.GaussEllipse(ax,data.LandVg.X,data.LandVg.Y,1)
        ax, Z2 = self.GaussEllipse(ax,data.LandVg.X,data.LandVg.Y,2)
        Limit = np.concatenate((data.X_TR.Z/2,data.X_TR.X,data.X_TR.Y,data.LandVg.X,data.LandVg.Y,Z1[:,0],Z1[:,1],Z2[:,0],Z2[:,1]))
        ax = self.ThreeDLimLabels(ax,Limit)

    def TSMFlightPlot(self,fig,data):
        """Produces trajectories for a two stage rocket, monte carlo

        Keyword arguments:
        fig -- figure handle
        data -- trajectory data, see Rdata
        """
        ax=Axes3D(fig)
        #ax.view_init(0,0)
        ax.plot(data.X_TR.X,data.X_TR.Y,data.X_TR.Z,label='LowerStage')
        ax.plot(data.X_US.X,data.X_US.Y,data.X_US.Z,label='UpperStage')
        ax, Z1 = self.GaussEllipse(ax,data.LLandVg.X,data.LLandVg.Y,1)
        ax, Z2 = self.GaussEllipse(ax,data.ULandVg.X,data.ULandVg.Y,1)
        ax, Z3 = self.GaussEllipse(ax,data.LLandVg.X,data.LLandVg.Y,2)
        ax, Z4 = self.GaussEllipse(ax,data.ULandVg.X,data.ULandVg.Y,2)
        Limit = np.concatenate((data.X_TR.Z/2,data.X_TR.X,data.X_TR.Y,data.X_US.X,data.X_US.Y,data.X_US.Z/2,data.LLandVg.X,data.LLandVg.Y,data.ULandVg.X,data.ULandVg.Y,Z1[:,0],Z1[:,1],Z2[:,0],Z2[:,1],Z3[:,0],Z3[:,1],Z4[:,0],Z4[:,1]))
        ax = self.ThreeDLimLabels(ax,Limit)

    def OSMCluster(self,fig,data):
        """Produces trajectories for a one stage rocket, monte carlo

        Keyword arguments:
        fig -- figure handle
        data -- trajectory data, see Rdata
        """
        ax=Axes3D(fig)
        #ax.view_init(0,0)
        for Fpath in data.FpathList:
            ax.plot(Fpath.X,Fpath.Y,Fpath.Z,label='FlightPath')
            Limit = np.concatenate((data.X_TR.Z/2,data.X_TR.X,data.X_TR.Y,data.LandVg.X,data.LandVg.Y))
            ax = self.ThreeDLimLabels(ax,Limit)


    def TSMCluster(self,fig,data):
        """Produces trajectories for a two stage rocket, monte carlo

        Keyword arguments:
        fig -- figure handle
        data -- trajectory data, see Rdata
        """
        ax=Axes3D(fig)
        #ax.view_init(0,0)
        for Fpath in data.LFpathList:
            ax.plot(Fpath.X,Fpath.Y,Fpath.Z,label='LowerStage')
        for Fpath in data.UFpathList:
            ax.plot(Fpath.X,Fpath.Y,Fpath.Z,label='UpperStage')
        Limit = np.concatenate((data.X_TR.Z/2,data.X_TR.X,data.X_TR.Y,data.X_US.X,data.X_US.Y,data.X_US.Z/2,data.LLandVg.X,data.LLandVg.Y,data.ULandVg.X,data.ULandVg.Y))
        ax = self.ThreeDLimLabels(ax,Limit)

    def GaussEllipse(self,ax,X,Y,sigma):
        """Returns the coordinates of a Gaussian ellipse, based on data and sigma (standard deviation)

        Keyword arguments:
        ax -- axes handle
        X -- x-axis data
        Y -- y-axis data
        sigma -- standard deviation
        """
        Landing = np.row_stack([X,Y])
        mean = np.array([np.mean(X),np.mean(Y)])
        covariance = np.matrix(np.cov(Landing))
        nplot = 30
        theta	= np.arange(nplot)*2*np.pi/(nplot-1)
        n = theta.size
        d = covariance.size
        r = np.ones(n)*sigma
        xe = r*np.cos(theta)
        ye = r*np.sin(theta)
        Xe = np.column_stack([xe,ye])

        # temp = la.sqrtm(covariance);
        U, s, V = np.linalg.svd(covariance, full_matrices=True);
        S = np.diag(np.sqrt(s));
        temp = np.dot(U, np.dot(S, V));
        temp = np.array(temp);

        Ze = np.dot(Xe,temp)
        Q1 = mean[0]*np.ones(n)
        Q2 = mean[1]*np.ones(n)
        Q = np.column_stack([Q1, Q2])
        Z = Ze+Q
        ax.plot(Z[:,0],Z[:,1])
        return(ax,Z)

    def DataPlot(self,Fig,data1,label1,data2,label2):
        """plot all data available, obtain a complete overview of the rocket behaviour

        keyword arguments:
        Fig -- figure handle
        data1 -- first dataset (see Rdata for format)
        label1 -- label of first dataset
        data2 -- second dataset (see Rdata for format)
        label2 -- label of second dataset
        """
        AD = AxisData()
        ax = Fig.add_subplot(111)
        ax.plot(data1,data2,label=AD.getLabel(label2.split(':')[0]))
        ax.set_xlabel(AD.getLabel(label1.split(':')[0]))
        ax.legend()

    def LaunchFigs(self):
        """shows the figures"""
        plt.show()

    def ThreeDLimLabels(self,ax,Limit):
        """sets the axis, limit and labels

        keyword arguments:
        ax -- axes figure
        Limit -- set limits for plotting
        """
        TopAlt = np.max(Limit)
        Lim = TopAlt*1.1
        ax.set_zlim3d([0,2*Lim])
        ax.set_xlim3d([-Lim,Lim])
        ax.set_ylim3d([-Lim,Lim])
        ax.set_xlabel("Eastings (m)")
        ax.set_ylabel("Northings (m)")
        ax.set_zlabel("Altitude (m)")
        return(ax)



class AxisData:
    """class to match all data with a suitable label
    """
    LabelDict ={"Time_0":"Time (s)",
    "AngleOfAttack_0":"Angle of Attack (rad)",
    "Thrust_0":"Thrust (N)",
    "Mass_0":"Mass (kg)",
    "CentreOfMass_0":"Centre of Mass (m)",
    "AtmosphericDensity_0":"Atmospheric Density (kg/m^3)",
    "AtmosphericTemperature_0":"Atmospheric Temperature (K)",
    "Position_X":"Eastings (m)",
    "Position_Y":"Northings (m)",
    "Position_Z":"Altitude (m)",
    "Position_Mag":"Distance from launch pad (m)",
    "Velocity_X": "X Velocity (m/s)",
    "Velocity_Y": "Y Velocity (m/s)",
    "Velocity_Z": "Z Velocity (m/s)",
    "Velocity_Mag": "Absolute Velocity (m/s)",
    "Accelleration_X":"X Accelleration (m/s^2)",
    "Accelleration_Y":"Y Accelleration (m/s^2)",
    "Accelleration_Z":"Z Accelleration (m/s^2)",
    "Accelleration_Mag":"Absolute Accelleration (m/s^2)",
    "AngularVelocity_X": "X Angular Velocity (rad/s)",
    "AngularVelocity_Y": "Y Angular Velocity (rad/s)",
    "AngularVelocity_Z": "Z Angular Velocity (rad/s)",
    "AngularVelocity_Mag": "Absolute Angular Velocity (rad/s)",
    "AngularAccelleration_X":"X Angular Accelleration (rad/s^2)",
    "AngularAccelleration_Y":"Y Angular Accelleration (rad/s^2)",
    "AngularAccelleration_Z":"Z Angular Accelleration (rad/s^2)",
    "AngularAccelleration_Mag":"Absolute Angular Accelleration (rad/s^2)",
    "Force_X":"X Force (N)",
    "Force_Y":"Y Force (N)",
    "Force_Z":"Z Force (N)",
    "Force_Mag":"Absolute Force (N)",
    "Torque_X":"X Torque (Nm)",
    "Torque_Y":"Y Torque (Nm)",
    "Torque_Z":"Z Torque (Nm)",
    "Torque_Mag":"Absolute Torque (Nm)",
    "Wind_X":"X Wind (m/s)",
    "Wind_Y":"Y Wind (m/s)",
    "Wind_Z":"Z Wind (m/s)",
    "Wind_Mag":"Absolute Wind (m/s)"}

    def getLabel(self,label):
        """returns a label based on the dictionary

        keyword argument:
        label -- label as seen in the data
        """
        return self.LabelDict[label]
