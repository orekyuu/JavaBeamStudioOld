package net.orekyuu.javatter.api;

import net.orekyuu.javatter.api.notification.NotificationSender;
import net.orekyuu.javatter.api.notification.NotificationTypeRegister;
import net.orekyuu.javatter.api.userprofile.UserProfileRegister;

/**
 * Javaビーム工房が提供するAPIです
 */
public final class API {
    private static final API instance = new API();
    private Application application;
    private NotificationSender notificationSender;
    private NotificationTypeRegister notificationTypeRegister;
    private UserProfileRegister userProfileRegister;

    private API() {

    }

    /**
     * アプリケーションを返します。
     *
     * @return {@link net.orekyuu.javatter.api.Application}
     * @since 1.0.0
     */
    public Application getApplication() {
        return application;
    }

    /**
     * @return 自身のインスタンス
     * @since 1.0.0
     */
    public static API getInstance() {
        return instance;
    }

    /**
     * 通知を送信するためのオブジェクトを返します。
     *
     * @return 通知を送信するためのオブジェクト
     * @since 1.0.0
     */
    public NotificationSender getNotificationSender() {
        return notificationSender;
    }

    /**
     * 通知タイプを登録するためのオブジェクトを返します。
     *
     * @return 通知タイプを登録するためのオブジェクト
     * @since 1.0.0
     */
    public NotificationTypeRegister getNotificationTypeRegister() {
        return notificationTypeRegister;
    }

    /**
     * ユーザープロファイルのタブを登録するためのレジスタを返します。
     * @return {@link net.orekyuu.javatter.api.userprofile.UserProfileRegister}
     */
    public UserProfileRegister getUserProfileRegister() {
        return userProfileRegister;
    }
}
