import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
public class client {
    public static void main ( String[] args ) throws IOException {
        String Extension;  // to store the name of the data that will be received from server
        Scanner in = new Scanner ( System.in ); // declaring a scanner (input stream) from user
        in.useDelimiter ( "\n" ); // make that scanner use Delimiter of new line
        try ( Socket socket1 = new Socket ( InetAddress.getLocalHost ( ), 28000 ) ) {  // first socket to pass the users' info
            Socket socket2 = new Socket ( InetAddress.getLocalHost ( ), 25188 ); // second socket to receive the data from server
            DataInputStream ServerReadSource = new DataInputStream ( socket1.getInputStream ( ) ); // the input-stream to read from server (receive from server)
            DataOutputStream ServerWriteSource = new DataOutputStream ( socket1.getOutputStream ( ) );  // the output-stream to write to server (send-to server)
            //recieving message from server, and printing it to the user
            String EnterUserName = ServerReadSource.readUTF ( );
            System.out.println ( EnterUserName );
            // allowing the user to enter the username and sending it to the server
            String UserName = in.next ( );
            ServerWriteSource.writeUTF ( UserName );
            // accepting the servers' respond
            String Login_Checker = ServerReadSource.readUTF ( );
            System.out.println ( Login_Checker );
            // if the user entered wrong username, will enter this if-condition and program terminates
            if ( Login_Checker.equals ( "Login Failed and the connection will terminate" ) ) {
                socket1.close ( );
                socket2.close ( );
                ServerWriteSource.close ( );
                ServerReadSource.close ( );
                return;
            }
            // to take the password from user if a right username was entered, and sending it to the server
            String Password = in.next ( );
            ServerWriteSource.writeUTF ( Password );
            // getting the server's response to the password
            Login_Checker = ServerReadSource.readUTF ( );
            // checker if the password is wrong will enter this condition and the program terminate
            if ( Login_Checker.equals ( "Login Failed and the connection will terminate" ) ) {
                socket1.close ( );
                socket2.close ( );
                ServerWriteSource.close ( );
                ServerReadSource.close ( );
                System.out.println ( Login_Checker );
                return;
            }
            // if the user entered right username and password, the program enters this scope
            else {
                while ( true ) {
                    String StartAgain = ServerReadSource.readUTF ( ); // to take response from server
                    System.out.println ( StartAgain ); // to print the server's response
                    String choice_of_user_to_continue_or_not = in.next ( );  // to take the user's option to continue the program or exit
                    ServerWriteSource.writeUTF ( choice_of_user_to_continue_or_not );  // to send the users' choice to the server
                    // if the user entered (close), the program will enter this if-condition & terminate
                    if ( choice_of_user_to_continue_or_not.equals ( "close" ) ) {
                        socket1.close ( );
                        socket2.close ( );
                        ServerWriteSource.close ( );
                        ServerReadSource.close ( );
                        break;
                    }
                    int Num_Of_Files = ServerReadSource.readInt ( );     //to store no. of files
                    String msg = ServerReadSource.readUTF ( );    // to store the server's respond
                    System.out.println ( msg );  // to print the server's respond
                    // to print the folders in this directory
                    for ( int i = 0 ; i < Num_Of_Files ; i++ ) {
                        String Directory = ServerReadSource.readUTF ( );
                        System.out.println ( Directory );
                    }
                    String kind_Of_Direction = in.next ( ); // to store the user's chosen folder
                    ServerWriteSource.writeUTF ( kind_Of_Direction );  // to send the folder's name to the server
                    msg =ServerReadSource.readUTF ();  // to receive the server's respond about this folder

                    // if the wanted folder doesn't exist, will enter this if-condition
                    if ( msg.equals ( "This directory is not found" ) )
                    {
                        System.out.println ( msg );
                        continue;
                    }
                    // if this folder exist, enters this scope
                    else {

                        Num_Of_Files = ServerReadSource.readInt ( );  // to receive the number of files in this folder
                        msg = ServerReadSource.readUTF ( ); // to receive a message from server
                        System.out.println ( msg );  // printing this message
                        // a loop to receive & print the existing files name in the folder
                        for ( int i = 0 ; i < Num_Of_Files ; i++ ) {
                            String Data = ServerReadSource.readUTF ( );
                            System.out.println ( Data );
                        }
                        String Kind_Of_Data = in.next ( ); // to take from user the wanted file (to be downloaded)
                        ServerWriteSource.writeUTF ( Kind_Of_Data ); //to send the wanted file to server
                        Extension = ServerReadSource.readUTF ( ); // to receive the server's respond

                        // if the wanted file doesn't exists in the folder, would enter the loop
                        if ( Extension.equals ( "File not exist" ) ) {
                            System.out.println ( Extension );
                            continue;
                        }

                        System.out.println ( ServerReadSource.readUTF ( ) ); // printing a received line from server
                        File DownloadedFile = new File ( "D:\\ftp2\\" + Extension ); // the path of the downloaded file
                        FileOutputStream FileWriter = new FileOutputStream ( DownloadedFile ); // to write to the folder that will contain the downloaded file
                        DataInputStream DataDownloaded = new DataInputStream ( socket2.getInputStream ( ) ); // receive the file that will be downloaded from the server
                        int length = DataDownloaded.readInt ( );   // the length of the file that will be downloaded

                        // If there is data in the file, send the required data to the folder of downloads
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
}
