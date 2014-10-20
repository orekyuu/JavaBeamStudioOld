package net.orekyuu.javatter.api.twitter;

import net.orekyuu.javatter.api.twitter.stream.JavatterStream;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

/**
 * クライアントを利用するユーザー
 */
public interface ClientUser {

    /**
     * アクセストークンを返します。<br>
     *
     * @return このユーザーのAccessToken
     */
    AccessToken getAccessToken();

    /**
     * ClientUserが所有するTwitterインスタンスを返します。
     * @return Twitter
     */
    Twitter getTitter();

    /**
     * ClientUserが所有するJavatterUserStreamを返します。
     *
     * @return JavatterUserStream
     */
    JavatterStream getStream();

    /**
     * ユーザーの名前を返します。<br>
     *
     * @return クライアントで表示用の名前
     */
    default String getName() {
        try {
            return getTitter().getScreenName();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }
}