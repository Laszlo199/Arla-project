package gui.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class ImageLoader {

    private static Path destinationPath;
    private final static String DESTINATION_PATH_Image = "src/../Data/ImageData/";

    public static Path loadImage(AnchorPane anchorPane) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images",
                "*.png", "*.jpg"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        Image image;

        if (!files.isEmpty()) {
            String fileP = files.get(0).toURI().toString();
            image = new Image(fileP);

            anchorPane.getChildren().removeAll();
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            anchorPane.getChildren().add(imageView);
            imageView.setFitHeight(anchorPane.getHeight());
            imageView.setFitWidth(anchorPane.getWidth());

            destinationPath = Path.of(DESTINATION_PATH_Image + files.get(0).getName());
            FileSaver.saveFile(files.get(0), destinationPath);
        }
        return destinationPath;
    }

    public static Path getDestinationPath() {
        return destinationPath;
    }

    public static void setDestinationPath(Path destinationPath) {
        ImageLoader.destinationPath = destinationPath;
    }
}
