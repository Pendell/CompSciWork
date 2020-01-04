#   Alex Pendell
#   March 25, 2019
#   Reverse Shell - EchoServer

# This is a practice server for the Reverse Shell server that will come later.
# This is the server that simply listens for a connection. When a connection is established,
# it echoes messages received from the client.

# Start with opening a connection and sending data back and forth

# 03/28/2019 -- THIS IS NO LONGER THE ECHO SERVER: IT'S SLOWLY BECOMING THE REVERSESHELL

import socket
import sys

# Server plan:
# Socket...
# 1. create()
# 2. bind()
# 3. listen()
# 4. accept()
# 5. send() <-> recv() with client
# 6. close()

def socket_create():
    global HOST
    global PORT
    global s
    # 127.0.0.1 is the standard 'localhost'
    HOST = '127.0.0.1'
    # Pick a port. (Ports over 1000 are low priority)
    PORT = 12345

    # A few quick notes on the following line:
    # AF_INET declares that we'll be using IPv4 instead of IPv6
    # SOCK_STREAM declares this socket will be used for TCP connections
    # Use socket.SOCK.DGRAM instead of socket.SOCK_STREAM
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    print(str(s))

def socket_bind():
    # We bind the HOST IP and the Port # to further define the socket.
    # (Can't really say 'create the socket' since it's already created
    # but we now give it a home, so to speak, so that it may communicate)
    s.bind((HOST, PORT))
    print(str(s))

    # In order to accept a connection, we need to open the port and listen.
    # the '5' is a 'backlog' parameter that tells the socket how many bad
    # connections to try before closing the port.
    # (As of Python 3.X.X, the parameter is optional and can be left empty)
    s.listen(5)
    print("Listening...")

def socket_accept():
    # Accept the connection here.
    # Something to note (I believe):
    # s.accept() creates a new socket to communicate through.
    # This socket is distinct from the one we were listening through
    conn, addr = s.accept()
    print(str(s))
    return establish_connection(conn)

def establish_connection(conn):
    # We are receiving and echoing messages from the client
    # Loop infinitely until we are done.
    try:
        greeting = "Connection established. Type 'quit' to quit gracefully..."
        print(greeting)
        conn.send(greeting.encode())
        
        while True:

            clientdirectory = str(conn.recv(1024), "utf-8")
            print(clientdirectory, '$ ',  end='')
            
            msg = input()
            conn.send(msg.encode())

            #if msg == 'ls':
            #    directory = str(conn.recv(1024), "utf-8")
            #    print(directory, end='')
            
            # Here, if msg = "quit", we close gracefully instead of crashing the server with errors
            if msg == 'close' or msg == 'quit':
                print("Received shutdown command.")
                conn.close()
                s.close() 
                return False
            
    except:
        print("ahhh")
    return True

def main():
    accepting = True
    while accepting:
        socket_create()
        socket_bind()
        accepting = socket_accept()

main()
