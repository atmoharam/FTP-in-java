import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class client {
    public static void main ( String[] args ) throws IOException {
        String temp;
        Scanner in = new Scanner ( System.in );
        in.useDelimiter ( "\n" );
        try ( Socket socket = new Socket ( InetAddress.getLocalHost ( ), 28000 ) ) {
            Socket socket2 = new Socket ( InetAddress.getLocalHost ( ), 25188 );
            DataInputStream ServerReadSource = new DataInputStream ( socket.getInputStream ( ) );
            DataOutputStream ServerWriteSource = new DataOutputStream ( socket.getOutputStream ( ) );
            //read please
            String message = ServerReadSource.readUTF ( );
            System.out.println ( message );
            //enter username
            String UserName = in.next ( );
            ServerWriteSource.writeUTF ( UserName );
            //
            String message2 = ServerReadSource.readUTF ( );
            System.out.println ( message2 );
            if ( message2.equals ( "Login Failed and the connection will terminate" ) ) {
                socket.close ( );
                socket2.close ( );
                ServerWriteSource.close ( );
                ServerReadSource.close ( );
                return;
            }

            UserName = in.next ( );
            ServerWriteSource.writeUTF ( UserName );

            message = ServerReadSource.readUTF ( );
            if ( message.equals ( "Login Failed and the connection will terminate" ) ) {
                socket.close ( );
                socket2.close ( );
                ServerWriteSource.close ( );
                ServerReadSource.close ( );
                System.out.println ( message );
                return;
            } else {
                while ( true ) {
                    String ko = ServerReadSource.readUTF ( );
                    System.out.println ( ko );
                    String sd = in.next ( );
                    ServerWriteSource.writeUTF ( sd );
                    if ( sd.equals ( "close" ) ) {
                        socket.close ( );
                        socket2.close ( );
                        ServerWriteSource.close ( );
                        ServerReadSource.close ( );
                        break;
                    }

                    int l = ServerReadSource.readInt ( );     //to store n.o dirs
                    String m = ServerReadSource.readUTF ( );
                    System.out.println ( m );
                    for ( int i = 0 ; i < l ; i++ ) {
                        String d = ServerReadSource.readUTF ( );
                        System.out.println ( d );
                    }
                    String kind = in.next ( );
                    ServerWriteSource.writeUTF ( kind );
                    String ss2 =ServerReadSource.readUTF ();

                    if ( ss2.equals ( "This directory is not found" ) )
                    {
                        System.out.println ( ss2 );
                        continue;
                    }
                    l = ServerReadSource.readInt ( );
                    String h = ServerReadSource.readUTF ( );
                    System.out.println ( h );
                    for ( int i = 0 ; i < l ; i++ ) {
                        String d = ServerReadSource.readUTF ( );
                        System.out.println ( d );
                    }
                    String name = in.next ( );
                    ServerWriteSource.writeUTF ( name );
                    temp = ServerReadSource.readUTF ( );
                    if(temp.equals ( "File not exist" )) {
                        System.out.println ( temp );
                        continue;
                    }
                    System.out.println ( ServerReadSource.readUTF ( ) );
                    File DownloadedFile = new File ( "D:\\ftp2\\" + temp );
                    FileOutputStream FileWriter = new FileOutputStream ( DownloadedFile );
                    DataInputStream DataDownloaded = new DataInputStream ( socket2.getInputStream ( ) );
                    int length = DataDownloaded.readInt ( );
                    if ( length > 0 ) {
                        byte[] DataBytes = new byte[ length ];
                        DataDownloaded.readFully ( DataBytes, 0, DataBytes.length );
                        FileWriter.write ( DataBytes );
                    }
                }

            }
        }
    }
}
