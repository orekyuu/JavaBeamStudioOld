package net.orekyuu.javatter.api.notification;

/**
 * 通知タイプの列挙
 */
public enum NotificationTypes implements NotificationType {
    MENTION("メンション", "メンション通知"),
    FOLLOW("フォローされました", "フォロー通知"),
    FAVORITE("お気に入りされました", "お気に入り通知"),
    RETWEET("リツイートされました" , "リツイート通知"),
    ADDED_LIST("リストに追加されました","リスト通知");
    private final String typeName;
    private final String title;

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
