package gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
      //  FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateNewScreen.fxml"));
        Parent root = loader.load();
        stage.setTitle("Arla");
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/Icons/arla.png")));
        stage.setScene(scene);
        stage.show();
      //  openOther();
    }

}
