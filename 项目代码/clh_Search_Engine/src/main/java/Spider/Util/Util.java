package Spider.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

public class Util {
    public static byte[] getImage(String url) throws IOException {
        URL imgurl = new URL(url);
        InputStream in = imgurl.openStream();
        byte[] imgData = in.readAllBytes();

        return Base64.getEncoder().encode(imgData);
    }
}
