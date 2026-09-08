import java.awt.image.BufferedImage;

public class MarginDetector_1 {
    public static boolean isMarginRow(BufferedImage image, int y) { //해당 줄이 여백인지?
        int max = 0;
        int min = 255;

        for (int x =2; x < image.getWidth()-2; x++) {
            int pixel = image.getRGB(x, y);

            int r = (pixel >> 16) & 0xFF;
            int g = (pixel >> 8)  & 0xFF;
            int b =  pixel        & 0xFF;
            int brightness = (r + g + b) / 3;   // 0~255

            max = Math.max(max, brightness); // 해당 줄의 픽셀값이 제일 밝은 픽셀값 
            min = Math.min(min, brightness); // 해당 줄의 픽셀값이 제일 어두운 픽셀값

        }

        // 12는 밝기 임계값. 이 행이 균일한가? 
        return (max - min) < 12;  
    }
}
