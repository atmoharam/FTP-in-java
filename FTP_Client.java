import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.*;

class client {
    public static void main ( String[] args ) throws Exception {
        try ( Socket socket = new Socket ( "localHost" , 25000 ) ) {
            //create a new file to store data
            File DownloadedFile = new File ( "D:/K.jpg" );
            //create file output stream to write data in file
            FileOutputStream FileWriter = new FileOutputStream ( DownloadedFile );
            //create data stream to read data from socket
            DataInputStream DataDownloaded = new DataInputStream ( socket.getInputStream ( ) );
            //read the length of data
            int length = DataDownloaded.readInt ( );
            //if no data sent will do nothing
            if ( length > 0 ) {
                //create array of bytes to store data with length
                byte[] DataBytes = new byte[ length ];
                //read the bytes of data
                DataDownloaded.readFully ( DataBytes , 0 , DataBytes.length );
                //write data from array to file
                FileWriter.write ( DataBytes );
            }
        }
    }
}