package net.orekyuu.javatter.core.config;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.config.ConfigPageBase;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.entity.Account;
import net.orekyuu.javatter.api.inject.Inject;
import net.orekyuu.javatter.api.loader.FxLoader;
import net.orekyuu.javatter.api.service.AccountService;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import net.orekyuu.javatter.core.Main;

import java.io.IOException;
import java.util.List;

public class AccountController extends ConfigPageBase {
    @FXML
    private ListView<ClientUser> accountList;
    @FXML
    private Button deleteAccountButton;

    @Inject
    private AccountService accountService;

    @Override
    protected void initializeBackground() {
        accountService.findAll().stream().forEach(accountList.getItems()::add);
    }

    @Override
    protected void initializeUI() {
        deleteAccountButton.disableProperty().bind(Bindings.isNull(accountList.getSelectionModel().selectedItemProperty()));
        accountList.setCellFactory(c -> new ListCell<ClientUser>() {
            @Override
            protected void updateItem(ClientUser item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty)
                    setText(item.getName());
                else
                    setText(null);
            }
        });
    }

    @FXML
    private void addAccount() {
        FxLoader loader = new FxLoader();
        try {
            Parent parent = loader.load(Main.class.getResourceAsStream("Signin.fxml"));
            SigninController controller = loader.getController();
            Stage stage = new Stage();
            controller.setThisStage(stage);
            stage.setScene(new Scene(parent));
            stage.setTitle("認証");
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            accountList.getItems().clear();
            List<ClientUser> accounts = accountService.findAll();
            accounts.forEach(accountList.getItems()::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteAccount() {
        Task<Void> task = new Task<Void>() {
            private ClientUser user;

            @Override
            protected Void call() {
                user = accountList.getSelectionModel().getSelectedItem();
                accountService.removeAccount(user);
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                accountList.getItems().remove(user);
            }
        };
        bindTask(task);
        TaskUtil.startTask(task);
    }
}
