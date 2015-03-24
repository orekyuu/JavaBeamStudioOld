package net.orekyuu.javatter.core.column;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TitledPane;
import net.orekyuu.javatter.api.API;
import net.orekyuu.javatter.api.column.ColumnController;
import net.orekyuu.javatter.api.entity.Account;
import net.orekyuu.javatter.api.inject.Inject;
import net.orekyuu.javatter.api.loader.FxLoader;
import net.orekyuu.javatter.api.service.AccountService;
import net.orekyuu.javatter.api.twitter.ClientUser;

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
    private AccountService accountService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountService.findAll().stream().map(Account::getScreenName).forEach(account.getItems()::add);
        root.setCollapsible(false);

        ColumnManager manager = (ColumnManager) API.getInstance().getColumnRegister();
        manager.getRegisteredColumns().stream().map(Column::getName).forEach(columnType.getItems()::add);

        columnType.getSelectionModel().selectedItemProperty().addListener(listener -> changeColumn());
        account.getSelectionModel().selectedItemProperty().addListener(listener -> changeColumn());
    }

    public void changeUserInfo() {
        String item = account.getSelectionModel().getSelectedItem();
        Platform.runLater(() ->{
            account.getItems().clear();
            accountService.findAll().stream().map(Account::getScreenName).forEach(account.getItems()::add);
            if (account.getItems().contains(item)) {
                account.getSelectionModel().select(item);
            }
        });
    }

    private void changeColumn() {
        ColumnManager manager = (ColumnManager) API.getInstance().getColumnRegister();
        String name = columnType.getSelectionModel().selectedItemProperty().get();
        Optional<Column> column = manager.findColumn(name);
        String userName = account.getSelectionModel().getSelectedItem();
        Optional<Account> user = accountService.findByScreenName(userName);
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

    public void setColumn(Optional<Column> column, Optional<Account> user) {
        columnState = Optional.empty();
        if (!column.isPresent()) {
            root.setContent(null);
            return;
        }
        if (!user.isPresent()) {
            return;
        }
        Account account = user.get();
        ClientUser clientUser = accountService.getClientUser(account);

        user.ifPresent(u -> columnState = Optional.of(new ColumnState(column.get().getName(), clientUser)));
        FxLoader loader = new FxLoader();
        try {
            Parent p = loader.load(column.get().createInputStream());
            ColumnController controller = loader.getController();
            controller.setClientUser(clientUser);
            root.setContent(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        columnType.setValue(columnState.get().getColumnName());
        this.account.setValue(user.get().getScreenName());
    }

    public void setOnChangeEvent(Consumer<String> onColumnChange) {
        this.onColumnChange = onColumnChange;
    }
}
