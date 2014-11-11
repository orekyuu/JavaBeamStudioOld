package net.orekyuu.javatter.core.config;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 一般設定を扱うためのヘルパークラスです。
 */
public class GeneralConfigHelper {

    private GeneralConfigHelper() {
        //インスタンス化抑制
        throw new AssertionError();
    }

    /**
     * データベースから一般設定の情報を読み込みます。
     * @return 読み込んだコンフィグの情報
     */
    public synchronized  static GeneralConfigModel loadConfigFromDB() {
        List<GeneralConfig> configs = loadConfigTableFromDB();
        Map<String, String> map = configs.stream()
                .collect(Collectors.toMap(GeneralConfig::getConfigName, GeneralConfig::getData));
        return GeneralConfigModel.create(map);
    }

    private static List<GeneralConfig> loadConfigTableFromDB() {
        JdbcPooledConnectionSource connectionSource = null;
        try {
            makeFile();
            connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite:" + ConfigPresenter.db_name);
            Dao<GeneralConfig, Long> dao = DaoManager.createDao(connectionSource, GeneralConfig.class);
            TableUtils.createTableIfNotExists(connectionSource, GeneralConfig.class);
            QueryBuilder<GeneralConfig, Long> queryBuilder = dao.queryBuilder();
            if (dao.countOf() == 0) {
                //デフォルトを保存してからもう一度ロードする
                saveToDB(new GeneralConfigModel());
                return loadConfigTableFromDB();
            }
            return queryBuilder.query();
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

    /**
     * 一般設定をデータベースに保存します。
     * @param model 一般設定の情報
     */
    public synchronized static void saveToDB(GeneralConfigModel model) {
        JdbcPooledConnectionSource connectionSource = null;
        List<GeneralConfig> configs = toGeneralConfigList(model);
        try {
            makeFile();
            connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite:" + ConfigPresenter.db_name);
            Dao<GeneralConfig, ?> dao = DaoManager.createDao(connectionSource, GeneralConfig.class);
            TableUtils.createTableIfNotExists(connectionSource, GeneralConfig.class);
            for (GeneralConfig config : configs) {
                dao.createOrUpdate(config);
            }
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

    private static List<GeneralConfig> toGeneralConfigList(GeneralConfigModel model) {
        Field[] fields = GeneralConfigModel.class.getDeclaredFields();
        List<GeneralConfig> configs = new LinkedList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            GeneralConfig generalConfig = new GeneralConfig();
            generalConfig.configName = field.getName();
            try {
                generalConfig.data = String.valueOf(field.get(model));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            configs.add(generalConfig);
        }
        return configs;
    }

    private static void makeFile() throws IOException {
        File file = new File(ConfigPresenter.db_name);
        if (!file.exists()) file.createNewFile();
    }

    @DatabaseTable(tableName = "general_config")
    private static class GeneralConfig {
        @DatabaseField(canBeNull = false, id = true)
        private String configName;
        @DatabaseField(canBeNull = true)
        private String data;

        private String getConfigName() {
            return configName;
        }

        private String getData() {
            return data;
        }
    }
}
