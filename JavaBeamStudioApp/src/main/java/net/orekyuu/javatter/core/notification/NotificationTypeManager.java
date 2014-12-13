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
    private static final String soundData = "sound.db";

    @Override
    public void register(NotificationType type) {
        notificationTypes.add(type);
    }

    public void initialize() {
        notificationConfigs = loadNotificationConfig();
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
                config.setNotificationType(type);
                res.add(config);
            }
        }
        return res;
    }

    /**
     * 情報を保存する
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
    public void saveNotificationSoundPath(NotificationSoundData data){
        makeFile();
        JdbcPooledConnectionSource connectionSource = null;
        try{
            connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite" + soundData);
            Dao<NotificationSoundData,?> dao = DaoManager.createDao(connectionSource, NotificationSoundData.class);
            dao.createOrUpdate(data);
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(connectionSource != null){
                try{
                    connectionSource.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static NotificationSoundData loadNotificationSoundData(){
        JdbcPooledConnectionSource connectionSource = null;
        NotificationSoundData data = null;
        try{
            connectionSource = new JdbcPooledConnectionSource("jdbc:sqlite" + soundData);
            Dao<NotificationSoundData,?> dao = DaoManager.createDao(connectionSource, NotificationSoundData.class);
            TableUtils.createTableIfNotExists(connectionSource, NotificationSoundData.class);
            data = dao.queryForAll().get(0);
            }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(connectionSource != null){
                try{
                    connectionSource.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    if(data == null){
        data = new NotificationSoundData();
        data.setNotificationSoundName("ファイルが見つかりません。ファイルを選択してください。");
        data.setNotificationSoundPath("");
    }
    return data;
    }

    /**
     * 通知するかを返す
     * @param type 通知タイプ
     * @return 通知設定がONならtrue
     */
    public boolean isNotice(NotificationType type) {
        Optional<NotificationConfig> notificationConfig = notificationConfigs.stream()
                .filter(t -> t.getNotificationType().equals(type.getTypeName()))
                .findFirst();
        return notificationConfig.map(NotificationConfig::isNotice).orElse(true);
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
            Path path = Paths.get("notification.db");
            Path path2 = Paths.get(soundData);
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            if(Files.notExists(path2)){
                Files.createFile(path2);
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

        public void setNotificationType(NotificationType notificationType) {
            this.notificationType = notificationType.getTypeName();
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
        private String notificationSoundPath;
        
        @DatabaseField(canBeNull = false)
        private String notificationSoundName;
        
        @DatabaseField(canBeNull = false,id = true)
        private static final int id = 0;
        
        public void setNotificationSoundPath(String path){
            notificationSoundPath = path;
        }
        
        public void setNotificationSoundName(String name){
            notificationSoundName = name;
        }
        
        public String getNotificationSoundPath(){
            return notificationSoundPath;
        }
        public String getNotificationSoundName(){
            return notificationSoundName;
        }
    }
}
