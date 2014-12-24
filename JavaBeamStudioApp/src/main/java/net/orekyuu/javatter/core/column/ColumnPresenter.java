package net.orekyuu.javatter.core.column;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TitledPane;
import net.orekyuu.javatter.api.API;
import net.orekyuu.javatter.api.JavatterColumn;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.twitter.ClientUserRegister;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ColumnPresenter implements Initializable {
    @FXML
    private TitledPane root;
    @FXML
    private ChoiceBox<String> columnType;
    @FXML
    private ChoiceBox<String> account;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ClientUserRegister.getInstance().registeredUserList()
                .stream()
                .map(ClientUser::getName)
                .forEach(account.getItems()::add);
        root.setCollapsible(false);

        ColumnManager manager = (ColumnManager) API.getInstance().getColumnRegister();
        manager.getRegisteredColumns().stream().map(Column::getName).forEach(columnType.getItems()::add);

        columnType.getSelectionModel().selectedItemProperty().addListener(listener -> changeColumn());
        account.getSelectionModel().selectedItemProperty().addListener(listener -> changeColumn());
    }

    private void changeColumn() {
        ColumnManager manager = (ColumnManager) API.getInstance().getColumnRegister();

        List<ClientUser> users = ClientUserRegister.getInstance().getUsers(c -> c.getName().equals(account.getSelectionModel().getSelectedItem()));

        String name = columnType.getSelectionModel().selectedItemProperty().get();
        Column column = manager.findColumn(name);
        FXMLLoader loader = new FXMLLoader();
        try {
            Parent p = loader.load(column.createInputStream());
            Object controller = loader.getController();
            if(controller instanceof JavatterColumn && !users.isEmpty())
                ((JavatterColumn) controller).setClientUser(users.get(0));
            root.setContent(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
