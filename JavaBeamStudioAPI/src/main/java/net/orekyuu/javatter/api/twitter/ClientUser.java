package net.orekyuu.javatter.api.twitter;

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
     * ユーザーの名前を返します。<br>
     *
     * @return クライアントで表示用の名前
     */
    default String getName() {
        return getAccessToken().getScreenName();
    }
}