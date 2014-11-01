package net.orekyuu.javatter.core.config;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GeneralConfigPresenter implements Initializable {

    public CheckBox checkTweet;
    public CheckBox checkReply;
    public CheckBox checkRT;
    public CheckBox checkFav;
    public ChoiceBox<String> nameDisplayType;
    public CheckBox isExpandURL;
    private GeneralConfigTable lasted;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameDisplayType.getItems().addAll(NameDisplayType.ID.configName(), NameDisplayType.NAME.configName(), NameDisplayType.ID_NAME.configName(), NameDisplayType.NAME_ID.configName());

        lasted = loadConfigFromDB();
        if (lasted == null) lasted = new GeneralConfigTable();
        checkTweet.setSelected(lasted.checkTweet);
        checkReply.setSelected(lasted.checkReply);
        checkRT.setSelected(lasted.checkRT);
        checkFav.setSelected(lasted.checkFav);
        nameDisplayType.getSelectionModel().select(NameDisplayType.valueOf(lasted.nameDisplayType).configName());
        isExpandURL.setSelected(lasted.isExpandURL);

    }

    public void save() {
        JdbcPooledConnectionSource connectionSource = null;
        GeneralConfigTable config = new GeneralConfigTable();
        config.checkFav = checkFav.isSelected();
        config.checkReply = checkReply.isSelected();
        config.checkTweet = checkTweet.isSelected();
        config.checkRT = checkRT.isSelected();
        config.isExpandURL = isExpandURL.isSelected();
        NameDisplayType name = Arrays.stream(NameDisplayType.values()).filter(type -> type.configName().equals(nameDisplayType.getValue())).findFirst().get();
        config.nameDisplayType = name.name();
        try {
            makeFile();
            connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite:" + ConfigPresenter.db_name);
            Dao<GeneralConfigTable, Long> dao = DaoManager.createDao(connectionSource, GeneralConfigTable.class);
            TableUtils.createTableIfNotExists(connectionSource, GeneralConfigTable.class);
            if (dao.countOf() == 0) {
                dao.create(config);
            } else {
                dao.deleteById(0L);
                dao.create(config);
            }
            lasted = config;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            ExceptionDialogBuilder.create(e);
        } finally {
            if (connectionSource != null) {
                try {
                    connectionSource.close();
                } catch (SQLException e) {
                    ExceptionDialogBuilder.create(e);
                }
            }
        }
    }

    public void cancel() {
        checkTweet.setSelected(lasted.checkTweet);
        checkReply.setSelected(lasted.checkReply);
        checkRT.setSelected(lasted.checkRT);
        checkFav.setSelected(lasted.checkFav);
        nameDisplayType.getSelectionModel().select(NameDisplayType.valueOf(lasted.nameDisplayType).configName());
        isExpandURL.setSelected(lasted.isExpandURL);
    }

    private void makeFile() throws IOException {
        File file = new File(ConfigPresenter.db_name);
        if (!file.exists()) file.createNewFile();
    }

    private GeneralConfigTable loadConfigFromDB() {
        JdbcPooledConnectionSource connectionSource = null;
        try {
            makeFile();
            connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite:" + ConfigPresenter.db_name);
            Dao<GeneralConfigTable, Long> dao = DaoManager.createDao(connectionSource, GeneralConfigTable.class);
            TableUtils.createTableIfNotExists(connectionSource, GeneralConfigTable.class);
            QueryBuilder<GeneralConfigTable, Long> queryBuilder = dao.queryBuilder();
            if (dao.countOf() == 0) return new GeneralConfigTable();
            return dao.queryForId(0L);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            ExceptionDialogBuilder.create(e);
        } finally {
            if (connectionSource != null) {
                try {
                    connectionSource.close();
                } catch (SQLException e) {
                    ExceptionDialogBuilder.create(e);
                }
            }
        }
        return null;
    }

    @DatabaseTable(tableName = "general_config")
    private static class GeneralConfigTable {
        @DatabaseField(id = true)
        private long id = 0;
        @DatabaseField(canBeNull = false)
        private boolean checkTweet = false;
        @DatabaseField(canBeNull = false)
        private boolean checkReply = false;
        @DatabaseField(canBeNull = false)
        private boolean checkRT = false;
        @DatabaseField(canBeNull = false)
        private boolean checkFav = false;
        @DatabaseField(canBeNull = false)
        private String nameDisplayType = NameDisplayType.ID_NAME.name();
        @DatabaseField(canBeNull = false)
        private boolean isExpandURL = true;
    }
}
