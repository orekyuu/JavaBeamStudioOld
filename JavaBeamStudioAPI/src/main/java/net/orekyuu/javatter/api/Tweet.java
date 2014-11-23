package net.orekyuu.javatter.api;

import java.io.File;
import net.orekyuu.javatter.api.twitter.ClientUser;

/**
 * ツイートに関するインターフェース
 */
public interface Tweet {
    /**
     * ツイートにイメージを添付する<br>
     * 最大4つまで
     * 
     * @param mediaFile
     *            ファイル
     * @return 自身のインスタンス
     */
    Tweet addMedia(File mediaFile);

    /**
     * ツイートの実行
     * 
     */
    void tweet();

    /**
     * ツイートを行うクライアントユーザーをセットする
     * 
     * @param user
     *            クライアントユーザー
     * @return 自身のインスタンス
     */
    Tweet setClientUser(ClientUser user);

    /**
     * リプライ先を設定する
     * 
     * @param id
     *            ステータスID
     * @return 自身のインスタンス
     */
    Tweet setReplyto(long id);

    /**
     * ツイートするテキストをセット
     * 
     * @param s
     *            テキスト
     * @return 自身のインスタンス
     */
    Tweet setText(String s);

    /**
     * 
     * @param callBack
     *            ツイートコールバック
     * @return 自身のインスタンス
     */
    Tweet setTweetCallBack(TweetCallBack callBack);

}
