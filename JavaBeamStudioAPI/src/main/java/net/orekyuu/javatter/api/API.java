package net.orekyuu.javatter.api;

import net.orekyuu.javatter.api.column.ColumnRegister;
import net.orekyuu.javatter.api.notification.NotificationSender;
import net.orekyuu.javatter.api.notification.NotificationTypeRegister;

public class API {
    private static final API instance = new API();
    private Application application;
    private ColumnRegister columnRegister;
    private NotificationSender notificationSender;
    private NotificationTypeRegister notificationTypeRegister;

    private API() {

    }

    /**
     * Application アプリケーションを返します。
     *
     * @return Application
     */
    public Application getApplication() {
        return application;
    }

    public static API getInstance() {
        return instance;
    }

    /**
     * カラムのRegisterを返します。
     * @return カラムのRegister
     */
    public ColumnRegister getColumnRegister() {
        return columnRegister;
    }

    /**
     * 通知を送信するためのオブジェクトを返します。
     * @return 通知を送信するためのオブジェクト
     */
    public NotificationSender getNotificationSender() {
        return notificationSender;
    }

    /**
     * 通知タイプを登録するためのオブジェクトを返します。
     * @return 通知タイプを登録するためのオブジェクト
     */
    public NotificationTypeRegister getNotificationTypeRegister() {
        return notificationTypeRegister;
    }
}
