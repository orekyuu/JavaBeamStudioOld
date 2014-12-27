package net.orekyuu.javatter.api.notification;

import javafx.scene.image.Image;

import java.util.Optional;

/**
 * 通知ポップアップの情報をまとめたクラスです。<br>
 * この情報を使って通知ポップアップを表示します。
 * @since 1.0.0
 */
public final class Notification {

    private final Optional<Image> subTitleImage;
    private final Optional<String> message;
    private final Optional<String> subTitle;
    private final NotificationType type;

    /**
     * {@link Notification}を作成します。
     * @param type ポップアップのタイプです
     * @param subTitle ポップアップのサブタイトル
     * @param subImage サブタイトルの画像
     * @param message ポップアップの詳細メッセージ
     * @since 1.0.0
     */
    protected Notification(NotificationType type, String subTitle, Image subImage, String message) {
        if (type == null)
            throw new NullPointerException("type is null.");
        if (type.getPopupTitle() == null)
            throw new NullPointerException("popup title is null.");

        this.type = type;
        this.subTitle = Optional.ofNullable(subTitle);
        this.subTitleImage = Optional.ofNullable(subImage);
        this.message = Optional.ofNullable(message);
    }

    /**
     * ポップアップのタイトルを返します。<br>
     * 戻り値のOptionalがemptyになることはありません。
     * @return ポップアップのタイトル
     * @since 1.0.0
     */
    public Optional<String> getTitle() {
        return Optional.of(type.getPopupTitle());
    }

    /**
     * サブタイトルの画像
     * @return サブタイトルの画像
     * @since 1.0.0
     */
    public Optional<Image> getSubTitleImage() {
        return subTitleImage;
    }

    /**
     * 通知の詳細メッセージ
     * @return 通知の詳細メッセージ
     * @since 1.0.0
     */
    public Optional<String> getMessage() {
        return message;
    }

    /**
     * 通知のサブタイトル
     * @return サブタイトル
     * @since 1.0.0
     */
    public Optional<String> getSubTitle() {
        return subTitle;
    }

    /**
     * 通知タイプを返します。
     * @return 通知タイプ
     * @since 1.0.0
     */
    public NotificationType getType() {
        return type;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Notification{");
        sb.append("title=").append(type.getPopupTitle());
        sb.append(", subTitleImage=").append(subTitleImage);
        sb.append(", message=").append(message);
        sb.append(", subTitle=").append(subTitle);
        sb.append(", type=").append(type.getTypeName());
        sb.append('}');
        return sb.toString();
    }
}
