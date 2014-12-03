package net.orekyuu.javatter.api.notification;

/**
 * 通知タイプの列挙
 */
public enum NotificationTypes implements NotificationType {
    MENTION("メンション通知", "メンション"),
    FOLLOW("フォロー通知", "フォローされました"),
    FAVORITE("お気に入り通知", "お気に入りに追加されました");

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
