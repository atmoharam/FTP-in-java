/////////////////////// image


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.*;

class Client {
    public static void main ( String[] args ) throws Exception {
        try ( Socket socket = new Socket ( "localHost", 25000 ) ) {
            BufferedImage image = ImageIO.read ( socket.getInputStream ( ) );
            File down = new File ( "E:\\ahmed.jpg" );
            ImageIO.write ( image, "jpg", down );
        }
    }
}



////////////////// Vedio

import javax.imageio.ImageIO;
        import java.awt.image.BufferedImage;
        import java.io.DataInputStream;
        import java.io.File;
        import java.io.FileOutputStream;
        import java.net.*;

class client {
    public static void main ( String[] args ) throws Exception {
        try ( Socket socket = new Socket ( "localHost", 25000 ) ) {

            File nf = new File("D:/K.mp4");
            FileOutputStream fw = new FileOutputStream(nf);
            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            int length = dIn.readInt();                    // read length of incoming message
            if(length>0) {
                byte[] message = new byte[length];
                dIn.readFully(message, 0, message.length); // read the message
                fw.write ( message );
            }
        }
    }
}