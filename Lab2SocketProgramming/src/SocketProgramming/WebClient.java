package SocketProgramming;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WebClient {
	//String server = "127.0.0.1";
	int portNum;
	Socket clientsocket = null;
	
	public static void main(String args[]){
		WebClient wc = null;
		wc = new WebClient();
		wc.userInterface();
	}
	
	public WebClient(){
		
	}
	
	/**
	 * This sendRequest() operation takes the file name and pass the request to the server
	 * @param filename
	 */
	private void sendRequest(String filename){
		PrintWriter output = null;
		try {
			clientsocket = new Socket("localhost", 51711);
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
			System.out.println("Request sent");
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("Client unknown host exception-"+e.getMessage());
		}catch(ConnectException e){
			System.out.println("Client connection exception-"+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Client io exception-"+e.getMessage());
		}
	}
	
	/**
	 * This userInterface() operation renders the user interface and gives the text field 
	 * to the user for entering file name and displays the content of file in the text area.
	 */
	public void userInterface(){
		JFrame guiFrame = new JFrame();
        
        //Added the following statement to make sure the program exits when the frame closes
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Web Client");
        guiFrame.setSize(900,500);
        guiFrame.setResizable(false);
        
      //To bring the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null);
        
      //The first JPanel contains a JLabel, JTextField, JButton and JTextArea
        JPanel comboPanel = new JPanel();
        JLabel comboLbl = new JLabel("Enter the file name: ");
        final JTextField txt1 = new JTextField(20);
        final JButton submit = new JButton( "Show File");
        final JTextArea textarea = new JTextArea(26,75);
        JScrollPane areaScrollPane = new JScrollPane(textarea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //areaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        comboPanel.add(comboLbl);
        comboPanel.add(txt1);
        comboPanel.add(submit);
        //comboPanel.add(textarea);
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
				txt1.setEnabled(false);
				submit.setEnabled(false);
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
						System.out.println(" "+e.getMessage());
					}
					finally{
						System.out.println("Client connection terminated");
						try {
							if(clientsocket != null){
								clientsocket.close();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.println("Client connection closed. "+e.getMessage());
						}
					}
				}
			}
		});
        
        guiFrame.add(comboPanel);
        
        //To set JFrame visibility true
        guiFrame.setVisible(true);
	}
}
