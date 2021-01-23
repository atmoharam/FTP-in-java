import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class server {

    public static void main ( String[] args ) throws IOException {
        ServerSocket server = new ServerSocket ( 28000 );
        try ( Socket client = server.accept ( ) ) {
            try {

                String[] dataOnLine = new String[ 9 ];
                int line = 1;
                try {
                    File myObj = new File ( "E:\\Data_of_users.txt" ); // craeted a file object named FileName
                    Scanner MyReader = new Scanner ( myObj );
                    while ( MyReader.hasNextLine ( ) ) {
                        String info = MyReader.nextLine ( );
                        dataOnLine[ line ] = info;
                        line++;
                    }

                } catch ( FileNotFoundException e ) {
                    System.out.println ( "FILE NOT FOUNDED" );
                }


                DataInputStream clientReadSource = new DataInputStream ( client.getInputStream ( ) );
                DataOutputStream clientWriteSource = new DataOutputStream ( client.getOutputStream ( ) );
                clientWriteSource.writeUTF ( "Please, Enter Username:" );
                String request1 = clientReadSource.readUTF ( );
                String user = request1;
                int dummy = 0;
                String request2 = "";
                for ( int i = 0 ; i < 8 ; i++ ) {
                    if ( request1.equals ( dataOnLine[ i ] ) ) {
                        request2 = dataOnLine[ i + 1 ];
                        dummy = 1;
                        break;
                    }
                }
                if ( dummy == 0 ) {
                    clientWriteSource.writeUTF ( "Login Failed and the connection will terminate" );
                    client.close ( );
                }
                clientWriteSource.writeUTF ( "Please Enter the password" );
                request1 = clientReadSource.readUTF ( );
                if ( request1.equals ( request2 ) ) {
                    clientWriteSource.writeUTF ( "Login Successfully" );
                } else {
                    clientWriteSource.writeUTF ( "Login Failed and the connection will terminate" );
                    client.close ( );
                }
                String msg = clientReadSource.readUTF ( );
                System.out.println ( msg );
                File file = new File ( "E:\\FTP\\" + user );
                String[] files = file.list ( );
                clientWriteSource.writeInt ( files.length );
                for ( String string : files ) {
                    String ps = string;
                    clientWriteSource.writeUTF ( ps );
                }
                String msg7 = clientReadSource.readUTF ( );
                File file2 = new File ( "E:\\FTP\\" + user+"\\" + msg7 );
                String[] files2 = file2.list ( );
                clientWriteSource.writeInt ( files2.length );
                for ( String string : files2 ) {
                    String ps = string;
                    clientWriteSource.writeUTF ( ps );
                }

                String msg2 = clientReadSource.readUTF ( );
                System.out.println ( msg2 );
                msg7.toLowerCase ( );
                String msgg = " ";
                if ( msg7.equals ( "movies" ) ) {
                    msgg = msg2 + ".mp4";
                } else if ( ( msg7.contains ( "music" ) ) ) {
                    msgg = msg2 + ".mp3";
                } else if ( ( msg7.contains ( "docs" ) ) ) {
                    msgg = msg2 + ".txt";
                } else if ( ( msg7.contains ( "image" ) ) ) {
                    msgg = msg2 + ".jpg";
                }
                clientWriteSource.writeUTF ( msgg );
                File file3 = new File ( "E:\\FTP\\" + user + "\\" + msg7 + "\\" + msgg );
                FileInputStream filereader = new FileInputStream ( file3 );
                byte Databytes[] = new byte[ ( int ) file3.length ( ) ];
                filereader.read ( Databytes );
                server.close ( );
                clientWriteSource.close ( );
                clientReadSource.close ( );

                ServerSocket serv = new ServerSocket ( 25188 );
                try ( Socket socket = serv.accept ( ) )
                {
                    DataOutputStream dataout = new DataOutputStream ( socket.getOutputStream ( ) );
                    dataout.writeInt (Databytes.length);
                    dataout.write (Databytes);
                    dataout.close ( );
                    socket.close ( );
                }
                serv.close ( );
            } catch ( Exception e ) {
                e.printStackTrace ( );
            }

        }

    }

}
