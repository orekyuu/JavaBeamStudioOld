package net.orekyuu.javatter.api;

import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * ツイートの成功の可否についてのインターフェース
 *
 */
public interface TweetCallBack {
    /**
     *成功時のイベント 
     */
    void successCallBack(Status model);
    /**
     * 失敗時のイベント
     * @param e TwitterException
     */
    void failureCallBack(TwitterException e);
}
