package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;

public record IconLoader() {

    public static final String FOLDER_ICON_PATH = "/folder.png";
    public static final String DELETE_ICON_PATH = "/delete.png";

    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;

    public ImageIcon loadIcon(String resourcePath) {
        URL url = Objects.requireNonNull(getClass().getResource(resourcePath));

        try {
            Image image = Objects.requireNonNull(ImageIO.read(url));
            Image scaledImage = image.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
