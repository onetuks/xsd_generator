package util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class IconLoader {

  public static final int ICON_SIZE = 20;
  public static final String FOLDER_ICON_PATH = "/folder.png";
  public static final String DELETE_ICON_PATH = "/delete.png";

  /**
   * 아이콘 리소스가 배포 패키징 실수 등으로 누락되어도 앱 전체가 죽지 않도록,
   * 실패 시 빈 아이콘으로 대체한다.
   */
  public ImageIcon loadIcon(String resourcePath) {
    URL url = getClass().getResource(resourcePath);
    if (url == null) {
      System.err.println("아이콘 리소스를 찾을 수 없습니다: " + resourcePath);
      return blankIcon();
    }

    try {
      Image image = ImageIO.read(url);
      if (image == null) {
        System.err.println("아이콘 리소스를 읽을 수 없습니다: " + resourcePath);
        return blankIcon();
      }
      Image scaledImage = image.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
      return new ImageIcon(scaledImage);
    } catch (IOException e) {
      System.err.println("아이콘 리소스를 읽는 중 오류가 발생했습니다: " + resourcePath);
      return blankIcon();
    }
  }

  private ImageIcon blankIcon() {
    return new ImageIcon(new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB));
  }
}
