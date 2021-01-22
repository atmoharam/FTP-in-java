
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Client {
    String temp= " ";

    Client() throws UnknownHostException, IOException{
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
            ServerWriteSource.writeUTF("show my directories");
            ServerReadSource.readUTF();
            String str=in.nextLine();
            str.trim();
            ServerWriteSource.writeUTF(str);
            int l=0;
            l=ServerReadSource.readInt();
            for (int i=0;i<l;i++){
                String d=ServerReadSource.readUTF();
                System.out.println(d);
            }
            String kind=in.nextLine();
            kind.trim();
            ServerWriteSource.writeUTF(kind);
            l=0;
            l=ServerReadSource.readInt();
            for (int i=0;i<l;i++){
                String d=ServerReadSource.readUTF();
                System.out.println(d);
            }
            String name=in.nextLine();
            ServerWriteSource.writeUTF(name);
            temp= ServerReadSource.readUTF();

        }

        try (Socket socket=new Socket("localHost",25000)){

            File DownloadedFile=new File("D:\\ftp2"+temp);
            FileOutputStream FileWriter=new FileOutputStream(DownloadedFile);
            DataInputStream DataDownloaded=new DataInputStream(socket.getInputStream());
            int length=DataDownloaded.readInt();
            if (length>0){
                byte[] DataBytes=new byte[length];
                DataDownloaded.readFully(DataBytes,0,DataBytes.length);
                FileWriter.write(DataBytes);
            }

        }
    }



}
