package net.orekyuu.javatter.api.twitter;

import net.orekyuu.javatter.api.entity.Account;
import net.orekyuu.javatter.api.twitter.stream.JavatterStream;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

/**
 * クライアントを利用するユーザー
 *
 * @since 1.0.0
 */
public interface ClientUser {

    /**
     * アクセストークンを返します。<br>
     *
     * @return このユーザーのAccessToken
     * @since 1.0.0
     */
    @Deprecated
    AccessToken getAccessToken();

    /**
     * アカウントを返します。
     * @return このユーザーのアカウント
     * @since 1.0.0
     */
    Account getAccount();

    /**
     * ClientUserが所有するTwitterインスタンスを返します。
     *
     * @return Twitter
     * @since 1.0.0
     */
    Twitter getTwitter();

    /**
     * {@link ClientUser}が所有する{@link JavatterStream}を返します。
     *
     * @return {@link JavatterStream}
     * @since 1.0.0
     */
    JavatterStream getStream();

    /**
     * ツイートを行うためのビルダーを作成します。
     * @return このユーザーでツイートを行うためのTweetBuilder
     */
    default TweetBuilder createTweet() {
        return new TweetBuilder(this);
    }

    /**
     * ユーザーの名前を返します。<br>
     *
     * @return クライアントで表示用の名前
     * @since 1.0.0
     */
    default String getName() {
        try {
            return getTwitter().getScreenName();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }
}