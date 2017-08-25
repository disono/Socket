require 'socket'

hostname = 'host'
port = 4321

s = TCPSocket.new(hostname, port)
s.write "This is ruby."

s.close();