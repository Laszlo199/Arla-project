package gui.Controller;

import com.jfoenix.controls.JFXSlider;
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
import javafx.util.Duration;

import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ResourceBundle;

/**
 *
 */
public class MoviePlayerController implements Initializable {
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
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public MoviePlayerController(String filePath) {
        this.filePath = filePath;
        initMovie();
    }

    private void initMovie() {
        Path path  = FileSystems.getDefault().getPath(getFilePath());
        media = new Media(path.toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);
        mediaView.setMediaPlayer(mediaPlayer);
        progressSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
        titleL.setText(filePath.substring(filePath.lastIndexOf("/")+1));
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
        setTimeProgressListener();
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
