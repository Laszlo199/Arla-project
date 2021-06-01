package gui.Controller;

import be.Screen;
import be.User;
import gui.Model.ScreenModel;
import gui.Model.UserModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AssignUserController implements Initializable {
    @FXML
    private TableView<User> userTableView;

    @FXML
    private TableColumn<User,String> userNameColumn;


    private UserModel userModel;
    private ScreenModel screenModel;
    private int screenID;
    private boolean isEdit = false;
    private boolean isInEdit = false;
    private Screen screen;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.userModel = new UserModel();
        this.screenModel = new ScreenModel();
        initUserTableView();

    }

    public void setScreenName(int screenName) {
        this.screenID = screenName;

    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    private void initUserTableView(){
        userNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
        ObservableList<User> users = userModel.getAllUser();
        userModel.loadUsers();
        for (int i = 0;i<users.size(); i++){
            if (!users.get(i).isAdmin())
                userTableView.getItems().add(users.get(i));
        }
    }




    public void btnAssign(javafx.event.ActionEvent actionEvent) {
        List<User> selectedUsers = userTableView.getSelectionModel().getSelectedItems();

        if(!isEdit) {
            for (int i = 0; i < selectedUsers.size(); i++) {
                screenModel.saveToUsersAndScreens(screenID, selectedUsers.get(i).getID());
            }
        } else screenModel.updateAssignedUsers(screenID, selectedUsers);


        if(isInEdit){
            System.out.println(" is id null: "+ screenID);
            screen.setRefreshNow(true);
            screenModel.update(screen);
            screen.setRefreshNow(false);
        }
    }


    public void selectUser(javafx.scene.input.MouseEvent event) {
        userTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void isInEdit() {
        isInEdit = true;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
}
