#!/usr/bin/python
#12.11.16
#HAckaTUM
#Software auf dem Pi --> Managed Bluetooth verbindung

#imports

from bluetooth import *


#constants:
settings_path = "/honeySettings.txt"


class ServerSocket():

    def __init__(self, slots=10):

        self.initServer
        
        self.clients = [slots][2]
        self.free = [slots]

        print "Waiting for connection"
        for i in range(size(self.free)):
            self.free[i]=True
        

        while True:
            for i in range(size(self.free)):
                if self.free[i]:
                    break
            
            self.clients[i][0], self.clients[i][1] = self.server_sock.accept()
            ClientSocket(self, i)
            self.free[i]=False

            
        
        #client_sock, client_info = self.server_sock.accept()
        #print "accepted connection from ", client_info
        #return client_sock

    def initServer(self):
        
        hostMACAddress = '00:00:00:00:00:00'

        self.server_sock=BluetoothSocket( RFCOMM )
        self.server_sock.bind((hostMACAddress, 3))#PORT_ANY
        self.server_sock.listen(1)

        uuid = "00001101-0000-1000-8000-00805F9B34FB"

        advertise_service(self.server_sock, "Echo Server",
             service_id = uuid,
             service_classes = [ uuid, SERIAL_PORT_CLASS ],
             profiles = [ SERIAL_PORT_PROFILE ]
         )
        #return server_sock


class ClientSocket():

    def __init__(self, server, num):
        self.sock = server.clients[num][
        


    

    

    def getClientConnection(self):
        
        print "Waiting for connection" 
        client_sock, client_info = self.server_sock.accept()
        print "accepted connection from ", client_info
        return client_sock



    def manageConnection(socket, server_socket):

        try:
            while True:
                data = socket.recv(1024)
                if len(data) == 0:
                    break
                print "received [%s]" % data
                socket.send("Echo from Pi: [%s]\n" % data)
        except IOError:


            print("Closing socket")
            socket.close()
            server_socket.close()


    
def main():
    server=initServer()
    while  True:
        client=getClientConnection(server)
        manageConnection(client)

        #ggf break kriterium

    print "terminating..."




if __name__ == "__main__":
    #TODO load settings
    main()




