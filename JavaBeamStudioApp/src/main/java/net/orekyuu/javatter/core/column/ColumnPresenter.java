package net.orekyuu.javatter.core.column;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TitledPane;
import javafx.util.StringConverter;
import net.orekyuu.javatter.api.column.Column;
import net.orekyuu.javatter.api.column.ColumnController;
import net.orekyuu.javatter.api.column.ColumnLoader;
import net.orekyuu.javatter.api.models.OpenColumnInfo;
import net.orekyuu.javatter.core.entity.Account;
import net.orekyuu.javatter.core.entity.OpenColumnEntity;
import net.orekyuu.javatter.api.inject.Inject;
import net.orekyuu.javatter.api.service.AccountService;
import net.orekyuu.javatter.api.service.ColumnManager;
import net.orekyuu.javatter.api.service.ColumnService;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.ApplicationImpl;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ColumnPresenter implements Initializable {
    @FXML
    private TitledPane root;
    @FXML
    private ChoiceBox<Column> columnType;
    @FXML
    private ChoiceBox<String> account;

    private ObjectProperty<ClientUser> owner = new SimpleObjectProperty<>();

    private ObjectProperty<Column> column = new SimpleObjectProperty<>();

    @Inject
    private AccountService accountService;
    @Inject
    private ColumnService columnService;
    @Inject
    private ColumnManager columnManager;

    private Optional<ColumnController> contentController = Optional.empty();

    private int index;

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

        accountService.findAll().stream().map(ClientUser::getName).forEach(account.getItems()::add);
        columnManager.findAll().forEach(columnType.getItems()::add);

        columnType.valueProperty().bindBidirectional(column);
        Bindings.bindBidirectional(account.valueProperty(), owner, new StringConverter<ClientUser>() {
            @Override
            public String toString(ClientUser object) {
                if (object == null) {
                    return "null";
                }
                return object.getName();
            }

            @Override
            public ClientUser fromString(String string) {
                if (string == null) {
                    return null;
                }
                return accountService.findByScreenName(string).get();
            }
        });

        owner.addListener((observable, oldValue, newValue) -> {
            changeColumn();
            updateColumnState();
        });

        column.addListener((observable, oldValue, newValue) -> {
            changeColumn();
            updateColumnState();
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

    private void updateColumnState() {
        if (column.get() != null) {
            OpenColumnInfo update = columnService.update(index, column.get(), accountService.findByScreenName(account.getValue()).orElse(null));
            setColumn(update);
        }
    }

    private void changeColumn() {
        loadContent(column.get() == null ? ApplicationImpl.EMPTY_COLUMN : column.get());
    }

    public void setColumn(OpenColumnInfo column) {
        index = column.getColumnIndex().intValue();
        owner.set(column.getClientUser().orElse(null));
        Optional<Column> columnById = columnManager.findColumnById(column.getColumnId());
        this.column.set(columnById.orElse(ApplicationImpl.EMPTY_COLUMN));

        Optional<ClientUser> clientUser = column.getClientUser();
        contentController.ifPresent(c -> c.setClientUser(clientUser));
    }

}
