////////////////image
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.*;

public class server {
    public static void main ( String[] args ) throws Exception {
        File imo = new File ( "D:\\New folder\\tito.jpg" );
        BufferedImage image = ImageIO.read ( imo );
        try ( ServerSocket serv = new ServerSocket ( 25000 ) ) {
            try ( Socket socket = serv.accept ( ) ) {
                ImageIO.write ( image , "jpg" , socket.getOutputStream ( ) );

            }
        }
    }
}


///////////////////////vedio
import javax.imageio.ImageIO;
        import java.awt.image.BufferedImage;
        import java.io.DataOutputStream;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.ObjectInputStream;
        import java.net.*;

public class server {
    public static void main ( String[] args ) throws Exception {
        File file = new File("D:\\op.mp4");
        FileInputStream fin = new FileInputStream(file);
        byte b[] = new byte[(int)file.length()];
        fin.read(b);
        try ( ServerSocket serv = new ServerSocket ( 25000 ) ) {
            try ( Socket socket = serv.accept ( ) ) {
                DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                dOut.writeInt(b.length); // write length of the message
                dOut.write(b);
            }
        }
    }
}

