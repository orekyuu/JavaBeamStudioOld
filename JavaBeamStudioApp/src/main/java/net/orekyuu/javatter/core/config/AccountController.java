package net.orekyuu.javatter.core.config;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.twitter.ClientUserRegister;
import net.orekyuu.javatter.core.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AccountController implements Initializable {
    @FXML
    private ListView<ClientUser> accountList;
    @FXML
    private Button deleteAccountButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deleteAccountButton.disableProperty().bind(Bindings.isNull(accountList.getSelectionModel().selectedItemProperty()));
        accountList.setCellFactory(c ->
            new ListCell<ClientUser>() {
                @Override
                protected void updateItem(ClientUser item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!empty)
                        setText(item.getName());
                    else
                        setText(null);
                }
            }
        );

        ClientUserRegister.getInstance().registeredUserList().stream().forEach(accountList.getItems()::add);
    }

    public void addAccount() {
        FXMLLoader loader = new FXMLLoader();
        try {
            Parent parent= loader.load(Main.class.getResourceAsStream("Signin.fxml"));
            SigninController controller = loader.getController();
            Stage stage = new Stage();
            controller.setThisStage(stage);
            stage.setScene(new Scene(parent));
            stage.setTitle("認証");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount() {

    }
}
