package net.orekyuu.javatter.api;

import twitter4j.TwitterException;

/**
 * ツイートの成功の可否についてのインターフェース
 *
 */
public interface TweetCallBack {
    /**
     *成功時のイベント 
     */
    void successCallBack();
    /**
     * 失敗時のイベント
     * @param e TwitterException
     */
    void failureCallBack(TwitterException e);
}
