package net.orekyuu.javatter.api.twitter;

import java.io.File;

/**
 * ツイートを行うためのクラスです。
 */
public interface TweetBuilder {
    /**
     * ツイートにイメージを添付します。<br>
     *
     * @param mediaFile 添付するファイル
     * @return 自身のインスタンス
     * @since 1.0.0
     */
    TweetBuilder addMedia(File mediaFile);

    /**
     * ツイートを行います。
     *
     * @since 1.0.0
     */
    void tweet();

    /**
     * ツイートを行うユーザーを設定します
     *
     * @param user 発言を行うユーザー
     * @return 自身のインスタンス
     * @since 1.0.0
     */
    TweetBuilder setClientUser(ClientUser user);

    /**
     * リプライ先を設定します
     *
     * @param id リプライ先のStatusID
     * @return 自身のインスタンス
     * @since 1.0.0
     */
    TweetBuilder setReplyTo(long id);

    /**
     * ツイートの内容を設定します
     *
     * @param text 発言内容
     * @return 自身のインスタンス
     * @since 1.0.0
     */
    TweetBuilder setText(String text);

    /**
     * ツイート失敗時のイベントを設定します
     *
     * @param callback ツイート失敗時の処理
     * @return 自身のインスタンス
     * @since 1.0.0
     */
    TweetBuilder setOnTweetFailed(TweetFailed callback);

    /**
     * ツイート成功時のイベントを設定する
     *
     * @param callback ツイート成功時の処理
     * @return 自身のインスタンス
     * @since 1.0.0
     */
    TweetBuilder setOnTweetSuccess(TweetSuccess callback);

    /**
     * ツイートを非同期で行います
     *
     * @return 自身のインスタンス
     * @since 1.0.0
     */
    TweetBuilder setAsync();

}
