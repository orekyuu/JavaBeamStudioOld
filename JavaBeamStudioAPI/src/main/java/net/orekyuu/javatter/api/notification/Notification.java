package net.orekyuu.javatter.api.notification;

import javafx.scene.image.Image;

import java.util.Optional;

/**
 * 通知ポップアップの情報をまとめたクラスです。<br>
 * この情報を使って通知ポップアップを表示します。
 */
public final class Notification {

    private final Optional<String> title;
    private final Optional<Image> subTitleImage;
    private final Optional<String> message;
    private final Optional<String> subTitle;

    /**
     * Notificationを作成します。
     * @param title ポップアップのタイトルです。この項目は必須です。
     * @param subTitle ポップアップのサブタイトル
     * @param subImage サブタイトルの画像
     * @param message ポップアップの詳細メッセージ
     */
    protected Notification(String title, String subTitle, Image subImage, String message) {
        if (title == null) {
            throw new NullPointerException("title is null.");
        }
        this.title = Optional.of(title);
        this.subTitle = Optional.ofNullable(subTitle);
        this.subTitleImage = Optional.ofNullable(subImage);
        this.message = Optional.ofNullable(message);
    }

    /**
     * ポップアップのタイトルを返します。<br>
     * 戻り値のOptionalがemptyになることはありません。
     * @return ポップアップのタイトル
     */
    public Optional<String> getTitle() {
        return title;
    }

    /**
     * サブタイトルの画像
     * @return サブタイトルの画像
     */
    public Optional<Image> getSubTitleImage() {
        return subTitleImage;
    }

    /**
     * 通知の詳細メッセージ
     * @return 通知の詳細メッセージ
     */
    public Optional<String> getMessage() {
        return message;
    }

    /**
     * 通知のサブタイトル
     * @return サブタイトル
     */
    public Optional<String> getSubTitle() {
        return subTitle;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Notification{");
        sb.append("title=").append(title);
        sb.append(", subTitleImage=").append(subTitleImage);
        sb.append(", message=").append(message);
        sb.append(", subTitle=").append(subTitle);
        sb.append('}');
        return sb.toString();
    }
}
