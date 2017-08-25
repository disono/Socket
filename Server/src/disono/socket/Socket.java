/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package disono.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;

/**
 * @author Archie Disono
 */
public class Socket {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        listenSocket(4321);
    }

    public static void listenSocket(int port) {
        ServerSocket server = null;

        try {
            System.out.println("Listening on port: " + port);
            server = new ServerSocket(port, 50, InetAddress.getByName("host"));

            while (true) {
                new Thread(new ClientWorker(server.accept())).start();
            }
        } catch (IOException e) {
            System.out.println("Listening(Error): " + e.getMessage());
        }
    }
}

class ClientWorker implements Runnable {

    private java.net.Socket client;

    ClientWorker(java.net.Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        String lineIn;

        BufferedReader in = null;
        PrintWriter out = null;
        
        System.out.println("Listening: " + client.getInetAddress());

        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("in or out failed");
            System.exit(-1);
        }

        try {
            String data = new Scanner(in).useDelimiter("\\r\\n\\r\\n").next();
            Matcher get = Pattern.compile("^GET").matcher(data);
            
            if (get.find()) {
                System.out.println("Listening: " + data);
                OutputStream outStream = client.getOutputStream();
                Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
                
                if (match.find()) {
                    byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                            + "Connection: Upgrade\r\n"
                            + "Upgrade: websocket\r\n"
                            + "Sec-WebSocket-Accept: "
                            + DatatypeConverter
                                    .printBase64Binary(MessageDigest
                                            .getInstance("SHA-1")
                                            .digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11")
                                                    .getBytes("UTF-8")))
                            + "\r\n\r\n")
                            .getBytes("UTF-8");

                    outStream.write(response, 0, response.length);
                    System.out.println("Req: Handshake");
                }
            } else {
                lineIn = in.readLine();

                // Send data back to client
                if (lineIn != null) {
                    System.out.println("Req(READLINE): " + lineIn);
                } else {
                    System.out.println("Req(SCANNER): " + data);
                }
                
                System.out.println("Response from server.");
            }
        } catch (IOException e) {
            System.out.println("Read failed: " + e.getMessage());
            System.exit(-1);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ClientWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
