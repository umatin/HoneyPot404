#!/usr/bin/python
#12.11.16
#HAckaTUM


#python 3

#imports:
import urllib.request
from time import sleep
import time
from groveLib.grovepi import *
from groveLib.grove_rgb_lcd.grove_rgb_lcd import *
import json
import os




#constants:
confFilePath = "/etc/hostapd/hostapd.conf"
configPath = "honeyConfig.txt"

link = "http://honeypot4431.cloudapp.net"




class handleEverything():

    def __init__(self, token):

        self.token = token
        if self.token==False:
            #self.makeHTTPRequest()#get token request pi
            self.token = self.getToken()
            setConfig(self.token)

        print(self.token)
            

        #self.ssid =

        self.mainLoop()



    def mainLoop(self):

        counter =0
        ssid=""
        name=""
        color=""
        times=""
        ssid=self.newSSID()
        #b = False
        #self.starthotspotservices()
        while True:
            #TODO write conquerer on lcd and chnage ssid every..
            

            #ssid, name, color = self.getSSID_Name_Color()
            
            

            if counter%10==0: #alle sieben 
                name, color, times = self.getSSID_Name_Color_Time()
                print(name, color, times)
                #b=True
                

            if counter >= 60:#alle drei minuten einnehmen
                ssid=self.newSSID()
                #self.changeSSID(ssis)
                counter=0

            if times+180-int(time.time())<=0: #and b:
                content = [name, "Conquerable!!!"]
                #b=False
            else:#if not time+60-time()<=0:
                #newSSID();

                content = [name, "Next: " + str(times+180-int(time.time()))]
                #b=True

            self.displayAusgabe(content, color)

            
            counter+=1

            sleep(1)

            
            


    #TODO, json decoder

    def getSSID_Name_Color_Time(self):#capture --> timestamp
        response= self.makeHTTPRequest("/hotspot/fetch?token="+self.token)
        print(response)
        if response:
            j = json.loads(response)

            #timestr = j["capture"]  #timestamp string
            
            return j["name"], j["color"], int(j["capture"])#j["ssid"],
        else:
            return self.name, self.name, self.times
    

    def getToken(self):
        response=self.makeHTTPRequest("/hotspot/setup?secret=lebonbon")
        print(str(response))
        if response:
            j = json.loads(str(response))
            return j["token"]
        else:
            quit()

    def newSSID(self):
        response=self.makeHTTPRequest("/hotspot/update?token="+self.token)
        print("New SSID for wifi")
        print(str(response))
        if response:
            j = json.loads(str(response))
            return j["ssid"]
        else:
            return ssid
        


    def makeHTTPRequest(self, sublink): #exp what to show/ who captured and get token and
        try:
            s=urllib.request.urlopen(link + sublink).read()
            return s.decode("utf-8")
        except:
            return False


                        #array mit 2 elementen
    def displayAusgabe(self, content, rgb):
        print(rgb, int('0x'+rgb[0:2], 16), int('0x'+rgb[2:4], 16), int('0x'+rgb[4:6], 16))
        setRGB(int('0x'+rgb[0:2], 16), int('0x'+rgb[2:4], 16), int('0x'+rgb[4:6], 16))

        #setRGB(int(rgb[0], 16), int(rgb[1], 16), int(rgb[2], 16))
        #convert content for lcd

        content[0]+="               "
        content[1]+="               "
        content[0]=content[0][:16]
        content[1]=content[1][:16]
        
        setText(content[0]+content[1])


    #todo service neu startten
    def changeSSID(self, ssid):

        
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
        elif b==False:
            com+=i
        else:
           arg+=i
    print(arg)
    return arg
        
            


def getConfig():
    try:
        f = open(configPath, 'r')
        token = f.readline()
        f.close()
        return getArgs(token)
        
    except:
        
        return False

def setConfig(token):
    try:
        f = open(configPath, 'w')
        f.write("Token:"+str(token))
        f.close()
        return token
        
    except:
        pass
        #return False




    


if __name__ == "__main__":
    handleEverything(getConfig())
