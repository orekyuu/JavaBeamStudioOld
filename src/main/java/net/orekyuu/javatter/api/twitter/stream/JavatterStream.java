package net.orekyuu.javatter.api.twitter.stream;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserList;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Javatterのユーザーストリームを表します
 */
public interface JavatterStream {

    /**
     * ユーザーストリームを開始します。
     */
    void start();

    /**
     * ユーザーストリームを終了します。
     */
    void shutdown();

    /**
     * 例外が発生した時の動作を追加します。<br>
     * Consumerには発生した例外が入力されます。
     *
     * @param exceptionConsumer ユーザーストリームに例外が発生した時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnException(Consumer<Exception> exceptionConsumer);

    /**
     * ツイートの位置情報が削除された時の動作を追加します。<br>
     * BiConsumerの第1引数には削除したユーザーのIDが入力されます。<br>
     * BiConsumerの第2引数にはStatusIDが入力されます。
     *
     * @param scrubGeoConsumer 位置情報が削除された時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnScrubGeo(BiConsumer<Long, Long> scrubGeoConsumer);

    /**
     * プロフィール情報を更新した時の動作を追加します。<br>
     * Consumerにはプロフィール更新をしたユーザーが入力されます。
     *
     * @param profileUpdateConsumer プロフィール情報を更新した時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnProfileUpdate(Consumer<User> profileUpdateConsumer);

    /**
     * ツイートが削除された時の動作を追加します。<br>
     * BiConsumerの第1引数にはツイートのIDが入力されます。<br>
     * BiConsumerの第2引数にはユーザーのIDが入力されます。
     *
     * @param deletionNotice 削除通知時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnDeletionNotice(BiConsumer<Long, Long> deletionNotice);

    /**
     * 新しいツイートがきた時の動作を追加します。<br>
     * Consumerには新着Statusが入力されます。
     *
     * @param statusConsumer 新着ツイートがきた時のイベント(新着Status)
     * @return JavatterStream
     */
    JavatterStream addOnStatus(Consumer<Status> statusConsumer);

    /**
     * ユーザーがブロックした時の動作を追加します。
     * BiConsumerの第1引数には削除したUserが入力されます。<br>
     * BiConsumerの第1引数には削除されたUserが入力されます。
     *
     * @param blockConsumer ブロックした時のイベント(削除したUser, 削除されたUser)
     * @return JavatterStream
     */
    JavatterStream addOnBlockUser(BiConsumer<User, User> blockConsumer);

    /**
     * お気に入り登録時の動作を追加します。<br>
     * ThConsumerの第1引数にはお気に入りしたユーザーが入力されます。<br>
     * ThConsumerの第2引数にはお気に入りされたユーザーが入力されます。<br>
     * ThConsumerの第3引数にはお気に入りされたStatusが入力されます。
     *
     * @param favoriteConsumer お気に入りのイベント
     * @return JavatterStream
     */
    JavatterStream addOnFavorite(ThConsumer<User, User, Status> favoriteConsumer);

    /**
     * ユーザーのフォロー時の動作を追加します。<br>
     * BiConsumerの第1引数にはフォローしたユーザーが入力されます。<br>
     * BiConsumerの第1引数にはフォローされたユーザーが入力されます。
     *
     * @param followConsumer フォロー時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnFollow(BiConsumer<User, User> followConsumer);

    /**
     * ユーザーがブロック解除した時の動作を追加します。
     * BiConsumerの第1引数には解除したUserが入力されます。<br>
     * BiConsumerの第1引数には解除されたUserが入力されます。
     *
     * @param unBlockConsumer ブロック解除した時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnUnBlockUser(BiConsumer<User, User> unBlockConsumer);

    /**
     * お気に入り登録解除時の動作を追加します。<br>
     * ThConsumerの第1引数にはお気に入り解除したユーザーが入力されます。<br>
     * ThConsumerの第2引数にはお気に入り解除されたユーザーが入力されます。<br>
     * ThConsumerの第3引数にはお気に入り解除されたStatusが入力されます。
     *
     * @param unFavoriteConsumer お気に入り解除のイベント
     * @return JavatterStream
     */
    JavatterStream addOnUnFavorite(ThConsumer<User, User, Status> unFavoriteConsumer);

