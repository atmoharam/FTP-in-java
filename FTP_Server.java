import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
public class server {
    public static void main ( String[] args ) throws IOException {
        ServerSocket serverSocket1 = new ServerSocket ( 28000 );    //first socket to pass info (username/password)
        ServerSocket serverSocket2 = new ServerSocket ( 25188 );    //second socket to pass data (file nedded)
        while ( true ) {        //loop to make thread
            Socket client = serverSocket1.accept ( );
            Socket server=serverSocket2.accept ();
            ClientConnection object_user = new ClientConnection ( client ,server);
            object_user.start ( );
        }
    }

    static class ClientConnection extends Thread {
        Socket ClientSocket1;
        Socket ClientSocket2;

        ClientConnection ( Socket clientSocket ,Socket serverSocket) {      //parameterized constructor
            this.ClientSocket1 = clientSocket;
            this.ClientSocket2=serverSocket;
        }

        public void run () {
            try {
                Vector<String> dataOnLine = new Vector<> ( 1 );     //to get user's info from file
                int line = 1;
                try {
                    File myObj = new File ( "E:\\Data_of_users.txt" ); // created a file object named myObj
                    Scanner MyReader = new Scanner ( myObj );  //create stream to read data from files
                    while ( MyReader.hasNextLine ( ) ) {        //to get all lines in file
                        String info = MyReader.nextLine ( );    //to read each line from file
                        dataOnLine.add ( info );               //add each line to vector
                        line++;
                    }
                } catch ( FileNotFoundException e ) {
                    System.out.println ( "File not founded" );
                }
                DataInputStream clientReadSource = new DataInputStream ( this.ClientSocket1.getInputStream ( ) );
                DataOutputStream clientWriteSource = new DataOutputStream ( this.ClientSocket1.getOutputStream ( ) );
                clientWriteSource.writeUTF ( "Please, Enter username:" );
                String request1 = clientReadSource.readUTF ( );
                String user = request1;
                int dummy = 0;
                String request2 = "";
                for ( int i = 0 ; i < dataOnLine.size ( ) ; i++ ) {
                    if ( request1.equals ( dataOnLine.get ( i ) ) ) {
                        request2 = dataOnLine.get ( i + 1 );
                        dummy = 1;
                        break;
                    }
                }
                if ( dummy == 0 ) {
                    clientWriteSource.writeUTF ( "Login Failed and the connection will terminate" );
                    System.out.println ( "Invalid user try to sign in" );
                } else {
                    clientWriteSource.writeUTF ( "Please Enter the password" );
                    request1 = clientReadSource.readUTF ( );
                    if ( request1.equals ( request2 ) ) {
                        clientWriteSource.writeUTF ( "Login Successfully" );
                        System.out.println ( "User: " + user + " is connected" );
                    } else {
                        clientWriteSource.writeUTF ( "Login Failed and the connection will terminate" );
                        System.out.println ( "Invalid user try to sign in" );
                    }
                    while ( true ) {
                        clientWriteSource.writeUTF ( "Enter ( show my directories / close )" );
                        String msg = clientReadSource.readUTF ( );
                        System.out.println ( "User " + user + " entered " + msg );
                        if ( msg.equals ( "close" ) ) {
                            System.out.println ( "client " + user + " terminated" );
                            break;
                        }

                        File file = new File ( "E:\\FTP\\" + user );
                        String[] files = file.list ( );
                        clientWriteSource.writeInt ( files.length );
                        clientWriteSource.writeUTF ( "the existing directories: " );
                        for ( String string : files ) {
                            String ps = string;
                            clientWriteSource.writeUTF ( ps );
                        }
                        String msg7 = clientReadSource.readUTF ( );
                        int ff = 0;
                        for ( String string : files ) {
                            String ps = string;
                            if ( msg7.equals ( ps ) )
                            {
                                ff=1;
                            }
                        }

                        if ( ff==0 )
                        {
                            clientWriteSource.writeUTF ( "This directory is not found" );
                            continue;
                        }

                        File file2 = new File ( "E:\\FTP\\" + user + "\\" + msg7 );
                        String[] files2 = file2.list ( );
                        clientWriteSource.writeInt ( files2.length );
                        Vector<String> v = new Vector<String> ( 1 );
                        clientWriteSource.writeUTF ( "the existing files: " );
                        for ( String string : files2 ) {
                            String ps = string;
                            clientWriteSource.writeUTF ( ps );
                            v.add ( ps );
                        }
                        Integer f = 0;
                        String msg2 = clientReadSource.readUTF ( );
                        String msgg = " ";
                        for ( int i = 0 ; i < v.size ( ) ; i++ ) {
                            if ( v.get ( i ).contains ( msg2 ) ) {
                                msgg = v.get ( i );
                                f=1;
                                break;
                            }
                        }
                        if(f==0)
                        {
                            System.out.println ( "File not exist" );
                            clientWriteSource.writeUTF ( "File not exist" );
                            continue;
                        }
                        clientWriteSource.writeUTF ( msgg );

                        System.out.println ( "User " + user + " download file: " + msgg );

                        File file3 = new File ( "E:\\FTP\\" + user + "\\" + msg7 + "\\" + msgg );
                        FileInputStream filereader = new FileInputStream ( file3 );
                        byte Databytes[] = new byte[ ( int ) file3.length ( ) ];
                        filereader.read ( Databytes );
                        clientWriteSource.writeUTF ( "File: " + msgg + " is downloaded" );
                        try {
                            DataOutputStream dataout = new DataOutputStream ( ClientSocket2.getOutputStream ( ) );
                            dataout.writeInt ( Databytes.length );
                            dataout.write ( Databytes );
                        } catch ( Exception e ) {
                            System.out.println ( "Download failed" );
                        }
                    }

                }
            }
            catch ( Exception e )
            {
                System.out.println ( "Something wrong" );
            }
        }

    }
}
