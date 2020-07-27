package elzoghbi;

import java.io.*;
import java.net.*;

public class Server {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		ServerSocket ss = new ServerSocket(1234);
		
		while (true) {
			Socket s =null;
			try {
				
				s = ss.accept();
				System.out.println("A new client is connected : " + s);
				
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				
				System.out.println("Assigning new thread for this client");
				
				Thread t = new ClientHandler(s, dis, dos);
				
				t.start();
				
			}catch (Exception e) {
				s.close();
				ss.close();
				System.out.println(e);
				// TODO: handle exception
			}
		}

	}

}


class ClientHandler extends Thread
{
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;
	
	// Constructor
	public ClientHandler (Socket s , DataInputStream dis , DataOutputStream dos) {
		this.s = s;
		this.dis = dis;
		this.dos = dos;
	}
	
	@Override
	public void run() {
		String receive;
		
		
		while (true) {
			try {
				dos.writeUTF("Enter name / Enter 'quit' to exit \n" + "Client :\n");
				
				// Receive The Answer
				receive = dis.readUTF();
				
				if (receive.equals("quit")) {
					System.out.println("Client " + this.s + " sends exit..."); 
                    System.out.println("Closing this connection."); 
                    this.s.close(); 
                    System.out.println("Connection closed"); 
                    break;
				}
				
				switch (receive) {
					case "www.yahoo.com":
						getLocal(receive);
						break;
					case "www.google.com":
						getLocal(receive);
						break;
					case "www.fci.com":
						getRoot(receive);
						break;
					case "www.ibm.com":
						getTLD(receive);
						break;
					case "gaia.cs.umass.edu":
						getAuth(receive);
						break;
					default:
						dos.writeUTF("Not Found URL. Please Try Again...");
						break;
				}
				
			}catch (Exception e) {
				System.out.println(e + " Server Ana Hena");
				// TODO: handle exception
			}
		}
	}
	
	public String getLocal (String receive) throws IOException {
		String found = "notFoundinL";
		RandomAccessFile local = new RandomAccessFile("local_dns_table.txt", "r");
		while (true) {
			String line = local.readLine();
			String[] split = (line.split(" "));
			
			if (line.equals(null)) {
				break;
			}else if (split[0].equals(receive)){
				dos.writeUTF("Reply from Server is : URL=" + split[0] + "\n" 
							+ "IP Address=" + split[1] +"\n"+
							"Quary Type = A,NS" + "\n" + "Server name = local DNS \n");
				
				System.out.println("Client Requested : " + split[0]+ "\n" +"URL=" + split[0] + "\n" 
							+ "IP Address=" + split[1] +"\n"+
							"Quary Type = A,NS" + "\n");
				found = "foundinL";
				break;
			}
		}
		local.close();
		return found;
	}
	
	public String getRoot(String receive) throws IOException {
		String found = "notFoundinR";
		RandomAccessFile root = new RandomAccessFile("root_dns_table.txt", "r");
		while (true) {
			String line = root.readLine();
			String[] split = (line.split(" "));
			
			if (line.equals(null)) {
				break;
			}else if (split[0].equals(receive)){
				dos.writeUTF("Reply from Server is : URL=" + split[0] + "\n" 
							+ "IP Address=" + split[1] +"\n"+
							"Quary Type = A,NS" + "\n" + "Server name = Root DNS \n");
				
				System.out.println("Client Requested : " + split[0]+ "\n" +"URL=" + split[0] + "\n" 
							+ "IP Address=" + split[1] +"\n"+
							"Quary Type = A,NS" + "\n");
				found = "foundinR";
				break;
			}
		}
		root.close();
		return found;
	}
	
	public String getTLD(String receive) throws IOException {
		String found = "notFoundinT";
		RandomAccessFile tld = new RandomAccessFile("TLD_dns_table.txt", "r");
		while (true) {
			String line = tld.readLine();
			String[] split = (line.split(" "));
			
			if (line.equals(null)) {
				break;
			}else if (split[0].equals(receive) && split.length ==3){
				dos.writeUTF("Reply from Server is : URL=" + split[0] + "\n" 
							+ "IP Address=" + split[1] +"\n"+
							"Quary Type = A,CNAME" + "\n" + "Server name = TLD DNS \n"
							+ "Canonical name:" + split[2] + "\n" 
							+ "Aliases:" + split[0]);
				
				System.out.println("Client Requested : " + split[0]+ "\n" +"URL=" + split[0] + "\n" 
							+ "IP Address=" + split[1] +"\n"+
							"Quary Type = A,CNAME" + "\n"
							+ "Canonical name:" + split[2] + "\n" 
							+ "Aliases:" + split[0]);
				found = "foundinT";
				break;
			}
		}
		tld.close();
		return found;
	}
	public String getAuth(String receive) throws IOException {
		String found = "notFoundinA";
		RandomAccessFile auth = new RandomAccessFile("authoritative_dns_table.txt", "r");
		while (true) {
			String line = auth.readLine();
			String[] split = (line.split(" "));
			
			if (line.equals(null)) {
				break;
			}else if (split[0].equals(receive) ){
				dos.writeUTF("Reply from Server is : URL=" + split[0] + "\n" 
							+ "IP Address=" + split[1] +"\n"+
							"Quary Type = A,NS" + "\n" + "Server name = authoritative DNS \n"
							+ "authoritative answer:\n" 
							+ "Name: authoritative_dns_table.txt" );
				
				System.out.println("Client Requested : " + split[0]+ "\n" +"URL=" + split[0] + "\n" 
							+ "IP Address=" + split[1] +"\n"+
							"Quary Type = A,NS" + "\n"
							+ "Found Record on authoritative DNS servers.");
				found = "foundinA";
				break;
			}
		}
		auth.close();
		return found;
	}
}