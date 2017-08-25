using System;
using System.Net.Sockets;

namespace Disono
{
	class Program
	{
		static void Main(string[] args)
		{
			TcpClient clientSocket = new TcpClient();
			clientSocket.Connect("host", 4321);

			NetworkStream serverStream = clientSocket.GetStream();
			byte[] outStream = System.Text.Encoding.ASCII.GetBytes("Working Now...$");
			serverStream.Write(outStream, 0, outStream.Length);
			serverStream.Flush();
			clientSocket.Close();

			Console.ReadKey();
		}
	}
}
