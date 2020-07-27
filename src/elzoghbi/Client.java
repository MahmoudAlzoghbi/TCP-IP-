package elzoghbi;

import java.util.Scanner;
import java.io.*; 
import java.net.*;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Scanner sc = new Scanner(System.in);
			
			InetAddress ip = InetAddress.getByName("localhost");
			
			Socket s = new Socket( ip , 1234);
			
			DataInputStream dis = new DataInputStream(s.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            
            while (true)  
            { 
                System.out.println(dis.readUTF()); 
                String tosend = sc.nextLine(); 
                dos.writeUTF(tosend); 
                  
                // If client sends exit,close this connection  
                // and then break from the while loop 
                if(tosend.equals("quit")) 
                { 
                    System.out.println("Closing this connection : " + s); 
                    s.close(); 
                    System.out.println("Connection closed"); 
                    break; 
                } 
                  
                // printing date or time as requested by client 
                String received = dis.readUTF(); 
                System.out.println(received); 
            }
            sc.close();
			dis.close();
			dos.close();
		}catch (Exception e) {
			System.out.println(e);
			// TODO: handle exception
		}

	}

}