    /**
     * ユーザーのアンフォロー時の動作を追加します。<br>
     * BiConsumerの第1引数にはアンフォローしたユーザーが入力されます。<br>
     * BiConsumerの第2引数にはアンフォローされたユーザーが入力されます。
     *
     * @param unFollowConsumer アンフォロー時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnUnFollow(BiConsumer<User, User> unFollowConsumer);

    /**
     * ダイレクトメッセージを受信した時の動作を追加します。<br>
     * ConsumerにはDirectMessageの情報が入力されます。
     *
     * @param directMessageConsumer ダイレクトメッセージ受信時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnDirectMessage(Consumer<DirectMessage> directMessageConsumer);

    /**
     * ユーザーリストに追加された時の動作を追加します。<br>
     * BiConsumerの第1引数にはリストの持ち主が入力されます。<br>
     * BiConsumerの第2引数にはユーザーリストが入力されます。
     *
     * @param userListUpdateConsumer ユーザーリストに追加された時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnUserListUpdate(BiConsumer<User, User> userListUpdateConsumer);

    /**
     * ユーザーリストがフォローされた時の動作を追加します。<br>
     * ThConsumerの第1引数にはフォローしたユーザーが入力されます。<br>
     * ThConsumerの第2引数にはリストの持ち主が入力されます。<br>
     * ThConsumerの第3引数にはフォローされたユーザーリストが入力されます。
     *
     * @param userListConsumer ユーザーリストがフォローされた時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnUserListSubscripton(ThConsumer<User, User, UserList> userListConsumer);

    /**
     * ユーザーリストがフォロー解除された時の動作を追加します。<br>
     * ThConsumerの第1引数にはフォロー解除したユーザーが入力されます。<br>
     * ThConsumerの第2引数にはリストの持ち主が入力されます。<br>
     * ThConsumerの第3引数にはフォロー解除されたユーザーリストが入力されます。
     *
     * @param userListConsumer ユーザーリストがフォロー解除された時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnUserListUnsubscripton(ThConsumer<User, User, UserList> userListConsumer);

    /**
     * ユーザーリストにメンバーが追加された時の動作を追加します。<br>
     * ThConsumerの第1引数には追加されたユーザーが入力されます。<br>
     * ThConsumerの第2引数にはリストの持ち主が入力されます。<br>
     * ThConsumerの第3引数には追加されたユーザーリストが入力されます。
     *
     * @param addMemberConsumer ユーザーリストにメンバーが追加された時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnUserMemberAdditon(ThConsumer<User, User, UserList> addMemberConsumer);

    /**
     * ユーザーリストからメンバーが削除された時の動作を追加します。<br>
     * ThConsumerの第1引数には削除されたユーザーが入力されます。<br>
     * ThConsumerの第2引数にはリストの持ち主が入力されます。<br>
     * ThConsumerの第3引数には削除されたユーザーリストが入力されます。
     *
     * @param deletionMemberConsumer ユーザーリストからメンバーが削除された時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnUserMemberDeletion(ThConsumer<User, User, UserList> deletionMemberConsumer);

    /**
     * ユーザーリストが削除された時の動作を追加します。<br>
     * BiConsumerの第1引数にはリストの持ち主が入力されます。<br>
     * BiConsumerの第2引数には削除されたユーザーリストが入力されます。
     *
     * @param userListDeletionConsumer ユーザーリストが削除された時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnUserListDeletion(BiConsumer<User, UserList> userListDeletionConsumer);

    /**
     * ユーザーリストが作成された時の動作を追加します。<br>
     * BiConsumerの第1引数にはリストの持ち主が入力されます。<br>
     * BiConsumerの第2引数には作成されたユーザーリストが入力されます。
     *
     * @param userListCreationConsumer ユーザーリストが作成された時のイベント
     * @return JavatterStream
     */
    JavatterStream addOnUserListCreation(BiConsumer<User, UserList> userListCreationConsumer);
}
