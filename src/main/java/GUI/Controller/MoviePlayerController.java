package gui.Controller;

import com.jfoenix.controls.JFXSlider;
import gui.util.FileSaver;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 */
public class MoviePlayerController implements Initializable {
    private final static String DESTINATION_PATH_VIDEO = "src/../Data/VideoData/";
    @FXML
    private StackPane stackPane;
    @FXML
    private Label titleL;
    @FXML
    private MediaView mediaView;
    @FXML
    private JFXSlider progressSlider;

    private Media media;
    private MediaPlayer mediaPlayer;
    private Boolean isLooping;
    private Path path;

    public void passFileChooser(FileChooser fileChooser) {
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        Path destinationPathVideo = Path.of(DESTINATION_PATH_VIDEO +
                files.get(0).getName());
        FileSaver.saveFile(files.get(0), destinationPathVideo);
        path = destinationPathVideo;
        initMovie();
    }


    private void initMovie() {
        //Path path  = FileSystems.getDefault().getPath(getFilePath());
        media = new Media(path.toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);
        mediaView.setMediaPlayer(mediaPlayer);
        progressSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
        titleL.setText(path.toString().substring(path.toString().lastIndexOf("/")+1));
        setTimeProgressListener();
    }

    /**
     * start playing/ stop playing
     * @param actionEvent
     */
    public void playButtonAction(ActionEvent actionEvent) {
        if(mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING))
            mediaPlayer.pause();
        else
            mediaPlayer.play();
    }

    public void backButtonAction(ActionEvent actionEvent) {
    }

    public void forwardButtonAction(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setProperties();
        //setTimeProgressListener();
    }

    private void setTimeProgressListener() {
      Runnable runnable = () -> {
       mediaPlayer.currentTimeProperty().addListener((observableValue, duration, t1) ->
               progressSlider.setValue(t1.toSeconds()));};
      Thread thread = new Thread(runnable);
      thread.start();
    }

    private void setProperties() {
      mediaView.fitHeightProperty().bind(stackPane.heightProperty());
      mediaView.fitWidthProperty().bind(stackPane.widthProperty());
      mediaView.setPreserveRatio(true);
    }

}
