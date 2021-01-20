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