package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;

public class IconLoader {

    public static final int ICON_SIZE = 20;
    public static final String FOLDER_ICON_PATH = "/folder.png";
    public static final String DELETE_ICON_PATH = "/delete.png";

    public ImageIcon loadIcon(String resourcePath) {
        URL url = Objects.requireNonNull(getClass().getResource(resourcePath));

        try {
            Image image = Objects.requireNonNull(ImageIO.read(url));
            Image scaledImage = image.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
