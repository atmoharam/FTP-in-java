
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Server {
    
     Server() throws IOException{
        try(ServerSocket server = new ServerSocket(28000)){
            Socket client = server.accept();
            try{
                
                String [] dataOnLine = new String [8];
                int line = 0;
                try{
                    File myObj = new File("C:\\Users\\Lenovo 500\\Documents\\data\\Data of users.txt"); // craeted a file object named FileName
                    Scanner MyReader = new Scanner(myObj); 
                     while(MyReader.hasNextLine()){

                            String info =MyReader.nextLine();
                            dataOnLine[line] = info;
                            line++;
                 }

                }
               catch(FileNotFoundException e)
			{
			System.out.println("FILE NOT FOUNDED");
			}
           
                
                    DataInputStream clientReadSource = new  DataInputStream(client.getInputStream());
                    DataOutputStream clientWriteSource = new DataOutputStream(client.getOutputStream());
                    clientWriteSource.writeUTF("Please, Enter Username:");
                    String request1 = clientReadSource.readUTF();
                    int dummy = 0 ;
                    String request2 = "";
                    
                    for(int i=0 ; i<8 ; i++){
                        if(request1.equals( dataOnLine[i] )){
                            request2 = dataOnLine[i+1];
                            dummy=1;
                            break;
                        }
                    }
                    if(dummy == 0){
                        clientWriteSource.writeUTF("Login Failed and the connection will terminate");
                        client.close();
                    }
                    clientWriteSource.writeUTF("Please Enter the password");
                    request1 = clientReadSource.readUTF();
                    if(request1.equals(request2)){
                         clientWriteSource.writeUTF("Login Successfully");
                    }
                    else{
                        clientWriteSource.writeUTF("Login Failed and the connection will terminate");
                        client.close();
                    }

            }
         catch(Exception e)
         { e.printStackTrace();}
        
           }
      
        }}
    

