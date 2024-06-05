import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Utility {
    public static BufferedImage flipImageHorizontally(BufferedImage image) {
        BufferedImage flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), 2);
        Graphics2D g2d = flippedImage.createGraphics();
        AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
        transform.translate(-image.getWidth(), 0);
        g2d.drawImage(image, transform, null);
        g2d.dispose();
        return flippedImage;
    }

    public static BufferedImage[][] flipEvery(BufferedImage[][] array) {
        int rows = array.length;
        int cols = array[0].length;

        BufferedImage[][] returnList = new BufferedImage[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                returnList[r][c] = flipImageHorizontally(array[r][c]);
            }
        }
        return returnList;
    }
}
