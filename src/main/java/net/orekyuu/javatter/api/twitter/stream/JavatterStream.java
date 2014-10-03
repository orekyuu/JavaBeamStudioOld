package net.orekyuu.javatter.api.twitter.stream;

import twitter4j.Status;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * ClientUserが持っているユーザーストリームの情報
 */
public class JavatterStream {

    /**
     * 例外が発生した時の動作を追加します。
     * @param exceptionConsumer ユーザーストリームに例外が発生した時のイベント
     * @return JavatterStream
     */
    public JavatterStream addOnException(Consumer<Exception> exceptionConsumer) {
        return this;
    }

    /**
     * ロケーションの削除通知がきた時の動作を追加します。
     * @param scrubGeoConsumer ロケーションが削除された時のイベント
     * @return JavatterStream
     */
    public JavatterStream addScrubGeo(BiConsumer<Long, Long> scrubGeoConsumer) {
        return this;
    }

    /**
     * 新しいツイートがきた時の動作を追加します。
     * @param statusConsumer 新着ツイートがきた時のイベント
     * @return JavatterStream
     */
    public JavatterStream addOnStatus(Consumer<Status> statusConsumer) {
        return this;
    }

    /**
     * ユーザーがブロックした時の動作を追加します。
     * @param blockInfoConsumer ブロックした時のイベント
     * @return JavatterStream
     */
    public JavatterStream addOnBlockUser(Consumer<OnBlockInfo> blockInfoConsumer) {
        return this;
    }

    /**
     * お気に入り登録時の動作を追加します。
     * @param favoriteConsumer お気に入りのイベント
     * @return JavatterStream
     */
    public JavatterStream addOnFavorite(Consumer<OnFavoriteInfo> favoriteConsumer) {
        return this;
    }

    /**
     * ユーザーのフォロー時の動作を追加します。
     * @param followConsumer フォロー時のイベント
     * @return JavatterStream
     */
    public JavatterStream addOnFollow(Consumer<OnFavoriteInfo> followConsumer) {
        return this;
    }

    /**
     * ダイレクトメッセージが届いた時の動作を追加します。
     * @param onDirectMessageConsumer ダイレクトメッセージ時のイベント
     * @return JavatterStream
     */
    public JavatterStream addOnDirectMessage(Consumer<OnDirectMessageInfo> onDirectMessageConsumer) {
        return this;
    }
}
