package net.orekyuu.javatter.api.notification;

/**
 * 通知タイプの列挙
 * @since 1.0.0
 */
public enum NotificationTypes implements NotificationType {
    /**
     * メンション通知
     * @since 1.0.0
     */
    MENTION("メンション", "メンション通知"),
    /**
     * フォロー通知
     * @since 1.0.0
     */
    FOLLOW("フォローされました", "フォロー通知"),
    /**
     * お気に入り通知
     * @since 1.0.0
     */
    FAVORITE("お気に入りされました", "お気に入り通知"),
    /**
     * リツイート通知
     * @since 1.0.0
     */
    RETWEET("リツイートされました" , "リツイート通知"),
    /**
     * リストに追加された時の通知
     * @since 1.0.0
     */
    ADDED_LIST("リストに追加されました","リスト通知");
    private final String typeName;
    private final String title;

    /**
     * 通知タイプを作成します。
     * @param title 通知のタイトル
     * @param typeName 通知タイプの名前
     * @since 1.0.0
     */
    NotificationTypes(String title, String typeName) {
        this.typeName = typeName;
        this.title = title;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public String getPopupTitle() {
        return title;
    }
}
