package GUI;

import GUI.Controller.ClientViewController;
import be.Users;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/Templates/CreateDefaultTemplate.fxml"));
        Parent root = loader.load();
        stage.setTitle("Production");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }




/*
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientView.fxml"));
        Parent root = loader.load();
        ClientViewController controller = loader.getController();
        controller.setUser(new Users(1, "xx", "xx"), stage);
        stage.setTitle("Screen for user 1");
    }

 */




}
