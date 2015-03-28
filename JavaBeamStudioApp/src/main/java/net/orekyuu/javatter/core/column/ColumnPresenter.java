package net.orekyuu.javatter.core.column;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TitledPane;
import javafx.util.StringConverter;
import net.orekyuu.javatter.api.column.Column;
import net.orekyuu.javatter.api.column.ColumnController;
import net.orekyuu.javatter.api.column.ColumnLoader;
import net.orekyuu.javatter.api.entity.Account;
import net.orekyuu.javatter.api.entity.OpenColumnEntity;
import net.orekyuu.javatter.api.inject.Inject;
import net.orekyuu.javatter.api.loader.FxLoader;
import net.orekyuu.javatter.api.service.AccountService;
import net.orekyuu.javatter.api.service.ColumnManager;
import net.orekyuu.javatter.api.service.ColumnService;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.ApplicationImpl;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ColumnPresenter implements Initializable {
    @FXML
    private TitledPane root;
    @FXML
    private ChoiceBox<Column> columnType;
    @FXML
    private ChoiceBox<String> account;

    private ObjectProperty<Account> owner = new SimpleObjectProperty<>();

    private ObjectProperty<Column> column = new SimpleObjectProperty<>();

    @Inject
    private AccountService accountService;
    @Inject
    private ColumnService columnService;
    @Inject
    private ColumnManager columnManager;

    private Optional<ColumnController> contentController = Optional.empty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnType.setConverter(new StringConverter<Column>() {
            @Override
            public String toString(Column object) {
                return object.getColumnName();
            }

            @Override
            public Column fromString(String string) {
                return null;
            }
        });

        accountService.findAll().stream().map(Account::getScreenName).forEach(account.getItems()::add);
        columnManager.findAll().forEach(columnType.getItems()::add);

        columnType.getSelectionModel().selectedItemProperty().addListener(listener -> changeColumn());
        account.getSelectionModel().selectedItemProperty().addListener(listener -> {
            Optional<Account> byScreenName = accountService.findByScreenName(account.getValue());
            if (byScreenName.isPresent()) {
                ClientUser clientUser = accountService.getClientUser(byScreenName.get());
                contentController.ifPresent(c -> c.setClientUser(Optional.of(clientUser)));
            } else {
                contentController.ifPresent(c -> c.setClientUser(Optional.empty()));
            }
        });

        //カラムの持ち主が変更されればChoiceBoxの選択をあわせる
        owner.addListener((observable, oldValue, newValue) -> {
            account.setValue(newValue.getScreenName());
        });

        //カラムのタイプが変更されればChoiceBoxの選択をあわせる
        column.addListener((observable, oldValue, newValue) -> {
            columnType.setValue(newValue);
        });

    }

    private void loadContent(Column column) {
        ColumnLoader columnLoader = new ColumnLoader(column);
        try {
            columnLoader.load();
            root.setContent(columnLoader.getRoot());
            contentController = Optional.ofNullable(columnLoader.getController());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeUserInfo() {
        String item = account.getSelectionModel().getSelectedItem();
        Platform.runLater(() -> {
            account.getItems().clear();
            accountService.findAll().stream().map(Account::getScreenName).forEach(account.getItems()::add);
            if (account.getItems().contains(item)) {
                account.getSelectionModel().select(item);
            }
        });
    }

    private void changeColumn() {
        Column column = columnType.getSelectionModel().selectedItemProperty().get();
        loadContent(column == null ? ApplicationImpl.EMPTY_COLUMN : column);
    }

    public void setColumn(OpenColumnEntity column) {
        owner.set(column.getAccount());
        Optional<Column> columnById = columnManager.findColumnById(column.getColumnId());
        this.column.set(columnById.orElse(ApplicationImpl.EMPTY_COLUMN));

        Account account = column.getAccount();
        Optional<ClientUser> clientUser = Optional.empty();
        if (account != null) {
            Optional<Account> byScreenName = accountService.findByScreenName(account.getScreenName());
            if (byScreenName.isPresent()) {
                clientUser = Optional.ofNullable(accountService.getClientUser(byScreenName.get()));
            }
        }
        final Optional<ClientUser> finalClientUser = clientUser;
        contentController.ifPresent(c -> c.setClientUser(finalClientUser));
    }

}
