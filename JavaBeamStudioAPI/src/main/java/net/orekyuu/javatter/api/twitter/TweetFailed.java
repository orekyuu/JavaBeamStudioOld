package net.orekyuu.javatter.api.twitter;

import twitter4j.TwitterException;

/**
 * ツイート失敗時のイベント
 * @since 1.0.0
 */
@FunctionalInterface
public interface TweetFailed {
    /**
     * ツイートが失敗した時のイベント
     * @param e 発生した例外
     * @since 1.0.0
     */
    void failed(TwitterException e);
}
