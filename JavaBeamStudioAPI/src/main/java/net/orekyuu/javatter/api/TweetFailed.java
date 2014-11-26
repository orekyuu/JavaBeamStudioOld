package net.orekyuu.javatter.api;

import twitter4j.TwitterException;

/**
 * ツイート失敗時のイベント
 */
@FunctionalInterface
public interface TweetFailed {
    /**
     * ツイートが失敗した時のイベント
     * @param e 発生した例外
     */
    void failed(TwitterException e);
}
