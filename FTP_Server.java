

package server_side;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import static java.util.Collections.list;
import java.util.Scanner;



public class Server_side {

   
    public static void main(String[] args) throws IOException {
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
                String user = request1;
                String request2 = "";
                String userName = "";

                for(int i=0 ; i<8 ; i++){
                      if(request1.equals( dataOnLine[i] )){
                            request2 = dataOnLine[i+1];
                            userName = request1;
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
               String msg=clientReadSource.readUTF();
                System.out.println(msg);
                File file=new File("E:\\FTP"+user);
                String[] files=file.list();
                clientWriteSource.writeUTF(String.valueOf(files.length));
                for (String string: files) {
                    clientWriteSource.writeUTF(string);
                }
                msg=clientReadSource.readUTF();
                System.out.println(msg);
                
                
                
                System.out.println(msg);
                File file2=new File("C:\\Users\\Lenovo 500\\Documents\\FTP\\"+user+msg);
                String[] files2=file2.list();
                clientWriteSource.writeUTF(String.valueOf(files2.length));
                for (String string: files2) {
                    clientWriteSource.writeUTF(string);
                }
                String msg2=clientReadSource.readUTF();
                System.out.println(msg2);
                msg.toLowerCase();
                String msgg=" ";
                if ((msg.contains("movies"))){
                    msg2.trim();
                    msgg=msg2+".mp4";
                }else if ((msg.contains("music"))){
                    msg2.trim();
                    msgg=msg2+".mp3";
                }else if ((msg.contains("docs"))){
                    msg2.trim();
                    msgg=msg2+".txt";
                }else if ((msg.contains("image"))){
                    msg2.trim();
                    msgg=msg2+".jpeg";
                }
                clientWriteSource.writeUTF(msgg);
                //Crearte a file of any ex(jpg, mp3, mp4, txt, pdf)
                File file3=new File("C:\\Users\\Lenovo 500\\Documents\\FTP\\"+user+msg+msgg);
                //Create a file input stream to read a file content
                FileInputStream filereader=new FileInputStream(file3);
                //Create an array of byte with file length
                byte Databytes[] =new byte[(int) file3.length()];
                //read the data and put data in arrray as bytes
                filereader.read(Databytes);
                try (ServerSocket serv=new ServerSocket(25000)){
                    try (Socket socket=serv.accept()){
                        //Create data output stream and pass the output to socket stream
                        DataOutputStream dataout=new DataOutputStream(socket.getOutputStream());
                        //Write length of data and data in socket stream
                        dataout.writeInt(Databytes.length);
                        dataout.write(Databytes);
                    }
                }

            }
         catch(Exception e)
         { e.printStackTrace();}
        
           }
      
        }
    
}



    

