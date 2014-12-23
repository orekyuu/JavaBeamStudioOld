package net.orekyuu.javatter.api.twitter;

import java.io.File;

/**
 * ツイートに関するインターフェース
 */
public interface TweetBuilder {
    /**
     * ツイートにイメージを添付する<br>
     * 最大4つまで
     * 
     * @param mediaFile
     *            ファイル
     * @return 自身のインスタンス
     */
    TweetBuilder addMedia(File mediaFile);

    /**
     * ツイートの実行
     * 
     */
    void tweet();

    /**
     * ツイートを行うクライアントユーザーをセットする
     * 
     * @param user 発言を行うユーザー
     * @return 自身のインスタンス
     */
    TweetBuilder setClientUser(ClientUser user);

    /**
     * リプライ先を設定する
     * 
     * @param id
     *            ステータスID
     * @return 自身のインスタンス
     */
    TweetBuilder setReplyTo(long id);

    /**
     * ツイートするテキストをセット
     * 
     * @param s
     *            テキスト
     * @return 自身のインスタンス
     */
    TweetBuilder setText(String s);

    /**
     * ツイート失敗時のイベントを設定する
     * @param callback ツイート失敗時の処理
     * @return 自身のインスタンス
     */
    TweetBuilder setOnTweetFailed(TweetFailed callback);

    /**
     * ツイート成功時のイベントを設定する
     * @param callback ツイート成功時の処理
     * @return 自身のインスタンス
     */
    TweetBuilder setOnTweetSuccess(TweetSuccess callback);

    /**
     * 非同期ツイートを有効化する
     * @return 自身のインスタンス
     */
    TweetBuilder setAsync();

}
