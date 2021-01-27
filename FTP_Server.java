import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
public class server {
    static Vector<String> dataOnLine = new Vector<> (  );     //to get User's info from file
    public static void main ( String[] args ) throws IOException {
        ServerSocket serverSocket1 = new ServerSocket ( 28000 );    //first socket to pass info (username/password)
        ServerSocket serverSocket2 = new ServerSocket ( 25188 );    //second socket to pass data (file nedded)
        try {
            File myObj = new File ( "E:\\Data_of_users.txt" ); // created a file object named myObj
            Scanner MyReader = new Scanner ( myObj );  //create stream to read data from files
            while ( MyReader.hasNextLine ( ) ) {        //to get all lines in file
                String info = MyReader.nextLine ( );    //to read each line from file
                dataOnLine.add ( info );               //add each line to vector
            }
            // if the file is not found
        } catch ( FileNotFoundException e ) {
            System.out.println ( "File not founded" );
        }
        while ( true ) {        //loop to make thread
            Socket client1 = serverSocket1.accept ( );
            Socket client2=serverSocket2.accept ();
            ClientConnection object_user = new ClientConnection ( client1 ,client2);
            object_user.start ( );
        }
    }

    static class ClientConnection extends Thread {
        // members of the thread
        Socket ClientSocket1;
        Socket ClientSocket2;

        ClientConnection ( Socket clientSocket1 ,Socket clientSocket2) {      //parameterized constructor
            this.ClientSocket1 = clientSocket1;
            this.ClientSocket2=clientSocket2;
        }

