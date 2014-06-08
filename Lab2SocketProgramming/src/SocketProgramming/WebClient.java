package SocketProgramming;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WebClient {
	//String server = "127.0.0.1";
	int portNum = 51210;
	Socket clientsocket = null;
	
	public static void main(String args[]){
		WebClient wc = null;
		try {
			wc = new WebClient();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("Client unknown host exception-"+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Client io exception-"+e.getMessage());
		}
		wc.userInterface();;
	}
	
	public WebClient() throws UnknownHostException, IOException{
		clientsocket = new Socket("localhost", 51711);
	}
	
	private void sendRequest(String filename){
		PrintWriter output = null;
		try {
			
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientsocket.getOutputStream())));
			
			/*
			//client enter the filename
			System.out.println("Enter file name: ");
			Scanner scan = new Scanner(System.in);
			String filename = scan.next(); 
			*/
			
			//Send filename to the server
			output.print("GET /"+filename+" HTTP/1.1\r\n\r\n");
			output.flush();
			System.out.println("request send");
			
			
			/*Get response from the server*/
			/*BufferedReader in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
			
			String line = in.readLine();
			System.out.println("Status="+line);
			
			String request;
			while((request = in.readLine()) != null){
				System.out.println(request);
			}
			*/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void userInterface(){
		JFrame guiFrame = new JFrame();
        
        //to make sure the program exits when the frame closes
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Web Client");
        guiFrame.setSize(900,500);
        
      //This will center the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null);
        
      //The first JPanel contains a JLabel and JCombobox
        JPanel comboPanel = new JPanel();
        JLabel comboLbl = new JLabel("Enter the file name: ");
        final JTextField txt1 = new JTextField(20);
        final JButton submit = new JButton( "Submit");
        final JTextArea textarea = new JTextArea(200,80);
        JScrollPane areaScrollPane = new JScrollPane(textarea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        comboPanel.add(comboLbl);
        comboPanel.add(txt1);
        comboPanel.add(submit);
        comboPanel.add(textarea);
        comboPanel.add(areaScrollPane);
        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);
        textarea.setEditable(false);
        comboPanel.setVisible(true);
        
      //The ActionListener class is used to handle the event that happens when the user clicks the button
        submit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				txt1.disable();
				Object source = event.getSource();
				
				if(source == submit){
					String filename = txt1.getText();
					sendRequest(filename);
					
					BufferedReader in = null;
					try {
						in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
						String line = in.readLine();
						System.out.println("Status="+line);
						textarea.setText(line);
						
						String request;
						while((request = in.readLine()) != null){
							System.out.println(request);
							textarea.setText(textarea.getText()+"\n"+request);
						}
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
							System.out.println("Client connection closed. "+e.getMessage());
						}
					}
				}
			}
		});
        
        guiFrame.add(comboPanel);
        
        //make sure the JFrame is visible
        guiFrame.setVisible(true);
	}
}
