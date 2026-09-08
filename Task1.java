import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Task1{
    public static void main(String[] args) throws IOException {
        File root = new File ("C:/Users/jbnu/Desktop/new09/jpg");

        for(File dir : root.listFiles()) {  // 001, 002, ... 013 디렉토리들
            if(!dir.isDirectory()) continue;

            File outDir = new File("C:/Users/jbnu/Desktop/new09/output/" + dir.getName());
            outDir.mkdirs(); // 출력 폴더 생성

            for(File img : dir.listFiles()) {
                if(!img.isFile()) continue;
                BufferedImage image = ImageIO.read(img);

                int width = image.getWidth();
                int height = image.getHeight();

                List<Integer> cuts = new ArrayList<>(); // 이 이미지의 컷 위치들
                int runStart = -1;

                for(int y=0; y<height; y++) {
                    boolean margin = MarginDetector_1.isMarginRow(image, y); // margin이 true면 이 행은 여백

                    if(margin == true && runStart == -1){
                        runStart = y;
                    }else if(margin == false && runStart != -1){ // 여백 구간 끝
                        int length = y - runStart;

                        // 진짜 여백: 충분히 길고(그림이 제대로 안쪼개져서 100에서 90으로 낮춤).
                        if(length > 50 && runStart != 0 ) { // (단, 맨 위 여백은 첫 그림과 묶음)
                            cuts.add((runStart + y) / 2); // 구간의 중간지점에서 컷
                        }
                        runStart = -1; // 여백 구간 초기화 (길이와 무관하게 항상)
                    }
                }

                // 자르기 & 저장
                String baseName = img.getName().replace(".jpg", "");
                int prev = 0;
                int pieceNum = 1;

                for(int cut : cuts) {
                    BufferedImage piece = image.getSubimage(0, prev, width, cut - prev);
                    ImageIO.write(piece, "jpg", new File(outDir, baseName + "_" + pieceNum + ".jpg"));
                    prev = cut;
                    pieceNum++;
                }

                // 마지막 조각: 마지막 컷부터 이미지 끝까지
                if(prev < height) {
                    BufferedImage piece = image.getSubimage(0, prev, width, height - prev);
                    ImageIO.write(piece, "jpg", new File(outDir, baseName + "_" + pieceNum + ".jpg"));
                }
            }
        }

    }
}
