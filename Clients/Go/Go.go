package main

import "net"
import "fmt"
import "bufio"
import "os"
import "strings"

func main() {
	// connect to this socket
	conn, _ := net.Dial("tcp", "host:port")
	
	// read in input from stdin
	reader := bufio.NewReader(os.Stdin)
	fmt.Print("Text to send: ")
	text, _ := reader.ReadString('\n')
			
	// send to socket
	clean := strings.Replace(text, "\n", "", -1)
	fmt.Fprintf(conn, strings.Replace(clean, "\r", "", -1))
	fmt.Print("Sending...")
			
	// listen for reply
	// message, _ := bufio.NewReader(conn).ReadString('\n')
	// fmt.Print("Message from server: " + message)
		
	conn.Close()
}