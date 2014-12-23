/**
 * 通知を行うためのAPIです。<br>
 * 通知を行うためには{@link net.orekyuu.javatter.api.notification.NotificationBuilder}から{@link net.orekyuu.javatter.api.notification.Notification}インスタンスを作成します。<br>
 * <br>
 * インスタンス化は以下のように行うことができます。<br>
 * {@code Notification notification = new NotificationBuilder(NotificationTypes.FOLLOW).setMessage("メッセージ").build();}<br>
 * 通知のオプションについては{@link net.orekyuu.javatter.api.notification.NotificationBuilder}を参照してください。<br>
 * <br>
 * コンストラクタには{@link net.orekyuu.javatter.api.notification.NotificationType}を与えます。<br>
 * 新しいTypeを拡張するためには、{@link net.orekyuu.javatter.api.notification.NotificationType}を実装したクラスを{@link net.orekyuu.javatter.api.notification.NotificationTypeRegister}に登録します。<br>
 * レジスタは{@link net.orekyuu.javatter.api.GlobalAccess#getNotificationTypeRegister}から取得できます。<br>
 * <br>
 * {@link net.orekyuu.javatter.api.notification.Notification}の通知は{@link net.orekyuu.javatter.api.notification.NotificationSender}で行います。<br>
 * このインスタンスも同じく{@link net.orekyuu.javatter.api.GlobalAccess#getNotificationSender}から取得できます。<br>
 *
 */
package net.orekyuu.javatter.api.notification;