        public void run () {
            // the run of the thread
            try {

                DataInputStream clientReadSource = new DataInputStream ( this.ClientSocket1.getInputStream ( ) ); // input stream that pass the info to the members of the thread
                DataOutputStream clientWriteSource = new DataOutputStream ( this.ClientSocket1.getOutputStream ( ) ); // output stream that gets the info from the members of the thread
                clientWriteSource.writeUTF ( "Please, Enter username:" ); // sending this message to the client
                String UserName = clientReadSource.readUTF ( ); // reading a replay from the client
                String User = UserName; //storing it in another variable
                int dummy = 0; // used as a checker if there's a problem in logging in
                String Password_Checker = ""; // to store the real password of this user

                // a loop to get that user's password
                for ( int i = 0 ; i < dataOnLine.size ( )-1 ; i+=2 ) {
                    if ( UserName.equals ( dataOnLine.get ( i ) ) ) {
                        Password_Checker = dataOnLine.get ( i + 1 );
                        dummy = 1;
                        break;
                    }
                }
                // if the username is not right, will enter this if-condition
                if ( dummy == 0 ) {
                    clientWriteSource.writeUTF ( "Login Failed and the connection will terminate" ); // sending this message to client
                    System.out.println ( "Invalid User try to sign in" ); // printing this message to the server window
                }
                //if the username if right
                else {
                    clientWriteSource.writeUTF ( "Please Enter the password" ); // send this message to the client to get the user's password
                    String Password_Of_User = clientReadSource.readUTF ( ); // reading the entered password from user
                    // to check if this password is correct or not
                    if ( Password_Of_User.equals ( Password_Checker ) ) {
                        clientWriteSource.writeUTF ( "Login Successfully" ); // if correct password, send this message to client
                        System.out.println ( "User: " + User + " is connected" ); // and prit this message in server
                    }
                    // if the password is wrong, send this message to the client
                    else {
                        clientWriteSource.writeUTF ( "Login Failed and the connection will terminate" );
                        System.out.println ( "Invalid User try to sign in" ); // and print this message in the server
                    }
                    // while loop to accept commands from user until enters (close)
                    while ( true ) {
                        clientWriteSource.writeUTF ( "Enter ( Show my directories / close )" );  // send this message to the client
                        String User_Request_For_its_Directories = clientReadSource.readUTF ( );  // receive the users' choice to close or open the directory
                        System.out.println ( "User " + User + " entered " + User_Request_For_its_Directories );
                        // if the user entered command the (close), will enter this if-condition
                        if ( User_Request_For_its_Directories.equals ( "close" ) ) {
                            System.out.println ( "Client " + User + " terminated" );
                            break;
                        }

                        // to store the users directory
                        File Path_of_user = new File ( "E:\\FTP\\" + User );
                        String[] List_of_Directories = Path_of_user.list ( ); // to store the users' list of directory
                        clientWriteSource.writeInt ( List_of_Directories.length ); // to send the length of the user's directory to the client
                        clientWriteSource.writeUTF ( "The existing directories: " );  // to send to the client this message

                        // a loop to send all the names of the folders in the existing directories
                        for ( String string : List_of_Directories ) {
                            String Directory = string;
                            clientWriteSource.writeUTF ( Directory );
                        }
                        String Chosen_Directory = clientReadSource.readUTF ( ); // to receive the user's choice for directories
                        int Selected_Directory_Checker = 0;  // a checker if the directory that the user want exists or not

                        for ( String string : List_of_Directories ) {
                            String ps = string;  // to store the name of each directory
                            if ( Chosen_Directory.equals ( ps ) )  // if the directory the user wants found, make checker = 1
                            {
                                Selected_Directory_Checker=1;
                            }
                        }
                        // if the directory is not found (checker ==0)
                        if ( Selected_Directory_Checker==0 )
                        {
                            clientWriteSource.writeUTF ( "This directory is not found" ); // send this message to the client
                            continue;  // re-start the while-loop
                        }
                        // if the directory is found
                        else {
                            clientWriteSource.writeUTF ( "no thing" );  // send this message to client

                        }

                        File Path_Of_Directory = new File ( "E:\\FTP\\" + User + "\\" + Chosen_Directory );  // the path of the directory
                        String[] List_of_Data = Path_Of_Directory.list ( );  // to store the list of files in that directory
                        clientWriteSource.writeInt ( List_of_Data.length ); // to send to the client the length of this list of files
                        Vector<String> v = new Vector<String> ( 1 );  // making a vector array that stores the name of the files in the directory
                        clientWriteSource.writeUTF ( "The existing List of Directories: " ); // sending this message to the client

                        // a loop to send the existing files in this directory to the client
                        for ( String string : List_of_Data ) {
                            String Data = string;
                            clientWriteSource.writeUTF ( Data );
                            v.add ( Data ); // add the file name to the vector array (v)
                        }
                        // a checker that equals 1, in case the file is found
                        Integer Selected_Data_Checker = 0;
                        // reading the name of data required by user from client
                        String Name_Of_Data_From_User = clientReadSource.readUTF ( );
                        String Chosen_Data = " "; // to store the data (from file)

                        // loop by the vector size to check if the data that the user wants exists or not
                        for ( int i = 0 ; i < v.size ( ) ; i++ ) {
                            if ( v.get ( i ).contains ( Name_Of_Data_From_User ) ) {  // to check if data is found
                                Chosen_Data = v.get ( i ); // save it in this variable
                                Selected_Data_Checker=1; // make checker = 1
                                break;  // break the loop
                            }
                        }
                        // if the data is not found in the file, enters this if-condition
                        if(Selected_Data_Checker==0)
                        {
                            System.out.println ( "File not exist" ); // printing this message in server
                            clientWriteSource.writeUTF ( "File not exist" );  // sending this message to the client
                            continue; // re-start the while loop
                        }
                        // send the wanted data name to the client
                        clientWriteSource.writeUTF ( Chosen_Data );

                        // printing this message to the server
                        System.out.println ( "User " + User + " download Path_of_user: " + Chosen_Data );

                        // file to store the complete path of the required data by user
                        File Path_of_Chosen_Data = new File ( "E:\\FTP\\" + User + "\\" + Chosen_Directory + "\\" + Chosen_Data );
                        FileInputStream FileReader = new FileInputStream ( Path_of_Chosen_Data ); // creating the input stream of the second socket
                        byte DataBytes[] = new byte[ ( int ) Path_of_Chosen_Data.length ( ) ]; // declaring array of bytes with the size of the data that will be downloaded in the client
                        FileReader.read ( DataBytes ); // to read the size of the data in the input stream of the second socket
                        clientWriteSource.writeUTF ( "File: " + Chosen_Data + " is downloaded" ); // to send this message to the client
                        try {
                            DataOutputStream DataOut = new DataOutputStream ( ClientSocket2.getOutputStream ( ) ); // creating output stream to send the data to the client
                            DataOut.writeInt ( DataBytes.length ); // to send the length of the data to the client
                            DataOut.write ( DataBytes ); // to send the data (itself) to the user
                        }
                        // if the data is not downloaded successfully, this message will be printed in the server
                        catch ( Exception e ) {
                            System.out.println ( "Download Failed" );
                        }
                    }

                }
            }
            catch ( Exception e )
            {
                System.out.println ( "Something Wrong" );
            }
        }

    }
}
