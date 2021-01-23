import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class client {
    public static void main ( String[] args ) throws IOException {
        String temp = " ";
        Scanner in = new Scanner ( System.in );
        try ( Socket socket = new Socket ( InetAddress.getLocalHost ( ), 28000 ) ) {
            DataInputStream ServerReadSource = new DataInputStream ( socket.getInputStream ( ) );
            DataOutputStream ServerWriteSource = new DataOutputStream ( socket.getOutputStream ( ) );
            String message = ServerReadSource.readUTF ( );
            System.out.println ( message );
            String UserName = in.next ( );
            ServerWriteSource.writeUTF ( UserName );
            message = ServerReadSource.readUTF ( );
            System.out.println ( message );
            UserName = in.next ( );
            ServerWriteSource.writeUTF ( UserName );
            message = ServerReadSource.readUTF ( );
            System.out.println ( message );
            ServerWriteSource.writeUTF ( "show my directories" );
            int l;
            l = ServerReadSource.readInt ( );     //to store n.o dirs
            for ( int i = 0 ; i < l ; i++ ) {
                String d = ServerReadSource.readUTF ( );
                System.out.println ( d );
            }
            String kind = in.next ( );
            ServerWriteSource.writeUTF ( kind );
            l = ServerReadSource.readInt ( );
            for ( int i = 0 ; i < l ; i++ ) {
                String d = ServerReadSource.readUTF ( );
                System.out.println ( d );
            }
            String name = in.next ( );
            ServerWriteSource.writeUTF ( name );
            temp = ServerReadSource.readUTF ( );
            socket.close ( );
            ServerReadSource.close ( );
            ServerWriteSource.close ( );
        }
        Socket socket2 = new Socket ( InetAddress.getLocalHost ( ), 25188 );
        File DownloadedFile = new File ( "D:\\ftp2\\" + temp );
        FileOutputStream FileWriter = new FileOutputStream ( DownloadedFile );
        DataInputStream DataDownloaded = new DataInputStream ( socket2.getInputStream ( ) );
        int length = DataDownloaded.readInt ( );
        if ( length > 0 ) {
            byte[] DataBytes = new byte[ length ];
            DataDownloaded.readFully ( DataBytes, 0, DataBytes.length );
            FileWriter.write ( DataBytes );
        }
        socket2.close ( );
        FileWriter.close ( );
        DataDownloaded.close ( );
    }
}
