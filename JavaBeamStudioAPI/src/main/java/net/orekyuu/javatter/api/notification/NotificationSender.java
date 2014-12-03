package net.orekyuu.javatter.api.notification;

/**
 * 通知を送信するオブジェクトを表すインターフェイス
 */
public interface NotificationSender {

    /**
     * 新しいポップアップ通知を送信する
     * @param notification 通知したい内容
     */
    void sendNotification(Notification notification);
}
