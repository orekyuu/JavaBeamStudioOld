package net.orekyuu.javatter.api.notification;

import javafx.scene.image.Image;

/**
 * {@link Notification}のビルダークラスです。
 * @since 1.0.0
 */
public class NotificationBuilder {

    private NotificationType type;
    private Image subTitleImage;
    private String message;
    private String subTitle;

    /**
     * @param type 通知ポップアップのタイプ
     * @exception java.lang.NullPointerException NotificationTypeがnullの時
     * @since 1.0.0
     */
    public NotificationBuilder(NotificationType type) {
        if (type == null) {
            throw new NullPointerException("NotificationType is null");
        }
        this.type = type;
    }

    /**
     * サブタイトルの画像を設定します。
     * @param subTitleImage サブタイトルの画像
     * @return {@link NotificationBuilder}
     * @since 1.0.0
     */
    public NotificationBuilder setSubTitleImage(Image subTitleImage) {
        this.subTitleImage = subTitleImage;
        return this;
    }

    /**
     * サブタイトルを設定します。
     * @param subTitle サブタイトル
     * @return {@link NotificationBuilder}
     * @since 1.0.0
     */
    public NotificationBuilder setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    /**
     * 通知の詳細メッセージを設定します。
     * @param message 詳細メッセージ
     * @return {@link NotificationBuilder}
     * @since 1.0.0
     */
    public NotificationBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * 設定された内容で{@link Notification}を作成します。
     * @return 値が設定された{@link Notification}
     * @since 1.0.0
     */
    public Notification build() {
        return new Notification(type, subTitle, subTitleImage, message);
    }
}
