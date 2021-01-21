import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.*;

public class server {
    public static void main ( String[] args ) throws Exception {
        //create a file of any ex (jpg, mp3 , mp4 , txt , pdf)
        File file = new File ( "D:\\op.jpg" );
        //create a file input stream to read the file content
        FileInputStream FileReader = new FileInputStream ( file );
        //create an array of byte with file length
        byte DataBytes[] = new byte[ ( int ) file.length ( ) ];
        //read the data and put data in array as bytes
        FileReader.read ( DataBytes );
        try ( ServerSocket serv = new ServerSocket ( 25000 ) ) {
            try ( Socket socket = serv.accept ( ) ) {
                //create a Data out put stream and pass the output to socket stream
                DataOutputStream DataOut = new DataOutputStream ( socket.getOutputStream ( ) );
                //write length of data and data in socket stream
                DataOut.writeInt ( DataBytes.length );
                DataOut.write ( DataBytes );
            }
        }
    }
}
