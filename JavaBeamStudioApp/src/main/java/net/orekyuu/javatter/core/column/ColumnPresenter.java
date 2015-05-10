package net.orekyuu.javatter.core.column;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TitledPane;
import net.orekyuu.javatter.api.column.ColumnController;
import net.orekyuu.javatter.api.column.ColumnRegister;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.twitter.ClientUserRegister;
import net.orekyuu.javatter.core.JavatterFXMLLoader;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ColumnPresenter implements Initializable {
    @FXML
    private TitledPane root;
    @FXML
    private ChoiceBox<String> columnType;
    @FXML
    private ChoiceBox<String> account;

    private Optional<ColumnState> columnState = Optional.empty();
    private Consumer<String> onColumnChange;

    @Inject
    private ColumnRegister columnRegister;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ClientUserRegister.getInstance().registeredUserList()
                .stream()
                .map(ClientUser::getName)
                .forEach(account.getItems()::add);
        root.setCollapsible(false);

        ColumnManager manager = (ColumnManager) columnRegister;
        manager.getRegisteredColumns().stream().map(Column::getName).forEach(columnType.getItems()::add);

        columnType.getSelectionModel().selectedItemProperty().addListener(listener -> changeColumn());
        account.getSelectionModel().selectedItemProperty().addListener(listener -> changeColumn());
    }

    public void changeUserInfo() {
        String item = account.getSelectionModel().getSelectedItem();
        Platform.runLater(() ->{
            account.getItems().clear();
            ClientUserRegister.getInstance().registeredUserList()
                    .stream()
                    .map(ClientUser::getName)
                    .forEach(account.getItems()::add);
            if (account.getItems().contains(item)) {
                account.getSelectionModel().select(item);
            }
        });
    }

    private void changeColumn() {
        ColumnManager manager = (ColumnManager) columnRegister;
        String name = columnType.getSelectionModel().selectedItemProperty().get();
        Optional<Column> column = manager.findColumn(name);
        String userName = account.getSelectionModel().getSelectedItem();
        Optional<ClientUser> user = ClientUserRegister.getInstance()
                .getUsers(c -> c.getName().equals(userName)).stream().findFirst();
        setColumn(column, user);
        if (onColumnChange == null)
            return;
        if (column.isPresent()) {
            onColumnChange.accept(column.get().getName());
        } else {
            onColumnChange.accept(null);
        }
    }

    public Optional<ColumnState> getColumnState() {
        return columnState;
    }

    public void setColumn(Optional<Column> column, Optional<ClientUser> user) {
        columnState = Optional.empty();
        if (!column.isPresent()) {
            root.setContent(null);
            return;
        }
        if (!user.isPresent()) {
            return;
        }

        user.ifPresent(u -> columnState = Optional.of(new ColumnState(column.get().getName(), user.get())));
        FXMLLoader loader = new JavatterFXMLLoader();
        try {
            Parent p = loader.load(column.get().createInputStream());
            Object controller = loader.getController();
            user.ifPresent(((ColumnController) controller)::setClientUser);
            root.setContent(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        columnType.setValue(columnState.get().getColumnName());
        account.setValue(user.get().getName());
    }

    public void setOnChangeEvent(Consumer<String> onColumnChange) {
        this.onColumnChange = onColumnChange;
    }
}
