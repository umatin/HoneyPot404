#!/usr/bin/python
#12.11.16
#HAckaTUM


#python 3

#imports:
import urllib2
from time import sleep
from groveLib.grovepi import *
from groveLib.grove_rgb_lcd import *
import json
import os




#constants:
confFilePath = "/etc/hostapd/hostapd.conf"
configPath = "honeyConfig.txt"

link = "http://honeypot4431.cloudapp.net"




class handleEverything():

    def __init__(self, token):

        self.token = token
        if not self.token:
            #self.makeHTTPRequest()#get token request pi
            self.token = self.getToken()
            setConfig(token)

        print(token)
            

        #self.ssid =

        self.mainLoop()



    def mainLoop(self):

        counter =0
        #self.starthotspotservices()
        while True:
            #TODO write conquerer on lcd and chnage ssid every..
            

            ssid, name, color = self.getSSID_Name_Color()
            print(ssid, name, color)
            if counter >= 60:
                #self.changeSSID(ssis)
                counter=0
            
            content = [name, "conquered it!"]
            self.displayAusgabe(content, color)
            counter+=1

            sleep(5)

            
            


    #TODO, json decoder

    def getSSID_Name_Color(self):
        response= self.makeHTTPRequest("hotspot/update?token="+self.token)
        print(response)
        j = json.loads(response)
        return j["SSID"], j["name"], j["color"]
    

    def getToken(self):
        response=self.makeHTTPRequest("/hotspot/setup?secret=lebonbon")
        print(str(response))
        j = json.loads(str(response))
        return j["token"]
        


    def makeHTTPRequest(self, sublink): #exp what to show/ who captured and get token and 
        return urllib2.urlopen(link + sublink).read().decode("utf-8")


                        #array mit 2 elementen
    def displayAusgabe(content, rgb):
        setRGB(rgb[0], rgb[1], rgb[2])
        setRGB(rgb[0], rgb[1], rgb[2])
        #convert content for lcd

        content[0]+="               "
        content[1]+="               "
        content[0]=content[0][:16]
        content[1]=content[1][:16]
        
        setText(content[0]+content[1])


    #todo service neu startten
    def changeSSID(ssid):

        
        os.system('sudo service hostapd stop')

        f = open(confFilePath, "r")
        content = f.read()
        f.close()
        newcontent=""

        for i in content:#ssid=
            if i[:5]=="ssid=":
                newcontent+="ssid=" + str(ssid) + "\n"
            else:
                newcontent+=i

        f = open(confFilePath, "w")
        f.write(newcontent)
        f.close()

        os.system('sudo service hostapd start')


    def starthotspotservices(self):
        os.system('sudo service isc-dhcp-server start')
        #os.system('sudo service hostapd start')


def getArgs(configstr):
    b=False
    com = ""
    arg = ""
    
    for i in configstr:
        if i == ":":
            b=True
        elif not b:
            com+=i
        else:
           arg+=i

    return arg
        
            


def getConfig():
    try:
        f = open(confFilePath, "r")
        token = f.readline()
        f.close()
        return getArgs(token)
        
    except:

        return False

def setConfig(token):
    try:
        f = open(confFilePath, "w")
        f.write("Token:"+str(token))
        f.close()
        #return token
        
    except:

        return False




    


if __name__ == "__main__":
    handleEverything(getConfig())
