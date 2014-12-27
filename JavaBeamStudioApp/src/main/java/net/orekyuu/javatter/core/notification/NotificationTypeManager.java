package net.orekyuu.javatter.core.notification;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import net.orekyuu.javatter.api.notification.NotificationType;
import net.orekyuu.javatter.api.notification.NotificationTypeRegister;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

public class NotificationTypeManager implements NotificationTypeRegister {

    private Set<NotificationType> notificationTypes = new LinkedHashSet<>();
    private static final String dbName = "notification.db";
    private List<NotificationConfig> notificationConfigs;
    private NotificationSoundData soundData;

    @Override
    public void register(NotificationType type) {
        notificationTypes.add(type);
    }

    public void initialize() {
        notificationConfigs = loadNotificationConfig();
        soundData = loadNotificationSoundData().orElse(new NotificationSoundData());
    }

    public Set<NotificationType> getNotificationTypes() {
        return notificationTypes;
    }

    public List<NotificationConfig> getConfigs() {
        List<NotificationConfig> res = new LinkedList<>();
        res.addAll(notificationConfigs);
        for (NotificationType type : notificationTypes) {
            boolean anyMatch = notificationConfigs.stream()
                    .anyMatch(conf -> conf.notificationType.equals(type.getTypeName()));
            if (!anyMatch) {
                NotificationConfig config = new NotificationConfig();
                config.setNotice(true);
                config.setNotificationType(type.getTypeName());
                res.add(config);
            }
        }
        return res;
    }

    /**
     * 情報を保存する
     *
     * @param configs 保存するコンフィグ情報
     */
    public void saveNotificationConfigs(List<NotificationConfig> configs) {
        makeFile();
        JdbcPooledConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite:" + dbName);
            Dao<NotificationConfig, ?> dao = DaoManager.createDao(connectionSource, NotificationConfig.class);
            TableUtils.createTableIfNotExists(connectionSource, NotificationConfig.class);
            for (NotificationConfig config : configs) {
                dao.createOrUpdate(config);
            }
            notificationConfigs = configs;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connectionSource != null) {
                try {
                    connectionSource.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 通知音についての情報を保存する
     *
     * @param data 保存する通知音のデータ
     */
    public void saveNotificationSound(NotificationSoundData data) {
        makeFile();
        JdbcPooledConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite:" + dbName);
            Dao<NotificationSoundData, Integer> dao = DaoManager.createDao(connectionSource, NotificationSoundData.class);
            TableUtils.createTableIfNotExists(connectionSource, NotificationSoundData.class);
            dao.createOrUpdate(data);
            soundData = data;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connectionSource != null) {
                try {
                    connectionSource.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 通知音に関するデータを返す
     *
     * @return 取得に失敗すると空のデータを返す。
     */
    public Optional<NotificationSoundData> loadNotificationSoundData() {
        makeFile();
        JdbcPooledConnectionSource connectionSource = null;
        NotificationSoundData data = null;
        try {
            connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite:" + dbName);
            Dao<NotificationSoundData, Integer> dao = DaoManager.createDao(connectionSource, NotificationSoundData.class);
            TableUtils.createTableIfNotExists(connectionSource, NotificationSoundData.class);
            data = dao.queryForId(0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connectionSource != null) {
                try {
                    connectionSource.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return Optional.ofNullable(data);
    }

    /**
     * 保存されている通知音のパスに実際にファイルが存在するかどうかを取得する。
     *
     * @return 存在しなければければtrue、存在すればFalse
     */
    public boolean soundDataIsEmpty() {
        Optional<NotificationSoundData> notificationSoundData = loadNotificationSoundData();
        if (!notificationSoundData.isPresent()) {
            return true;
        }
        Optional<String> path = notificationSoundData.map(NotificationSoundData::getNotificationSoundPath);
        return Files.notExists(Paths.get(path.get()));
    }

    /**
     * 通知するかを返す
     *
     * @param type 通知タイプ
     * @return 通知設定がONならtrue
     */
    public boolean isNotice(NotificationType type) {
        Optional<NotificationConfig> notificationConfig = notificationConfigs.stream()
                .filter(t -> t.getNotificationType().equals(type.getTypeName()))
                .findFirst();
        return notificationConfig.map(NotificationConfig::isNotice).orElse(true);
    }

    /**
     * 通知音に関する設定を返す
     *
     * @return 通知音の設定
     */
    public NotificationSoundData getNotificationSoundData() {
        NotificationSoundData notificationSoundData = new NotificationSoundData();
        notificationSoundData.setNotificationSoundName(soundData.getNotificationSoundName());
        notificationSoundData.setNotificationSoundPath(soundData.getNotificationSoundPath());
        notificationSoundData.setNotificationSoundVolume(soundData.getNotificationSoundVolume());
        return notificationSoundData;
    }

    private List<NotificationConfig> loadNotificationConfig() {
        makeFile();
        List<NotificationConfig> configs = new LinkedList<>();

        JdbcPooledConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite:" + dbName);
            Dao<NotificationConfig, ?> dao = DaoManager.createDao(connectionSource, NotificationConfig.class);
            TableUtils.createTableIfNotExists(connectionSource, NotificationConfig.class);
            dao.forEach(configs::add);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connectionSource != null) {
                try {
                    connectionSource.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return configs;
    }

    private void makeFile() {
        try {
            Path path = Paths.get(dbName);
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DatabaseTable(tableName = "notification_config")
    public static class NotificationConfig {
        @DatabaseField(canBeNull = false, id = true)
        private String notificationType;
        @DatabaseField(canBeNull = false)
        private boolean isNotice;

        public String getNotificationType() {
            return notificationType;
        }

        public void setNotificationType(String typeName) {
            this.notificationType = typeName;
        }

        public boolean isNotice() {
            return isNotice;
        }

        public void setNotice(boolean isNotice) {
            this.isNotice = isNotice;
        }
    }

    @DatabaseTable(tableName = "sound")
    public static class NotificationSoundData {
        @DatabaseField(canBeNull = false)
        private String notificationSoundPath = "";

        @DatabaseField(canBeNull = false)
        private String notificationSoundName = "";

        @DatabaseField(canBeNull = false)
        private double notificationSoundvolume = 0.5;

        @DatabaseField(canBeNull = false, id = true)
        private int id = 0;

        public void setNotificationSoundPath(String path) {
            notificationSoundPath = path;
        }

        public void setNotificationSoundName(String name) {
            notificationSoundName = name;
        }

        public void setNotificationSoundVolume(double volume) {
            notificationSoundvolume = volume;
        }

        public String getNotificationSoundPath() {
            return notificationSoundPath;
        }

        public String getNotificationSoundName() {
            return notificationSoundName;
        }

        public double getNotificationSoundVolume() {
            return notificationSoundvolume;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("NotificationSoundData{");
            sb.append("notificationSoundPath='").append(notificationSoundPath).append('\'');
            sb.append(", notificationSoundName='").append(notificationSoundName).append('\'');
            sb.append(", notificationSoundvolume=").append(notificationSoundvolume);
            sb.append(", id=").append(id);
            sb.append('}');
            return sb.toString();
        }
    }
}
