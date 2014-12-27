package net.orekyuu.javatter.api;

import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import net.orekyuu.javatter.api.twitter.ClientUser;

import java.io.File;
import java.util.List;

/**
 * メインウィンドウを表すインターフェイスです。<br>
 *
 * @since 1.0.0
 */
public interface CurrentWindow {
    /**
     * リプライ先を設定します。
     *
     * @param id              リプライ先のステータスid
     * @param destinationName リプライ先のユーザーの名前
     * @since 1.0.0
     */
    void setReply(long id, String destinationName);

    /**
     * 投稿する画像ファイルを取得する。
     * 投稿する画像ファイルがない場合は空のリストを返します。
     *
     * @return List 添付画像ファイルのリスト
     * @since 1.0.0
     */
    List<File> getAppendedImages();

    /**
     * 現在のツイートを行うユーザーを取得する
     *
     * @return ClientUser
     * @since 1.0.0
     */
    Property<ClientUser> getCurrentUserProperty();

    /**
     * カレントユーザーを設定します。
     *
     * @param user 新しく設定したいユーザー
     * @since 1.0.0
     */
    void assignUser(ClientUser user);

    /**
     * ツイート用TextArea内の文字を取得する.
     *
     * @return ツイート用のTextArea二入力されている文字列
     * @since 1.0.0
     */
    String getTweetText();

    /**
     * ツイート用TextAreaに文字列をセットする。
     *
     * @param text ツイート用TextAreaにセットしたい文字列。
     * @since 1.0.0
     */
    void setTweetText(String text);

    /**
     * ツイートのTextAreaのtextPropertyを返します。
     *
     * @return ツイートのプロパティ
     * @since 1.0.0
     */
    StringProperty getTweetTextProperty();

}
