package SocketProgramming;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	BufferedReader inStream;
	Socket socket;
		
	public Server(){
		
	}
	
	/**
	 * This serveStartUp() operation creates the welcome socket and listens to the client 
	 * @throws IOException 
	 * 
	 */
	private void serverStartUp(){
		try {
		/* instantiated ServerSocket object and specified the port number
		 * on which communication is going to occur
		 */
		ServerSocket welcomesock = new ServerSocket(51711);
		 
		//while(true){
			/* server waits until client get connected to port
			 */
				System.out.println("Server waiting...");
				socket = welcomesock.accept();
				processRequest();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Oops! serverStartUp error occured-->"+e.getMessage());
			}
			
			finally{
				System.out.println("Connection terminated");
				try {
					inStream.close();
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Oops! serverStartUp error occured-->"+e.getMessage());
				}
			}
		//}
		
	}
	
	/**
	 * This processRequest() operation receives the HTTP request from the connection and
	 * parse the request to determine the specific file being requested.
	 * @param s
	 * @return
	 */
	private boolean processRequest(){
		try {
			inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String request;
			/*
			while((request = inStream.readLine()) != null){
				System.out.println(request);
			}
			*/
			request = inStream.readLine();
			System.out.println(request);
			String[] reqLine = request.split(" ");
			String token = reqLine[0];
			String fileName = reqLine[1]; 
			
			if(token.equals("GET")){
				if(fileName.equals("/")){
					System.out.println("No file requested from user");
					return false;
				}
				else{
					fileName = fileName.substring(1);
					fileSendingProcess(fileName);
				}
			}
						
			System.out.println("request: "+request);
			System.out.println("token: "+token);
			System.out.println("filename: "+fileName);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to process client Request-->"+e.getMessage());
		}
		return true;
	}
	
	/**
	 * This fileSendingProcess() operation sends the requested file to the web client
	 * @param fileName
	 */
	private void fileSendingProcess(String fileName){
		FileReader filereader;
		PrintWriter output = null;
		try {
			System.out.println("Request file name: "+fileName);
			File myfile = new File("E://SONCHIRDI_STUDY_MATERIAL//MSS_TEXTBOOKS//Term2-Text books//516-Web Technologies//cics516//hw1//"+fileName);
			filereader = new FileReader(myfile);
			BufferedReader reader = new BufferedReader(filereader);
			
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

			output.print("HTTP/1.1 200 OK\r\n"); 
	        output.print("Content-Type: text/html\r\n"); 
	        output.print("Connection: keep-alive\r\n"); 
	        output.print("\r\n"); 
	        
			String line;
			while((line = reader.readLine()) != null){
				output.print(line+"\r\n");
			}
			System.out.println("Reached here");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Requested file couldn't be found-->"+e.getMessage());
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Output stream-->"+e.getMessage());
		}   
		finally{
			output.close();
		}
	}
	
	public static void main(String args[]){
		Server server  = new Server();
		server.serverStartUp();
	}
}
