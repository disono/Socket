import java.net.*;
import java.io.*;

public class Client {
	public static void main(String []args) {
		Socket smtpSocket;
		DataOutputStream os = null;
		
		try {
			smtpSocket = new Socket("host", 4321);
			os = new DataOutputStream(smtpSocket.getOutputStream());
			os.writeBytes("Java Client.");
			os.close();
			smtpSocket.close();
		} catch (IOException e) {
			
		}
	}
}