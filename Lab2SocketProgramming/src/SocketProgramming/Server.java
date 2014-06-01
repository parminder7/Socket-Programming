package SocketProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	BufferedReader inStream;
	Socket socket;
		
	public Server(){
		
	}
	
	/**
	 * This serveStartUp() operation 
	 * @throws IOException 
	 * 
	 */
	private void serverStartUp() throws IOException{
		/* instantiated ServerSocket object and specified the port number
		 * on which communication is going to occur
		 */
		ServerSocket welcomesock = new ServerSocket(9999);
		 
		while(true){
			/* server waits until client get connected to port
			 */
			socket = welcomesock.accept();
			processRequest(socket);
			
			System.out.println("Connection terminated");
			inStream.close();
			socket.close();
		}
		
	}
	
	/**
	 * This processRequest() operation receives the HTTP request from the connection and
	 * parse the request to determine the specific file being requested.
	 * @param s
	 */
	public void processRequest(Socket s){
		try {
			inStream = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String request;
			/*
			while((request = inStream.readLine()) != null){
				System.out.println(request);
			}
			*/
			request = inStream.readLine();
			String[] reqLine = request.split(" ");
			String token = reqLine[0];
			String fileName = reqLine[1]; 
			
			if(token.equals("GET")){
				fileName = fileName.substring(1);
			}
			
			System.out.println("request: "+request);
			System.out.println("token: "+token);
			System.out.println("filename: "+fileName);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to process client Request-->"+e.getMessage());
		}
	}
	
	public static void main(String args[]){
		Server server  = new Server();
		try {
			server.serverStartUp();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Oops! serverStartUp error occured-->"+e.getMessage());
		}
	}
}
