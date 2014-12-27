package net.orekyuu.javatter.api.notification;

/**
 * 通知タイプのレジスタです。<br>
 * {@link net.orekyuu.javatter.api.notification.NotificationBuilder}で使用するためには、必ず登録が必要です。
 * 　@since 1.0.0
 */
public interface NotificationTypeRegister {

    /**
     * 新しい通知タイプを登録します。
     *
     * @param type 登録する通知タイプ
     * @since 1.0.0
     */
    void register(NotificationType type);
}
