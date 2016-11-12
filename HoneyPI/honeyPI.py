#!/usr/bin/python
#12.11.16
#HAckaTUM

#imports:
import urllib2




#constants:
confFilePath = "/etc/hostapd/hostapd.conf"
configPath = "honeyConfig.txt"

link = "honeypot4431.cloudapp.net"




class handleEverything():

    def __init__(self, token):

        self.token = token
        if not self.token:
            self.makeHTTPRequest()#get token request pi
            

        #self.ssid = 



    def makeHTTPRequest(self, sublink): #exp what to show/ who captured and get token and 
        return urllib2.urlopen(link + sublink).read()



def changeSSID(ssid):

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

        #return False




    


if __name__ == "__main__":
    handleEverything(getConfig())
