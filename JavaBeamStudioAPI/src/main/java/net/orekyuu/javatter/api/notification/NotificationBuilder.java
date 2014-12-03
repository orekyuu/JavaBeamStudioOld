package net.orekyuu.javatter.api.notification;

import javafx.scene.image.Image;

/**
 * Notificationのビルダークラスです。
 */
public class NotificationBuilder {

    private NotificationType type;
    private Image subTitleImage;
    private String message;
    private String subTitle;

    /**
     * @param type 通知ポップアップのタイプ
     */
    public NotificationBuilder(NotificationType type) {
        this.type = type;
    }

    /**
     * サブタイトルの画像を設定します。
     * @param subTitleImage サブタイトルの画像
     * @return NotificationBuilder
     */
    public NotificationBuilder setSubTitleImage(Image subTitleImage) {
        this.subTitleImage = subTitleImage;
        return this;
    }

    /**
     * サブタイトルを設定します。
     * @param subTitle サブタイトル
     * @return NotificationBuilder
     */
    public NotificationBuilder setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    /**
     * 通知の詳細メッセージを設定します。
     * @param message 詳細メッセージ
     * @return NotificationBuilder
     */
    public NotificationBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * 設定された内容でNotificationを作成します。
     * @return 値が設定されたNotification
     */
    public Notification build() {
        return new Notification(type, subTitle, subTitleImage, message);
    }
}
