import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.*;

public class Server {
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
