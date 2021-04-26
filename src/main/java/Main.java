import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("test.fxml"));
        Parent root = loader.load();
        stage.setTitle("Production");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
