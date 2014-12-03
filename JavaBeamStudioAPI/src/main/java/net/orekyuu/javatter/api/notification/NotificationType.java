package net.orekyuu.javatter.api.notification;

/**
 * 通知のタイプを表すインターフェイス
 */
public interface NotificationType {
    /**
     * 通知タイプの名前を返します。<br>
     * この値はコンフィグの項目名に使用されます。
     * @return 通知のタイプ名
     */
    String getTypeName();

    /**
     * ポップアップのタイトルを返します。
     * @return ポップアップのタイトル
     */
    String getPopupTitle();

    /**
     * 通知タイプが等しいかを返します
     * @param type 比較対象
     * @return 等しければtrue
     */
    default boolean equals(NotificationTypes type) {
        return type != null && getTypeName().equals(type.getTypeName());
    }
}
