

package client_side;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;



public class Client_side {

    
     public static void main(String []args) throws UnknownHostException, IOException{
       Scanner in=new Scanner (System.in);
          try (Socket socket = new Socket (InetAddress.getLocalHost(),28000)){
              DataInputStream ServerReadSource = new  DataInputStream(socket.getInputStream());
              DataOutputStream ServerWriteSource = new DataOutputStream(socket.getOutputStream());
              String message = ServerReadSource.readUTF();
              System.out.println(message);
              String UserName = in.next();
              ServerWriteSource.writeUTF(UserName);
              message = ServerReadSource.readUTF();
              System.out.println(message);
              UserName = in.next();
              ServerWriteSource.writeUTF(UserName);
              message = ServerReadSource.readUTF();
              System.out.println(message);
          }
        
    }
    
}
