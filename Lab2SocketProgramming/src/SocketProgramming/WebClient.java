package SocketProgramming;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class WebClient {
	//String server = "127.0.0.1";
	int portNum = 51210;
	
	public static void main(String args[]){
		WebClient wc = new WebClient();
		wc.sendRequest();
	}
	
	private void sendRequest(){
		Socket clientsocket = null;
		PrintWriter output = null;
		try {
			clientsocket = new Socket("localhost", 51711);
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientsocket.getOutputStream())));
			
			//client enter the filename
			System.out.println("Enter file name: ");
			Scanner scan = new Scanner(System.in);
			String filename = scan.next(); 
			
			//Send filename to the server
			output.print("GET /"+filename+" HTTP/1.1\r\n\r\n");
			System.out.println("request send");
			/*Get response from the server*/
			//BufferedReader in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
			
			//String line = in.readLine();
			//System.out.println("Status="+line);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			System.out.println("Client connection terminated");
			try {
				clientsocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
