package gui.Controller;

import be.CSVInfo;
import be.Screen;
import be.ScreenElement;
import com.jfoenix.controls.JFXTextField;
import gui.Model.ScreenModel;
import gui.util.FileSaver;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EditViewController {
    @FXML private Label usersLbl;
    @FXML private JFXTextField nameField;
    @FXML private AnchorPane space;
    GridPane gridPane = new GridPane();

    private Screen screen;
    private ScreenModel model;
    private List<ScreenElement> sections = new ArrayList<>();
    private List<ScreenElement> sectionsToUpdate = new ArrayList<>();

    public void setScreen(Screen screen) {
        this.screen = screen;
        model = ScreenModel.getInstance();
        sections = model.getSections(screen);
        nameField.setText(screen.getName());

        String users = usersLbl.getText();
        List<String> usernames = model.getUsersForScreen(screen.getId());
        if(!usernames.isEmpty()) for(String u : usernames) users += "\n"+u;
        else users += "\n-";
        usersLbl.setText(users);

        loadScreen();
    }

    private void loadScreen() {
        for(ScreenElement section : sections) {
            if (section.getFilepath() != null) {
                String filePath = section.getFilepath().replace("\\", "/");
                String[] temp = filePath.split("/");
                String fileName = temp[temp.length-1];

                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setStyle("-fx-background-color: white");
                VBox vBox = new VBox();
                Label fileNameLbl = new Label(fileName);
                Button changeFileBtn = new Button("Choose new file");
                changeFileBtn.setOnAction(event -> chooseNew(event, section, fileNameLbl));
                vBox.getChildren().addAll(fileNameLbl, changeFileBtn);
                vBox.setSpacing(10);
                vBox.setPadding(new Insets(10, 10, 10, 10));
                anchorPane.getChildren().addAll(vBox);

                gridPane.add(anchorPane, section.getColIndex(),
                        section.getRowIndex(), section.getColSpan(), section.getRowSpan());
                GridPane.setHgrow(anchorPane, Priority.ALWAYS);
                GridPane.setVgrow(anchorPane, Priority.ALWAYS);
            }
        }
        gridPane.setStyle("-fx-background-color: lightgrey");
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.prefHeightProperty().bind(space.heightProperty());
        gridPane.prefWidthProperty().bind(space.widthProperty());
        setConstraints(gridPane.getRowCount(), gridPane.getColumnCount());
        space.getChildren().add(gridPane);
    }

    private void setConstraints(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setVgrow(Priority.NEVER);
            rowConst.setPercentHeight(100.0 / rows);
            gridPane.getRowConstraints().add(rowConst);

        }
        for (int i = 0; i < cols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setHgrow(Priority.NEVER);
            colConst.setPercentWidth(100.0 / cols);
            gridPane.getColumnConstraints().add(colConst);

        }
    }

    private void chooseNew(ActionEvent event, ScreenElement section, Label fileNameLbl) {
        Path destinationPath=null;
        String destinationFolder="";
        boolean isCSV = false;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Files",
                "*.mp4", "*.WEBM", "*.jpg", "*.png", "*.csv", "*.xlsx", "*.pdf"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        if (!files.isEmpty()) {
            String fileP = files.get(0).toURI().toString();
            String fileType = fileP.substring(fileP.length() - 4);
            switch (fileType) {
                case ".png", ".jpg":
                    destinationFolder = "src/../Data/ImageData/";
                    break;
                case ".pdf":
                    destinationFolder = "src/../Data/PDFData/";
                    break;
                case ".csv":
                    destinationFolder = "src/../Data/CSVData/";
                    getCSVInfo(section);
                    isCSV = true;
                    break;
                case "xlsx":
                    destinationFolder = "src/../Data/XLSXData/";
                    break;
                case ".mp4", "WEBM":
                    destinationFolder = "src/../Data/VideoData/";
                    break;
            }
            destinationPath = Path.of(destinationFolder + files.get(0).getName());
            FileSaver.saveFile(files.get(0), destinationPath);

            if(!isCSV) section.setCsvInfo(null);
            section.setFilepath(destinationPath.toString());
            sectionsToUpdate.add(section);

            String filePath = section.getFilepath().replace("\\", "/");
            String[] t = filePath.split("/");
            fileNameLbl.setText(t[t.length-1]);
        }
    }

    private void getCSVInfo(ScreenElement section) {
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        vBox.setPadding(new Insets(20, 20, 20, 20));
        vBox.setSpacing(10);
        hBox.setSpacing(10);
        RadioButton radioButton = new RadioButton();
        Label label = new Label("first row is a header");
        hBox.getChildren().addAll(radioButton, label);
        ComboBox<String> comboBox = new ComboBox<>();
        List<String> options = new ArrayList<>();
        options.add("barchart");
        options.add("linechart");
        options.add("table");
        comboBox.setItems(FXCollections.observableList(options));
        TextField textField = new TextField("chart title");
        Button button = new Button("load");
        button.setOnAction(event -> saveCSVInfo(radioButton, comboBox, textField, section));
        vBox.getChildren().addAll(hBox, comboBox, textField, button);
        Stage stage = new Stage();
        stage.setScene(new Scene(vBox));
        stage.show();
    }

    private void saveCSVInfo(RadioButton radioButton, ComboBox<String> comboBox, TextField textField, ScreenElement section) {
        Stage stage = (Stage) radioButton.getScene().getWindow();
        stage.close();

        boolean isHeader = false;
        if(radioButton.isSelected()) isHeader = true;
        String title;
        if(textField.getText().equals(null) || textField.getText().equals("chart title")) title = "";
        else title = textField.getText();
        String chartType = comboBox.getSelectionModel().getSelectedItem();

        switch (chartType) {
            case "linechart" -> section.setCsvInfo(new CSVInfo(isHeader, title, CSVInfo.CSVType.LINECHART));
            case "table" -> section.setCsvInfo(new CSVInfo(isHeader, title, CSVInfo.CSVType.TABLE));
            case "barchart" -> section.setCsvInfo(new CSVInfo(isHeader, title, CSVInfo.CSVType.BARCHART));
        }
    }

    public void discardButton(ActionEvent actionEvent) {
        Stage stage = (Stage) space.getScene().getWindow();
        stage.close();
    }

    public void saveButton(ActionEvent actionEvent) {
        screen.setName(nameField.getText());
        model.update(screen);
        model.updateSections(sectionsToUpdate);
    }

    public void selectUsers(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AssignUser.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            AssignUserController controller = fxmlLoader.getController();
            controller.setScreenName(screen.getId());
            controller.setEdit(true);
            stage.setTitle("Assign Users");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
