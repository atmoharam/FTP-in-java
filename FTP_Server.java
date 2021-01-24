import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class server {

    public static void main ( String[] args ) throws IOException {
        ServerSocket server = new ServerSocket ( 28000 );
        ServerSocket serv = new ServerSocket ( 25188 );
        while ( true ) {
            Socket client = server.accept ( );
            Socket servo=serv.accept ();
            ClientConnection nee = new ClientConnection ( client ,servo);
            nee.start ( );
        }
    }


    static class ClientConnection extends Thread {
        Socket ClientSocket;
        Socket serv;

        ClientConnection ( Socket clientSocket ,Socket serv) {
            this.ClientSocket = clientSocket;
            this.serv=serv;

        }

        public void run () {
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
                    DataInputStream clientReadSource = new DataInputStream ( this.ClientSocket.getInputStream ( ) );
                    DataOutputStream clientWriteSource = new DataOutputStream ( this.ClientSocket.getOutputStream ( ) );
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

                    }
                    clientWriteSource.writeUTF ( "Please Enter the password" );
                    request1 = clientReadSource.readUTF ( );
                    if ( request1.equals ( request2 ) ) {
                        clientWriteSource.writeUTF ( "Login Successfully" );
                    } else {
                        clientWriteSource.writeUTF ( "Login Failed and the connection will terminate" );
                    }
                while ( true ) {
                    String msg = clientReadSource.readUTF ( );
                    System.out.println ( msg );
                    if(msg.equals ( "close" ))
                    {
                        System.out.println ( "client " +user+ " terminated" );
                        break;
                    }

                    File file = new File ( "E:\\FTP\\" + user );
                    String[] files = file.list ( );
                    clientWriteSource.writeInt ( files.length );
                    for ( String string : files ) {
                        String ps = string;
                        clientWriteSource.writeUTF ( ps );
                    }
                    String msg7 = clientReadSource.readUTF ( );
                    File file2 = new File ( "E:\\FTP\\" + user + "\\" + msg7 );
                    String[] files2 = file2.list ( );
                    clientWriteSource.writeInt ( files2.length );
                    Vector<String> v = new Vector<String> ( 8 );
                    for ( String string : files2 ) {
                        String ps = string;
                        clientWriteSource.writeUTF ( ps );
                        v.add ( ps );
                    }
                    String msg2 = clientReadSource.readUTF ( );
                    System.out.println ( msg2 );
                    msg7.toLowerCase ( );
                    String msgg = " ";
                    for ( int i = 0 ; i < v.size ( ) ; i++ ) {
                        if ( v.get ( i ).contains ( msg2 ) ) {
                            msgg = v.get ( i );
                            break;
                        }
                    }
                    clientWriteSource.writeUTF ( msgg );
                    File file3 = new File ( "E:\\FTP\\" + user + "\\" + msg7 + "\\" + msgg );
                    FileInputStream filereader = new FileInputStream ( file3 );
                    byte Databytes[] = new byte[ ( int ) file3.length ( ) ];
                    filereader.read ( Databytes );
                    try  {
                        DataOutputStream dataout = new DataOutputStream ( serv.getOutputStream ( ) );
                        dataout.writeInt ( Databytes.length );
                        dataout.write ( Databytes );

//                        dataout.close ( );
                    } catch ( Exception e ) {
                        System.out.println ( "Error" );
                        //ClientSocket.close ( );
                    }

                }
            }
            catch ( Exception e )
            {

                System.out.println ( "error" );
            }
        }
    }

}
